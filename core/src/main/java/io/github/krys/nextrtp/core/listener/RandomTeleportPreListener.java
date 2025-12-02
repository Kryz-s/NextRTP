package io.github.krys.nextrtp.core.listener;

import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportEndEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreEndEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreStartEvent;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import io.github.krys.nextrtp.common.condition.ConditionProcessorContainer;
import io.github.krys.nextrtp.common.condition.ConditionType;
import io.github.krys.nextrtp.common.condition.excp.InvalidConditionException;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.common.obj.ChunkLocationSnapshot;
import io.github.krys.nextrtp.common.obj.ContextualTeleport;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.core.rtp.TeleportPendingRepository;
import io.github.krys.nextrtp.core.util.RandomPointGeneratorService;
import io.github.krys.nextrtp.core.vault.VaultService;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RandomTeleportPreListener {

  private final TeleportPendingRepository repository;
  private final VaultService service;

  public RandomTeleportPreListener(TeleportPendingRepository repository, VaultService service) {
    this.repository = repository;
    this.service = service;
  }

  @Listener(priority = Byte.MAX_VALUE)
  public void onStart(final RandomTeleportPreStartEvent event) {
    final BaseTeleportInfo info = event.teleportInfo;

    final var player = event.player;

    if (info == null) {
      Messager.NOT_ALLOWED.accept(player);
      event.setCancelled(true);
      return;
    }

    final var condition = info.preCondition;

    final var teleportPending = repository.get(player);

    if (teleportPending != null && teleportPending.times > 0) {
      Messager.PROGRESS.accept(player);
      event.setCancelled(true);
      return;
    }

    if (info.cooldown.isInCooldown(player)) {
      Messager.ON_COOLDOWN.accept(player,
          Placeholder.unparsed("cooldown", String.valueOf(info.cooldown.getSecondsLeft(player))));
      event.setCancelled(true);
      return;
    }

    if (condition != null) {
      final var processorArray = condition.processors();
      final World world = player.getWorld();
      final Location loc = player.getLocation();
      final ChunkSnapshot snapshot = world.getChunkAt(loc.getBlockX() >> 4, loc.getBlockZ() >> 4, true)
          .getChunkSnapshot(true, true, false);
      final var contextual = new ContextualTeleport(new ChunkLocationSnapshot(snapshot, loc), info, player);
      boolean thrown = false;
      for (ConditionProcessorContainer<?> conditionProcessor : processorArray) {
        if (thrown)
          break;
        try {
          conditionProcessor.check(contextual, ConditionType.PRE);
        } catch (final InvalidConditionException e) {
          Messager.FAILED.accept(player, Placeholder.component("reason", e.component));
          thrown = true;
        }
      }
      if (thrown) {
        event.setCancelled(true);
        return;
      }
    }

    Messager.STARTING.accept(player);
  }

  @Listener(priority = Byte.MIN_VALUE)
  public void onStop(final RandomTeleportPreEndEvent event) {
    final var info = event.teleportInfo;
    final var condition = info.posCondition;

    if (condition != null && info.attempts > 1) {
      processAttemptsAsync(event, 0);
      return;
    }

    processSingleAttemptAsync(event);
  }

  private void processAttemptsAsync(final RandomTeleportPreEndEvent event, final int attempt) {
    final var info = event.teleportInfo;
    final var player = event.player;
    final var condition = info.posCondition;
    final World world = event.world;

    if (condition == null) {
      processSingleAttemptAsync(event);
      return;
    }

    if (attempt >= info.attempts) {
      event.setCancelled(true);
      return;
    }

    final var processorArray = condition.processors();

    final double[] relativeCoords = RandomPointGeneratorService.getRandomPoint(info);
    final int x = (int) relativeCoords[0];
    final int z = (int) relativeCoords[1];

    world
        .getChunkAtAsync(x >> 4, z >> 4)
        .thenAcceptAsync(chunk -> {
          final ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, true, false);
          final int y = getHighestBlockFromY(snapshot, x & 15, z & 15, info.minY, info.maxY);

          final Location loc = new Location(world, relativeCoords[0], y, relativeCoords[1]);
          final var contextual = new ContextualTeleport(new ChunkLocationSnapshot(snapshot, loc), info, player);

          boolean thrown = false;
          for (ConditionProcessorContainer<?> conditionProcessor : processorArray) {
            if (thrown)
              break;
            try {
              conditionProcessor.check(contextual, ConditionType.POS);
            } catch (final InvalidConditionException e) {
              // Only show FAILED on the last attempt
              if (attempt + 1 >= info.attempts) {
                Messager.FAILED.accept(player, Placeholder.component("reason", e.component));
              }
              thrown = true;
            }
          }

          if (thrown) {
            processAttemptsAsync(event, attempt + 1);
            return;
          }

          if (!service.has(player, info.money) && !player.hasPermission("nextrtp.bypass.money")) {
            Messager.NO_MONEY.accept(
                player, Placeholder.unparsed("money", String.valueOf(info.money)));
            event.setCancelled(true);
            return;
          }

          if (!player.hasPermission("nextrtp.bypass.cooldown"))
            info.cooldown.toCooldown(player);

          if (service.available() && !player.hasPermission("nextrtp.bypass.money")) {
            service.take(player, info.money);
            Messager.CHARGED.accept(
                player, Placeholder.unparsed("money", String.valueOf(info.money)));
          }

          player.teleportAsync(loc.add(0, 1, 0));

          Bukkit.getPluginManager().callEvent(
              new RandomTeleportEndEvent(player, info, event.identity));
        });
  }

  private void processSingleAttemptAsync(final RandomTeleportPreEndEvent event) {
    final var info = event.teleportInfo;
    final var player = event.player;
    final var condition = info.posCondition;
    final World world = event.world;

    final double[] relativeCoords = RandomPointGeneratorService.getRandomPoint(info);
    final int x = (int) relativeCoords[0];
    final int z = (int) relativeCoords[1];

    world
        .getChunkAtAsync(x >> 4, z >> 4)
        .thenAcceptAsync(chunk -> {
          final ChunkSnapshot snapshot = chunk.getChunkSnapshot(true, true, false);
          final int y = getHighestBlockFromY(snapshot, x & 15, z & 15, info.minY, info.maxY);

          final Location loc = new Location(world, relativeCoords[0], y, relativeCoords[1]);

          if (condition != null) {
            final var processorArray = condition.processors();
            final var contextual = new ContextualTeleport(new ChunkLocationSnapshot(snapshot, loc), info, player);

            boolean thrown = false;
            for (ConditionProcessorContainer<?> conditionProcessor : processorArray) {
              if (thrown)
                break;
              try {
                conditionProcessor.check(contextual, ConditionType.POS);
              } catch (final InvalidConditionException e) {
                Messager.FAILED.accept(player, Placeholder.component("reason", e.component));
                thrown = true;
              }
            }

            if (thrown) {
              event.setCancelled(true);
              return;
            }
          }

          if (!service.has(player, info.money) && !player.hasPermission("nextrtp.bypass.money")) {
            Messager.NO_MONEY.accept(
                player, Placeholder.unparsed("money", String.valueOf(info.money)));
            event.setCancelled(true);
            return;
          }

          if (!player.hasPermission("nextrtp.bypass.cooldown"))
            info.cooldown.toCooldown(player);

          if (service.available() && !player.hasPermission("nextrtp.bypass.money")) {
            service.take(player, info.money);
            Messager.CHARGED.accept(
                player, Placeholder.unparsed("money", String.valueOf(info.money)));
          }

          player.teleportAsync(loc.add(0, 1, 0));

          Bukkit.getPluginManager().callEvent(
              new RandomTeleportEndEvent(player, info, event.identity));
        });
  }

  private int getHighestBlockFromY(
      ChunkSnapshot snapshot,
      int x,
      int z,
      int startY,
      int endY) {
    for (int y = startY; y >= endY; y--) {
      Material material = snapshot.getBlockType(x, y, z);
      if (material.isSolid() && material != Material.AIR) {
        return y;
      }
    }
    return snapshot.getHighestBlockYAt(x, z);
  }
}
