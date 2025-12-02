package io.github.krys.nextrtp.module.animation.channel;

import io.github.krys.nextrtp.module.animation.service.AnimationService;
import io.github.krys.nextrtp.module.animation.wrapper.InternalPacketWrapper;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.entity.Player;

public final class PacketChannelDispatcher extends ChannelDuplexHandler {

  private final Player player;

  private final PacketProcessor[] processors;

  private static final Object2IntMap<Class<?>> CLASS_INDEX = new Object2IntOpenHashMap<>();

  private static final int NO_INDEX = -1;

  static {
    CLASS_INDEX.defaultReturnValue(NO_INDEX);

    CLASS_INDEX.put(ClientboundSetTitleTextPacket.class, 0);
    CLASS_INDEX.put(ClientboundSetSubtitleTextPacket.class, 1);
    CLASS_INDEX.put(ClientboundSetTitlesAnimationPacket.class, 2);
    CLASS_INDEX.put(InternalPacketWrapper.class, 3);
  }


  public PacketChannelDispatcher(Player player) {
    this.player = player;
    this.processors = new PacketProcessor[CLASS_INDEX.size()];

    this.processors[0] = this::cancelPacketInAnimation;
    this.processors[1] = this::cancelPacketInAnimation;
    this.processors[2] = this::cancelPacketInAnimation;
    this.processors[3] = (PacketProcessor<InternalPacketWrapper>) this::internalPacketWrapper;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {

    int index = CLASS_INDEX.getInt(msg.getClass());

    if (index != NO_INDEX) {
      PacketProcessor proc = processors[index];
      if (proc != null) {
        Object out = proc.process(player, msg);
        if (out == null) {
          return;
        }
        msg = out;
      }
    }

    ctx.write(msg, promise);
  }

  public Object cancelPacketInAnimation(Player player, Object packet) {
    if (AnimationService.PLAYERS_IN_ANIMATION.contains(player.getUniqueId()))
      return null;
    return packet;
  }

  public Object internalPacketWrapper(Player player, InternalPacketWrapper wrapper) {
    return wrapper.wrapperPacket;
  }
}
