package io.github.krys.nextrtp.core.rtp;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.github.krys.nextrtp.common.EventBusDispatcher;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportEndEvent;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportFailEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreEndEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportProcessEvent;
import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.core.rtp.checker.DefaultLocationValidator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public final class TeleportPending implements Consumer<ScheduledTask> {

  private final Player player;
  private final BaseTeleportInfo info;
  private final @NotNull Location initLocation;
  private final @NotNull TeleportIdentity identity;
  private final @Nullable CheckStatementValidator statementValidator;
  private final @Nullable Runnable onStop;
  public int times;

  public static final CheckStatementValidator DEFAULT = new DefaultLocationValidator();

  private static final Consumer<Event> consumerEvent = Bukkit.getPluginManager()::callEvent;

  public TeleportPending(@NotNull Player player,
                         @NotNull BaseTeleportInfo info,
                         @Nullable CheckStatementValidator statementValidator,
                         @NotNull TeleportIdentity identity,
                         @Nullable Runnable onStop) {
    this.player = player;
    this.initLocation = player.getLocation();
    this.info = info;
    this.statementValidator = statementValidator;
    this.times = info.delay;
    this.identity = identity;
    this.onStop = onStop;
  }

  @Override
  public void accept(ScheduledTask scheduledTask) {
    if (!player.isOnline()) {
      scheduledTask.cancel();
      onStop.run();
      return;
    }

    if (times <= 0) {
      scheduledTask.cancel();
      onStop.run();
      final var endEvent = new RandomTeleportPreEndEvent(info, player, identity);
      EventBusDispatcher.instance().call(endEvent, false);
      if(endEvent.isCancelled()) {
        return;
      }
      consumerEvent.accept(new RandomTeleportEndEvent(player, info, identity));
      return;
    }

    if (statementValidator != null) {
      if (!statementValidator.validate(player, initLocation)) {
        scheduledTask.cancel();
        onStop.run();
        consumerEvent.accept(new RandomTeleportFailEvent(player, info, identity, statementValidator));
        return;
      }
    }

    EventBusDispatcher.instance().call(new RandomTeleportProcessEvent(player, info, identity, times), false);

    times--;
  }
}
