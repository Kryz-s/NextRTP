package io.github.krys.nextrtp.module.sound.listener;

import io.github.krys.nextrtp.module.sound.configuration.SoundConfiguration;
import io.github.krys.nextrtp.module.sound.configuration.SoundSection;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportProcessEvent;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.NotNull;

public final class RandomTeleportProcessListener {

  private final @NotNull SoundConfiguration soundConfiguration;

  public RandomTeleportProcessListener(@NotNull SoundConfiguration soundConfiguration) {
    this.soundConfiguration = soundConfiguration;
  }

  @Listener(priority = 120)
  public void onProcess(final @NotNull RandomTeleportProcessEvent event) {
    String key = event.teleportInfo.id;

    final SoundSection soundSection = soundConfiguration.getSoundSection(key, event.getIdentity());

    if (soundSection == null) return;
    final Sound sound = soundSection.delay();
    if (sound == null) return;
    event.player.playSound(sound);
  }
}
