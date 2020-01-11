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
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link PlayerRepository} which utilizes
 * {@link Database} to retrieve player data stored in an SQL
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
                    .select(field(name(PLAYER_TABLE, PLAYER_NAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_LEVEL_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_EXP_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_FAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_JOB_COLUMN)),
                            field(name(GUILD_TABLE, GUILD_NAME_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .leftJoin(table(dictionary.value(GUILD_TABLE)))
                    .on(field(name(PLAYER_TABLE, PLAYER_GUILD_ID_COLUMN)).eq(field(name(GUILD_TABLE, GUILD_ID_COLUMN))))
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

    @Transactional
    @Override
    public List<Player> playersByFame(long offset, long limit) throws Exception {
        Cache<Long, List<Player>> pageCache = manager.getCache(PLAYER_FAME_PAGE_CACHE);
        if (pageCache.containsKey(offset))
            return pageCache.get(offset);

        Connection connection = database.getConnection();
        try {
            List<Player> players = using(connection)
                    .select(field(name(PLAYER_TABLE, PLAYER_NAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_LEVEL_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_EXP_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_FAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_JOB_COLUMN)),
                            field(name(GUILD_TABLE, GUILD_NAME_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .leftJoin(table(dictionary.value(GUILD_TABLE)))
                    .on(field(name(PLAYER_TABLE, PLAYER_GUILD_ID_COLUMN)).eq(field(name(GUILD_TABLE, GUILD_ID_COLUMN))))
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

    @Transactional
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

    @Transactional
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
                    .select(field(name(PLAYER_TABLE, PLAYER_NAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_LEVEL_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_EXP_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_FAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_JOB_COLUMN)),
                            field(name(GUILD_TABLE, GUILD_NAME_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .leftJoin(table(dictionary.value(GUILD_TABLE)))
                    .on(field(name(PLAYER_TABLE, PLAYER_GUILD_ID_COLUMN)).eq(field(name(GUILD_TABLE, GUILD_ID_COLUMN))))
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

    @Transactional
    @Override
    public List<Player> searchByQuery(String query) throws Exception {
        query = "%" + query + "%";

        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .select(field(name(PLAYER_TABLE, PLAYER_NAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_LEVEL_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_EXP_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_FAME_COLUMN)),
                            field(name(PLAYER_TABLE, PLAYER_JOB_COLUMN)),
                            field(name(GUILD_TABLE, GUILD_NAME_COLUMN)))
                    .from(table(dictionary.value(PLAYER_TABLE)))
                    .leftJoin(table(dictionary.value(GUILD_TABLE)))
                    .on(field(name(PLAYER_TABLE, PLAYER_GUILD_ID_COLUMN)).eq(field(name(GUILD_TABLE, GUILD_ID_COLUMN))))
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
                .name(record.getValue(name(PLAYER_TABLE, PLAYER_NAME_COLUMN), String.class))
                .level(record.getValue(name(PLAYER_TABLE, PLAYER_LEVEL_COLUMN), Integer.class))
                .exp(record.getValue(name(PLAYER_TABLE, PLAYER_EXP_COLUMN), Long.class))
                .fame(record.getValue(name(PLAYER_TABLE, PLAYER_FAME_COLUMN), Long.class))
                .job(record.getValue(name(PLAYER_TABLE, PLAYER_JOB_COLUMN), Long.class))
                .guild(record.getValue(name(GUILD_TABLE, GUILD_NAME_COLUMN), String.class))
                .build();
    }

    private String name(String table, String key) {
        return dictionary.value(table) + "." + dictionary.value(key);
    }
}
