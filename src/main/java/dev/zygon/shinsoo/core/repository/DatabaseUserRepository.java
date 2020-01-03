package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.database.Database;
import org.jooq.Record5;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

@ApplicationScoped
public class DatabaseUserRepository implements UserRepository {

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

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
