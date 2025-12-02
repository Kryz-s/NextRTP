package io.github.krys.nextrtp.core.util;

import java.util.function.Predicate;

import io.papermc.paper.command.brigadier.CommandSourceStack;

public enum Permission implements Predicate<CommandSourceStack> {
  COMMAND("nextrtp.command.rtp"),
  SUDO("nextrtp.command.sudo"),
  WORLD("nextrtp.command.world");

  private final String permission;

  Permission(String permission) {
    this.permission = permission;
  }

  @Override
  public boolean test(CommandSourceStack t) {
    return t.getSender().hasPermission(permission);
  }

}
