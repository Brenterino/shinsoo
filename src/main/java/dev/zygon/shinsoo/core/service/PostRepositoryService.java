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
package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.core.validation.PostFormValidator;
import dev.zygon.shinsoo.message.*;
import dev.zygon.shinsoo.repository.PostRepository;
import dev.zygon.shinsoo.service.PostService;
import dev.zygon.shinsoo.validation.Failures;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implementation for {@link PostService} which utilizes
 * {@link PostRepository} to retrieve posts from a data
 * repository.  Generates response messages based on the
 * data retrieved from the repository.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@ApplicationScoped
public class PostRepositoryService implements PostService {

    @Inject
    PostRepository repository;

    @Override
    public SimpleResponse<Post> post(long id) {
        try {
            Post post = repository.post(id);
            if (post == null)
                return createFailedPostPayload("Requested post does not exist.");
            else {
                repository.updateViews(id, post.getViews() + 1);
                return createPostPayload(post);
            }
        } catch (Exception ex) {
            log.error("Unable to load posts from repository.", ex);
            return createFailedPostPayload("Unable to load post. Please try again later.");
        }
    }

    private SimpleResponse<Post> createFailedPostPayload(String message) {
        return SimpleResponse.<Post>builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private SimpleResponse<Post> createPostPayload(Post post) {
        return SimpleResponse.<Post>builder()
                .success(true)
                .data(post)
                .build();
    }

    @Override
    public Paginated<?> posts(long page) {
        try {
            long totalPages = (long) Math.ceil(repository.count() / (double) Paginated.DEFAULT_PAGE_SIZE);
            if (page < 0 || page > totalPages)
                return createFailedPagination("No results to display.");
            else {
                long offset = (page - 1) * Paginated.DEFAULT_PAGE_SIZE;
                List<Post> posts = repository.posts(offset, Paginated.DEFAULT_PAGE_SIZE);
                return createPaginatedPosts(posts, page, totalPages);
            }
        } catch (Exception ex) {
            log.error("Unable to load paginated posts from repository.", ex);
            return createFailedPagination("Unable to load posts. Please try again later.");
        }
    }

    private Paginated<?> createFailedPagination(String message) {
        return Paginated.<Void>builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private Paginated<?> createPaginatedPosts(List<Post> posts, long page, long totalPages) {
        return Paginated.<List<Post>>builder()
                .success(true)
                .prev(Math.max(page - 1, 1))
                .current(page)
                .next(Math.min(page + 1, totalPages))
                .last(totalPages)
                .data(posts)
                .build();
    }

    @Override
    public List<Post> posts() {
        try {
            return repository.posts(0, repository.count());
        } catch (Exception ex) {
            log.error("Unable to load posts from repository.", ex);
            return Collections.emptyList();
        }
    }

    @Override
    public SimpleResponse<?> create(Post post) {
        PostFormValidator validator = new PostFormValidator(post);
        Failures formFailures = validator.validate();
        if (formFailures == Failures.NONE)
            return attemptCreate(post);
        else
            return createFailedOperationMessage(formFailures.getMessage());
    }

    private SimpleResponse<?> attemptCreate(Post post) {
        try {
            if (repository.create(post))
                return createSuccessfulOperationMessage("Successfully created post.");
            else
                return createFailedOperationMessage("Post could not be created for an unknown reason.");
        } catch (Exception ex) {
            log.error("Unable to create post in repository.", ex);
            return createFailedOperationMessage("Unable to insert post into repository. Try again later.");
        }
    }

    @Override
    public SimpleResponse<?> delete(long id) {
        try {
            if (repository.delete(id))
                return createSuccessfulOperationMessage("Successfully deleted post.");
            else
                return createFailedOperationMessage("Either post could not be deleted or it no longer exists.");
        } catch (Exception ex) {
            log.error("Unable to delete post from repository.", ex);
            return createFailedOperationMessage("Unable to delete post from repository. Try again later.");
        }
    }

    @Override
    public SimpleResponse<?> update(long id, Post post) {
        PostFormValidator validator = new PostFormValidator(post);
        Failures formFailures = validator.validate();
        if (formFailures == Failures.NONE)
            return attemptUpdate(id, post);
        else
            return createFailedOperationMessage(formFailures.getMessage());
    }

    private SimpleResponse<?> attemptUpdate(long id, Post post) {
        try {
            if (repository.update(id, post))
                return createSuccessfulOperationMessage("Successfully updated post.");
            else
                return createFailedOperationMessage("Either post could not be updated or it no longer exists.");
        } catch (Exception ex) {
            log.error("Unable to update post in repository.", ex);
            return createFailedOperationMessage("Unable to update post in repository. Try again later.");
        }
    }

    private SimpleResponse<?> createFailedOperationMessage(String message) {
        return SimpleResponse.<Void>builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private SimpleResponse<?> createSuccessfulOperationMessage(String message) {
        return SimpleResponse.<List<String>>builder()
                .success(true)
                .data(singletonList(message))
                .build();
    }
}
