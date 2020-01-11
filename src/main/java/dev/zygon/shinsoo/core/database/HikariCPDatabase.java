/*
    Shinsoo: Java-Quarkus Back End for Aria
    Copyright (C) 2020  Brenterino <brent@zygon.dev>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package dev.zygon.shinsoo.core.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.zygon.shinsoo.database.Database;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Implementation for {@link Database} which utilizes
 * HikariCP to retrieve connections in a pooled fashion.
 * Releasing a connection does not close it and instead
 * returns it to the pool for reuse.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Singleton
public class HikariCPDatabase implements Database {

    @ConfigProperty(name = "database.driver", defaultValue = "")
    String driver;

    @ConfigProperty(name = "database.url", defaultValue = "")
    String url;

    @ConfigProperty(name = "database.user", defaultValue = "")
    Optional<String> username;

    @ConfigProperty(name = "database.pass", defaultValue = "")
    Optional<String> password;

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
        config.setUsername(username.orElse(""));
        config.setPassword(password.orElse(""));

        source = new HikariDataSource(config);
        initialized = true;
    }

    @Override
    public void release(Connection connection) throws SQLException {
        connection.close();
    }
}
