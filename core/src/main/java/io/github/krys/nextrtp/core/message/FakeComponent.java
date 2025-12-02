package io.github.krys.nextrtp.core.message;

import java.util.Locale;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.krys.nextrtp.core.message.translator.MiniMessageTranslator;
import net.kyori.adventure.pointer.Pointered;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public final class FakeComponent {

  private static final MiniMessageTranslator TRANSLATOR = new MiniMessageTranslator();

  @Nullable
  public static <T> Component asComponent(String text, Player player, TagResolver... resolvers) {
    return TRANSLATOR.parse(text, player, player.locale(), resolvers);
  }

  @Nullable
  public static <T> Component asComponent(String text, Pointered pointered, TagResolver... resolvers) {
    return TRANSLATOR.parse(text, pointered, Locale.US, resolvers);
  }
}
