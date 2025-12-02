package io.github.krys.nextrtp.core.papi;

import io.papermc.paper.plugin.configuration.PluginMeta;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

import java.util.regex.Pattern;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"UnstableApiUsage"})
public final class Placeholders extends PlaceholderExpansion {

  private final @NotNull PluginMeta meta;
  private static final Pattern PATTERN = Pattern.compile(":+");

  public Placeholders(@NotNull PluginMeta meta) {
    this.meta = meta;
  }

  @Override
  public @NotNull String getIdentifier() {
    return meta.getName().toLowerCase();
  }

  @Override
  public @NotNull String getAuthor() {
    return String.join(", ", meta.getAuthors());
  }

  @Override
  public @NotNull String getVersion() {
    return meta.getVersion();
  }

  @Override
  public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
    final String[] split = params.split(":", 2);
    final String identifier = split[0];

    final PlaceholderElement element = PlaceholderRepository.PLACEHOLDERS.get(identifier);
    if (element == null) return null;

    if (element.minArguments() == 0) return element.onRequest(player, null);

    if (split.length < 2) return null;

    final String plainArguments = split[1];
    final String[] strArguments = PATTERN.split(plainArguments);
    
    if (element.minArguments() > strArguments.length) return null;

    final Arguments arguments = new Arguments(strArguments);
    
    return element.onRequest(player, arguments);
  }
}
