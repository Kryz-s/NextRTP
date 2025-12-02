package io.github.krys.nextrtp.common.reload;

public interface Loader extends Reloadable {

  void load();

  @Override
  default void reload() {
    this.load();
  }

  @Override
  default void onReload() {}
}
