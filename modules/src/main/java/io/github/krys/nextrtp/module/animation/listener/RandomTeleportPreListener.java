package io.github.krys.nextrtp.module.animation.listener;

import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportFailEvent;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportStartEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreEndEvent;
import io.github.krys.nextrtp.module.animation.service.AnimationService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class RandomTeleportPreListener implements Listener {

  private final AnimationService service;

  public RandomTeleportPreListener(AnimationService service) {
    this.service = service;
  }

  @EventHandler(ignoreCancelled = true)
  public void onStart(RandomTeleportStartEvent event) {
    final String key = event.getIdentity().id() + "." + event.getTeleportInfo().id;
    service.startAnimation(event.getPlayer(), key);
  }
  
  public void onFail(RandomTeleportFailEvent event) {
    service.stopAnimation(event.getPlayer());
  }

  @io.github.krys.nextrtp.common.asmbus.listener.Listener
  public void onStop(RandomTeleportPreEndEvent event) {
    service.stopAnimation(event.player);
  }
}
