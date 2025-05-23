package ur.kryz.rtp.animation.impl

import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class VortexAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val loc = player.location
        val radius = 1.5
        val height = 2.5
        val loops = 3
        val points = 20

        for (i in 0..points) {
            val angle = t + i * (Math.PI * 2 / points)
//            val y = i.toDouble() / points * height
            val x = cos(angle) * radius
            val z = sin(angle) * radius
            player.world.spawnParticle(Particle.WITCH, loc.clone().add(x, 0.0, z), 0, 0.0, 0.0, 0.0)
        }
    }
}
