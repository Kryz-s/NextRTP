package io.github.krys.nextrtp.core.rtp;

import io.github.krys.nextrtp.common.EventBusDispatcher;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportStartEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreStartEvent;
import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.core.papi.PlaceholderRepository;
import io.github.krys.nextrtp.core.papi.Placeholders;
import io.github.krys.nextrtp.core.service.schedule.MinecraftScheduleService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class TeleportPendingRepository {

  private static final Map<UUID, TeleportPending> PENDING = new HashMap<>();

  private final MinecraftScheduleService scheduleService;

  public TeleportPendingRepository(MinecraftScheduleService scheduleService) {
    this.scheduleService = scheduleService;
    PlaceholderRepository.register(new SecondsLeftPlaceholder(this));
  }

  public void start(Player player, BaseTeleportInfo info, @Nullable CheckStatementValidator validator, TeleportIdentity identity) {

    final var event = new RandomTeleportPreStartEvent(info, player, identity, validator);

    EventBusDispatcher.instance().call(event, false);

    if (event.isCancelled()) return;

    final Runnable onStop = () -> PENDING.remove(player.getUniqueId());

    final var teleportPending = new TeleportPending(player, info, validator, identity, onStop);

    scheduleService.runSchedule(teleportPending, 20L);

    Bukkit.getPluginManager().callEvent(new RandomTeleportStartEvent(player, info, identity, validator));

    PENDING.put(player.getUniqueId(), teleportPending);
  }

  @Nullable
  public TeleportPending get(Player player) {
    return PENDING.get(player.getUniqueId());
  }
}
