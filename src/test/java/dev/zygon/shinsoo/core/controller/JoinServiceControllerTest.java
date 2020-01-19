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

import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.service.UserJoinService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static dev.zygon.shinsoo.message.SimpleResponse.success;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JoinServiceControllerTest {

    @Mock
    private UserJoinService service;

    @InjectMocks
    private JoinServiceController controller;

    @Test
    void whenJoinIsCalledRequestIsAcceptedByUserJoinServiceAndResponseIsProvidedByUserJoinService() {
        SimpleResponse simpleResponse = success("Hello, world!");
        when(service.join(any(JoinCredentials.class)))
                .thenReturn(simpleResponse);

        Response response = controller.join(new JoinCredentials());

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(simpleResponse, response.getEntity());
    }
}
