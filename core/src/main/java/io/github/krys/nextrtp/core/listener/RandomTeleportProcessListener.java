package io.github.krys.nextrtp.core.listener;

import io.github.krys.nextrtp.common.api.event.bus.RandomTeleportProcessEvent;
import io.github.krys.nextrtp.common.asmbus.listener.Listener;
import io.github.krys.nextrtp.core.message.Messager;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;

import org.bukkit.entity.Player;

public final class RandomTeleportProcessListener {

  @Listener(priority = Byte.MAX_VALUE)
  public void onProcess(final RandomTeleportProcessEvent event) {
    final Player sender = event.player;

    Messager.WAITING.accept(sender, Placeholder.unparsed("seconds", String.valueOf(event.seconds)));
  }
}
