package ur.kryz.rtp.api.events

import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class SignTeleportStartEvent(player: Player, private val sign: Sign) : PlayerEvent(player, true), Cancellable {
    private var cancelled = false

    fun getSign() : Sign = sign

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
