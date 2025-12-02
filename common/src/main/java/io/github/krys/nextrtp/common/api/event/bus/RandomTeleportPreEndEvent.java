package io.github.krys.nextrtp.common.api.event.bus;

import io.github.krys.nextrtp.common.asmbus.event.AbstractCancellableEvent;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RandomTeleportPreEndEvent extends AbstractCancellableEvent {
  public final @NotNull BaseTeleportInfo teleportInfo;
  public final @NotNull Player player;
  public final @NotNull TeleportIdentity identity;

  public @Nullable World world;

  public RandomTeleportPreEndEvent(@NotNull BaseTeleportInfo teleportInfo, @NotNull Player player, @NotNull TeleportIdentity identity) {
    this.teleportInfo = teleportInfo;
    this.player = player;
    this.identity = identity;
    this.world = player.getWorld();
  }
}
