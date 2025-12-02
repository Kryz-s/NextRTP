package io.github.krys.nextrtp.module.core.placeholder;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import io.github.krys.nextrtp.common.info.WorldTeleportInfo;
import io.github.krys.nextrtp.common.registry.Registries;
import io.github.krys.nextrtp.core.papi.Arguments;
import io.github.krys.nextrtp.core.papi.PlaceholderElement;

public final class WorldTeleportInfoPlaceholder extends PlaceholderElement {

  public WorldTeleportInfoPlaceholder() {
    super("world_teleport_info");
  }

  @Override
  public int minArguments() {
    return 2;
  }

  @Override
  public @Nullable String onRequest(Player player, Arguments arguments) {
    final String worldName = arguments.next();
    final WorldTeleportInfo teleportInfo = Registries.WORLD_TELEPORT_INFO.get(worldName);

    if (teleportInfo == null) return null;
    final String section = arguments.next();

    return switch(section) {
      case "id" -> teleportInfo.id;
      case "money" -> String.valueOf(teleportInfo.money);
      case "algorithm" -> teleportInfo.algorithm.id;
      case "cooldown" -> String.valueOf(teleportInfo.cooldown.getSecondsLeft(player));
      case "in_cooldown" -> String.valueOf(teleportInfo.cooldown.isInCooldown(player));
      case "center_x" -> String.valueOf(teleportInfo.centerX);
      case "center_z" -> String.valueOf(teleportInfo.centerZ);
      case "permission" -> teleportInfo.permission;
      case "delay" -> String.valueOf(teleportInfo.delay);
      case "min_y" -> String.valueOf(teleportInfo.minY);
      case "max_y" -> String.valueOf(teleportInfo.maxY);
      case "min_radius" -> String.valueOf(teleportInfo.minRadius);
      case "max_radius" -> String.valueOf(teleportInfo.maxRadius);
      default -> null;
    };
  }

}
