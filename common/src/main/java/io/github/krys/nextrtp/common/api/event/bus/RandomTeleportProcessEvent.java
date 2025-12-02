package io.github.krys.nextrtp.common.api.event.bus;

import io.github.krys.nextrtp.common.asmbus.event.AbstractCancellableEvent;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class RandomTeleportProcessEvent extends AbstractCancellableEvent {

  public final @NotNull Player player;
  public final @NotNull BaseTeleportInfo teleportInfo;
  public final @NotNull TeleportIdentity id;
  public final int seconds;

  public RandomTeleportProcessEvent(@NotNull Player who, @NotNull BaseTeleportInfo teleportInfo, @NotNull TeleportIdentity id, int seconds) {
    this.player = who;
    this.teleportInfo = teleportInfo;
    this.id = id;
    this.seconds = seconds;
  }


  @NotNull
  public BaseTeleportInfo getTeleportInfo() {
    return teleportInfo;
  }

  @NotNull
  public TeleportIdentity getIdentity() {
    return id;
  }

  public int getSeconds() {
    return seconds;
  }
}
