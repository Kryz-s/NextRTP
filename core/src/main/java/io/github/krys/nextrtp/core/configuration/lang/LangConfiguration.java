package io.github.krys.nextrtp.core.configuration.lang;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import io.github.krys.nextrtp.common.placeholder.LegacyParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class LangConfiguration extends AbstractConfiguration {

  public LangConfiguration(File file, @Nullable InputStream stream) {
    super(() -> {
      try {
        if (stream == null)
          return YamlDocument.create(file,
            UpdaterSettings.builder().setVersioning(new BasicVersioning("lang-version")).build(),
            GeneralSettings.builder().setDefaultString(null).build());
        return YamlDocument.create(file, stream,
          UpdaterSettings.builder().setVersioning(new BasicVersioning("lang-version")).build(),
          GeneralSettings.builder().setDefaultString(null).build());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  @Nullable
  public String getParsedMiniMessageString(final @NotNull String key) {
    final @Nullable String msg = this.getString(key, null);
    if (msg == null) return null;
    return LegacyParser.parse(msg);
  }
}
