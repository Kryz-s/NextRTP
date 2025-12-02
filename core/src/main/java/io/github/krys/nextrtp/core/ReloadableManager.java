package io.github.krys.nextrtp.core;

import io.github.krys.nextrtp.common.configuration.Configuration;
import io.github.krys.nextrtp.common.logger.LoggerService;
import io.github.krys.nextrtp.common.reload.Loader;
import io.github.krys.nextrtp.common.reload.Reloadable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.MinecraftServer;

import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.List;

@NullMarked
public final class ReloadableManager {

  private final HashSet<Configuration<?>> reloadableFiles;
  private final HashSet<Reloadable> reloadableElements;
  private final HashSet<Loader> loaders;

  private final LoggerService logger;

  public ReloadableManager(LoggerService logger) {
    this.reloadableFiles = new HashSet<>();
    this.reloadableElements = new HashSet<>();
    this.loaders = new HashSet<>();
    this.logger = logger;
  }

  public void add(Reloadable reloadable) {
    if(reloadable instanceof Configuration<?> configuration) {
      this.reloadableFiles.add(configuration);
      return;
    }
    if(reloadable instanceof Loader loader) {
      this.loaders.add(loader);
      return;
    }
    this.reloadableElements.add(reloadable);
  }

  public void reload() {
    reloadableFiles.forEach(Configuration::reload);

    reloadableElements.forEach(reloadable -> {
      reloadable.reload();
      reloadable.onReload();
    });

    allLoaders();
  }

  public void allLoaders() {
    loaders.forEach(Loader::load);
  }
}
