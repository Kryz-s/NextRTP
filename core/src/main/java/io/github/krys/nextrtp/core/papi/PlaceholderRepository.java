package io.github.krys.nextrtp.core.papi;

import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class PlaceholderRepository {
  static final Map<String, PlaceholderElement> PLACEHOLDERS = new Object2ObjectOpenHashMap<>();

  public static void register(PlaceholderElement element) {
    PLACEHOLDERS.put(element.identifier(), element);
  }
}
