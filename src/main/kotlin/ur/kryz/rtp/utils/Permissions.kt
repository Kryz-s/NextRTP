package ur.kryz.rtp.utils

import io.papermc.paper.command.brigadier.CommandSourceStack


object Permissions {

    @JvmStatic
    fun anyPermissions(source: CommandSourceStack, vararg p: String): Boolean {
        val sender = source.sender
        for (permission in p) {
            if (sender.hasPermission(permission)) {
                return true
            }
        }
        return false
    }
}