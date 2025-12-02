(function() {

  const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
  const Particle = Java.type("org.bukkit.Particle");
  const height = 2.0
  const radius = 0.7
  const steps = 8
  const cos = Math.cos;
  const sin = Math.sin;

  function tick(t) {

    const results = [];

    for (let i = 0; i <= steps; i++) {
        const y = height * i / steps
        const angle1 = t + i * 0.3
        const angle2 = -t + i * 0.3
        const x1 = cos(angle1) * radius
        const z1 = sin(angle1) * radius
        const x2 = cos(angle2) * radius
        const z2 = sin(angle2) * radius

        results.push(new ParticleScriptResult(Particle.HAPPY_VILLAGER, x1, y, z1));
        results.push(new ParticleScriptResult(Particle.END_ROD, x2, y, z2));
    }
    
    return results;
  }

  return {
    tick: tick
  }
})();