package io.github.krys.nextrtp.common.asmbus.reflection;

import java.lang.reflect.Method;

public final class ListenerMethodInfo {
  public final Object instance;
  public final Method method;

  public ListenerMethodInfo(Object instance, Method method) {
    this.instance = instance;
    this.method = method;
  }
}