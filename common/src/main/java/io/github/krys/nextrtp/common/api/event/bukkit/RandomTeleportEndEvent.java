package io.github.krys.nextrtp.common.api.event.bukkit;

import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class RandomTeleportEndEvent extends PlayerEvent {
  private static final HandlerList handlerList = new HandlerList();

  private final @NotNull BaseTeleportInfo teleportInfo;
  private final @NotNull TeleportIdentity identity;

  public RandomTeleportEndEvent(@NotNull Player player, @NotNull BaseTeleportInfo teleportInfo, @NotNull TeleportIdentity identity) {
    super(player);
    this.teleportInfo = teleportInfo;
    this.identity = identity;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }

  @NotNull
  public BaseTeleportInfo getTeleportInfo() {
    return teleportInfo;
  }

  @NotNull
  public TeleportIdentity getIdentity() {
    return identity;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }
}