package ur.kryz.rtp.modules.main.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.World
import org.bukkit.entity.Player
import ur.kryz.rtp.brigadier.Arguments
import ur.kryz.rtp.brigadier.ExceptionConstants
import ur.kryz.rtp.command.paper.SubCommand
import ur.kryz.rtp.command.paper.UsageDisplayable
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.utils.Permissions.anyPermissions
import ur.kryz.rtp.utils.PlayerUtil.sendParsed
import java.util.*

class RtpWorldCommand(private val module: RandomTeleportModule) : SubCommand<CommandSourceStack>, UsageDisplayable {
    override fun build(builder: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return builder.then(
            Commands.literal("world")
                .requires { source: CommandSourceStack? ->
                    anyPermissions(
                        source!!,
                        "nextrtp.command.world"
                    )
                }
                .then(
                    Commands.argument("world", Arguments.bukkitWorld())
                        .executes { context: CommandContext<CommandSourceStack> -> this.execute(context) }
                )
                .executes { context: CommandContext<CommandSourceStack> -> this.usage(context) }
        )
    }

    @Throws(CommandSyntaxException::class)
    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val world = context.getArgument("world", Optional::class.java) as Optional<World>
        val sender = context.source.sender as? Player ?: throw ExceptionConstants.BAD_SOURCE.create()
        val w = world.orElse(null)
        if(w == null){
            sendParsed(sender, "messages.command.invalid_world")
            return Command.SINGLE_SUCCESS
        };

        RtpCommand.Util.rtp(module, sender, w)

        return Command.SINGLE_SUCCESS
    }

    override fun usage(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender
        sendParsed(sender, "messages.usage.rtp_world")
        return Command.SINGLE_SUCCESS
    }
}
