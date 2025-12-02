package io.github.krys.nextrtp.common.logger;

import io.github.krys.nextrtp.common.configuration.Configuration;
import io.github.krys.nextrtp.common.reload.Reloadable;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public final class DebugService implements Reloadable {
  private static final Logger DEBUG = LoggerFactory.getLogger("NextRTP-Debug");
  @NotNull private final Configuration<?> configuration;
  private static boolean enabled;

  public DebugService(@NotNull Configuration<?> configuration) {
    this.configuration = configuration;
    enabled = configuration.getBoolean("debug", false);
  }

  public static void criticalDebug(@NotNull final Supplier<String> supplier, @NotNull Throwable throwable) {
    if(!enabled) return;
    DEBUG.error(supplier.get(), throwable);
  }

  public static void debug(@NotNull final Supplier<String> supplier, @NotNull final Object... objects) {
    if(!enabled) return;
    DEBUG.info(supplier.get(), objects);
  }

  @Override
  public void reload() {
    enabled = configuration.getBoolean("debug", false);
  }

  @Override
  public void onReload() {
    this.debug(() -> "Reloading DebugService...");
  }
}
