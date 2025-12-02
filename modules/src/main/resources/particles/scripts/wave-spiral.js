(
  function() {
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const Particle = Java.type("org.bukkit.Particle");
    const cos = Math.cos;
    const sin = Math.sin;
    const height = 2.0
    const steps = 8
    const radius = 0.8

    function tick(t) {

      const results = [];

      for (i = 0; i <= steps; i++) {
        const y = height * i / steps
        const angle = t + i * 0.2
        const wave = sin(t * 2 + i * 0.5) * 0.3
        const x = cos(angle) * (radius + wave)
        const z = sin(angle) * (radius + wave)

        results.push(new ParticleScriptResult(Particle.CLOUD, x, y, z));
      }

      return results;
    }

    return {
      tick: tick
    }
  }
)()