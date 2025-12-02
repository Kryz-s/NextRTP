package io.github.krys.nextrtp.common.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface BrigadierCommandBuilder<A extends ArgumentBuilder<CommandSourceStack, A>> extends BrigadierCommand {

  ArgumentBuilder<CommandSourceStack, A> builder();

  @Override
  default CommandNode<CommandSourceStack> node() {
    return this.builder().build();
  }

}
