package io.github.krys.nextrtp.common.condition;

import io.github.krys.nextrtp.common.condition.excp.InvalidConditionException;
import io.github.krys.nextrtp.common.obj.ContextualTeleport;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public final class ConditionProcessorContainer<T> {

  private final Optional<T> obj;
  private final ConditionProcessor<T> conditionProcessor;

  public ConditionProcessorContainer(T obj, ConditionProcessor<T> conditionProcessor) {
    this.obj = Optional.of(obj);
    this.conditionProcessor = conditionProcessor;
  }

  public void check(ContextualTeleport teleport, ConditionType type) throws InvalidConditionException {
    this.conditionProcessor.onCondition(teleport, obj, type);
  }
}
