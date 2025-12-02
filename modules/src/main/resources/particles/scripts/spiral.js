(
  function() {
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const Particle = Java.type("org.bukkit.Particle");
    const DustOptions = Java.type("org.bukkit.Particle.DustOptions");
    const Color = Java.type("org.bukkit.Color");
    const cos = Math.cos;
    const sin = Math.sin;

    const height = 3.0
    const radius = 1.0

    function tick(t) {

      const y = t % height

      const results = [];

      for (i = 0; i <= 2; i++) {
        const angleOffset = (2 * Math.PI / 3) * i
        const angle = y * 2 * Math.PI + angleOffset
        const x = cos(angle) * radius
        const z = sin(angle) * radius

        switch (i) {
            case 0: results.push(new ParticleScriptResult(Particle.DUST, x, y, z, new DustOptions(Color.RED, 1)))

            case 1: results.push(new ParticleScriptResult(Particle.INSTANT_EFFECT, x, y, z, 0, 0, 0, 1))

            case 2: results.push(new ParticleScriptResult(Particle.DUST, x, y, z, new DustOptions(Color.BLUE, 1)))
        }
      }

      return results;
    }

    return {
      tick: tick
    }
  }
)();