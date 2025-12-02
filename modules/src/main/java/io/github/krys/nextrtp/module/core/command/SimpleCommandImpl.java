package io.github.krys.nextrtp.module.core.command;

import io.github.krys.nextrtp.module.core.provider.WorldTeleportInfoProvider;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.github.krys.nextrtp.common.NextAPI;
import io.github.krys.nextrtp.common.command.SimpleCommand;
import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.core.message.Messager;
import io.github.krys.nextrtp.core.rtp.TeleportPending;
import io.github.krys.nextrtp.core.rtp.identity.TeleportIdentityTypes;
import com.mojang.brigadier.context.CommandContext;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class SimpleCommandImpl implements SimpleCommand {

  private final @NotNull NextAPI nextAPI;
  private final @NotNull WorldTeleportInfoProvider provider;

  public SimpleCommandImpl(@NotNull NextAPI nextAPI, @NotNull WorldTeleportInfoProvider provider) {
    this.nextAPI = nextAPI;
    this.provider = provider;
  }

  @Override
  public void execute(CommandContext<CommandSourceStack> context) {
    final CommandSourceStack stack = (CommandSourceStack) context.getSource();
    final CommandSender sender = stack.getSender();
    if (!(sender instanceof Player player)) {
      Messager.INVALID_SENDER.accept(sender);
      return;
    }
    final WorldTeleportInfo teleportInfo = provider.getOrCreate(player.getWorld().getName(), player.getWorld());

    nextAPI.startTeleport(player, teleportInfo, TeleportPending.DEFAULT, TeleportIdentityTypes.CORE);
  }
}
