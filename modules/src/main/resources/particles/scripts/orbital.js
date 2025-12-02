(
  function() {
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const Particle = Java.type("org.bukkit.Particle");
    const cos = Math.cos;
    const sin = Math.sin;
    
    const orbiters = 4
    const radius = 1.0

    function tick(t) {
      const results = [];
      
      for (i = 0; i < orbiters; i++) {
        const angle = t + (2 * Math.PI * i / orbiters)
        const x = cos(angle) * radius
        const z = sin(angle) * radius
        const y = sin(t * 2 + i) * 0.5
        results.push(new ParticleScriptResult(Particle.FLAME, x, y, z))
      }

      return results;
    }

    return {
      tick: tick
    }
  }
)();