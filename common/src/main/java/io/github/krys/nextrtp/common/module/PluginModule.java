package io.github.krys.nextrtp.common.module;

import io.github.krys.nextrtp.common.PluginBridge;
import org.jetbrains.annotations.NotNull;

public abstract class PluginModule implements Module<PluginBridge> {
  protected final String name;
  protected final String id;

  public PluginModule(@NotNull String name, @NotNull String id) {
    this.name = name;
    this.id = id;
  }

  public PluginModule(@NotNull String id) {
    this.name = this.getClass().getSimpleName();
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
}
