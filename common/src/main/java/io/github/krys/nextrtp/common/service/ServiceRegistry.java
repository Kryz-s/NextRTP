package io.github.krys.nextrtp.common.service;

import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public final class ServiceRegistry {
    private final Map<Class<? extends Service>, Service> serviceMap;

    public ServiceRegistry() {
        this.serviceMap = new HashMap<>();
    }

    public <S extends Service> void add(S service) {
        if(serviceMap.containsKey(service.getClass())) return;
        this.serviceMap.put(service.getClass(), service);
    }

    @SuppressWarnings("unchecked")
    public <S extends Service> S get(Class<S> serviceClass) {
        final S service = (S) this.serviceMap.get(serviceClass);
        if (service == null)
            throw new NullPointerException("No service find for " + serviceClass);
        return service;
    }
}
