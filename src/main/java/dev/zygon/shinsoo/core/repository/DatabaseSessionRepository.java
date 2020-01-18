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

import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.SessionRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jooq.DatePart;
import org.jooq.Record3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link SessionRepository} which utilizes
 * {@link Database} to retrieve session data stored in an SQL
 * database.  Utilizes {@link DSLDictionary} in order to
 * resolve any fields that may vary from application to
 * application.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class DatabaseSessionRepository implements SessionRepository {

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Transactional
    @Override
    public boolean sessionActive(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(SESSION_TABLE)))
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce)
                      .and(field(dictionary.value(SESSION_EXPIRE_COLUMN)).le(currentTimestamp())))
                    .fetchOne(0, int.class);
            return count > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public UserStatus session(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .select(field(dictionary.value(SESSION_USERNAME_COLUMN)),
                            field(dictionary.value(SESSION_MAPLE_ID_COLUMN)),
                            field(dictionary.value(SESSION_GM_LEVEL_COLUMN)))
                    .from(table(dictionary.value(SESSION_TABLE)))
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce)
                      .and(field(dictionary.value(SESSION_EXPIRE_COLUMN)).le(currentTimestamp())))
                    .fetchOne(this::mapStatus);
        } finally {
            database.release(connection);
        }
    }

    private UserStatus mapStatus(Record3<Object, Object, Object> record) {
        return UserStatus.builder()
                .username(record.getValue(dictionary.value(SESSION_USERNAME_COLUMN), String.class))
                .mapleId(record.getValue(dictionary.value(SESSION_MAPLE_ID_COLUMN), String.class))
                .gmLevel(record.getValue(dictionary.value(SESSION_GM_LEVEL_COLUMN), Integer.class))
                .loggedIn(true)
                .build();
    }

    @Transactional
    @Override
    public boolean beginSession(String nonce, UserStatus status, long expirationSeconds) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int rowsInserted = using(connection)
                    .insertInto(table(dictionary.value(SESSION_TABLE)))
                    .columns(field(dictionary.value(SESSION_NONCE_COLUMN)),
                             field(dictionary.value(SESSION_EXPIRE_COLUMN)),
                             field(dictionary.value(SESSION_USERNAME_COLUMN)),
                             field(dictionary.value(SESSION_MAPLE_ID_COLUMN)),
                             field(dictionary.value(SESSION_GM_LEVEL_COLUMN)))
                    .values(nonce, timestampAdd(currentTimestamp(), expirationSeconds, DatePart.SECOND),
                            status.getUsername(), status.getMapleId(), status.getGmLevel())
                    .execute();
            return rowsInserted > 0;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean endSession(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            int rowsUpdated = using(connection)
                    .update(table(dictionary.value(SESSION_TABLE)))
                    .set(field(dictionary.value(SESSION_EXPIRE_COLUMN)), currentTimestamp())
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce))
                    .execute();
            return rowsUpdated > 0;
        } finally {
            database.release(connection);
        }
    }
}
