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

import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.Post;
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

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NewsServiceControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private UserService userService;

    @InjectMocks
    private NewsServiceController controller;

    private Paginated paginatedResponse;

    @BeforeEach
    void setup() {
        paginatedResponse = Paginated.builder()
                .success(true)
                .data("holthelper is holt")
                .build();
    }

    @Test
    void whenNewsIsRequestedWithNoPageDefaultPageIsUsed() {
        when(postService.posts(Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.news();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenNewsIsRequestedWithPageResponseIsProduced() {
        when(postService.posts(anyLong()))
                .thenReturn(paginatedResponse);

        Response response = controller.news(7);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
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
        void whenRetrievingAllNewsAndClientIsUnauthorizedResponseWithUnauthorizedStatusCodeIsReturned() {
            Response response = controller.all();

            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertNull(response.getEntity());
        }
    }

    @Nested
    class AuthorizedTestCases {

        @BeforeEach
        void setup() {
            UserStatus authorizedUser = mock(UserStatus.class);

            when(authorizedUser.isAuthorizedUser())
                    .thenReturn(true);
            when(userService.session())
                    .thenReturn(authorizedUser);
        }

        @Test
        void whenCreatingPostAndClientIsAuthorizedResponseWillBeOkStatusAndHaveResponsePayload() {
            List<Post> posts = new LinkedList<>();
            when(postService.posts())
                    .thenReturn(posts);

            Response response = controller.all();

            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(posts, response.getEntity());
        }
    }
}
