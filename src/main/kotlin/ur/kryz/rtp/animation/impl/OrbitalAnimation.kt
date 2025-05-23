package ur.kryz.rtp.animation.impl

import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class OrbitalAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val center = player.location.clone().add(0.0, 1.0, 0.0)
        val orbiters = 4
        val radius = 1.0

        for (i in 0 until orbiters) {
            val angle = t + (2 * Math.PI * i / orbiters)
            val x = cos(angle) * radius
            val z = sin(angle) * radius
            val y = sin(t * 2 + i) * 0.5
            player.world.spawnParticle(Particle.FLAME, center.clone().add(x, y, z), 0)
        }
    }
}
