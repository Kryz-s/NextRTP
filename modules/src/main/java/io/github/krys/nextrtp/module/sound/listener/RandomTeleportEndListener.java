package io.github.krys.nextrtp.module.sound.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportEndEvent;
import io.github.krys.nextrtp.common.api.event.bukkit.RandomTeleportFailEvent;
import io.github.krys.nextrtp.module.sound.configuration.SoundConfiguration;
import io.github.krys.nextrtp.module.sound.configuration.SoundSection;
import net.kyori.adventure.sound.Sound;

public final class RandomTeleportEndListener implements Listener {

  
  private final @NotNull SoundConfiguration soundConfiguration;

  public RandomTeleportEndListener(@NotNull SoundConfiguration soundConfiguration) {
    this.soundConfiguration = soundConfiguration;
  }

  @EventHandler
  public void onFail(RandomTeleportFailEvent event) {
    String key = event.getTeleportInfo().id;

    final SoundSection soundSection = soundConfiguration.getSoundSection(key, event.getIdentity());
    if (soundSection == null) return;

    final Sound sound = soundSection.fail();
    if (sound == null) return;

    event.getPlayer().playSound(sound);
  }

  @EventHandler
  public void onEnd(RandomTeleportEndEvent event) {
    String key = event.getTeleportInfo().id;

    final SoundSection soundSection = soundConfiguration.getSoundSection(key, event.getIdentity());
    if (soundSection == null) return;

    final Sound sound = soundSection.fail();
    if (sound == null) return;
    
    event.getPlayer().playSound(sound);
  }
}
