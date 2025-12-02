package io.github.krys.nextrtp.module.core.command.argument;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

public final class PlayerArgumentResolver implements CustomArgumentType<Player, PlayerSelectorArgumentResolver> {

    @Override
    public Player parse(StringReader reader) {
        throw new UnsupportedOperationException("This method will never be called.");
    }

    @Override
    public <S> Player parse(StringReader reader, S source) throws CommandSyntaxException {
      final Player player = getNativeType().parse(reader).resolve((CommandSourceStack) source).getFirst();

      return player;
    }

    @Override
    public ArgumentType<PlayerSelectorArgumentResolver> getNativeType() {
        return ArgumentTypes.player();
    }

    public static PlayerArgumentResolver resolver() {
      return new PlayerArgumentResolver();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> ctx, SuggestionsBuilder builder) {
        Bukkit.getOnlinePlayers().forEach(player -> {
          if (player.getName().toLowerCase().startsWith(builder.getRemainingLowerCase()))
            builder.suggest(player.getName());
        });;
        return builder.buildFuture();
    }
}