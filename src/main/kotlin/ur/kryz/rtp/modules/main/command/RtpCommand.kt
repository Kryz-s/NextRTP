package ur.kryz.rtp.modules.main.command

import com.destroystokyo.paper.console.TerminalConsoleCommandSender
import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import io.papermc.paper.command.brigadier.CommandSourceStack
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.World
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import ur.kryz.rtp.brigadier.ExceptionConstants
import ur.kryz.rtp.command.paper.SubCommand
import ur.kryz.rtp.modules.main.RandomTeleportModule
import ur.kryz.rtp.utils.DateFormatUtil.formatTime
import ur.kryz.rtp.utils.Permissions.anyPermissions
import ur.kryz.rtp.utils.PlayerUtil.hasMoney
import ur.kryz.rtp.utils.PlayerUtil.sendParsed

class RtpCommand(private val module: RandomTeleportModule) : SubCommand<CommandSourceStack> {
    override fun build(builder: LiteralArgumentBuilder<CommandSourceStack>): LiteralArgumentBuilder<CommandSourceStack> {
        return builder.requires { source: CommandSourceStack ->
            val perm = anyPermissions(
                source,
                "nextrtp.command.rtp"
            )
            return@requires perm
        }.executes { context: CommandContext<CommandSourceStack> -> this.execute(context) }
    }

    @Throws(CommandSyntaxException::class)
    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val sender = context.source.sender as? Player ?: throw ExceptionConstants.BAD_SOURCE.create()

        Util.rtp(module, sender, sender.world)

        return Command.SINGLE_SUCCESS
    }

    object Util {
        @JvmStatic
        fun rtp(module: RandomTeleportModule, player: Player, world: World) {
            if (module.task.isQueued(player)) {
                sendParsed(player, "messages.teleport.progress")
                return
            }

            val money: Number = module.config.get("teleport.money", 100.0)
            val doubleMoney = money.toDouble()
            val permString = "nextrtp.world." + world.name
            if (!player.hasPermission(permString) &&
                !player.hasPermission("nextrtp.world.*")
            ) {
                sendParsed(
                    player, "messages.world.not_allowed",
                    Placeholder.unparsed("world_name", world.name)
                )
                return
            }

            if (!hasMoney(player, doubleMoney) && !player.hasPermission("nextrtp.bypass.money")) {
                sendParsed(
                    player, "messages.currency.no_money",
                    Placeholder.unparsed("money", doubleMoney.toString())
                )
                return
            }

            val cooldownManager = module.cooldownManager
            if (cooldownManager.isOnCooldown(player)) {
                val left = cooldownManager.getTimeLeft(player) / 1000
                sendParsed(
                    player, "messages.teleport.cooldown",
                    Placeholder.unparsed("time", formatTime(left.toInt()))
                )
                return
            }

            module.task.queue(player, module.config.get<Int>("teleport.delay")!!, world)
        }
    }
}
