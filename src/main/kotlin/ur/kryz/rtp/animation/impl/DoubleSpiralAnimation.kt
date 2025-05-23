package ur.kryz.rtp.animation.impl

import org.bukkit.Particle
import org.bukkit.entity.Player
import ur.kryz.rtp.animation.ParticleAnimation
import kotlin.math.cos
import kotlin.math.sin

class DoubleSpiralAnimation : ParticleAnimation {
    override fun tick(player: Player, t: Double) {
        val loc = player.location
        val height = 2.0
        val radius = 0.7
        val steps = 8

        for (i in 0..steps) {
            val y = height * i / steps
            val angle1 = t + i * 0.3
            val angle2 = -t + i * 0.3

            val x1 = cos(angle1) * radius
            val z1 = sin(angle1) * radius
            val x2 = cos(angle2) * radius
            val z2 = sin(angle2) * radius

            player.world.spawnParticle(Particle.HAPPY_VILLAGER, loc.clone().add(x1, y , z1), 0)
            player.world.spawnParticle(Particle.END_ROD, loc.clone().add(x2, y, z2), 0)
        }
    }
}
