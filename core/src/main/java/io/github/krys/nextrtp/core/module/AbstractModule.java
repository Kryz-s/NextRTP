package io.github.krys.nextrtp.core.module;

import io.github.krys.nextrtp.common.PluginBridge;
import io.github.krys.nextrtp.core.command.CommandManager;
import io.github.krys.nextrtp.common.module.Module;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractModule implements Module<PluginBridge> {
    protected final String name;
    protected final String id;

    public AbstractModule(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @Override
    public void onEnable(@NotNull PluginBridge plugin) {}

    @Override
    public void onDisable(@NotNull PluginBridge plugin) {}

    public void handleCommand(@NotNull CommandManager manager, @NotNull PluginBridge pluginBridge) {}
}