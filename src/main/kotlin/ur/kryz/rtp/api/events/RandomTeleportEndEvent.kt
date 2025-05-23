package ur.kryz.rtp.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class RandomTeleportEndEvent(who: Player) : PlayerEvent(who), Cancellable {
    private var cancel = false
    var isFailed: Boolean = false
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }

    companion object {
        val handlerList: HandlerList = HandlerList()
    }
}
