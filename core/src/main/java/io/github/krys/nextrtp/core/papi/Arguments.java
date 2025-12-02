package io.github.krys.nextrtp.core.papi;

import org.jetbrains.annotations.Nullable;

public final class Arguments {
  private final String[] arguments;
  private int index = 0;

  public Arguments(String[] arguments) {
    this.arguments = arguments;
  }

  public boolean hasNext() {
    return index < arguments.length;
  }

  @Nullable
  public String next() {
    if (!hasNext()) return null;
    return arguments[index++];
  }
}
