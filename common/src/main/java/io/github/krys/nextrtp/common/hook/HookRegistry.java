package io.github.krys.nextrtp.common.hook;

import io.github.krys.nextrtp.common.logger.LoggerService;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class HookRegistry {
  private final Plugin plugin;
  @NotNull
  private final LoggerService logger;
  private final Map<Class<? extends Hook>, Hook> hooks = new HashMap<>();

  public HookRegistry(@NotNull Plugin plugin, @NotNull LoggerService service) {
    this.plugin = plugin;
    this.logger = service;
  }

  public <T extends Hook> void register(T hook) {
    hooks.put(hook.getClass(), hook);
    if (hook.isPresent()) {
      hook.onHook(plugin);
      logger.info("Hook loaded: {} ", hook.getHookName());
    } else {
      logger.info("Hook ignored: {}", hook.getHookName());
    }
  }

  public <T extends Hook> T get(Class<T> clazz) {
    return (T) hooks.get(clazz);
  }
}

