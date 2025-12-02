package io.github.krys.nextrtp.core.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportFailEvent;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.core.rtp.checker.DefaultLocationValidator;

public final class ServerLoadEventListener implements Listener {

  @EventHandler
  public void onFail(RandomTeleportFailEvent event) {
    final var validator = event.getValidator();

    if(!(validator instanceof DefaultLocationValidator)) return;

    Messager.MOVE.accept(event.getPlayer());
  }
}
