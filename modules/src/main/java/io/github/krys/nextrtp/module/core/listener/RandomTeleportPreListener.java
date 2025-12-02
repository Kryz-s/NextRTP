package io.github.krys.nextrtp.module.core.listener;

import org.bukkit.entity.Player;

import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreEndEvent;
import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportPreStartEvent;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.core.message.Messager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

public final class RandomTeleportPreListener {

  @Listener
  public void onPreStart(RandomTeleportPreStartEvent event) {
    final BaseTeleportInfo info = event.teleportInfo;
    final Player player = event.player;

    if (info == null) return;  

    if (!(info instanceof WorldTeleportInfo)) return;

    if (info.permission != null && !player.hasPermission(info.permission)) {
      Messager.WORLD_NO_PERMISSION.accept(player, Placeholder.unparsed("world", info.id));
      event.setCancelled(true);
      return;
    }
  }
  
  @Listener
  public void onPreEnd(RandomTeleportPreEndEvent event) {
    final BaseTeleportInfo info = event.teleportInfo;

    if (!(info instanceof WorldTeleportInfo teleportInfo)) return;

    event.world = teleportInfo.world;
  }
}
