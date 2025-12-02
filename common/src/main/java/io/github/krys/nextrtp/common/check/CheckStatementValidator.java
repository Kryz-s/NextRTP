package io.github.krys.nextrtp.common.check;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface CheckStatementValidator {

  boolean validate(@NotNull Player player, @NotNull Location initLocation);
}
