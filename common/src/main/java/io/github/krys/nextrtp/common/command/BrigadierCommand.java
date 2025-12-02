package io.github.krys.nextrtp.common.command;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface BrigadierCommand {

  CommandNode<CommandSourceStack> node();

  default LiteralArgumentBuilder<CommandSourceStack> literal(String name) {
    return LiteralArgumentBuilder.literal(name);
  }

  default <T> RequiredArgumentBuilder<CommandSourceStack, T> argument(String name, ArgumentType<T> type) {
    return RequiredArgumentBuilder.argument(name, type);
  }
}
