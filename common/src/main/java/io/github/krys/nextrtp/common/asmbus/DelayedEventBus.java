package io.github.krys.nextrtp.common.asmbus;

import io.github.krys.nextrtp.common.asmbus.bus.EventBus;
import io.github.krys.nextrtp.common.asmbus.dispatcher.EventDispatcher;
import io.github.krys.nextrtp.common.asmbus.event.Event;
import io.github.krys.nextrtp.common.asmbus.reflection.ListenerMethodInfo;
import io.github.krys.nextrtp.common.asmbus.util.DispatcherBuilder;
import io.github.krys.nextrtp.common.asmbus.util.ObjectScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;


public class DelayedEventBus implements EventBus {

  private final Map<Class<? extends Event>, List<ListenerMethodInfo>> listenerMap = new ConcurrentHashMap<>();
  private final Map<Class<? extends Event>, EventDispatcher<Event>> dispatcherCache = new ConcurrentHashMap<>();

  private final Set<Class<? extends Event>> dirtyEventTypes = ConcurrentHashMap.newKeySet();

  private final Executor executor;

  public DelayedEventBus(Executor executor) {
    this.executor = executor;
  }

  public DelayedEventBus() {
    this(null);
  }

  public void register(Object listenerInstance) {
    Map<Class<? extends Event>, List<ListenerMethodInfo>> newListeners = ObjectScanner.scan(listenerInstance);

    newListeners.forEach((eventType, infoList) -> {
      listenerMap.computeIfAbsent(eventType, k -> new ArrayList<>()).addAll(infoList);
    });

    dirtyEventTypes.addAll(newListeners.keySet());
  }

  public void build() {
    if (dirtyEventTypes.isEmpty()) {
      return;
    }

    for (Class<? extends Event> eventType : dirtyEventTypes) {
      DispatcherBuilder.buildDispatcher(listenerMap.get(eventType), dispatcherCache, eventType);
    }

    dirtyEventTypes.clear();
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