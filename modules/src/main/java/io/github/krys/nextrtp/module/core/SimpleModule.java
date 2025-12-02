package io.github.krys.nextrtp.module.core;

import io.github.krys.nextrtp.module.core.command.RtpWorldCommand;
import io.github.krys.nextrtp.module.core.command.RtpSudoWorldCommand;
import io.github.krys.nextrtp.module.core.command.SimpleCommandImpl;
import io.github.krys.nextrtp.module.core.listener.RandomTeleportPreListener;
import io.github.krys.nextrtp.module.core.placeholder.WorldTeleportInfoPlaceholder;
import io.github.krys.nextrtp.module.core.provider.WorldTeleportInfoProvider;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.command.CommandManager;
import io.github.krys.nextrtp.core.configuration.rtp.RtpConfiguration;
import io.github.krys.nextrtp.core.module.AbstractModule;
import io.github.krys.nextrtp.core.papi.PlaceholderRepository;
import io.github.krys.nextrtp.core.service.loader.WorldTeleportInfoLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public final class SimpleModule extends AbstractModule {

  private final @NotNull JavaPlugin javaPlugin;
  private WorldTeleportInfoProvider provider;

  public SimpleModule(@NotNull JavaPlugin javaPlugin) {
    super("SimpleModule", "simple");
    this.javaPlugin = javaPlugin;
  }

  @Override
  public void onEnable(@NotNull PluginBridge plugin) {
    plugin.addReloadableElement(Registries.WORLD_TELEPORT_INFO);
    final var rtpConfig = new RtpConfiguration(new File(javaPlugin.getDataFolder(), "rtp/rtp.yml"),
        javaPlugin.getResource("rtp/rtp.yml"));
    plugin.addReloadableElement(rtpConfig);
    plugin.addReloadableElement(new WorldTeleportInfoLoader(rtpConfig));

    plugin.addEventBusListener(new RandomTeleportPreListener());
    provider = new WorldTeleportInfoProvider(rtpConfig);
    PlaceholderRepository.register(new WorldTeleportInfoPlaceholder());
  }

  @Override
  public void handleCommand(@NotNull CommandManager manager, @NotNull PluginBridge pluginBridge) {
    final var rtpWorldCommand = new RtpWorldCommand(pluginBridge.getAPI(), provider);
    manager.register(rtpWorldCommand);
    manager.register(new RtpSudoWorldCommand(rtpWorldCommand, pluginBridge.getAPI()));
    manager.setSimpleCommand(new SimpleCommandImpl(pluginBridge.getAPI(), provider));
  }
}
