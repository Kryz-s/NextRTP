package io.github.krys.nextrtp.common.condition;

import org.jetbrains.annotations.NotNull;

public record Condition(@NotNull ConditionProcessorContainer<?>... processors) {}
