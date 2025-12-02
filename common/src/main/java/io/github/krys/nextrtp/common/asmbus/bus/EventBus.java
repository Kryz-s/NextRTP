package io.github.krys.nextrtp.common.asmbus.bus;

public interface EventBus extends EventBusCaller {

  void register(Object inst);

}
