package io.github.krys.nextrtp.common.asmbus.util;


import io.github.krys.nextrtp.common.asmbus.event.Event;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import io.github.krys.nextrtp.common.asmbus.reflection.ListenerMethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ObjectScanner {
  public static Map<Class<? extends Event>, List<ListenerMethodInfo>> scan(Object listenerInstance) {
    Map<Class<? extends Event>, List<ListenerMethodInfo>> found = new HashMap<>();

    for (Method method : listenerInstance.getClass().getMethods()) {
      if (method.isAnnotationPresent(Listener.class) && method.getParameterCount() == 1) {
        Class<?> paramType = method.getParameterTypes()[0];

        if (Event.class.isAssignableFrom(paramType)) {
          @SuppressWarnings("unchecked")
          Class<? extends Event> eventType = (Class<? extends Event>) paramType;

          ListenerMethodInfo info = new ListenerMethodInfo(listenerInstance, method);

          found.computeIfAbsent(eventType, k -> new ArrayList<>()).add(info);
        }
      }
    }
    return found;
  }

}
