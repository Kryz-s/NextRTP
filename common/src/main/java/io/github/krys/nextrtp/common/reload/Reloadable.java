package io.github.krys.nextrtp.common.reload;

public interface Reloadable {
  void reload();

  default void onReload() {}
}
