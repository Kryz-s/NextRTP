package io.github.krys.nextrtp.common.module;

import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.logger.LoggerService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class ModuleManager<T extends PluginBridge> {
  @NotNull
  private final T bridge;
  @NotNull
  private final LoggerService logger;
  private final Map<String, Module<T>> modules = new HashMap<>();

  public ModuleManager(@NotNull T bridge, @NotNull LoggerService logger) {
    this.bridge = bridge;
    this.logger = logger;
  }

  public void register(@NotNull Module<T> module) {
    modules.put(module.getId(), module);
    module.onEnable(bridge);
    logger.info("Module {} enabled.", module.getName());
  }

  @Nullable
  public Module<T> getModule(@NotNull String id) {
    return this.modules.get(id);
  }

  @NotNull
  public Collection<Module<T>> getModules() {
    return modules.values();
  }

  public void disableModules() {
    modules.values().forEach(module -> module.onDisable(bridge));
  }
}