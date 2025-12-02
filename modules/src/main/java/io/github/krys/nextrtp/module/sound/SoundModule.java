package io.github.krys.nextrtp.module.sound;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.module.sound.configuration.SoundConfiguration;
import io.github.krys.nextrtp.module.sound.listener.RandomTeleportEndListener;
import io.github.krys.nextrtp.module.sound.listener.RandomTeleportProcessListener;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.core.module.AbstractModule;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public final class SoundModule extends AbstractModule {

  private final @NotNull JavaPlugin plugin;

  public SoundModule(@NotNull JavaPlugin plugin) {
    super("SoundModule", "sound");
    this.plugin = plugin;
  }

  @Override
  public void onEnable(@NotNull PluginBridge plugin) {
    final var config = new SoundConfiguration(() -> {
      final File file = new File(this.plugin.getDataFolder(), "sound/sound.yml");
      final InputStream stream = this.plugin.getResource("sound/sound.yml");
      assert stream != null;
      return YamlDocument.create(file, stream);
    });
    plugin.addReloadableElement(config);
    plugin.addEventBusListener(new RandomTeleportProcessListener(config));

    Bukkit.getPluginManager().registerEvents(new RandomTeleportEndListener(config), this.plugin);
  }
}
