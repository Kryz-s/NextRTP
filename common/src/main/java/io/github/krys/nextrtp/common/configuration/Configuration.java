package io.github.krys.nextrtp.common.configuration;

import io.github.krys.nextrtp.common.reload.Reloadable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface Configuration<T> extends Reloadable {
    Optional<String> getString(String path);
    Optional<Integer> getInt(String path);
    Optional<Boolean> getBoolean(String path);
    Optional<Double> getDouble(String path);
    Optional<Long> getLong(String path);
    Optional<List<String>> getStringList(String path);

    Map<String, Object> getMap(String path);

    @NotNull
    String getString(String path, String defaultValue);
    int getInt(String path, int defaultValue);
    boolean getBoolean(String path, boolean defaultValue);
    double getDouble(String path, double defaultValue);
    long getLong(String path, long defaultValue);
    @NotNull
    List<String> getStringList(String path, List<String> defaultValue);

    <C> List<C> getList(String path);

    T getSection(String path);
    boolean hasPath(String path);
    @NotNull
    Set<String> getKeys(String path);
    @NotNull
    String getName();
    boolean isLoaded();

    void requirePath(String path) throws ConfigException;
    <O> O getRequired(String path, Class<O> type) throws ConfigException;
}