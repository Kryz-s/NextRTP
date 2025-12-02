package io.github.krys.nextrtp.core.service.loader;

import io.github.krys.nextrtp.common.reload.Loader;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class WorldTeleportInfoLoader implements Loader {

  private final AbstractConfiguration configuration;

  public WorldTeleportInfoLoader(@NotNull AbstractConfiguration configuration) {
    this.configuration = configuration;
  }

  @SuppressWarnings("unchecked")
  public void load() {
    final var sectionList = configuration.getSection("teleport");

    final var registry = Registries.WORLD_TELEPORT_INFO;

    final var list = sectionList.getMapList("per-world-area");

    for (Map<?, ?> infoMap : list) {
      registry.add((String) infoMap.get("id"), new WorldTeleportInfo((Map<String, Object>) infoMap));
    }
  }
}
