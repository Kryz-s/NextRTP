(
  function() {
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const Particle = Java.type("org.bukkit.Particle");
    const cos = Math.cos;
    const sin = Math.sin;

    const radius = 1.5
    //const height = 2.5
    //const loops = 3
    const points = 20

    function tick(t) {
      const results = []

      for (let i = 0; i <= points; i++) {
        const angle = t + i * (Math.PI * 2 / points)
//        const y = i.toDouble() / points * height
        const x = cos(angle) * radius
        const z = sin(angle) * radius

        results.push(new ParticleScriptResult(Particle.WITCH, x, 0, z))
      }

      return results;
    }

    return {
      tick: tick
    }
  }
)();