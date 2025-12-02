package io.github.krys.nextrtp.core.vault;

import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.t;

import io.github.krys.nextrtp.common.service.Service;
import net.milkbowl.vault.economy.Economy;

public final class VaultService implements Service {
  private final Economy economy;

  public VaultService(Economy economy) {
    this.economy = economy;
  }

  public boolean available() {
    return economy != null;
  }

  public void take(Player player, double amount) {
    if (!this.available()) return;
    this.economy.withdrawPlayer(player, amount);
  }

  public boolean has(Player player, double amount) {
    if (!this.available()) return true;  
    return this.economy.has(player, amount);
  }
}
