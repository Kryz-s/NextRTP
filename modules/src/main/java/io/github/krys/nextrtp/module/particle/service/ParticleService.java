package io.github.krys.nextrtp.module.particle.service;

import io.github.krys.nextrtp.module.particle.script.AnimationScript;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.common.service.Service;
import io.github.krys.nextrtp.core.service.schedule.MinecraftScheduleService;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public final class ParticleService implements Consumer<ScheduledTask>, Service {

    private final MinecraftScheduleService scheduleService;
    private final Map<UUID, AnimationContext<?>> activeAnimations = new ConcurrentHashMap<>();
    private ScheduledTask globalTask;

    public ParticleService(MinecraftScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    private void ensureTaskRunning() {
        if (globalTask == null || globalTask.getExecutionState() == ScheduledTask.ExecutionState.CANCELLED) {
            globalTask = scheduleService.runAsyncSchedule(this,  2L);
        }
    }

    public <T> void startAnimation(Player player, AnimationScript<T> animation, BaseTeleportInfo teleportInfo) {
        if (player == null || animation == null) return;

        stopAnimation(player);

        AnimationContext<T> context = new AnimationContext<>(
          player,
          animation,
          teleportInfo.delay
        );

        activeAnimations.put(player.getUniqueId(), context);
        ensureTaskRunning();
    }

    public void stopAnimation(Player player) {
        if (player == null) return;
        activeAnimations.remove(player.getUniqueId());
    }

    @Override
    public void accept(ScheduledTask task) {
        if (activeAnimations.isEmpty()) {
            globalTask = null;
            task.cancel();
            return;
        }

        Iterator<AnimationContext<?>> iterator = activeAnimations.values().iterator();

        while (iterator.hasNext()) {
            AnimationContext<?> ctx = iterator.next();

            if (!ctx.player.isOnline()) {
                iterator.remove();
                continue;
            }

            if (ctx.time <= 0) {
                iterator.remove();
                continue;
            }

            ctx.time -= 0.1;

            try {
                final var results = ctx.animation.tick(ctx.time);
                for (int i = 0; i < results.length; i++) {
                    final var result = results[i];
                    final Player player = ctx.player;
                    final Location location = player.getLocation();
                    player.spawnParticle(
                            result.particle(),
                            location.x() + result.x(),
                            location.y() + result.y(),
                            location.z() + result.z(),
                            result.count(),
                            result.offsetX(),
                            result.offsetY(),
                            result.offsetZ(),
                            result.extra(),
                            result.data(),
                            result.force()
                        );
                }
            } catch (Exception e) {
                e.printStackTrace();
                iterator.remove();
            }
        }
    }

    public void shutdown() {
        if (globalTask != null) {
            globalTask.cancel();
        }
        activeAnimations.clear();
    }

    private static class AnimationContext<T> {
        final Player player;
        final AnimationScript<T> animation;
        double time;

        AnimationContext(Player player, AnimationScript<T> animation, double startTime) {
            this.player = player;
            this.animation = animation;
            this.time = startTime;
        }
    }
}