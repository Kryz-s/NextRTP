package io.github.krys.nextrtp.core.processor;

import com.google.common.reflect.TypeToken;
import io.github.krys.nextrtp.common.condition.ConditionProcessor;
import io.github.krys.nextrtp.common.condition.ConditionType;
import io.github.krys.nextrtp.common.condition.excp.InvalidConditionException;
import io.github.krys.nextrtp.common.logger.DebugService;
import io.github.krys.nextrtp.common.obj.ChunkLocationSnapshot;
import io.github.krys.nextrtp.common.obj.ContextualTeleport;
import io.github.krys.nextrtp.core.message.FakeComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class BlockConditionProcessor implements ConditionProcessor<List<String>> {
  
  @Override
  public void onCondition(@NotNull ContextualTeleport ctx, @NotNull Optional<List<String>> condition, @NotNull ConditionType type) throws InvalidConditionException {
    DebugService.debug(() -> "BlockCondition process...");

    final ChunkLocationSnapshot snapshot = ctx.snapshot();
    Location loc = snapshot.location();
    int chunkX = loc.getBlockX() & 15;
    int chunkZ = loc.getBlockZ() & 15;
    final BlockData block = snapshot.snapshot().getBlockData(chunkX, loc.getBlockY(), chunkZ);
    final BlockData lowBlock = snapshot.snapshot().getBlockData(chunkX, loc.getBlockY() - 1, chunkZ);

    final var material = block.getMaterial().name();
    final var lowMaterial = lowBlock.getMaterial().name();

    if (condition.isEmpty()) return;
    if (condition.get().contains(material)) {

      DebugService.debug(() -> "Throwing InvalidConditionException...");

      throw new InvalidConditionException(
          FakeComponent.asComponent(
              "modules.core.teleport.reasons.invalid-block",
              ctx.player(),
              Placeholder.component("block", Component.translatable(block.getMaterial().translationKey()))));
    }
    if (condition.get().contains(lowMaterial)) {
      
      DebugService.debug(() -> "Throwing InvalidConditionException...");

      throw new InvalidConditionException(
          FakeComponent.asComponent(
              "modules.core.teleport.reasons.invalid-block",
              ctx.player(),
              Placeholder.component("block", Component.translatable(lowBlock.getMaterial().translationKey()))));
    }
  }
}
