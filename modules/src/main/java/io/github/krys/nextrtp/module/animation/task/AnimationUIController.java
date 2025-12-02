package io.github.krys.nextrtp.module.animation.task;

import io.github.krys.nextrtp.module.animation.model.Animation;
import io.github.krys.nextrtp.module.animation.model.UIContainer;
import io.github.krys.nextrtp.module.animation.service.AnimationService;
import io.github.krys.nextrtp.module.animation.wrapper.InternalPacketWrapper;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import io.github.krys.nextrtp.core.service.papi.PlaceholderAPIService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Iterator;
import java.util.function.Consumer;

public final class AnimationUIController implements Consumer<ScheduledTask> {
  private static final MiniMessage MM = MiniMessage.miniMessage();
  private static final int FADE_TICKS = 15;

  public static boolean running = false;
  private final PlaceholderAPIService service;

  public AnimationUIController(PlaceholderAPIService service) {
    this.service = service;
  }

  @Override
  public void accept(ScheduledTask scheduledTask) {
    final var animations = AnimationService.ANIMATIONS;

    if (animations.isEmpty()) {
      scheduledTask.cancel();
      running = false;
      return;
    }

    final Iterator<Animation> anim = animations.iterator();
    while (anim.hasNext()) {
      final Animation animation = anim.next();
      final Player player = animation.player();

      if (!player.isOnline()) {
        animation.markFinalized();
        anim.remove();
        continue;
      }

      var connection = ((CraftPlayer) player).getHandle().connection.connection;

      int major = 1000;

      UIContainer title = animation.title();
      String nextTitle = null;

      if (title != null) {
        nextTitle = title.next();
        major = title.interval();
        if (nextTitle != null) {
          var nmsComponent = toNms(nextTitle, player);
          connection.channel.write(new InternalPacketWrapper(new ClientboundSetTitleTextPacket(nmsComponent)));
        }
      }

      UIContainer subtitle = animation.subtitle();
      String nextSubtitle = null;

      if (subtitle != null) {
        nextSubtitle = subtitle.next();
        major = Math.min(major, subtitle.interval());
        if (nextSubtitle != null) {
          var nmsComponent = toNms(nextSubtitle, player);
          connection.channel.write(new InternalPacketWrapper(new ClientboundSetSubtitleTextPacket(nmsComponent)));
        }
      }

      if (nextSubtitle != null || nextTitle != null) {
        if (!animation.hasSentTimes()) {
          animation.markTimesSent();
          connection.channel.write(new InternalPacketWrapper(new ClientboundSetTitlesAnimationPacket(FADE_TICKS, major / 50, 0)));
        } else {
          connection.channel.write(new InternalPacketWrapper(new ClientboundSetTitlesAnimationPacket(0, major / 50, FADE_TICKS)));
        }
      }

      UIContainer actionBar = animation.actionBar();
      if (actionBar != null) {
        String nextAction = actionBar.next();
        if (nextAction != null) {
          var nmsComponent = toNms(nextAction, player);
          connection.send(new ClientboundSetActionBarTextPacket(nmsComponent));
        }
      }

      if (animation.isFinalized()) anim.remove();
    }
  }

  private net.minecraft.network.chat.Component toNms(String text, Player player) {
    String parsed = text;

    if (text.indexOf('%') != -1) {
      parsed = service.parse(player, text);
    }

    return PaperAdventure.asVanilla(MM.deserialize(parsed));
  }
}
