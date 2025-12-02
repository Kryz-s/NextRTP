package io.github.krys.nextrtp.common.asmbus.dispatcher;

import io.github.krys.nextrtp.common.asmbus.event.Event;

public interface EventDispatcher<E extends Event> {
    void dispatch(E event);
}