package io.github.krys.nextrtp.core.service.loader;

import io.github.krys.nextrtp.common.condition.Condition;
import io.github.krys.nextrtp.common.condition.ConditionProcessor;
import io.github.krys.nextrtp.common.condition.ConditionProcessorContainer;
import io.github.krys.nextrtp.common.condition.ConditionType;
import io.github.krys.nextrtp.common.logger.LoggerService;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.common.reload.Loader;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public final class ConditionLoader implements Loader {

  private final @NotNull AbstractConfiguration configuration;
  private final @NotNull LoggerService logger;

  public ConditionLoader(@NotNull AbstractConfiguration configuration, @NotNull LoggerService logger) {
    this.configuration = configuration;
    this.logger = logger;
  }

  public void load() {

    final List<Map<String, Object>> preList = configuration.getList("pre_conditions");

    preList.forEach(objectMap -> {

      final String id = (String) objectMap.get("id");

      final var nested = ((List<Map<String, Object>>) objectMap.get("conditions"));

      if (nested == null) return;
      if (nested.isEmpty()) return;

      final var registryTo = Registries.CONDITION;

      registryTo.get(ConditionType.PRE).add(id, buildCondition(nested));
    });

    final List<Map<String, Object>> posList = configuration.getList("pos_conditions");

    posList.forEach(objectMap -> {

      final String id = (String) objectMap.get("id");

      final var nested = ((List<Map<String, Object>>) objectMap.get("conditions"));

      if (nested == null) return;
      if (nested.isEmpty()) return;

      final var registryTo = Registries.CONDITION;

      registryTo.get(ConditionType.POS).add(id, buildCondition(nested));
    });
  }

  private Condition buildCondition(final @NotNull List<Map<String, Object>> obj) {
    final var conditionProcessors = new ConditionProcessorContainer<?>[obj.size()];

    for (int i = 0; i < obj.size(); i++) {
      final var registryFrom = Registries.CONDITION_PROCESSOR;
      final var objectMap = obj.get(i);
      final String id = (String) objectMap.get("id");
      final ConditionProcessor<?> processor = registryFrom.getProcessor(id);
      if(processor == null) {
        logger.err("The processor {} not exist, please, put a id of a available processor.", id);
        continue;
      }
      final Object condition = objectMap.get("condition");
      conditionProcessors[i] = new ConditionProcessorContainer<>(condition, (ConditionProcessor<Object>) processor);
    }

    return new Condition(conditionProcessors);
  }
}
