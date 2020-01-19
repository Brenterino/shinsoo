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
package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.service.PostService;
import dev.zygon.shinsoo.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostServiceController controller;

    @Test
    void whenPostIsQueriedWithNoIdPostServiceIsCalledWithUnknownPost() {
        SimpleResponse<Post> simpleResponse = SimpleResponse.<Post>builder()
                .success(false)
                .error(Collections.singletonList("Unknown post."))
                .build();
        when(postService.post(PostServiceController.UNKNOWN_POST))
                .thenReturn(simpleResponse);

        Response response = controller.post();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(simpleResponse, response.getEntity());
    }

    @Test
    void whenPostIsQueriedWithPostIdPostServiceIsCalled() {
        SimpleResponse<Post> simpleResponse = SimpleResponse.<Post>builder()
                .success(true)
                .data(new Post())
                .build();
        when(postService.post(anyLong()))
                .thenReturn(simpleResponse);

        Response response = controller.post(5);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(simpleResponse, response.getEntity());
    }

    @Nested
    class UnauthorizedTestCases {

        @BeforeEach
        void setup() {
            UserStatus unauthorizedUser = mock(UserStatus.class);

            when(unauthorizedUser.isAuthorizedUser())
                    .thenReturn(false);
            when(userService.session())
                    .thenReturn(unauthorizedUser);
        }

        @Test
        void whenCreatingPostAndClientIsUnauthorizedResponseWithUnauthorizedStatusCodeIsReturned() {
            Response response = controller.create(new Post());

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }

        @Test
        void whenDeletingPostAndClientIsUnauthorizedResponseWithUnauthorizedStatusCodeIsReturned() {
            Response response = controller.delete(1);

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }

        @Test
        void whenUpdatingPostAndClientIsUnauthorizedResponseWithUnauthorizedStatusCodeIsReturned() {
            Response response = controller.update(1, new Post());

            assertEquals(Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }
    }

    @Nested
    class AuthorizedTestCases {

        private SimpleResponse responseMessage;

        @BeforeEach
        void setup() {
            UserStatus authorizedUser = mock(UserStatus.class);

            when(authorizedUser.isAuthorizedUser())
                    .thenReturn(true);
            when(userService.session())
                    .thenReturn(authorizedUser);

            responseMessage = SimpleResponse.<String>builder()
                    .success(true)
                    .data("Hello, world!")
                    .build();
        }

        @Test
        void whenCreatingPostAndClientIsAuthorizedResponseWillBeOkStatusAndHaveResponsePayload() {
            when(postService.create(any(Post.class)))
                    .thenReturn(responseMessage);

            Response response = controller.create(new Post());

            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            assertEquals(responseMessage, response.getEntity());
        }

        @Test
        void whenDeletingPostAndClientIsAuthorizedResponseWillBeOkStatusAndHaveResponsePayload() {
            when(postService.delete(anyLong()))
                    .thenReturn(responseMessage);

            Response response = controller.delete(1);

            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            assertEquals(responseMessage, response.getEntity());
        }

        @Test
        void whenUpdatingPostAndClientIsAuthorizedResponseWillBeOkStatusAndHaveResponsePayload() {
            when(postService.update(anyLong(), any(Post.class)))
                    .thenReturn(responseMessage);

            Response response = controller.update(1, new Post());

            assertEquals(Status.OK.getStatusCode(), response.getStatus());
            assertEquals(responseMessage, response.getEntity());
        }
    }
}
