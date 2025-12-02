package io.github.krys.nextrtp.common.hook;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public interface Hook {

    @NotNull
    String getHookName();

    boolean isPresent();

    void onHook(Plugin plugin);
}
