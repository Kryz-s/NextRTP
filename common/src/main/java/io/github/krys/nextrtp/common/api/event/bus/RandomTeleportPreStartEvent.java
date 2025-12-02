package io.github.krys.nextrtp.common.api.event.bus;

import io.github.krys.nextrtp.common.asmbus.event.AbstractCancellableEvent;
import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RandomTeleportPreStartEvent extends AbstractCancellableEvent {
  public final @Nullable BaseTeleportInfo teleportInfo;
  public final @NotNull Player player;
  public final @NotNull TeleportIdentity identity;
  public final @Nullable CheckStatementValidator validator;

  public RandomTeleportPreStartEvent(@Nullable BaseTeleportInfo teleportInfo, @NotNull Player player, @NotNull TeleportIdentity identity, @Nullable CheckStatementValidator validator) {
    this.teleportInfo = teleportInfo;
    this.player = player;
    this.identity = identity;
    this.validator = validator;
  }
}
