package io.github.krys.nextrtp.core.vault;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import net.milkbowl.vault.economy.Economy;

import io.github.krys.nextrtp.common.hook.AbstractHook;
import io.github.krys.nextrtp.common.service.ServiceRegistry;

public final class VaultHook extends AbstractHook {

  private final ServiceRegistry registry;

  public VaultHook(ServiceRegistry registry) {
    super("Vault");
    this.registry = registry;
    registry.add(new VaultService(null));
  }

  @Override
  public void onHook(Plugin plugin) {
    final var service = Bukkit.getServicesManager().getRegistration(Economy.class);
    if (service == null) {
      registry.add(new VaultService(null));
      return;
    }
    final Economy economy = service.getProvider();
    registry.add(new VaultService(economy));
  }
}
