package ur.kryz.rtp.manager

import org.bukkit.entity.Player
import java.util.*
import kotlin.math.max

class CooldownManager(cooldownSeconds: Int) {
    private val cooldowns: MutableMap<UUID, Long> = HashMap()
    private val cooldownDurationMillis = cooldownSeconds * 1000L

    fun isOnCooldown(player: Player): Boolean {
        val now = System.currentTimeMillis()
        return cooldowns.getOrDefault(player.uniqueId, 0L) > now
    }

    fun getTimeLeft(player: Player): Long {
        val now = System.currentTimeMillis()
        return max(0.0, (cooldowns.getOrDefault(player.uniqueId, 0L) - now).toDouble()).toLong()
    }

    fun applyCooldown(player: Player) {
        cooldowns[player.uniqueId] = System.currentTimeMillis() + cooldownDurationMillis
    }

    fun clear(player: Player) {
        cooldowns.remove(player.uniqueId)
    }
}
