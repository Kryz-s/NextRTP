package io.github.krys.nextrtp.common.command;

import com.mojang.brigadier.context.CommandContext;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface SimpleCommand {

  void execute(CommandContext<CommandSourceStack> context);
}
