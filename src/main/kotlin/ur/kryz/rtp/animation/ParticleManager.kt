package ur.kryz.rtp.animation

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ParticleManager(private val plugin: Plugin) : BukkitRunnable() {

    private var running = false
    private val activeAnimations: MutableMap<UUID, AnimationState> = HashMap()

    private data class AnimationState(val player: Player, val animation: ParticleAnimation, var t: Double)

    fun isRunning() : Boolean = running

    fun startFor(player: Player, animation: ParticleAnimation?) {
        if(animation == null) return
        activeAnimations[player.uniqueId] = AnimationState(player, animation, 0.0)
        if (!running) {
            runTaskTimerAsynchronously(plugin, 0L, 1L)
            running = true
        }
    }

    fun stopFor(player: Player) {
        activeAnimations.remove(player.uniqueId)
    }

    override fun run() {
        if (activeAnimations.isEmpty()) return
        val it: MutableIterator<Map.Entry<UUID, AnimationState>> = activeAnimations.entries.iterator()
        while (it.hasNext()) {
            val entry = it.next()
            val state = entry.value

            if (!state.player.isOnline) {
                it.remove()
                continue
            }

            state.t += 0.05
            state.animation.tick(state.player, state.t)
        }
    }
}
