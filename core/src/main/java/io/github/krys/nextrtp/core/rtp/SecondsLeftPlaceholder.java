package io.github.krys.nextrtp.core.rtp;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.krys.nextrtp.core.papi.Arguments;
import io.github.krys.nextrtp.core.papi.PlaceholderElement;

public final class SecondsLeftPlaceholder extends PlaceholderElement {

  private final TeleportPendingRepository repository;
  private static final String ZERO = "0";

  public SecondsLeftPlaceholder(TeleportPendingRepository repository) {
    super("teleport_seconds_left");
    this.repository = repository;
  }

  @Override
  public int minArguments() {
    return 0;
  }

  @Override
  public @Nullable String onRequest(Player player, Arguments a) { 
    final TeleportPending pending = repository.get(player);
    if (pending == null) return ZERO;
    return String.valueOf(pending.times);
  }

}
