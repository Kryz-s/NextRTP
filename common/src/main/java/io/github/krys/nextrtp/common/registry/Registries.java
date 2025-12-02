package io.github.krys.nextrtp.common.registry;

import io.github.krys.nextrtp.common.condition.registry.ConditionProcessorRegistry;
import io.github.krys.nextrtp.common.condition.registry.ConditionRegistry;
import io.github.krys.nextrtp.common.info.registry.WorldTeleportInfoRegistry;

public final class Registries {
  public static final ConditionProcessorRegistry CONDITION_PROCESSOR;

  public static final ConditionRegistry CONDITION;

  public static final WorldTeleportInfoRegistry WORLD_TELEPORT_INFO;

  static {
    CONDITION = new ConditionRegistry();
    CONDITION_PROCESSOR = new ConditionProcessorRegistry();
    WORLD_TELEPORT_INFO = new WorldTeleportInfoRegistry();
  }
}
