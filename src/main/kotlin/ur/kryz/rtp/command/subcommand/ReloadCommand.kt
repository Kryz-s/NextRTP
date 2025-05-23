package ur.kryz.rtp.command.subcommand

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import ur.kryz.rtp.NextRTPPlugin
import ur.kryz.rtp.command.paper.SubCommand
import ur.kryz.rtp.utils.Permissions
import ur.kryz.rtp.utils.PlayerUtil

class ReloadCommand(
    private val plugin: NextRTPPlugin
) : SubCommand<CommandSourceStack> {

    override fun execute(context: CommandContext<CommandSourceStack>) : Int {
        val sender = context.source.sender
        plugin.reload()
        PlayerUtil.sendParsed(sender,"messages.command.reload.success")
        return Command.SINGLE_SUCCESS
    }

    override fun build(builder: LiteralArgumentBuilder<CommandSourceStack>) : LiteralArgumentBuilder<CommandSourceStack> {
        return builder.then(
            Commands.literal("reload")
                .requires { req ->
                    Permissions.anyPermissions(
                        req,"nextrtp.command.reload"
                    )
                }
                .executes(this::execute)
        )
    }
}