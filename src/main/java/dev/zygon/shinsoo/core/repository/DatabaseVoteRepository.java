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
import dev.zygon.shinsoo.repository.VoteRepository;
import org.jooq.Field;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.Connection;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link VoteRepository} which utilizes
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
public class DatabaseVoteRepository implements VoteRepository {

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Override
    public boolean canVote(String mapleId, long delayBetweenVotesMillis) throws Exception {
        Connection connection = database.getConnection();
        try {
            long lastVoted = using(connection)
                    .select(field(dictionary.value(USER_LAST_VOTED_COLUMN)))
                    .from(table(dictionary.value(USER_TABLE)))
                    .where(field(dictionary.value(USER_MAPLE_ID_COLUMN)).eq(mapleId))
                    .fetchOne(0, long.class);

            return (lastVoted + delayBetweenVotesMillis) <= System.currentTimeMillis();
        } finally {
            database.release(connection);
        }
    }

    @Override
    public boolean addVote(String mapleId) throws Exception {
        Connection connection = database.getConnection();
        try {
            // extracted because Field<Object> not distinguishable from Object for set(..., ...)
            Field<Long> votes = field(dictionary.value(USER_VOTES_COLUMN), Long.class);
            long rowsUpdated = using(connection)
                    .update(table(dictionary.value(USER_TABLE)))
                    .set(votes, votes.add(1))
                    .set(field(dictionary.value(USER_LAST_VOTED_COLUMN)), currentTimestamp())
                    .where(field(dictionary.value(USER_MAPLE_ID_COLUMN)).eq(mapleId))
                    .execute();
            return rowsUpdated > 0;
        } finally {
            database.release(connection);
        }
    }
}
