package io.github.krys.nextrtp.module.animation.listener;

import io.github.krys.nextrtp.module.animation.channel.PacketChannelDispatcher;
import io.netty.channel.Channel;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Set;
import java.util.UUID;

public final class PlayerJoinListener implements Listener {

  private static final Set<UUID> INJECTED_PLAYERS = new ObjectOpenHashSet<>();
  private static final String DISPATCHER = "animation-packet-channel-dispatcher";

  @EventHandler
  public void onJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    if (INJECTED_PLAYERS.contains(uuid)) return;

    Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;

    if (channel.pipeline().get(DISPATCHER) == null) {
      channel.pipeline().addBefore("packet_handler", DISPATCHER, new PacketChannelDispatcher(player));
    }

    INJECTED_PLAYERS.add(uuid);
  }

  @EventHandler
  public void onLeave(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    UUID uuid = player.getUniqueId();

    if (!INJECTED_PLAYERS.contains(uuid)) return;

    Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;

    channel.eventLoop().execute(() -> {
      if (channel.pipeline().get(DISPATCHER) != null) {
        channel.pipeline().remove(DISPATCHER);
      }
    });

    INJECTED_PLAYERS.remove(uuid);
  }
}
