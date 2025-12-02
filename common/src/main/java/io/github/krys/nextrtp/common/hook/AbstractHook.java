package io.github.krys.nextrtp.common.hook;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHook implements Hook {

    private final String hookName;
    protected boolean isActive = false;

    protected AbstractHook(@NotNull String hookName) {
        this.hookName = hookName;
    }

    @Override
    public boolean isPresent() {
        return isActive = Bukkit.getPluginManager().getPlugin(hookName) != null;
    }

    @NotNull
    @Override
    public String getHookName() {
        return hookName;
    }
}
