package ur.kryz.rtp.brigadier

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType
import io.papermc.paper.command.brigadier.MessageComponentSerializer
import ur.kryz.rtp.NextRTPPlugin

object ExceptionConstants {
    private val MSG = NextRTPPlugin.MINI_MESSAGE.deserialize(
        "<red>Illegal CommandSender, you must be a Player to execute this command!"
    )
    val BAD_SOURCE: SimpleCommandExceptionType = SimpleCommandExceptionType(
        MessageComponentSerializer.message().serialize(
            MSG
        )
    )
}
