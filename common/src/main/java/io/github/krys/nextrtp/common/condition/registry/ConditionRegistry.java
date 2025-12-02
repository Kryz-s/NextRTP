package io.github.krys.nextrtp.common.condition.registry;

import io.github.krys.nextrtp.common.condition.Condition;
import io.github.krys.nextrtp.common.condition.ConditionType;
import io.github.krys.nextrtp.common.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class ConditionRegistry extends Registry<ConditionType, Registry<String, Condition>> {

  public ConditionRegistry() {
    super(new HashMap<>());
    super.add(ConditionType.POS, new PosConditionRegistry(new HashMap<>()));
    super.add(ConditionType.PRE, new PreConditionRegistry(new HashMap<>()));
  }

  @Override
  public void add(@NotNull ConditionType key, @NotNull Registry<String, Condition> value) { }

  @Override
  public void reload() {
    this.get(ConditionType.PRE).reload();
    this.get(ConditionType.POS).reload();
  }

  static final class PosConditionRegistry extends Registry<String, Condition> {

    public PosConditionRegistry(Map<String, Condition> registries) {
      super(registries);
    }
  }

  static final class PreConditionRegistry extends Registry<String, Condition> {

    public PreConditionRegistry(Map<String, Condition> registries) {
      super(registries);
    }
  }
}
