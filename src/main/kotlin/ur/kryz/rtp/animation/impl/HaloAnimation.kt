package ur.kryz.rtp.animation.impl

import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class HaloAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val loc = player.location.clone().add(0.0, 2.2, 0.0)
        val radius = 0.8
        val points = 20

        for (i in 0 until points) {
            val angle = t + (2 * Math.PI * i / points)
            val x = cos(angle) * radius
            val z = sin(angle) * radius
            player.world.spawnParticle(Particle.ENCHANT, loc.clone().add(x, 0.0, z), 0, 0.0, 0.0, 0.0)
        }
    }
}
