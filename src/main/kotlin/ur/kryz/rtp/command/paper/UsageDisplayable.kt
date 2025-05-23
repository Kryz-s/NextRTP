package ur.kryz.rtp.command.paper

import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack

interface UsageDisplayable {
    fun usage(context: CommandContext<CommandSourceStack>): Int
}
