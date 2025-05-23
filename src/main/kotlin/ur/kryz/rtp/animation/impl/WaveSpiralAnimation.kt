package ur.kryz.rtp.animation.impl

import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class WaveSpiralAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val loc = player.location.clone().add(0.0, 1.0, 0.0)
        val height = 2.0
        val steps = 8
        val radius = 0.8

        for (i in 0..steps) {
            val y = height * i / steps
            val angle = t + i * 0.2
            val wave = sin(t * 2 + i * 0.5) * 0.3
            val x = cos(angle) * (radius + wave)
            val z = sin(angle) * (radius + wave)

            player.world.spawnParticle(Particle.CLOUD, loc.clone().add(x, y, z), 0)
        }
    }
}
