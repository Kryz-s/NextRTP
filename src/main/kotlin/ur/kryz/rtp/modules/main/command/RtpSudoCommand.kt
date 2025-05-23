package ur.kryz.rtp.modules.main.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import ur.kryz.rtp.brigadier.Arguments
import ur.kryz.rtp.command.paper.SubCommand
import ur.kryz.rtp.command.paper.UsageDisplayable
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.utils.Permissions.anyPermissions
import ur.kryz.rtp.utils.PlayerUtil.sendParsed
import java.util.*

class RtpSudoCommand(private val module: RandomTeleportModule) : SubCommand<CommandSourceStack>, UsageDisplayable {
    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val opt: Optional<Player> = context.getArgument("player", Optional::class.java) as Optional<Player>
        val sender = context.source.sender

        val player = opt.orElse(null)
        if (player == null) {
            sendParsed(sender, "messages.command.invalid_player")
            return Command.SINGLE_SUCCESS
        }

        RtpCommand.Util.rtp(module, player, player.world)

        return Command.SINGLE_SUCCESS
    }

    override fun build(builder: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return builder.then(
            Commands.literal("sudo")
                .requires { source: CommandSourceStack? ->
                    anyPermissions(
                        source!!,
                        "nextrtp.command.sudo"
                    )
                }.then(
                    Commands.argument("player", Arguments.optionalPlayer())
                        .executes { context: CommandContext<CommandSourceStack> -> this.execute(context) }
                )
                .executes { context: CommandContext<CommandSourceStack> -> this.usage(context) }
        )
    }

    override fun usage(context: CommandContext<CommandSourceStack>): Int {
        sendParsed(context.source.sender, "messages.usage.rtp_sudo")
        return Command.SINGLE_SUCCESS
    }
}
