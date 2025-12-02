package io.github.krys.nextrtp.module.core.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.command.BrigadierCommand;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.core.rtp.TeleportPending;
import io.github.krys.nextrtp.core.rtp.identity.TeleportIdentityTypes;
import io.github.krys.nextrtp.core.util.Permission;
import io.github.krys.nextrtp.module.core.command.argument.PlayerArgumentResolver;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class RtpSudoWorldCommand implements BrigadierCommand {

  private final RtpWorldCommand rtpWorldCommand;
  private final NextAPI nextAPI;

  public RtpSudoWorldCommand(@NotNull RtpWorldCommand rtpWorldCommand, NextAPI nextAPI) {
    this.rtpWorldCommand = rtpWorldCommand;
    this.nextAPI = nextAPI;
  }

  @Override
  public CommandNode<CommandSourceStack> node() {;
    return literal("sudo")
        .requires(Permission.SUDO)
        .then(argument("player", PlayerArgumentResolver.resolver())
            .then(rtpWorldCommand.builder()
                .requires(source -> source.getSender().hasPermission("nextrtp.command.sudo"))
                .executes(this::onSudoWorld)))
        .build();
  }

  private int onSudoWorld(CommandContext<CommandSourceStack> context) {
    final Player player = context.getArgument("player", Player.class);
    if (player == null) {
      return 0;
    }

    WorldTeleportInfo teleportInfo;

    try {
      teleportInfo = context.getArgument("world", WorldTeleportInfo.class);
    } catch (Exception e) {
      teleportInfo = null;
    }

    if (teleportInfo == null) {
      return 0;
    }
    // final String perm = teleportInfo.permission;
    // if (player.hasPermission("nextrtp.command.world.bypass") || bypass) {
    nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.CORE);
    // }
    // if (perm == null) {
    // nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT,
    // TeleportIdentityTypes.CORE);
    // return;
    // } else if (player.hasPermission(perm)) {
    // nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT,
    // TeleportIdentityTypes.CORE);
    // return;
    // }
    // throw new NoWorldPermissionException(teleportInfo.world.getName());
    return 1;
  }
}
