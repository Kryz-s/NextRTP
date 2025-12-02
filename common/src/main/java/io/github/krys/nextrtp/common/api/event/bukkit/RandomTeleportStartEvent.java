package io.github.krys.nextrtp.common.api.event.bukkit;

import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class RandomTeleportStartEvent extends AbstractCancellableEvent {
  private static final HandlerList handlerList = new HandlerList();
  private final BaseTeleportInfo teleportInfo;
  private final TeleportIdentity identity;
  private final @Nullable CheckStatementValidator validator;

  public RandomTeleportStartEvent(@NotNull Player player, @NotNull BaseTeleportInfo teleportInfo, TeleportIdentity identity, @Nullable CheckStatementValidator validator) {
    super(player);
    this.teleportInfo = teleportInfo;
    this.identity = identity;
    this.validator = validator;
  }

  public RandomTeleportStartEvent(Player player, BaseTeleportInfo teleportInfo, TeleportIdentity identity) {
    this(player, teleportInfo, identity, null);
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return handlerList;
  }

  public static HandlerList getHandlerList() {
    return handlerList;
  }

  public TeleportIdentity getIdentity() {
    return identity;
  }

  @Nullable
  public CheckStatementValidator getValidator() {
    return validator;
  }

  public BaseTeleportInfo getTeleportInfo() {
    return teleportInfo;
  }
}