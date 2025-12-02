(
  function() {
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const Particle = Java.type("org.bukkit.Particle");
    const cos = Math.cos;
    const sin = Math.sin;

    const radius = 0.8
    const points = 20

    function tick(t) {
      const results = [];

      for (i = 0; i <= 20; i++) {
        const angle = t + (2 * Math.PI * i / points)
        const x = cos(angle) * radius
        const z = sin(angle) * radius
        results.push(new ParticleScriptResult(Particle.ENCHANT, x, 0, z))
      }
    
      return results;
    }

    return {
      tick: tick
    }
  }
)();