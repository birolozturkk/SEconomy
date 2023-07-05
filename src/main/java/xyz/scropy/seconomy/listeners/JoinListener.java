package xyz.scropy.seconomy.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.scropy.seconomy.SEconomyPlugin;
import xyz.scropy.seconomy.user.User;

public class JoinListener implements Listener {

    private final SEconomyPlugin plugin;

    public JoinListener(SEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getUserManager().createUserIfAbsent(event.getPlayer().getUniqueId());
    }
}
