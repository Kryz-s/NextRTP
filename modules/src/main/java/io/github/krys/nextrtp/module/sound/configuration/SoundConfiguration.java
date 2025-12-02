package io.github.krys.nextrtp.module.sound.configuration;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.common.configuration.ConfigSupplier;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class SoundConfiguration extends AbstractConfiguration {

  public SoundConfiguration(@NotNull ConfigSupplier<YamlDocument> document) {
    super(document);
  }

  @Nullable
  public SoundSection getSoundSection(@NotNull String key, @NotNull TeleportIdentity identity) {
    final String fullKey = identity.id() + "." + key;
    SoundSection section = (SoundSection) super.customCache.get(fullKey);
    if(section != null) return section;

    return (SoundSection) super.customCache.computeIfAbsent(fullKey, k -> {
      Map<String, Object> soundMap = this.getMap(k);
      if (soundMap == null || soundMap.isEmpty()) {
        soundMap = this.getMap(identity.id() + ".default");
        if (soundMap == null || soundMap.isEmpty()) return null;
      }
      return new SoundSection(soundMap);
    });
  }
}
