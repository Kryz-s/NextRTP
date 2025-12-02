package io.github.krys.nextrtp.common.info.registry;

import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.common.registry.Registry;

import java.util.HashMap;

public final class WorldTeleportInfoRegistry extends Registry<String, WorldTeleportInfo> {

  public WorldTeleportInfoRegistry() {
    super(new HashMap<>());
  }
}
