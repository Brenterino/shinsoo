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
