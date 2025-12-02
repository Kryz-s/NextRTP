package io.github.krys.nextrtp.module.animation;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.module.animation.config.AnimationUIConfiguration;
import io.github.krys.nextrtp.module.animation.listener.PlayerJoinListener;
import io.github.krys.nextrtp.module.animation.listener.RandomTeleportPreListener;
import io.github.krys.nextrtp.module.animation.service.AnimationService;
import io.github.krys.nextrtp.module.animation.task.AnimationUIController;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.module.PluginModule;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import io.github.krys.nextrtp.core.service.papi.PlaceholderAPIService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.concurrent.TimeUnit;

public final class AnimationTitleModule extends PluginModule {

  private final JavaPlugin javaPlugin;

  public AnimationTitleModule(JavaPlugin javaPlugin) {
    super("animation-title");
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void onEnable(@NotNull PluginBridge plugin) {
    final var animations = new AnimationUIConfiguration(javaPlugin.getDataFolder(),
      javaPlugin.getResource("animation/animations.yml"));
    final var config = new AbstractConfiguration(() -> YamlDocument.create(new File(javaPlugin.getDataFolder(), "animation/config.yml"), javaPlugin.getResource("animation/config.yml")));

    plugin.addReloadableElement(animations);
    plugin.addReloadableElement(config);

    final var placeholderApi = plugin.getServiceRegistry().get(PlaceholderAPIService.class);
    final var controller = new AnimationUIController(placeholderApi);

    final Runnable initTask = () -> Bukkit.getAsyncScheduler().runAtFixedRate(javaPlugin, controller, 1L, 10L, TimeUnit.MILLISECONDS);

    final AnimationService service = new AnimationService(animations, config, initTask);

    Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), javaPlugin);

    final var listener = new RandomTeleportPreListener(service);
    Bukkit.getPluginManager().registerEvents(listener, javaPlugin);
    plugin.addEventBusListener(listener);
  }
}
