package io.github.krys.nextrtp.module.particle.script;

import org.bukkit.Particle;

public record ParticleScriptResult<T>(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data, boolean force ) {

  public ParticleScriptResult(Particle particle) {
    this(particle, 0, 0, 0, 0, 0, 0, 0, 1, null, false);
  }

  public ParticleScriptResult(Particle particle, double x, double y, double z) {
    this(particle, x, y, z, 0, 0, 0, 0, 1, null, false);
  }

  public ParticleScriptResult(Particle particle, double x, double y, double z, T data) {
    this(particle, x, y, z, 0, 0, 0, 0, 1, data, false);
  }

  public ParticleScriptResult(Particle particle, double x, double y, double z, int count, T data) {
    this(particle, x, y, z, count, 0, 0, 0, 1, data, false);
  }

  public ParticleScriptResult(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, T data) {
    this(particle, x, y, z, count, offsetX, offsetY, offsetZ, 1, data, false);
  }

  public ParticleScriptResult(Particle particle, double x, double y, double z, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
    this(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, false);
  }
}
