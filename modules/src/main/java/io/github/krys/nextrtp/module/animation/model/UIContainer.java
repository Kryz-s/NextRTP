package io.github.krys.nextrtp.module.animation.model;

import io.github.krys.nextrtp.common.placeholder.LegacyParser;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class UIContainer {
  private final int interval;
  private final List<String> lines;
  private int index = 0;
  private long lastUpdate = 0;

  private static final String EMPTY = "";

  public UIContainer(int delay, List<String> lines) {
    this.interval = delay;
    this.lines = new ArrayList<>();
    lines.forEach(string -> this.lines.add(LegacyParser.parse(string)));
  }

  public int interval() {
    return interval;
  }

  public @Nullable String next() {
    long now = System.currentTimeMillis();
    if (now - lastUpdate < interval)
      return null;

    lastUpdate = now;
    String frame = lines.get(index);
    index = (index + 1) % lines.size();
    return frame;
  }
}