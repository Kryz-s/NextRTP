package io.github.krys.nextrtp.module.animation.config;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import io.github.krys.nextrtp.module.animation.model.UIContainer;
import io.github.krys.nextrtp.common.placeholder.LegacyParser;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class AnimationUIConfiguration extends AbstractConfiguration {

  public AnimationUIConfiguration(@NotNull File file, InputStream stream) {
    super(() -> YamlDocument.create(new File(file, "animation/animations.yml"), stream));
  }

  public UIContainer getUI(String key) {
    if (key == null) return null;
    if (!key.startsWith("animations.")) key = "animations." + key;
    UIFactory factory = (UIFactory) this.customCache.get(key);
    if (factory == null) {
      final Section section = this.getSection(key);
      if (section == null) return null;
      factory = new UIFactory(section.getInt("interval"), section.getStringList("value"));
      this.customCache.put(key, factory);
    }
    return factory.generateUI();
  }

  public final static class UIFactory {
    private final int delay;
    private final List<String> lines;

    public UIFactory(int delay, List<String> lines) {
      this.delay = delay;
      this.lines = new ArrayList<>();

      lines.forEach(string -> this.lines.add(LegacyParser.parse(string)));
    }

    public UIContainer generateUI() {
      return new UIContainer(delay, lines);
    }
  }

}
