package xyz.scropy.seconomy.managers;

import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownProvider {

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Duration duration;

    public CooldownProvider(Duration duration) {
        this.duration = duration;
    }

    public void apply(UUID player) {
        cooldowns.put(player, duration.plusMillis(System.currentTimeMillis()).toMillis());
    }

    public long getRemainingTime(UUID player) {
        if(!isOnCooldown(player)) return 0;
        return duration.minusMillis(System.currentTimeMillis()).toSeconds();
    }

    public boolean isOnCooldown(UUID player) {
        if (!cooldowns.containsKey(player)) return false;
        if (cooldowns.get(player) < System.currentTimeMillis()) {
            cooldowns.remove(player);
            return false;
        }
        return true;
    }
}
