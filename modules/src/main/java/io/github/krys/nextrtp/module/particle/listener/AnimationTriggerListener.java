package io.github.krys.nextrtp.module.particle.listener;

import io.github.krys.nextrtp.module.particle.config.ParticleConfiguration;
import io.github.krys.nextrtp.module.particle.service.ParticleService;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportFailEvent;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportStartEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreEndEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class AnimationTriggerListener implements Listener {

  private final ParticleService particleService;
  private final ParticleConfiguration configuration;

  public AnimationTriggerListener(ParticleService particleService, ParticleConfiguration configuration) {
    this.particleService = particleService;
    this.configuration = configuration;
  }

  @EventHandler
  public void onTeleportFail(RandomTeleportFailEvent event) {
    particleService.stopAnimation(event.getPlayer());
  }

  @EventHandler
  public void onTeleportStart(RandomTeleportStartEvent event) {

    final var animation = configuration.getScript(event.getTeleportInfo().id, event.getIdentity());

    System.out.println(animation);
    if (animation == null) return;
    particleService.startAnimation(event.getPlayer(), animation, event.getTeleportInfo());
  }

  @io.github.krys.nextrtp.common.asmbus.listener.Listener(priority = Byte.MIN_VALUE)
  public void onStop(RandomTeleportPreEndEvent event) {
    particleService.stopAnimation(event.player);
  }
}