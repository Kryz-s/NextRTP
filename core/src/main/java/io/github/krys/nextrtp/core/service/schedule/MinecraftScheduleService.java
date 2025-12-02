package io.github.krys.nextrtp.core.service.schedule;

import io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.github.krys.nextrtp.common.service.Service;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@NullMarked
public final class MinecraftScheduleService implements Service {

  @NotNull
  private final JavaPlugin plugin;
  @NotNull
  private final ScheduledExecutorService executorService;

  public MinecraftScheduleService(@NotNull JavaPlugin plugin) {
    this.plugin = plugin;
    try {
      final var field = FoliaAsyncScheduler.class.getDeclaredField("timerThread");
      field.setAccessible(true);
      this.executorService = (ScheduledExecutorService) field.get(Bukkit.getAsyncScheduler());
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void runNow(@NotNull final Runnable runnable) {
    MinecraftServer.getServer().executeIfPossible(runnable);
  }

  public CompletableFuture<Void> runAsyncNow(@NotNull final Runnable runnable) {
    return CompletableFuture.runAsync(runnable, executorService);
  }

  public void runAsync(@NotNull Consumer<ScheduledTask> scheduledTaskConsumer, long time) {
    this.runAsync(scheduledTaskConsumer, time, TimeUnit.MILLISECONDS);
  }

  public void runAsync(@NotNull Consumer<ScheduledTask> scheduledTaskConsumer, long time, @NotNull TimeUnit unit) {
    Bukkit.getAsyncScheduler().runDelayed(plugin, scheduledTaskConsumer, time, unit);
  }

  public void runAsyncSchedule(@NotNull Consumer<ScheduledTask> scheduledTaskConsumer, @Range(from = 1, to = Long.MAX_VALUE) long time, @NotNull TimeUnit unit) {
    Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTaskConsumer, 1L, time, unit);
  }

  public ScheduledTask runAsyncSchedule(@NotNull Consumer<ScheduledTask> scheduledTaskConsumer, @Range(from = 1, to = Long.MAX_VALUE) long ticks) {
    return Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTaskConsumer, 1L, ticks + 50L, TimeUnit.MILLISECONDS);
  }

  public ScheduledTask runSchedule(Consumer<ScheduledTask> scheduledTaskConsumer, long ticks) {
    return Bukkit.getGlobalRegionScheduler().runAtFixedRate(plugin, scheduledTaskConsumer, 1L, ticks);
  }
}
