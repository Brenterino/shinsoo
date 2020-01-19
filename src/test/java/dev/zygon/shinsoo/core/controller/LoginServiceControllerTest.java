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

import dev.zygon.shinsoo.core.message.CookieHoldingUserStatus;
import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static dev.zygon.shinsoo.message.SimpleResponse.failure;
import static dev.zygon.shinsoo.message.SimpleResponse.success;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginServiceControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private LoginServiceController controller;

    @Test
    void whenServiceLoginFailsAndNoDataIsAvailableNoCookieIsAttachedToResponse() {
        SimpleResponse failResponse = failure(":feelsbadman:");
        when(service.login(any(LoginCredentials.class)))
                .thenReturn(failResponse);

        Response response = controller.login(new LoginCredentials());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getCookies().isEmpty());
    }

    @Test
    void whenServiceLoginSucceedsAndCookieIsAvailableCookieIsAttachedToResponse() {
        CookieHoldingUserStatus status = CookieHoldingUserStatus.builder()
                .cookie(new NewCookie("big", "cookie"))
                .build();
        SimpleResponse successResponse = success(status);
        when(service.login(any(LoginCredentials.class)))
                .thenReturn(successResponse);

        Response response = controller.login(new LoginCredentials());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertFalse(response.getCookies().isEmpty());
    }
}
