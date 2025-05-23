package ur.kryz.rtp.command.paper

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException

interface ExecutorContext<S> {

    @Throws(CommandSyntaxException::class)
    fun execute(context: CommandContext<S>) : Int
}