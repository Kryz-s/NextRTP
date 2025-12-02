package io.github.krys.nextrtp.module.animation.wrapper;

import net.minecraft.network.protocol.Packet;

public final class InternalPacketWrapper {
  public final Packet<?> wrapperPacket;

  public InternalPacketWrapper(Packet<?> packet) {
    this.wrapperPacket = packet;
  }
}
