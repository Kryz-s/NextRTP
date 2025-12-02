package io.github.krys.nextrtp.common.info;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public final class WorldTeleportInfo extends BaseTeleportInfo {

  public final double centerX, centerZ;
  public final @NotNull World world;

  public WorldTeleportInfo(@NotNull Map<String, Object> map) throws NullPointerException {
    super(map);
    final World world = Bukkit.getWorld(super.id);
    if (world == null)
      throw new NullPointerException("World: [" + super.id + "] not found");
    this.world = world;
    final boolean useWorldBorder = (Boolean) map.getOrDefault("use-world-border", false);

    this.centerX = useWorldBorder ? world.getWorldBorder().getCenter().getX() : ((Number) map.get("center-x")).doubleValue();
    this.centerZ = useWorldBorder ? world.getWorldBorder().getCenter().getZ() : ((Number) map.get("center-z")).doubleValue();
  }

  public WorldTeleportInfo(@NotNull Map<String, Object> map, @NotNull World world) {
    super(map);
    this.world = Objects.requireNonNull(world, "world");
    final boolean useWorldBorder = (Boolean) map.getOrDefault("use-world-border", false);

    this.centerX = useWorldBorder ? world.getWorldBorder().getCenter().getX() : (double) map.get("center-x");
    this.centerZ = useWorldBorder ? world.getWorldBorder().getCenter().getZ() : (double) map.get("center-z");
  }
}
