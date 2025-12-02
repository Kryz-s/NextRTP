package io.github.krys.nextrtp.core.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

public enum Messager {
  NO_MONEY("modules.core.currency.no-money"),
  CHARGED("modules.core.currency.charged"),

  WORLD_NO_PERMISSION("modules.core.command.world.no-permission"),
  WORLD_NO_FIND("modules.core.command.world.no-find"),
  RELOAD_SUCCESS("modules.core.command.reload.success"),
  RELOAD_ERROR("modules.core.command.reload.error"),
  INVALID_SENDER("modules.core.command.invalid-sender"),
  INVALID_PLAYER("modules.core.command.invalid-player"),

  STARTING("modules.core.teleport.starting"),
  WAITING("modules.core.teleport.waiting"),
  MOVE("modules.core.teleport.move"),
  PROGRESS("modules.core.teleport.progress"),
  TELEPORT_NO_PERMISSION("modules.core.teleport.no-permission"),
  ON_COOLDOWN("modules.core.teleport.on-cooldown"),
  NOT_ALLOWED("modules.core.teleport.not-allowed"),
  FAILED("modules.core.teleport.failed"),
  //REASON_INVALID_BIOME("modules.core.teleport.reasons.invalid-biome"),
  //REASON_INVALID_BLOCK("modules.core.teleport.reasons.invalid-block"),

  SIGN_SUCCESS("modules.sign.sign-success");

  private final String path;

  Messager(String path) {
    this.path = path;
  }

  public void accept(CommandSender sender) {
    final Component component = FakeComponent.asComponent(path, sender);
    if (component == null)
      return;
    sender.sendMessage(component);
  }

  public void accept(CommandSender sender, TagResolver... resolvers) {
    final Component component = FakeComponent.asComponent(path, sender, resolvers);
    if (component == null)
      return;
    sender.sendMessage(component);
  }

  public void accept(Player player) {
    final Component component = FakeComponent.asComponent(path, player);
    if (component == null)
      return;
    player.sendMessage(component);
  }

  public void accept(Player player, TagResolver... resolvers) {
    final Component component = FakeComponent.asComponent(path, player, resolvers);
    if (component == null)
      return;
    player.sendMessage(component);
  }
}
