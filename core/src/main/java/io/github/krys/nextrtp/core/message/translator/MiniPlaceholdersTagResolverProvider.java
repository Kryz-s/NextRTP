package io.github.krys.nextrtp.core.message.translator;

import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;

public final class MiniPlaceholdersTagResolverProvider {

  private final boolean enabled;

  public MiniPlaceholdersTagResolverProvider() {
    this.enabled = Bukkit.getPluginManager().getPlugin("MiniPlaceholders") != null;
  }

  public TagResolver resolvers() {
    if(!enabled) return TagResolver.empty();
    return MiniPlaceholders.audienceGlobalPlaceholders();
  }
}
