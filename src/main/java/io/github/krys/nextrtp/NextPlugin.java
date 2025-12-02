package io.github.krys.nextrtp;

import dev.dejvokep.boostedyaml.YamlDocument;
import io.github.krys.nextrtp.core.listener.ServerLoadEventListener;
import io.github.krys.nextrtp.module.animation.AnimationTitleModule;
import io.github.krys.nextrtp.module.core.SimpleModule;
import io.github.krys.nextrtp.module.particle.ParticleModule;
import io.github.krys.nextrtp.module.sign.SignModule;
import io.github.krys.nextrtp.module.sound.SoundModule;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.hook.HookRegistry;
import io.github.krys.nextrtp.common.logger.DebugService;
import io.github.krys.nextrtp.common.logger.LoggerService;
import io.github.krys.nextrtp.common.module.Module;
import io.github.krys.nextrtp.common.module.ModuleManager;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.common.service.ServiceRegistry;
import io.github.krys.nextrtp.core.NextAPIImpl;
import io.github.krys.nextrtp.core.PluginBridgeImpl;
import io.github.krys.nextrtp.core.ReloadableManager;
import io.github.krys.nextrtp.core.command.CommandManager;
import io.github.krys.nextrtp.core.command.ReloadCommand;
import io.github.krys.nextrtp.core.configuration.AbstractConfiguration;
import io.github.krys.nextrtp.core.configuration.lang.LangConfiguration;
import io.github.krys.nextrtp.core.configuration.rtp.RtpConfiguration;
import io.github.krys.nextrtp.core.listener.RandomTeleportPreListener;
import io.github.krys.nextrtp.core.listener.RandomTeleportProcessListener;
import io.github.krys.nextrtp.core.message.translator.TranslatorFiles;
import io.github.krys.nextrtp.core.module.AbstractModule;
import io.github.krys.nextrtp.core.papi.PapiHook;
import io.github.krys.nextrtp.core.processor.BiomeConditionProcessor;
import io.github.krys.nextrtp.core.processor.BlockConditionProcessor;
import io.github.krys.nextrtp.core.rtp.TeleportPendingRepository;
import io.github.krys.nextrtp.core.service.loader.ConditionLoader;
import io.github.krys.nextrtp.core.service.papi.PlaceholderAPIService;
import io.github.krys.nextrtp.core.service.schedule.MinecraftScheduleService;
import io.github.krys.nextrtp.core.vault.VaultHook;
import io.github.krys.nextrtp.core.vault.VaultService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.io.File;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@NullMarked
@SuppressWarnings("unused")
public final class NextPlugin extends JavaPlugin {

  private @Nullable ModuleManager<PluginBridge> moduleManager;

  @Override
  public void onEnable() {
    final var langConfig = new LangConfiguration(
        new File(this.getDataFolder(), "lang/lang.yml"),
        this.getResource("lang/lang.yml"));

    TranslatorFiles.INSTANCE.add(Locale.US, langConfig);

    final var pluginConfig = new RtpConfiguration(new File(this.getDataFolder(), "config.yml"),
        this.getResource("config.yml"));

    final var conditionConfig = new AbstractConfiguration(() -> YamlDocument
        .create(new File(this.getDataFolder(), "conditions.yml"), this.getResource("conditions.yml")));

    final var serviceRegistry = new ServiceRegistry();

    serviceRegistry.add(new LoggerService());
    final HookRegistry hookRegistry = new HookRegistry(this, serviceRegistry.get(LoggerService.class));
    serviceRegistry.add(new MinecraftScheduleService(this));
    serviceRegistry.add(new PlaceholderAPIService());

    hookRegistry.register(new VaultHook(serviceRegistry));
    hookRegistry.register(new PapiHook());

    final var reloadableManager = new ReloadableManager(serviceRegistry.get(LoggerService.class));

    final var debug = new DebugService(pluginConfig);
    reloadableManager.add(pluginConfig);
    reloadableManager.add(langConfig);
    reloadableManager.add(conditionConfig);
    reloadableManager.add(debug);
    reloadableManager.add(Registries.CONDITION);
    // reloadableManager.add(Registries.CONDITION_PROCESSOR);

    Registries.CONDITION_PROCESSOR.addProcessor("blacklist_blocks", new BlockConditionProcessor());
    Registries.CONDITION_PROCESSOR.addProcessor("blacklist_biomes", new BiomeConditionProcessor());

    reloadableManager.add(new ConditionLoader(
        conditionConfig,
        serviceRegistry.get(LoggerService.class)));

    final var commandManager = new CommandManager(this);

    commandManager.register(new ReloadCommand(reloadableManager));

    final var pluginBridge = new PluginBridgeImpl(serviceRegistry, reloadableManager);
    moduleManager = new ModuleManager<>(pluginBridge, serviceRegistry.get(LoggerService.class));

    final var teleportRepository = new TeleportPendingRepository(
        pluginBridge.getServiceRegistry().get(MinecraftScheduleService.class));

    pluginBridge.addEventBusListener(
        new RandomTeleportPreListener(teleportRepository, serviceRegistry.get(VaultService.class)));
    pluginBridge.addEventBusListener(new RandomTeleportProcessListener());

    Bukkit.getPluginManager().registerEvents(new ServerLoadEventListener(), this);

    final var nextApi = new NextAPIImpl(moduleManager, teleportRepository);
    Bukkit.getServicesManager().register(NextAPI.class, nextApi, this, ServicePriority.Normal);

    checkModules(pluginConfig, moduleManager);

    moduleManager.getModules().forEach(pluginBridgeModule -> {
      if (!(pluginBridgeModule instanceof AbstractModule abstractModule))
        return;
      abstractModule.handleCommand(commandManager, pluginBridge);
    });

    this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
      commands.registrar().register(commandManager.buildCommands(), "NextRTP command");
    });

    CompletableFuture.runAsync(() -> reloadableManager.reload())
        .exceptionally(thrown -> {
          this.getSLF4JLogger().error("An error ocurred while reloading", thrown);
          return null;
        });

  }

  @Override
  public void onDisable() {
    if (moduleManager == null)
      return;
    moduleManager.disableModules();
  }

  @SuppressWarnings("unchecked")
  public void checkModules(RtpConfiguration configuration, ModuleManager<PluginBridge> moduleManager) {
    Module<?>[] modules = {
        new SimpleModule(this),
        new SignModule(this),
        new SoundModule(this),
        new ParticleModule(this),
        new AnimationTitleModule(this)
    };

    for (Module<?> pluginBridgeModule : modules) {
      if (!configuration.getBoolean("modules." + pluginBridgeModule.getId(), false))
        continue;
      moduleManager.register((Module<PluginBridge>) pluginBridgeModule);
    }
  }
}
