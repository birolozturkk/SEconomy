package xyz.scropy.seconomy;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.scropy.seconomy.commands.*;
import xyz.scropy.seconomy.config.Config;
import xyz.scropy.seconomy.listeners.JoinListener;
import xyz.scropy.seconomy.managers.DatabaseManager;
import xyz.scropy.seconomy.user.User;
import xyz.scropy.seconomy.user.UserManager;

import java.sql.SQLException;

public final class SEconomyPlugin extends JavaPlugin {

    private DatabaseManager databaseManager;
    private UserManager userManager;

    private final Config config = new Config(this, "config");

    private BukkitAudiences adventure;
    private static SEconomyPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        this.adventure = BukkitAudiences.create(this);
        config.create();
        registerCommands();
        registerListeners();
        setupManagers();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new JoinListener(this), this);
    }

    private void registerCommands() {
        getServer().getPluginCommand("balance").setExecutor(new BalanceCommand(this));
        getServer().getPluginCommand("setbalance").setExecutor(new SetBalanceCommand(this));
        getServer().getPluginCommand("earn").setExecutor(new EarnCommand(this));
        getServer().getPluginCommand("give").setExecutor(new GiveCommand(this));
        getServer().getPluginCommand("seconomy").setExecutor(new ReloadCommand(this));
    }

    private void setupManagers() {
        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.init();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        userManager = new UserManager(databaseManager.getUserRepository());
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public BukkitAudiences getAdventure() {
        return adventure;
    }

    public static SEconomyPlugin getInstance() {
        return instance;
    }

    @Override
    public Config getConfig() {
        return config;
    }
}
