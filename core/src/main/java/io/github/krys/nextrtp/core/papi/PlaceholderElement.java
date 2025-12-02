package io.github.krys.nextrtp.core.papi;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public abstract class PlaceholderElement {
  private final String identifier;

  public PlaceholderElement(String identifier) {
    this.identifier = identifier;
  }

  public String identifier() {
    return this.identifier;
  }

  public abstract int minArguments();

  @Nullable
  public abstract String onRequest(Player player, Arguments arguments);
}
