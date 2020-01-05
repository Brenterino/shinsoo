package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.message.PostList;
import dev.zygon.shinsoo.repository.PostRepository;
import dev.zygon.shinsoo.service.NewsService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;

import static java.util.Collections.singletonList;

@Slf4j
@ApplicationScoped
public class NewsRepositoryService implements NewsService {

    @Inject
    PostRepository repository;

    @Override
    public Paginated news(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) PostList.DEFAULT_PAGE_SIZE);
            if (page < 0 || page > totalPages)
                return createFailedPagination("No results to display.");
            else {
                long offset = (page - 1) * PostList.DEFAULT_PAGE_SIZE;
                List<Post> posts = repository.posts(offset, PostList.DEFAULT_PAGE_SIZE);
                return createPaginatedPosts(posts, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated posts from repository.", ex);
            return createFailedPagination("");
        }
    }

    private Paginated createPaginatedPosts(List<Post> posts, long page, long totalPages) {
        return PostList.builder()
                .success(true)
                .prev(Math.max(page - 1, 1))
                .current(page)
                .next(Math.min(page + 1, totalPages))
                .last(totalPages)
                .posts(posts)
                .build();
    }

    private Paginated createFailedPagination(String message) {
        return Paginated.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }
}
