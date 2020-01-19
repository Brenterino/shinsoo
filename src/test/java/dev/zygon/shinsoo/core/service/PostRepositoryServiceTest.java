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

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostRepositoryServiceTest {

    @Mock
    private PostRepository repository;

    @InjectMocks
    private PostRepositoryService service;

    @Test
    void whenSinglePostIsRequestedAndItDoesNotExistFailureResponseIsReturned() throws Exception {
        when(repository.post(anyLong()))
                .thenReturn(null);

        SimpleResponse<Post> response = service.post(1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenSinglePostIsRequestedAndItExistsSuccessMessageWithThatPostIsReturnedAndViewCountIsIncremented() throws Exception {
        Post post = new Post();
        when(repository.post(anyLong()))
                .thenReturn(post);

        SimpleResponse<Post> response = service.post(2);

        verify(repository, times(1)).updateViews(2, post.getViews() + 1);

        assertTrue(response.isSuccess());
        assertEquals(post, response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenSinglePostIsRequestedAndExceptionOccursOnRetrievalFailureResponseIsReturned() throws Exception {
        when(repository.post(anyLong()))
                .thenThrow(new Exception());

        SimpleResponse<Post> response = service.post(3);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenSinglePostIsRequestedAndExceptionOccursOnUpdatingViewsFailureResponseIsReturned() throws Exception {
        Post post = new Post();
        when(repository.post(anyLong()))
                .thenReturn(post);
        doThrow(new Exception())
                .when(repository)
                .updateViews(anyLong(), anyLong());

        SimpleResponse<Post> response = service.post(4);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndItIsLessThanZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.posts(-1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndItIsEqualToZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.posts(0);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndItIsGreaterThanTheTotalPageCountFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.posts(10);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndPostCountThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenThrow(new Exception());

        Paginated<?> response = service.posts(2);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndItIsValidAndRetrievalIsAttemptAndExceptionIsThrownFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);
        when(repository.posts(anyLong(), anyLong()))
                .thenThrow(new Exception());

        Paginated<?> response = service.posts(1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfPostsIsRequestedAndItIsValidAndRetrievedFromRepositorySuccessfulResponseIsReturned() throws Exception {
        List<Post> posts = new LinkedList<>();
        when(repository.count())
                .thenReturn(1L);
        when(repository.posts(anyLong(), anyLong()))
                .thenReturn(posts);

        Paginated<?> response = service.posts(1);

        assertTrue(response.isSuccess());
        assertEquals(posts, response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenAllPostsAreRequestedAndExceptionIsThrownWhenCountIsRetrievedEmptyListIsReturned() throws Exception {
        when(repository.count())
                .thenThrow(new Exception());

        List<Post> response = service.posts();

        assertTrue(response.isEmpty());
    }

    @Test
    void whenAllPostsAreRequestedAndExceptionIsThrownWhenPostsAreRetrievedEmptyListIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);
        when(repository.posts(0, 1L))
                .thenThrow(new Exception());

        List<Post> response = service.posts();

        assertTrue(response.isEmpty());
    }

    @Test
    void whenAllPostsAreRequestedAndRetrievedFromRepositoryPopulatedListIsReturned() throws Exception {
        List<Post> posts = new LinkedList<>();
        posts.add(new Post());

        when(repository.count())
                .thenReturn(1L);
        when(repository.posts(0, 1L))
                .thenReturn(posts);

        List<Post> response = service.posts();

        assertFalse(response.isEmpty());

        assertEquals(posts, response);
    }

    @Test
    void whenPostIsBeingCreatedAndPostIsFailingValidationFailureMessageIsReturned() {
        SimpleResponse<?> response = service.create(new Post());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPostsFailsBeingDeletedFailureMessageIsReturned() throws Exception {
        when(repository.delete(anyLong()))
                .thenReturn(false);

        SimpleResponse<?> response = service.delete(2L);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenExceptionIsThrownWhilePostIsBeingDeletedFailureMessageIsReturned() throws Exception {
        when(repository.delete(anyLong()))
                .thenThrow(new Exception());

        SimpleResponse<?> response = service.delete(3L);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPostIsCreatedSuccessfulMessageIsReturned() throws Exception {
        when(repository.delete(anyLong()))
                .thenReturn(true);

        SimpleResponse<?> response = service.delete(2L);

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenPostIsBeingUpdatedAndPostIsFailingValidationFailureMessageIsReturned() {
        SimpleResponse<?> response = service.update(1L, new Post());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Nested
    class WithValidPost {

        private Post post;

        @BeforeEach
        void setup() {
            post = Post.builder()
                    .title("Clickbait Title")
                    .author("Literally Any Tabloid")
                    .type("Fake News")
                    .content("Boring article as always...")
                    .build();
        }

        @Test
        void whenPostsFailsBeingCreatedFailureMessageIsReturned() throws Exception {
            when(repository.create(post))
                    .thenReturn(false);

            SimpleResponse<?> response = service.create(post);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenExceptionIsThrownWhilePostIsBeingCreatedFailureMessageIsReturned() throws Exception {
            when(repository.create(post))
                    .thenThrow(new Exception());

            SimpleResponse<?> response = service.create(post);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenPostIsCreatedSuccessfulMessageIsReturned() throws Exception {
            when(repository.create(post))
                    .thenReturn(true);

            SimpleResponse<?> response = service.create(post);

            assertTrue(response.isSuccess());
            assertNotNull(response.getData());
            assertTrue(response.getError().isEmpty());
        }

        @Test
        void whenPostsFailsBeingUpdatedFailureMessageIsReturned() throws Exception {
            when(repository.update(1L, post))
                    .thenReturn(false);

            SimpleResponse<?> response = service.update(1L, post);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenExceptionIsThrownWhilePostIsBeingUpdatedFailureMessageIsReturned() throws Exception {
            when(repository.update(1L, post))
                    .thenThrow(new Exception());

            SimpleResponse<?> response = service.update(1L, post);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenPostIsUpdatedSuccessfulMessageIsReturned() throws Exception {
            when(repository.update(1L, post))
                    .thenReturn(true);

            SimpleResponse<?> response = service.update(1L, post);

            assertTrue(response.isSuccess());
            assertNotNull(response.getData());
            assertTrue(response.getError().isEmpty());
        }
    }
}
