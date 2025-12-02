package io.github.krys.nextrtp.common.condition;

import io.github.krys.nextrtp.common.condition.excp.InvalidConditionException;
import io.github.krys.nextrtp.common.obj.ContextualTeleport;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ConditionProcessor<C> {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    void onCondition(@NotNull ContextualTeleport ctx,
                     @NotNull Optional<C> condition,
                     @NotNull ConditionType type) throws InvalidConditionException;
}
