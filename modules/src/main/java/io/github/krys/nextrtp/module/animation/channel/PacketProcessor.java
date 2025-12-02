package io.github.krys.nextrtp.module.animation.channel;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface PacketProcessor<P> {
  @Nullable
  Object process(Player player, P packet);
}
