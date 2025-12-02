(function() {

    const Particle = Java.type("org.bukkit.Particle");
    const Color = Java.type("org.bukkit.Color");
    const DustOptions = Java.type("org.bukkit.Particle.DustOptions");
    const ParticleScriptResult = Java.type("io.github.krys.nextrtp.module.particle.script.ParticleScriptResult");
    const cos = Math.cos;
    const sin = Math.sin;

    const dustRed = new DustOptions(Color.RED, 1.0);
    const dustBlue = new DustOptions(Color.BLUE, 1.0);

    function tick(t) {
        var height = 2.0;
        var steps = 8;
        var radius = 0.7;

        const results = [];

        for (let i = 0; i <= steps; i++) {
            var y = height * i / steps; 
            var angle = t + i * 0.3;
            var x1 = cos(angle) * radius;
            var z1 = sin(angle) * radius;
            var x2 = -x1;
            var z2 = -z1;
            results.push(new ParticleScriptResult(Particle.DUST, x1, y, z1, 0, dustRed));
            results.push(new ParticleScriptResult(Particle.DUST, x2, y, z2, 0, dustBlue));
        }

        return results;
    }

    return {
        tick: tick
    };

})();