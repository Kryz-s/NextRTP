package io.github.krys.nextrtp.common.obj;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Cooldown {

    private final Map<UUID, Long> cooldownMap;
    private final long cooldown;

    public Cooldown(final int cooldown) {
        this.cooldown = cooldown * 1000L;
        this.cooldownMap = new HashMap<>();
    }

    public void toCooldown(Player player) {
        final long time = System.currentTimeMillis() + cooldown;
        this.cooldownMap.put(player.getUniqueId(), time);
    }

    public boolean isInCooldown(Player player) {
        final long now = System.currentTimeMillis();
        return cooldownMap.getOrDefault(player.getUniqueId(), 0L) > now;
    }

    public int getSecondsLeft(Player player) {
        long expiry = this.cooldownMap.getOrDefault(player.getUniqueId(), 0L);
        long now = System.currentTimeMillis();

        if (now >= expiry) {
            return 0;
        }

        return (int) ((expiry - now) / 1000);
    }
}
