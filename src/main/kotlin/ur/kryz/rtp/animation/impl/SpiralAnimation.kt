package ur.kryz.rtp.animation.impl

import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class SpiralAnimation : ParticleAnimation {

    override fun tick(player: Player, t: Double) {
        val height = 3.0
        val radius = 1.0
        val y = t % height

        for (i in 0..2) {
            val angleOffset = (2 * Math.PI / 3) * i
            val angle = y * 2 * Math.PI + angleOffset
            val x = cos(angle) * radius
            val z = sin(angle) * radius

            val loc: Location = player.location.clone().add(x, y, z)

            var particle: Particle = Particle.DUST

            when (i) {
                0 -> player.spawnParticle(
                    Particle.DUST,
                    loc,
                    0,
                    Particle.DustOptions(Color.RED, 1f)
                )

                1 -> player.spawnParticle(
                    Particle.INSTANT_EFFECT,
                    loc,
                    0,
                    0.0, 0.0, 1.0
                )

                2 -> player.spawnParticle(
                    Particle.DUST,
                    loc,
                    0,
                    Particle.DustOptions(Color.BLUE, 1f)
                )
            }
        }
    }
}
