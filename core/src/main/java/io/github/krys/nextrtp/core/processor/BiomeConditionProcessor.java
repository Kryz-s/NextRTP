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
import net.kyori.adventure.translation.Translatable;
import org.bukkit.Keyed;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class BiomeConditionProcessor implements ConditionProcessor<List<String>> {

  @Override
  public void onCondition(@NotNull ContextualTeleport ctx,
                          @NotNull Optional<List<String>> biomes,
                          @NotNull ConditionType type) throws InvalidConditionException {

    DebugService.debug(() -> "BiomeCondition process, type: " + type);

    final ChunkLocationSnapshot snapshot = ctx.snapshot();

    Location loc = snapshot.location();
    int chunkX = loc.getBlockX() & 15;
    int chunkZ = loc.getBlockZ() & 15;

    final Keyed biomeKeyed = snapshot.snapshot().getBiome(chunkX, loc.getBlockY(), chunkZ);
    final String translatableKey = ((Translatable) biomeKeyed).translationKey();

    final var keyString = biomeKeyed.key().asString();

    if (biomes.isEmpty()) {
      DebugService.debug(() -> "Biome list empty, returning.");
      return;
    }

    if (!biomes.get().contains(keyString)) {
      DebugService.debug(() -> "List doesn't contains Biome");
      DebugService.debug(() -> "List: {}", biomes);
      DebugService.debug(() -> "Biome: {}", keyString);
      return;
    }

    DebugService.debug(() -> "Throwing InvalidConditionException...");

    throw new InvalidConditionException(
      FakeComponent.asComponent(
        "modules.core.teleport.reasons.invalid-biome",
        ctx.player(),
        Placeholder.component("biome", Component.translatable(translatableKey))
      )
    );
  }
}
