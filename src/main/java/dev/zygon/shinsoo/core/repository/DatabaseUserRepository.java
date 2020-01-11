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

import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.database.Database;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jooq.Record5;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link UserRepository} which utilizes
 * {@link Database} to retrieve user data stored in an SQL
 * database.  Utilizes {@link DSLDictionary} in order to
 * resolve any fields that may vary from application to
 * application.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class DatabaseUserRepository implements UserRepository {

    private static final String USERS_ONLINE_CACHE = "UsersOnlineCache";

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Inject
    EmbeddedCacheManager manager;

    @Transactional
    @Override
    public long usersOnline() throws Exception {
        Cache<String, Long> cache = manager.getCache(USERS_ONLINE_CACHE);
        if (cache.containsKey(USERS_ONLINE_CACHE))
            return cache.get(USERS_ONLINE_CACHE);

        Connection connection = database.getConnection();
        try {
            long online = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(USER_TABLE)))
                    .where(field(dictionary.value(USER_LOGIN_STATUS_COLUMN)).ne(UserStatus.LOGGED_OUT_STATUS))
                    .fetchOne(0, long.class);
            cache.put(USERS_ONLINE_CACHE, online);
            return online;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean userExists(String username) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(USER_TABLE)))
                    .where(field(dictionary.value(USER_USERNAME_COLUMN)).eq(username))
                    .fetchOne(0, int.class);
            return count > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean idExists(String id) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(USER_TABLE)))
                    .where(field(dictionary.value(USER_USERNAME_COLUMN)).eq(id))
                    .fetchOne(0, int.class);
            return count > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean emailExists(String email) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(USER_TABLE)))
                    .where(field(dictionary.value(USER_EMAIL_COLUMN)).eq(email))
                    .fetchOne(0, int.class);
            return count > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean create(UserDetails details) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int rowsInserted = using(connection)
                    .insertInto(table(dictionary.value(USER_TABLE)))
                    .columns(field(dictionary.value(USER_USERNAME_COLUMN)),
                             field(dictionary.value(USER_MAPLE_ID_COLUMN)),
                             field(dictionary.value(USER_PASSWORD_COLUMN)),
                             field(dictionary.value(USER_EMAIL_COLUMN)))
                    .values(details.getUsername(), details.getMapleId(), details.getPassword(), details.getEmail())
                    .execute();
            return rowsInserted > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public UserDetails detailsForEmail(String email) throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                .select(field(dictionary.value(USER_USERNAME_COLUMN)),
                        field(dictionary.value(USER_MAPLE_ID_COLUMN)),
                        field(dictionary.value(USER_EMAIL_COLUMN)),
                        field(dictionary.value(USER_PASSWORD_COLUMN)),
                        field(dictionary.value(USER_GM_LEVEL_COLUMN)))
                .from(table(dictionary.value(USER_TABLE)))
                .where(field(dictionary.value(USER_EMAIL_COLUMN)).eq(email))
                .fetchOne(this::mapDetails);
        } finally {
            database.release(connection);
        }
    }

    private UserDetails mapDetails(Record5<Object, Object, Object, Object, Object> record) {
        return UserDetails.builder()
                .username(record.getValue(dictionary.value(USER_USERNAME_COLUMN), String.class))
                .mapleId(record.getValue(dictionary.value(USER_MAPLE_ID_COLUMN), String.class))
                .email(record.getValue(dictionary.value(USER_EMAIL_COLUMN), String.class))
                .password(record.getValue(dictionary.value(USER_PASSWORD_COLUMN), String.class))
                .gmLevel(record.getValue(dictionary.value(USER_GM_LEVEL_COLUMN), Integer.class))
                .build();
    }
}
