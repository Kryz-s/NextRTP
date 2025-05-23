package ur.kryz.rtp.animation.impl

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class DNAAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val loc = player.location
        val height = 2.0
        val steps = 8
        val radius = 0.7

        for (i in 0..steps) {
            val y = height * i / steps
            val angle = t + i * 0.3
            val x1 = cos(angle) * radius
            val z1 = sin(angle) * radius
            val x2 = -x1
            val z2 = -z1

            val base = loc.clone().add(0.0, y, 0.0)
            player.world.spawnParticle(Particle.DUST, base.clone().add(x1, 0.0, z1), 0, Particle.DustOptions(Color.RED, 1f))
            player.world.spawnParticle(Particle.DUST, base.clone().add(x2, 0.0, z2), 0, Particle.DustOptions(Color.BLUE, 1f))
        }
    }
}
