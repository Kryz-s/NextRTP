package io.github.krys.nextrtp.module.core.command.argument;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.checkerframework.checker.units.qual.s;
import org.jetbrains.annotations.NotNull;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.core.command.excp.NoWorldFindException;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.module.core.provider.WorldTeleportInfoProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public final class WorldTeleportInfoArgumentType implements CustomArgumentType<WorldTeleportInfo, String> {

  private final @NotNull WorldTeleportInfoProvider provider;

  public WorldTeleportInfoArgumentType(@NotNull WorldTeleportInfoProvider provider) {
    this.provider = provider;
  }
  @Override
  public WorldTeleportInfo parse(StringReader reader) throws CommandSyntaxException {
    throw new UnsupportedOperationException("Unimplemented method 'parse'");
  }

  @Override
  public <S> WorldTeleportInfo parse(StringReader reader, S source) throws CommandSyntaxException {
    final String input = getNativeType().parse(reader);
    
    final WorldTeleportInfo info = provider.getOrCreate(input, Bukkit.getWorld(input));
    if (info == null) {
      final CommandSourceStack stack = (CommandSourceStack) source;
      Messager.WORLD_NO_FIND.accept(stack.getSender(), Placeholder.unparsed("world", input));
      return null;
    }

    return info;
  }

  @Override
  public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
    
    for (World world : Bukkit.getWorlds()) {
      if (world.getName().toLowerCase().startsWith(builder.getRemainingLowerCase()))
        builder.suggest(world.getName());
    }

    return builder.buildFuture();
  }

  @Override
  public ArgumentType<String> getNativeType() {
    return StringArgumentType.word();
  }

  public static WorldTeleportInfoArgumentType argumentType(@NotNull WorldTeleportInfoProvider provider) {
    return new WorldTeleportInfoArgumentType(provider);
  }
}
