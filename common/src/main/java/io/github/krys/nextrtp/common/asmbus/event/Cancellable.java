package io.github.krys.nextrtp.common.asmbus.event;

public interface Cancellable {
  boolean isCancelled();
  void setCancelled(boolean cancelled);
}
