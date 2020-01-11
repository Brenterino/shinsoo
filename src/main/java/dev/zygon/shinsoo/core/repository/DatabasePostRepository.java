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
import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.repository.PostRepository;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jooq.Record8;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static dev.zygon.shinsoo.core.dsl.DSLKeys.*;
import static org.jooq.impl.DSL.*;

/**
 * Implementation for {@link PostRepository} which utilizes
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
public class DatabasePostRepository implements PostRepository {

    private static final String POST_CACHE = "PostCache";
    private static final String POST_COUNT_CACHE = "PostCountCache";
    private static final String POST_PAGE_CACHE = "PostPageCache";

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Inject
    EmbeddedCacheManager manager;

    @Transactional
    @Override
    public Post post(long id) throws Exception {
        Cache<Long, Post> postCache = manager.getCache(POST_CACHE);
        if (postCache.containsKey(id))
            return postCache.get(id);

        Connection connection = database.getConnection();
        try {
            Post post = using(connection)
                    .select(field(dictionary.value(POST_ID_COLUMN)),
                            field(dictionary.value(POST_TYPE_COLUMN)),
                            field(dictionary.value(POST_VIEWS_COLUMN)),
                            field(dictionary.value(POST_TITLE_COLUMN)),
                            field(dictionary.value(POST_AUTHOR_COLUMN)),
                            field(dictionary.value(POST_CREATED_COLUMN)),
                            field(dictionary.value(POST_UPDATED_COLUMN)),
                            field(dictionary.value(POST_CONTENT_COLUMN)))
                    .from(table(dictionary.value(POST_TABLE)))
                    .where(field(dictionary.value(POST_ID_COLUMN)).eq(id))
                    .fetchOne(this::mapPost);
            if (post != null)
                postCache.put(id, post);
            return post;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public boolean updateViews(long id, long views) throws Exception {
        Connection connection = database.getConnection();
        try {
            int rowsUpdated = using(connection)
                    .update(table(dictionary.value(POST_TABLE)))
                    .set(field(dictionary.value(POST_VIEWS_COLUMN)), views)
                    .where(field(dictionary.value(POST_ID_COLUMN)).eq(id))
                    .execute();
            updateCachedPostsViews(id, views);
            return rowsUpdated > 1;
        } finally {
            database.release(connection);
        }
    }

    private void updateCachedPostsViews(long id, long views) {
        updateSinglePostViewCount(id, views);
        updateCachedPagedPostViewCount(id, views);
    }

    private void updateSinglePostViewCount(long id, long views) {
        Cache<Long, Post> postCache = manager.getCache(POST_PAGE_CACHE);
        if (postCache.containsKey(id))
            postCache.get(id).setViews(views);
    }

    private void updateCachedPagedPostViewCount(long id, long views) {
        long pageSize = computePageSize();
        if (pageSize > 0)
            updateCachedPagedPostViewCountWithPageSize(id, views, pageSize);
    }

    private long computePageSize() {
        Cache<Long, List<Post>> pageCache = manager.getCache(POST_PAGE_CACHE);
        return pageCache.values()
                .stream()
                .findAny()
                .map(List::size)
                .orElse(0);
    }

    private void updateCachedPagedPostViewCountWithPageSize(long id, long views, long pageSize) {
        Cache<Long, List<Post>> pageCache = manager.getCache(POST_PAGE_CACHE);
        long page = Math.max(0, id - 1) / pageSize;
        if (pageCache.containsKey(page)) {
            List<Post> posts = pageCache.get(page);
            posts.stream()
             .filter(post -> post.getId() == id)
             .findAny()
             .ifPresent(post -> post.setViews(views));
        }
    }

    @Transactional
    @Override
    public long count() throws Exception {
        Cache<String, Long> countCache = manager.getCache(POST_COUNT_CACHE);
        if (countCache.containsKey(POST_COUNT_CACHE))
            return countCache.get(POST_COUNT_CACHE);

        Connection connection = database.getConnection();
        try {
            long count = using(connection)
                    .selectCount()
                    .from(table(dictionary.value(POST_TABLE)))
                    .fetchOne(0, long.class);
            countCache.put(POST_COUNT_CACHE, count);
            return count;
        } finally {
            database.release(connection);
        }
    }

    @Transactional
    @Override
    public List<Post> posts(long offset, long limit) throws SQLException {
        Cache<Long, List<Post>> pageCache = manager.getCache(POST_PAGE_CACHE);
        if (pageCache.containsKey(offset))
            return pageCache.get(offset);

        Connection connection = database.getConnection();
        try {
            List<Post> posts = using(connection)
                    .select(field(dictionary.value(POST_ID_COLUMN)),
                            field(dictionary.value(POST_TYPE_COLUMN)),
                            field(dictionary.value(POST_VIEWS_COLUMN)),
                            field(dictionary.value(POST_TITLE_COLUMN)),
                            field(dictionary.value(POST_AUTHOR_COLUMN)),
                            field(dictionary.value(POST_CREATED_COLUMN)),
                            field(dictionary.value(POST_UPDATED_COLUMN)),
                            field(dictionary.value(POST_CONTENT_COLUMN)))
                    .from(table(dictionary.value(POST_TABLE)))
                    .orderBy(field(dictionary.value(POST_ID_COLUMN)).desc())
                    .offset(offset)
                    .limit(limit)
                    .fetch(this::mapPost);
            if (!posts.isEmpty())
                pageCache.put(offset, posts);
            return posts;
        } finally {
            database.release(connection);
        }
    }

    private Post mapPost(Record8<Object, Object, Object, Object, Object, Object, Object, Object> record) {
        return Post.builder()
                .id(record.getValue(dictionary.value(POST_ID_COLUMN), Long.class))
                .type(record.getValue(dictionary.value(POST_TYPE_COLUMN), String.class))
                .views(record.getValue(dictionary.value(POST_VIEWS_COLUMN), Long.class))
                .title(record.getValue(dictionary.value(POST_TITLE_COLUMN), String.class))
                .author(record.getValue(dictionary.value(POST_AUTHOR_COLUMN), String.class))
                .createdTime(record.getValue(dictionary.value(POST_CREATED_COLUMN), String.class))
                .updatedTime(record.getValue(dictionary.value(POST_UPDATED_COLUMN), String.class))
                .content(record.getValue(dictionary.value(POST_CONTENT_COLUMN), String.class))
                .build();
    }
}
