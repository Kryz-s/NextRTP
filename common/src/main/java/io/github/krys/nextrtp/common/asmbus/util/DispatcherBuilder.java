package io.github.krys.nextrtp.common.asmbus.util;

import io.github.krys.nextrtp.common.asmbus.dispatcher.EventDispatcher;
import io.github.krys.nextrtp.common.asmbus.event.Event;
import io.github.krys.nextrtp.common.asmbus.generator.ASMEventDispatcherGenerator;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import io.github.krys.nextrtp.common.asmbus.reflection.ListenerMethodInfo;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class DispatcherBuilder {
  public static <E extends Event> void buildDispatcher(
    List<ListenerMethodInfo> allListeners,
    Map<Class<? extends Event>, EventDispatcher<Event>> dispatcherCache,
    Class<E> eventType) {

    if (allListeners == null || allListeners.isEmpty()) {
      dispatcherCache.remove(eventType);
      return;
    }

    List<ListenerMethodInfo> sortedListeners = allListeners.stream()
      .sorted(Comparator.comparingInt(info -> info.method.getAnnotation(Listener.class).priority()))
      .collect(Collectors.toList());

    try {
      EventDispatcher<Event> dispatcher = ASMEventDispatcherGenerator.generate(eventType, sortedListeners);

      dispatcherCache.put(eventType, dispatcher);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
