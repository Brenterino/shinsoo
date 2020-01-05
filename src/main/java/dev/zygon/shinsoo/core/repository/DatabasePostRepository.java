package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.database.Database;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.repository.PostRepository;
import org.jooq.Record8;

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
public class DatabasePostRepository implements PostRepository {

    @Inject
    Database database;

    @Inject
    DSLDictionary dictionary;

    @Override
    public long count() throws Exception {
        Connection connection = database.getConnection();
        try {
            return using(connection)
                    .selectCount()
                    .from(table(dictionary.value(POST_TABLE)))
                    .fetchOne(0, long.class);
        } finally {
            database.release(connection);
        }
    }

    @Override
    public List<Post> posts(long offset, long limit) throws SQLException {
        Connection connection = database.getConnection();
        try {
            return using(connection)
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
        } finally {
            database.release(connection);
        }
    }

    private Post mapPost(Record8 record) {
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