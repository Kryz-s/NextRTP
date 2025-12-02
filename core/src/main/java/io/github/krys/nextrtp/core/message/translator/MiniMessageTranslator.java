package io.github.krys.nextrtp.core.message.translator;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.krys.nextrtp.common.placeholder.LegacyParser;
import io.github.krys.nextrtp.core.service.papi.PlaceholderAPIService;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public class MiniMessageTranslator {
  
  private static final MiniMessage miniMessage = MiniMessage.miniMessage();
  private static final MiniPlaceholdersTagResolverProvider provider = new MiniPlaceholdersTagResolverProvider();

  private final PlaceholderAPIService service;

  public MiniMessageTranslator() {
    this.service = new PlaceholderAPIService();
  }

  public String getMiniMessageString(String text, Locale locale) {
    return TranslatorFiles.INSTANCE.getLang(Locale.US).getString(text, null);
  }

  // TODO: Locale message system
  @Nullable
  public <T> Component parse(String key, Pointered pointer, Locale locale, TagResolver... resolvers) {
    
    String miniMessageString = this.getMiniMessageString(key, locale);
    if (miniMessageString == null) return null;

    final Component resultingComponent;
    if (resolvers.length == 0 || resolvers == null) {

      final var parsed = this.getMiniMessageString("prefix", locale);
      TagResolver resolver = TagResolver.empty();

      if (parsed != null) {
        resolver = TagResolver.resolver(Placeholder.parsed("prefix", LegacyParser.parse(parsed)));
      }

      resultingComponent = miniMessage.deserialize(LegacyParser.parse(miniMessageString), resolver);
    }
    else {
      TagResolver.Builder tagResolverBuilder = TagResolver.builder();

      final var prefixParsed = this.getMiniMessageString("prefix", locale);

      if (prefixParsed != null) {
        tagResolverBuilder.resolver(Placeholder.parsed("prefix", LegacyParser.parse(prefixParsed)));
      }

      for (TagResolver argument : resolvers) {
        tagResolverBuilder.resolver(argument);
      }

      tagResolverBuilder.resolver(provider.resolvers());

      while (true) {
        if (pointer == null) {
          resultingComponent = miniMessage.deserialize(LegacyParser.parse(miniMessageString),
              tagResolverBuilder.build());
          break;
        }

        if (pointer instanceof Player player) {
          miniMessageString = service.parse(player, miniMessageString);
        }

        resultingComponent = miniMessage.deserialize(LegacyParser.parse(miniMessageString), pointer,
            tagResolverBuilder.build());
        break;
      }
    }

    return resultingComponent;
  }
}
