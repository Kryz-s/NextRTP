package ur.kryz.rtp.animation

import org.bukkit.entity.Player

interface ParticleAnimation {
    fun tick(player: Player, t: Double)
}