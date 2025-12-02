package io.github.krys.nextrtp.module.sound.configuration;

import io.github.krys.nextrtp.module.sound.wrapper.SoundWrapper;
import net.kyori.adventure.sound.Sound;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import dev.dejvokep.boostedyaml.block.implementation.Section;

import java.util.Map;

@NullMarked
public record SoundSection(@Nullable Sound delay, @Nullable Sound success, @Nullable Sound fail) {

  public SoundSection(Map<String, Object> map) {
    this(
      SoundBuilder.build((Section) map.get("delay")),
      SoundBuilder.build((Section) map.get("success")),
      SoundBuilder.build((Section) map.get("fail"))
    );
  }

  private static final class SoundBuilder {

    @Nullable
    private static Sound build(@Nullable Section map) {
      Sound sound = null;

      if (map != null) {
        var soundType = SoundWrapper.valueOf((String) map.get("sound"));
        if (soundType == null) return null;
        sound = Sound.sound()
          .type(soundType)
          .source(Sound.Source.MASTER)
          .volume((float) map.getFloat("volume", 1.0f))
          .pitch((float) map.getFloat("pitch", 1.0f))
          .build();
      }
      return sound;
    }
  }
}
