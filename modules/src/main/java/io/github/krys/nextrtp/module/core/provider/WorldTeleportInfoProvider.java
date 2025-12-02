package io.github.krys.nextrtp.module.core.provider;

import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.configuration.rtp.RtpConfiguration;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WorldTeleportInfoProvider {
  private final @NotNull RtpConfiguration rtpConfiguration;

  public WorldTeleportInfoProvider(@NotNull RtpConfiguration rtpConfiguration) {
    this.rtpConfiguration = rtpConfiguration;
  }

  @Nullable
  public WorldTeleportInfo getOrCreate(@NotNull String key, @Nullable World world) {
    final var element = Registries.WORLD_TELEPORT_INFO.get(key);
    if (element != null) return element;
    if (world == null) return null;
    final var defaultAreaSection = rtpConfiguration.getMap("teleport.default-area");

    if (defaultAreaSection == null) return null;
    final var newInfo = new WorldTeleportInfo(defaultAreaSection, world);
    Registries.WORLD_TELEPORT_INFO.add(world.getName(), newInfo);
    return newInfo;
  }
}
