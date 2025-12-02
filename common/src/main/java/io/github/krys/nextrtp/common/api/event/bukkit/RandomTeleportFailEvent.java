package io.github.krys.nextrtp.common.api.event.bukkit;

import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class RandomTeleportFailEvent extends PlayerEvent {

  private static final HandlerList handlerList = new HandlerList();
  private final @NotNull BaseTeleportInfo info;
  private final @NotNull TeleportIdentity identity;
  private final @NotNull CheckStatementValidator validator;

  public RandomTeleportFailEvent(@NotNull Player who, @NotNull BaseTeleportInfo info, @NotNull TeleportIdentity identity, @NotNull CheckStatementValidator validator) {
    super(who);
    this.info = info;
    this.identity = identity;
    this.validator = validator;
  }

  @NotNull
  public BaseTeleportInfo getTeleportInfo() {
    return info;
  }

  @NotNull
  public TeleportIdentity getIdentity() {
    return identity;
  }

  @NotNull
  public CheckStatementValidator getValidator() {
    return validator;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }

}
