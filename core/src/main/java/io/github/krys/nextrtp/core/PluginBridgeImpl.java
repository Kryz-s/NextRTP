package io.github.krys.nextrtp.core;

import io.github.krys.nextrtp.common.EventBusDispatcher;
import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.common.reload.Reloadable;
import io.github.krys.nextrtp.common.service.ServiceRegistry;
import org.bukkit.Bukkit;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class PluginBridgeImpl implements PluginBridge {
  private final ServiceRegistry registry;

  private final ReloadableManager reloadableManager;

  private @Nullable NextAPI nextAPI = null;

  public PluginBridgeImpl(ServiceRegistry registry, ReloadableManager reloadableManager) {
    this.registry = registry;
    this.reloadableManager = reloadableManager;
  }

  @Override
  public ServiceRegistry getServiceRegistry() {
    return registry;
  }

  @Override
  public void addReloadableElement(Reloadable reloadable) {
    this.reloadableManager.add(reloadable);
  }

  @Override
  public void addEventBusListener(Object object) {
    EventBusDispatcher.instance().registerListener(object);
  }

  public NextAPI getAPI() {
    if(nextAPI == null) nextAPI = Bukkit.getServicesManager().load(NextAPI.class);
    assert nextAPI != null;
    return nextAPI;
  }
}
