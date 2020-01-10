package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Player;
import dev.zygon.shinsoo.repository.PlayerRepository;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jooq.Record6;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;
import static org.jooq.impl.DSL.using;

@ApplicationScoped
public class DatabasePlayerRepository implements PlayerRepository {

    private static final String PLAYER_COUNT_CACHE = "PlayerCountCache";
    private static final String PLAYER_PAGE_CACHE = "PlayerPageCache";
    private static final String PLAYER_FAME_PAGE_CACHE = "PlayerFameCache";
    private static final String PLAYER_JOB_COUNT_CACHE = "PlayerJobCountCache";
    private static final String PLAYER_JOB_PAGE_CACHE = "PlayerJobPageCache";

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Inject
    EmbeddedCacheManager manager;

    @Transactional
    @Override
    public long count() throws SQLException {
        Cache<String, Long> countCache = manager.getCache(PLAYER_COUNT_CACHE);
        if (countCache.containsKey(PLAYER_COUNT_CACHE))
            return countCache.get(PLAYER_COUNT_CACHE);

        Connection connection = database.getConnection();
        try {
            long count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED))
                    .fetchOne(0, long.class);
            countCache.put(PLAYER_COUNT_CACHE, count);
            return count;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public List<Player> players(long offset, long limit) throws SQLException {
        Cache<Long, List<Player>> pageCache = manager.getCache(PLAYER_PAGE_CACHE);
        if (pageCache.containsKey(offset))
            return pageCache.get(offset);

        Connection connection = database.getConnection();
        try {
            List<Player> players = using(connection)
                    .select(field(dictionary.value(PLAYER_NAME_COLUMN)),
                            field(dictionary.value(PLAYER_LEVEL_COLUMN)),
                            field(dictionary.value(PLAYER_EXP_COLUMN)),
                            field(dictionary.value(PLAYER_FAME_COLUMN)),
                            field(dictionary.value(PLAYER_JOB_COLUMN)),
                            field(dictionary.value(PLAYER_GUILD_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED))
                    .orderBy(field(dictionary.value(PLAYER_RANK_COLUMN)).asc())
                    .offset(offset)
                    .limit(limit)
                    .fetch(this::mapPlayer);
            if (!players.isEmpty())
                pageCache.put(offset, players);
            return players;
        } finally {
            database.release(connection);
        }
    }

    @Override
    public List<Player> playersByFame(long offset, long limit) throws Exception {
        Cache<Long, List<Player>> pageCache = manager.getCache(PLAYER_FAME_PAGE_CACHE);
        if (pageCache.containsKey(offset))
            return pageCache.get(offset);

        Connection connection = database.getConnection();
        try {
            List<Player> players = using(connection)
                    .select(field(dictionary.value(PLAYER_NAME_COLUMN)),
                            field(dictionary.value(PLAYER_LEVEL_COLUMN)),
                            field(dictionary.value(PLAYER_EXP_COLUMN)),
                            field(dictionary.value(PLAYER_FAME_COLUMN)),
                            field(dictionary.value(PLAYER_JOB_COLUMN)),
                            field(dictionary.value(PLAYER_GUILD_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED))
                    .orderBy(field(dictionary.value(PLAYER_FAME_COLUMN)).desc())
                    .offset(offset)
                    .limit(limit)
                    .fetch(this::mapPlayer);
            if (!players.isEmpty())
                pageCache.put(offset, players);
            return players;
        } finally {
            database.release(connection);
        }
    }

    @Override
    public long countJob(JobRange range) throws Exception {
        Cache<JobRange, Long> countCache = manager.getCache(PLAYER_JOB_COUNT_CACHE);
        if (countCache.containsKey(range))
            return countCache.get(range);

        Connection connection = database.getConnection();
        try {
            long count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED)
                      .and(field(dictionary.value(PLAYER_JOB_COLUMN)).between(range.getStart(), range.getEnd())))
                    .fetchOne(0, long.class);
            countCache.put(range, count);
            return count;
        } finally {
            database.release(connection);
        }
    }

    @Override
    public List<Player> playersByJob(JobRange range, long offset, long limit) throws Exception {
        Cache<JobRange, Map<Long, List<Player>>> jobCache = manager.getCache(PLAYER_JOB_PAGE_CACHE);
        if (jobCache.containsKey(range)) {
            Map<Long, List<Player>> pageCache = jobCache.get(range);
            if (pageCache.containsKey(offset))
                return pageCache.get(offset);
        }

        Connection connection = database.getConnection();
        try {
            List<Player> players = using(connection)
                    .select(field(dictionary.value(PLAYER_NAME_COLUMN)),
                            field(dictionary.value(PLAYER_LEVEL_COLUMN)),
                            field(dictionary.value(PLAYER_EXP_COLUMN)),
                            field(dictionary.value(PLAYER_FAME_COLUMN)),
                            field(dictionary.value(PLAYER_JOB_COLUMN)),
                            field(dictionary.value(PLAYER_GUILD_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED)
                      .and(field(dictionary.value(PLAYER_JOB_COLUMN)).between(range.getStart(), range.getEnd())))
                    .orderBy(field(dictionary.value(PLAYER_RANK_COLUMN)).asc())
                    .offset(offset)
                    .limit(limit)
                    .fetch(this::mapPlayer);
            jobCache.putIfAbsent(range, new HashMap<>());
            jobCache.get(range).put(offset, players);
            return players;
        } finally {
            database.release(connection);
        }
    }

    @Override
    public List<Player> searchByQuery(String query) throws Exception {
        query = "%" + query + "%";

        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .select(field(dictionary.value(PLAYER_NAME_COLUMN)),
                            field(dictionary.value(PLAYER_LEVEL_COLUMN)),
                            field(dictionary.value(PLAYER_EXP_COLUMN)),
                            field(dictionary.value(PLAYER_FAME_COLUMN)),
                            field(dictionary.value(PLAYER_JOB_COLUMN)),
                            field(dictionary.value(PLAYER_GUILD_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .where(field(dictionary.value(PLAYER_RANK_COLUMN)).gt(Player.PLAYER_UNRANKED)
                      .and(field(dictionary.value(PLAYER_NAME_COLUMN)).like(query)))
                    .orderBy(field(dictionary.value(PLAYER_RANK_COLUMN)).asc())
                    .fetch(this::mapPlayer);
        } finally {
            database.release(connection);
        }
    }

    private Player mapPlayer(Record6<Object, Object, Object, Object, Object, Object> record) {
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
