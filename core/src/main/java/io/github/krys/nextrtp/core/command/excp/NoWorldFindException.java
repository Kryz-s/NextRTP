package io.github.krys.nextrtp.core.command.excp;

import org.jetbrains.annotations.NotNull;

public final class NoWorldFindException extends RuntimeException {

  public final @NotNull String input;

  public NoWorldFindException(@NotNull String input) {
    this.input = input;
  }
}
