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
package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.core.dto.ServerDetails;
import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.Server;
import dev.zygon.shinsoo.repository.ServerRepository;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jooq.Record3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link } which utilizes
 * {@link Database} to retrieve post data stored in an SQL
 * database.  Utilizes {@link DSLDictionary} in order to
 * resolve any fields that may vary from application to
 * application.  Caches results using {@link EmbeddedCacheManager}
 * in order to improve performance and reduce database load.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class DatabaseServerRepository implements ServerRepository {

    private static final String SERVER_STATUS_CACHE = "ServerStatusCache";

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Inject
    EmbeddedCacheManager manager;

    @Transactional
    @Override
    public List<Server> servers() throws Exception {
        Cache<String, List<Server>> cache = manager.getCache(SERVER_STATUS_CACHE);
        if (cache.containsKey(SERVER_STATUS_CACHE))
            return cache.get(SERVER_STATUS_CACHE);

        List<Server> servers = fromDatabase().stream()
                .map(this::createServerStatus)
                .collect(Collectors.toList());
        cache.put(SERVER_STATUS_CACHE, servers);
        return servers;
    }

    private List<ServerDetails> fromDatabase() throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .select(field(dictionary.value(SERVER_NAME_COLUMN)),
                            field(dictionary.value(SERVER_IP_COLUMN)),
                            field(dictionary.value(SERVER_PORT_COLUMN)))
                    .from(table(dictionary.value(SERVER_TABLE)))
                    .orderBy(field(dictionary.value(SERVER_ID_COLUMN)).asc())
                    .fetch(this::mapDetails);
        } finally {
            database.release(connection);
        }
    }

    private ServerDetails mapDetails(Record3<Object, Object, Object> record) {
        return ServerDetails.builder()
                .name(record.getValue(field(dictionary.value(SERVER_NAME_COLUMN), String.class)))
                .ip(record.getValue(field(dictionary.value(SERVER_IP_COLUMN), String.class)))
                .port(record.getValue(field(dictionary.value(SERVER_PORT_COLUMN), int.class)))
                .build();
    }

    private Server createServerStatus(ServerDetails details) {
        return Server.builder()
                .name(details.getName())
                .status(serverOnline(details))
                .build();
    }

    private boolean serverOnline(ServerDetails details) {
        try (Socket socket = new Socket(details.getIp(), details.getPort())) {
            return socket.isConnected();
        } catch (IOException ex) {
            return false;
        }
    }
}
