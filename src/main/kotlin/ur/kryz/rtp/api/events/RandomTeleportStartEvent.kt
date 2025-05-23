package ur.kryz.rtp.api.events

import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.HandlerList
import org.bukkit.event.player.PlayerEvent

class RandomTeleportStartEvent(who: Player) : PlayerEvent(who), Cancellable {
    private var cancel = false
    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    } //

    //    public boolean isFailed() {
    //        return failed;
    //    }
    //
    //    public void setFailed(boolean failed) {
    //        this.failed = failed;
    //    }
    companion object {
        //    private final Location location;
        val handlerList: HandlerList = HandlerList()
    }
}
