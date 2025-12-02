package io.github.krys.nextrtp.core.configuration;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import io.github.krys.nextrtp.common.configuration.ConfigException;
import io.github.krys.nextrtp.common.configuration.ConfigSupplier;
import io.github.krys.nextrtp.common.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

public class AbstractConfiguration implements Configuration<Section> {

  protected final YamlDocument document;
  protected final Map<String, Object> customCache;

  public AbstractConfiguration(@NotNull ConfigSupplier<YamlDocument> document) {
    try {
      this.document = document.get();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    this.customCache = new HashMap<>();
  }

  @Override
  public Optional<String> getString(String path) {
    return document.getOptionalString(path);
  }

  @Override
  public Optional<Integer> getInt(String path) {
    return document.getOptionalInt(path);
  }

  @Override
  public Optional<Boolean> getBoolean(String path) {
    return document.getOptionalBoolean(path);
  }

  @Override
  public Optional<Double> getDouble(String path) {
    return document.getOptionalDouble(path);
  }

  @Override
  public Optional<Long> getLong(String path) {
    return document.getOptionalLong(path);
  }

  @Override
  public Optional<List<String>> getStringList(String path) {
    return document.getOptionalStringList(path);
  }

  @Override
  @Nullable
  public Map<String, Object> getMap(String path) {
    return document.getSection(path).getStringRouteMappedValues(false);
  }

  @Override
  public @NotNull String getString(String path, String defaultValue) {
    return document.getString(path, defaultValue);
  }

  @Override
  public int getInt(String path, int defaultValue) {
    return document.getInt(path, defaultValue);
  }

  @Override
  public boolean getBoolean(String path, boolean defaultValue) {
    return document.getBoolean(path, defaultValue);
  }

  @Override
  public double getDouble(String path, double defaultValue) {
    return document.getDouble(path, defaultValue);
  }

  @Override
  public long getLong(String path, long defaultValue) {
    return document.getLong(path, defaultValue);
  }

  @Override
  public @NotNull List<String> getStringList(String path, List<String> defaultValue) {
    return document.getStringList(path, defaultValue);
  }

  @Override
  public <C> List<C> getList(String path) {
    return (List<C>) this.document.getList(path);
  }

  @Override
  public Section getSection(String path) {
    return document.getSection(path);
  }

  @Override
  public boolean hasPath(String path) {
    return document.contains(path);
  }

  @Override
  public @NotNull Set<String> getKeys(String path) {
    return this.getSection(path) != null ? this.getSection(path).getRoutesAsStrings(false) : Collections.emptySet();
  }

  @Override
  public @NotNull String getName() {
    return document.getFile().getName();
  }

  @Override
  public boolean isLoaded() {
    return document.getFile().exists();
  }

  @Override
  public void requirePath(String path) throws ConfigException {
    if (!hasPath(path)) {
      throw new ConfigException("Required path not found: " + path + " in config: " + getName());
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public <O> O getRequired(String path, Class<O> type) throws ConfigException {
    requirePath(path);

    Object value = document.get(path);
    if (value == null) {
      throw new ConfigException("Required value is null at path: " + path);
    }

    if (!type.isInstance(value)) {
      throw new ConfigException("Value at path " + path + " is not of type " + type.getSimpleName());
    }

    return (O) value;
  }

  public void set(String path, Object value) {
    this.document.set(path, value);
  }

  public void load() {
    try {
      this.customCache.clear();
      this.document.reload();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void save() {
    try {
      this.document.save();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void reload() {
    this.load();
  }

}
