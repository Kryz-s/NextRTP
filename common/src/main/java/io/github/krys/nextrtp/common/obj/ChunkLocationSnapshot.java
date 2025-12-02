package io.github.krys.nextrtp.common.obj;

import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public record ChunkLocationSnapshot(@NotNull ChunkSnapshot snapshot,
                                    @NotNull Location location) {
}
