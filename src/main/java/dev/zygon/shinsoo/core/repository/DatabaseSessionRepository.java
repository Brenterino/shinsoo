package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.SessionRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jooq.Record3;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

@ApplicationScoped
public class DatabaseSessionRepository implements SessionRepository {

    private static long HOURS_TO_MILLISECONDS = 3600000;

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @ConfigProperty(name = "session.expiration.hours", defaultValue = "72")
    long expirationHours;

    @Override
    public boolean sessionActive(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            long currentTime = System.currentTimeMillis();
            int count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(SESSION_TABLE)))
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce)
                      .and(field(dictionary.value(SESSION_EXPIRE_COLUMN)).le(currentTime)))
                    .fetchOne(0, int.class);
            return count > 0;
        } finally {
            database.release(connection);
        }
    }

    @Override
    public UserStatus session(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            long currentTime = System.currentTimeMillis();
            return using(connection)
                    .select(field(dictionary.value(SESSION_USERNAME_COLUMN)),
                            field(dictionary.value(SESSION_MAPLE_ID_COLUMN)),
                            field(dictionary.value(SESSION_GM_LEVEL_COLUMN)))
                    .from(table(dictionary.value(SESSION_TABLE)))
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce)
                      .and(field(dictionary.value(SESSION_EXPIRE_COLUMN)).le(currentTime)))
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

    @Override
    public boolean beginSession(String nonce, UserStatus status) throws SQLException {
        Connection connection = database.getConnection();
        try {
            long expirationTime = System.currentTimeMillis() + expirationHoursToMilliseconds();
            int rowsInserted = using(connection)
                    .insertInto(table(dictionary.value(SESSION_TABLE)))
                    .columns(field(dictionary.value(SESSION_NONCE_COLUMN)),
                             field(dictionary.value(SESSION_EXPIRE_COLUMN)),
                             field(dictionary.value(SESSION_USERNAME_COLUMN)),
                             field(dictionary.value(SESSION_MAPLE_ID_COLUMN)),
                             field(dictionary.value(SESSION_GM_LEVEL_COLUMN)))
                    .values(nonce, expirationTime, status.getUsername(), status.getMapleId(), status.getGmLevel())
                    .execute();
            return rowsInserted > 0;
        } finally {
            database.release(connection);
        }
    }

    private long expirationHoursToMilliseconds() {
        return expirationHours * HOURS_TO_MILLISECONDS;
    }

    @Override
    public boolean endSession(String nonce) throws SQLException {
        Connection connection = database.getConnection();
        try {
            long currentTime = System.currentTimeMillis();
            int rowsUpdated = using(connection)
                    .update(table(dictionary.value(SESSION_TABLE)))
                    .set(field(dictionary.value(SESSION_EXPIRE_COLUMN)), currentTime)
                    .where(field(dictionary.value(SESSION_NONCE_COLUMN)).eq(nonce))
                    .execute();
            return rowsUpdated > 0;
        } finally {
            database.release(connection);
        }
    }
}
