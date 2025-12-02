package io.github.krys.nextrtp.common.obj;

import io.github.krys.nextrtp.common.info.BaseTeleportInfo;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public record ContextualTeleport(@NotNull ChunkLocationSnapshot snapshot,
                                 @NotNull BaseTeleportInfo info,
                                 @NotNull Player player) { }
