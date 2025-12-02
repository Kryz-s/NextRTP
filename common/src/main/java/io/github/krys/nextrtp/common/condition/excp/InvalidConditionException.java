package io.github.krys.nextrtp.common.condition.excp;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

public final class InvalidConditionException extends RuntimeException {
  @NotNull
  public final MessageType messageType;
  @NotNull
  public final Component component;

  public static final String EMPTY = "";

  public InvalidConditionException(final @NotNull Component message, final @NotNull MessageType messageType) {
    super(EMPTY);
    this.messageType = messageType;
    this.component = message;
  }

  public InvalidConditionException(final @NotNull Component message) {
    super(EMPTY);
    this.component = message;
    this.messageType = MessageType.KEY;
  }
}
