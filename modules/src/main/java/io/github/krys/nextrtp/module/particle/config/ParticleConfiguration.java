package io.github.krys.nextrtp.module.particle.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.module.particle.script.AnimationScript;
import io.github.krys.nextrtp.module.particle.script.ScriptManager;
import io.github.krys.nextrtp.common.identity.TeleportIdentity;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;

public final class ParticleConfiguration extends AbstractConfiguration {
  private final @NotNull ScriptManager scriptManager;

  public ParticleConfiguration(@NotNull File file, @Nullable InputStream stream, @NotNull ScriptManager scriptManager) {
    super(() -> YamlDocument.create(new File(file, "particles/config.yml"), stream));
    this.scriptManager = scriptManager;
  }

  @Nullable
  public <T> AnimationScript<T> getScript(@Nullable String key, @NotNull TeleportIdentity identity) {
    if (key == null) return null;
    final String module = identity.id();
    final String scriptId = this.getString(module + "." + key, null);

    if (scriptId == null) return null;

    System.out.println(scriptId);

    return scriptManager.getAnimation(scriptId);
  }
}
