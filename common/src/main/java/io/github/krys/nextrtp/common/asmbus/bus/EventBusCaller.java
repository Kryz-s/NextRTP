package io.github.krys.nextrtp.common.asmbus.bus;

import io.github.krys.nextrtp.common.asmbus.event.Event;

public interface EventBusCaller {
  void call(Event event);
  void callAsync(Event event);
}
