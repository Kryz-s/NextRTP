package io.github.krys.nextrtp.common;

import io.github.krys.nextrtp.common.reload.Reloadable;
import io.github.krys.nextrtp.common.service.ServiceRegistry;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface PluginBridge {

    ServiceRegistry getServiceRegistry();

    void addReloadableElement(Reloadable reloadable);

    void addEventBusListener(Object object);

    NextAPI getAPI();
}
