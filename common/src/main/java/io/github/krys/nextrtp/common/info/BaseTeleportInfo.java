package io.github.krys.nextrtp.common.info;

import io.github.krys.nextrtp.common.condition.Condition;
import io.github.krys.nextrtp.common.obj.Algorithm;
import io.github.krys.nextrtp.common.obj.Cooldown;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static io.github.krys.nextrtp.common.condition.ConditionType.POS;
import static io.github.krys.nextrtp.common.condition.ConditionType.PRE;
import static io.github.krys.nextrtp.common.registry.Registries.CONDITION;

public class BaseTeleportInfo {

  public final int maxRadius, minRadius;

  public final int money;
  public final int maxY, minY;
  public final int delay;
  public final int attempts;

  @NotNull
  public final Algorithm algorithm;
  @NotNull
  public final Cooldown cooldown;

  @Nullable
  public final String permission;

  @NotNull
  public final String id;

  @Nullable
  public final Condition preCondition, posCondition;

  public final double centerX, centerZ;

  public BaseTeleportInfo(final @NotNull Map<String, Object> map) {
    this.money = (int) map.getOrDefault("money", 0);
    this.cooldown = new Cooldown((int) map.getOrDefault("cooldown", 0));
    this.delay = (int) map.getOrDefault("delay", 0);
    this.maxRadius = (int) map.getOrDefault("max-radius", 50);
    this.minRadius = (int) map.getOrDefault("min-radius", 0);
    this.maxY = (int) map.getOrDefault("max-y", 100);
    this.minY = (int) map.getOrDefault("min-y", 0);

    String algorithmStr = (String) map.getOrDefault("algorithm", "SQUARE");
    this.algorithm = Algorithm.valueOf(algorithmStr.toUpperCase());

    this.id = (String) map.get("id");
    this.permission = (String) map.get("permission");
    this.centerX = ((Number) map.get("center-x")).doubleValue();
    this.centerZ = ((Number) map.get("center-z")).doubleValue();
    
    this.attempts = (int) map.getOrDefault("attempts", 1);

    this.preCondition = CONDITION.get(PRE).get((String) map.getOrDefault("pre-condition", ""));
    this.posCondition = CONDITION.get(POS).get((String) map.getOrDefault("pos-condition", ""));
  }

  public boolean isUsable() {
    return (maxRadius > -1 || minRadius > -1);
  }
}
