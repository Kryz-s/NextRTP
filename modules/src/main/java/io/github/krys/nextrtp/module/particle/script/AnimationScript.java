package io.github.krys.nextrtp.module.particle.script;

@FunctionalInterface
public interface AnimationScript<T> {

    ParticleScriptResult<T>[] tick(double time);
}