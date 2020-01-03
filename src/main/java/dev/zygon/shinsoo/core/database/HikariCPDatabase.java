package dev.zygon.shinsoo.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.zygon.shinsoo.database.Database;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public class HikariCPDatabase implements Database {

    @ConfigProperty(name = "database.driver", defaultValue = "")
    String driver;

    @ConfigProperty(name = "database.url", defaultValue = "")
    String url;

    @ConfigProperty(name = "database.user", defaultValue = "")
    String username;

    @ConfigProperty(name = "database.pass", defaultValue = "")
    String password;

    private HikariConfig config;
    private HikariDataSource source;
    private boolean initialized = false;

    public HikariCPDatabase() {
        config = new HikariConfig();
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (!initialized)
            init();
        return source.getConnection();
    }

    private void init() {
        config.setDriverClassName(driver);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);

        source = new HikariDataSource(config);
        initialized = true;
    }

    @Override
    public void release(Connection connection) throws SQLException {
        connection.close();
    }
}
