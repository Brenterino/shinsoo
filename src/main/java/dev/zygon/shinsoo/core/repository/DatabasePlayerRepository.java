package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.Player;
import dev.zygon.shinsoo.repository.PlayerRepository;
import org.jooq.Record6;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

@ApplicationScoped
public class DatabasePlayerRepository implements PlayerRepository {

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Override
    public long count() throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .selectCount()
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED))
                    .fetchOne(0, long.class);
        } finally {
            database.release(connection);
        }
    }

    @Override
    public List<Player> players(long offset, long limit) throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .select(field(dictionary.value(PLAYER_NAME_COLUMN)),
                            field(dictionary.value(PLAYER_LEVEL_COLUMN)),
                            field(dictionary.value(PLAYER_EXP_COLUMN)),
                            field(dictionary.value(PLAYER_FAME_COLUMN)),
                            field(dictionary.value(PLAYER_JOB_COLUMN)),
                            field(dictionary.value(PLAYER_GUILD_COLUMN)))
                    .from(table(field(dictionary.value(PLAYER_TABLE))))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED))
                    .orderBy(field(dictionary.value(PLAYER_RANK_COLUMN)).asc())
                    .offset(offset)
                    .limit(limit)
                    .fetch(this::mapPlayer);
        } finally {
            database.release(connection);
        }
    }

    private Player mapPlayer(Record6 record) {
        return Player.builder()
                .name(record.getValue(dictionary.value(PLAYER_NAME_COLUMN), String.class))
                .level(record.getValue(dictionary.value(PLAYER_LEVEL_COLUMN), Integer.class))
                .exp(record.getValue(dictionary.value(PLAYER_EXP_COLUMN), Long.class))
                .fame(record.getValue(dictionary.value(PLAYER_FAME_COLUMN), Long.class))
                .job(record.getValue(dictionary.value(PLAYER_JOB_COLUMN), Long.class))
                .guild(record.getValue(dictionary.value(PLAYER_GUILD_COLUMN), String.class))
                .build();
    }
}
