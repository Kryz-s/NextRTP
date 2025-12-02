package io.github.krys.nextrtp.core.service.papi;

import io.github.krys.nextrtp.common.service.Service;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class PlaceholderAPIService implements Service {

  private final boolean enabled;

  public PlaceholderAPIService() {
    this.enabled = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
  }

  public String parse(Player player, String text) {
    if (!enabled) return text;
    text = PlaceholderAPI.setBracketPlaceholders(player, text);
    return PlaceholderAPI.setPlaceholders(player, text);
  }
}
