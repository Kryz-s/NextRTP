package io.github.krys.nextrtp.common.condition.registry;

import io.github.krys.nextrtp.common.condition.ConditionProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class ConditionProcessorRegistry {

  private final @NotNull Map<String, ConditionProcessor<?>> processorMap;

  public ConditionProcessorRegistry() {
    this.processorMap = new HashMap<>();
  }

  public <T> void addProcessor(final @NotNull String key, final @NotNull ConditionProcessor<T> processor) {
    this.processorMap.put(key, processor);
  }

  @Nullable
  public <T> ConditionProcessor<T> getProcessor(final @NotNull String key) {
    return (ConditionProcessor<T>) this.processorMap.get(key);
  }
}
