package ur.kryz.rtp.animation

import ur.kryz.rtp.animation.impl.*

object ParticleProvider {
    @JvmStatic
    fun get(type: String) : ParticleAnimation? = when (type) {
        "SPIRAL" -> SpiralAnimation()
        "DOUBLE_SPIRAL" -> DoubleSpiralAnimation()
        "HALO" -> HaloAnimation()
        "VORTEX" -> VortexAnimation()
        "ORBITAL" -> OrbitalAnimation()
        "DNA" -> DNAAnimation()
        "WAVE" -> WaveSpiralAnimation()
        "" -> null
        else -> null
    }
}
