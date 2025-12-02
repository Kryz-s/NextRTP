package io.github.krys.nextrtp.core.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.krys.nextrtp.common.command.BrigadierCommand;
import io.github.krys.nextrtp.common.command.SimpleCommand;
import io.github.krys.nextrtp.core.util.Permission;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class CommandManager {

    private final JavaPlugin plugin;
    private final Set<CommandNode<CommandSourceStack>> commands;
    private SimpleCommand simpleCommand;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.commands = new HashSet<>();
    }

    public void register(BrigadierCommand instance) {
        commands.add(instance.node());
    }

    public void register(CommandNode<CommandSourceStack> instance) {
        commands.add(instance);
    }

    public SimpleCommand getSimpleCommand() {
        return simpleCommand;
    }

    public void setSimpleCommand(SimpleCommand simpleCommand) {
        this.simpleCommand = simpleCommand;
    }

    public LiteralCommandNode<CommandSourceStack> buildCommands() {
        plugin.getSLF4JLogger().info("Building commands...");
        final LiteralArgumentBuilder<CommandSourceStack> rootCommand = Commands.literal("rtp")
                .requires(Permission.COMMAND)
                .executes(ctx -> {
                    if (simpleCommand != null) {
                        simpleCommand.execute(ctx);
                    }
                    return 1;
                });
        for (CommandNode<?> node : commands) {
            if (node instanceof LiteralCommandNode<?> literal) {
                @SuppressWarnings("unchecked")
                LiteralCommandNode<CommandSourceStack> typed = (LiteralCommandNode<CommandSourceStack>) literal;
                rootCommand.then(typed);
            }
        }
        plugin.getSLF4JLogger().info("Commands builded.");
        return rootCommand.build();
    }
}