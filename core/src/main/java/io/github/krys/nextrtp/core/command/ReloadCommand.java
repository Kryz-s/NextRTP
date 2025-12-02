package io.github.krys.nextrtp.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import io.github.krys.nextrtp.common.command.BrigadierCommand;
import io.github.krys.nextrtp.core.ReloadableManager;
import io.github.krys.nextrtp.core.message.Messager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

public final class ReloadCommand implements BrigadierCommand {

  private final @NotNull ReloadableManager manager;

  public ReloadCommand(@NotNull ReloadableManager manager) {
    this.manager = manager;
  }

  @Override
  public CommandNode<CommandSourceStack> node() {
    LiteralArgumentBuilder<CommandSourceStack> builder = literal("reload")
            .requires(source -> source.getSender().hasPermission("nextrtp.command.reload"))
            .executes(this::execute);

    return builder.build();
  }

  private int execute(CommandContext<CommandSourceStack> context) {
    try {
      manager.reload();
      Messager.RELOAD_SUCCESS.accept(context.getSource().getSender());
    } catch (Exception e) {
      Messager.RELOAD_ERROR.accept(context.getSource().getSender());
      throw new RuntimeException(e);
    }
    return 1;
  }
}
