package io.github.krys.nextrtp.module.particle;

import io.github.krys.nextrtp.module.particle.config.ParticleConfiguration;
import io.github.krys.nextrtp.module.particle.listener.AnimationTriggerListener;
import io.github.krys.nextrtp.module.particle.script.ScriptManager;
import io.github.krys.nextrtp.module.particle.service.ParticleService;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.core.module.AbstractModule;
import io.github.krys.nextrtp.core.service.schedule.MinecraftScheduleService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class ParticleModule extends AbstractModule {

  private final JavaPlugin javaPlugin;

  public ParticleModule(JavaPlugin javaPlugin) {
    super("ParticleModule", "particles");
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void onEnable(@NotNull PluginBridge plugin) {

    ScriptManager scriptManager = new ScriptManager(javaPlugin);

    final var service = new ParticleService(plugin.getServiceRegistry().get(MinecraftScheduleService.class));

    plugin.getServiceRegistry().add(service);

    final ParticleConfiguration particleConfiguration = new ParticleConfiguration(javaPlugin.getDataFolder(), javaPlugin.getResource("particles/config.yml"), scriptManager);

    plugin.addReloadableElement(particleConfiguration);
    plugin.addReloadableElement(scriptManager);

    final var listener =
      new AnimationTriggerListener(service, particleConfiguration);

    Bukkit.getPluginManager().registerEvents(listener,
      javaPlugin
    );

    plugin.addEventBusListener(listener);
  }

  @Override
  public void onDisable(@NotNull PluginBridge plugin) {
    plugin.getServiceRegistry().get(ParticleService.class).shutdown();
  }
}