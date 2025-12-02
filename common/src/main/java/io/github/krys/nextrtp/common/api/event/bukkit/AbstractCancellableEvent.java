package io.github.krys.nextrtp.common.api.event.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

abstract class AbstractCancellableEvent extends PlayerEvent implements Cancellable {

  private boolean isCancelled;

  public AbstractCancellableEvent(@NotNull Player who) {
    super(who);
  }

  @Override
  public boolean isCancelled() {
    return isCancelled;
  }

  @Override
  public void setCancelled(boolean b) {
    this.isCancelled = b;
  }

  @Override
  public abstract @NotNull HandlerList getHandlers();
}
