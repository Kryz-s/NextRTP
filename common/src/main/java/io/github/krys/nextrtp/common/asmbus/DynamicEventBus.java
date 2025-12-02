package io.github.krys.nextrtp.common.asmbus;

import io.github.krys.nextrtp.common.asmbus.dispatcher.EventDispatcher;
import io.github.krys.nextrtp.common.asmbus.bus.EventBus;
import io.github.krys.nextrtp.common.asmbus.event.Event;
import io.github.krys.nextrtp.common.asmbus.reflection.ListenerMethodInfo;
import io.github.krys.nextrtp.common.asmbus.util.DispatcherBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import static io.github.krys.nextrtp.common.asmbus.util.ObjectScanner.scan;

public class DynamicEventBus implements EventBus {

  private final Map<Class<? extends Event>, List<ListenerMethodInfo>> listenerMap = new ConcurrentHashMap<>();

  private final Map<Class<? extends Event>, EventDispatcher<Event>> dispatcherCache = new ConcurrentHashMap<>();

  private final Executor executor;

  public DynamicEventBus(Executor executor) {
    this.executor = executor;
  }

  public DynamicEventBus() {
    this(null);
  }

  public void register(Object listenerInstance) {
    Map<Class<? extends Event>, List<ListenerMethodInfo>> newListeners = scan(listenerInstance);

    newListeners.forEach((eventType, infoList) -> {
      listenerMap.computeIfAbsent(eventType, k -> new ArrayList<>()).addAll(infoList);
    });

    newListeners.keySet().forEach(aClass -> {
      DispatcherBuilder.buildDispatcher(listenerMap.get(aClass), dispatcherCache, aClass);
    });
  }

  @SuppressWarnings("unchecked")
  @Override
  public void call(Event event) {
    EventDispatcher<Event> dispatcher = dispatcherCache.get(event.getClass());

    if (dispatcher != null) {
      dispatcher.dispatch(event);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void callAsync(Event event) {
    EventDispatcher<Event> dispatcher = dispatcherCache.get(event.getClass());

    if (dispatcher != null) {
      if (executor == null)
        CompletableFuture.runAsync(() -> dispatcher.dispatch(event));
      else
        CompletableFuture.runAsync(() -> dispatcher.dispatch(event), executor);
    }
  }
}