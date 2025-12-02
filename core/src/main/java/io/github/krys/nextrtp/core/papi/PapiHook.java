package io.github.krys.nextrtp.core.papi;

import io.github.krys.nextrtp.common.hook.AbstractHook;

import org.bukkit.plugin.Plugin;

public final class PapiHook extends AbstractHook {

  public PapiHook() {
    super("PlaceholderAPI");
  }

  @Override
  public void onHook(Plugin plugin) {
    new Placeholders(plugin.getPluginMeta()).register();
  }
}
