package io.github.krys.nextrtp.module.sign;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.module.sign.listener.SignListener;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.module.PluginModule;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;

public final class SignModule extends PluginModule {

  private final AbstractConfiguration signConfig;

  private final JavaPlugin plugin;

  public SignModule(JavaPlugin plugin) {
    super("SignModule", "sign");

    this.signConfig = new AbstractConfiguration(() -> {
      final File file = new File(plugin.getDataFolder(), "sign/sign.yml");
      final InputStream stream = plugin.getResource("sign/sign.yml");
      if (stream == null)
        return YamlDocument.create(file);
      return YamlDocument.create(file, stream);
    });

    this.plugin = plugin;
  }

  @Override
  public void onEnable(@NotNull PluginBridge plugin) {
    plugin.addReloadableElement(signConfig);
    Bukkit.getPluginManager().registerEvents(new SignListener(signConfig, plugin.getAPI()), this.plugin);
  }
}
