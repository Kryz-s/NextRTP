package io.github.krys.nextrtp.core.rtp.checker;

import io.github.krys.nextrtp.common.check.CheckStatementValidator;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class DefaultLocationValidator implements CheckStatementValidator {

  @Override
  public boolean validate(@NotNull Player player, @NotNull Location executorLocation) {
    final int Ex = (int) executorLocation.getX();
    final int Ey = (int) executorLocation.getY();
    final int Ez = (int) executorLocation.getZ();

    final int Px = (int) player.getLocation().getX();
    final int Py = (int) player.getLocation().getY();
    final int Pz = (int) player.getLocation().getZ();

    return Ex == Px && Ey == Py && Ez == Pz;
  }

}
