package io.github.krys.nextrtp.common.module;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Module<T> {
    String getName();

    String getId();

    void onEnable(T t);
    void onDisable(T t);
}