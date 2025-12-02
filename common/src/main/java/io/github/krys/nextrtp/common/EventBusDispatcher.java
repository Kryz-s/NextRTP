package io.github.krys.nextrtp.common;

import io.github.krys.nextrtp.common.asmbus.DynamicEventBus;
import io.github.krys.nextrtp.common.asmbus.bus.EventBus;
import io.github.krys.nextrtp.common.asmbus.event.Event;
import org.bukkit.Bukkit;

import java.util.concurrent.ScheduledExecutorService;

public final class EventBusDispatcher {

  private static final EventBusDispatcher instance = new EventBusDispatcher();
  private static final EventBus EVENT_BUS;

  static {
    Class<?> clazz;
    try {
      clazz = Class.forName("io.papermc.paper.threadedregions.scheduler.FoliaAsyncScheduler");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    try {
      final var field = clazz.getDeclaredField("timerThread");
      field.setAccessible(true);
      final var executorService = (ScheduledExecutorService) field.get(Bukkit.getAsyncScheduler());
      EVENT_BUS = new DynamicEventBus(executorService);
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  public void registerListener(Object object) {
    EVENT_BUS.register(object);
  }

  public void call(Event event, boolean async) {
    if (async) {
      EVENT_BUS.callAsync(event);
      return;
    }
    EVENT_BUS.call(event);
  }

  public static EventBusDispatcher instance() {
    return instance;
  }
}
