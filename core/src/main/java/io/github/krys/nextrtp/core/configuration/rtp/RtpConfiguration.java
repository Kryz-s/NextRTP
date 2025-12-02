package io.github.krys.nextrtp.core.configuration.rtp;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class RtpConfiguration extends AbstractConfiguration {

    public RtpConfiguration(File file, InputStream stream) {
        super(() -> {
            try {
                return YamlDocument.create(file, stream,
                        UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build(),
                        GeneralSettings.builder().setDefaultString(null).build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
