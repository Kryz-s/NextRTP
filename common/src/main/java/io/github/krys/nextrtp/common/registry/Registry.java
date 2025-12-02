package io.github.krys.nextrtp.common.registry;

import io.github.krys.nextrtp.common.reload.Reloadable;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class Registry<K, T> implements Reloadable {
    protected final Map<K, T> registries;

    public Registry(Map<K, T> registries) {
        this.registries = registries;
    }

    public void add(@NotNull K key, @NotNull T value) {
        this.registries.put(key, value);
    }

    public T get(@NotNull K key ) {
        return this.registries.get(key);
    }

    public T getOrDefault(@NotNull K key, @NotNull T defaultValue) {
        return this.registries.getOrDefault(key, defaultValue);
    }

    @Override
    public void reload() {
        this.registries.clear();
    }

    @Override
    public void onReload() { }
}
