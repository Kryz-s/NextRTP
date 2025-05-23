package ur.kryz.rtp.brigadier

import ur.kryz.rtp.brigadier.arguments.BukkitWorldArgument
import ur.kryz.rtp.brigadier.arguments.OptionalPlayer

object Arguments {
    fun optionalPlayer() : OptionalPlayer {
        return OptionalPlayer()
    }

    fun bukkitWorld() : BukkitWorldArgument {
        return BukkitWorldArgument()
    }
}
