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

import dev.zygon.shinsoo.message.VotePingback;
import dev.zygon.shinsoo.service.VoteService;
import org.jboss.resteasy.spi.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteServiceControllerTest {

    @Mock
    private HttpRequest request;

    @Mock
    private VoteService service;

    @InjectMocks
    private VoteServiceController controller;

    @BeforeEach
    void setup() {
        when(request.getRemoteAddress())
                .thenReturn("127.0.0.1");
    }

    @Test
    void whenVoteServiceCannotProcessPingbackResponseHasBadRequestStatus() {
        when(service.process(any(VotePingback.class)))
                .thenReturn(false);

        Response response = controller.vote("", 0, "", "", "");

        assertEquals(Status.BAD_REQUEST.getStatusCode(), response.getStatus());
    }

    @Test
    void whenVoteServiceProcessesPingbackResponseHasOkStatus() {
        when(service.process(any(VotePingback.class)))
                .thenReturn(true);

        Response response = controller.vote("", 0, "", "", "");

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
    }
}
