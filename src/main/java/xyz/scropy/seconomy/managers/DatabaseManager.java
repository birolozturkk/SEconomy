package xyz.scropy.seconomy.managers;


import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.jdbc.db.DatabaseTypeUtils;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.logger.NullLogBackend;
import com.j256.ormlite.support.ConnectionSource;
import org.bukkit.configuration.Configuration;
import xyz.scropy.seconomy.SEconomyPlugin;
import xyz.scropy.seconomy.repository.Repository;
import xyz.scropy.seconomy.user.User;

import java.io.File;
import java.sql.SQLException;
import java.util.Comparator;

public class DatabaseManager {

    private final SEconomyPlugin plugin;
    private ConnectionSource connectionSource;

    private Repository<User, Long> userRepository;

    public DatabaseManager(SEconomyPlugin plugin) {
        this.plugin = plugin;
    }

    public void init() throws SQLException {
        LoggerFactory.setLogBackendFactory(new NullLogBackend.NullLogBackendFactory());

        Configuration config = plugin.getConfig();
        String databaseURL = getDatabaseURL(config);

        this.connectionSource = new JdbcConnectionSource(
                databaseURL,
                config.getString("username"),
                config.getString("password"),
                DatabaseTypeUtils.createDatabaseType(databaseURL)
        );

        userRepository = new Repository<>(connectionSource, User.class, Comparator.comparing(User::getUniqueId));

 }

    /**
     * Database connection String used for establishing a connection.
     *
     * @return The database URL String
     */
    private String getDatabaseURL(Configuration config) {
        switch (config.getString("sql.driver")) {
            case "MYSQL":
                return "jdbc:" + config.getString("sql.driver").toLowerCase() + "://" + config.getString("sql.host") + ":" + config.getInt("sql.port") + "/" + config.getString("sql.database") + "?useSSL=" + plugin.getConfig().getBoolean("sql.useSSL");
            case "SQLITE":
                return "jdbc:sqlite:" + new File(plugin.getDataFolder(), config.getString("sql.database") + ".db");
            default:
                throw new UnsupportedOperationException("Unsupported driver (how did we get here?): " + config.getString("driver"));
        }
    }

    public Repository<User, Long> getUserRepository() {
        return userRepository;
    }
}
