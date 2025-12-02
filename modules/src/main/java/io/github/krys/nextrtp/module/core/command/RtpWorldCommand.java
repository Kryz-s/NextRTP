package io.github.krys.nextrtp.module.core.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.command.BrigadierCommand;
import io.github.krys.nextrtp.common.command.BrigadierCommandBuilder;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.core.rtp.TeleportPending;
import io.github.krys.nextrtp.core.rtp.identity.TeleportIdentityTypes;
import io.github.krys.nextrtp.core.util.Permission;
import io.github.krys.nextrtp.module.core.command.argument.WorldTeleportInfoArgumentType;
import io.github.krys.nextrtp.module.core.provider.WorldTeleportInfoProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class RtpWorldCommand implements BrigadierCommandBuilder<LiteralArgumentBuilder<CommandSourceStack>> {

  private final @NotNull NextAPI nextAPI;
  private final @NotNull WorldTeleportInfoProvider provider;

  public RtpWorldCommand(@NotNull NextAPI nextAPI, @NotNull WorldTeleportInfoProvider provider) {
    this.nextAPI = nextAPI;
    this.provider = provider;
  }

  public LiteralArgumentBuilder<CommandSourceStack> builder() {
    return literal("world")
            .requires(Permission.WORLD)
            .then(argument("world", WorldTeleportInfoArgumentType.argumentType(provider))
                .executes(this::onWorld));
  }

  private int onWorld(CommandContext<CommandSourceStack> context) {
    final CommandSourceStack stack = context.getSource();
    final Player player = (Player) stack.getSender();
    final WorldTeleportInfo teleportInfo = context.getArgument("world", WorldTeleportInfo.class);

    if (teleportInfo == null) {
      return 0;
    }
    final String perm = teleportInfo.permission;
    if (player.hasPermission("nextrtp.command.world.bypass")) {
      nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.CORE);
      return 1;
    }
    if (perm == null) {
      nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.CORE);
      return 1;
    } else if (player.hasPermission(perm)) {
      nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.CORE);
      return 1;
    }
    Messager.NOT_ALLOWED.accept(player);
    return 0;
  }
}
