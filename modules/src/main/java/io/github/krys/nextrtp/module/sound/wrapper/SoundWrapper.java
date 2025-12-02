package io.github.krys.nextrtp.module.sound.wrapper;

import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public final class SoundWrapper {

  private static final Object2ObjectOpenHashMap<String, Key> keyCache = new Object2ObjectOpenHashMap<>(Sound.class.getDeclaredFields().length);

  @Nullable
  public static Sound valueOf(@Subst("minecraft:example_sound.phase.2") @NotNull String key) {
    if (key == null) return null;

    Key kyoriKey = keyCache.get(key);
    if (kyoriKey == null) {
      kyoriKey = Key.key(key);
      keyCache.put(key, kyoriKey);
    }
    return Registry.SOUNDS.getOrThrow(kyoriKey);
  }
}
