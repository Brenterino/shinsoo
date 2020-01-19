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

import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.Player;
import dev.zygon.shinsoo.message.PlayerList;
import dev.zygon.shinsoo.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingsServiceControllerTest {

    @Mock
    private RankingService service;

    @InjectMocks
    private RankingServiceController controller;

    private Paginated paginatedResponse;

    @BeforeEach
    void setup() {
        paginatedResponse = Paginated.<List<Player>>builder()
                .success(true)
                .data(Collections.emptyList())
                .build();
    }

    @Test
    void whenRankingsAreRequestedWithNoPageDefaultPageIsUsed() {
        when(service.rankings(Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.rankings();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenRankingsAreRequestedWithPageResponseIsProduced() {
        when(service.rankings(anyLong()))
                .thenReturn(paginatedResponse);

        Response response = controller.rankings(3);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenOverallRankingsAreRequestedWithNoPageDefaultPageIsUsed() {
        when(service.rankings(Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.overallRankings();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenOverallRankingsAreRequestedWithPageResponseIsProduced() {
        when(service.rankings(anyLong()))
                .thenReturn(paginatedResponse);

        Response response = controller.overallRankings(3);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenJobRankingsAreRequestedWithNoJobTypeAndNoPageUnknownRangeAndDefaultPageIsUsed() {
        when(service.jobRankings(JobRange.UNKNOWN.name(), Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.jobRankings();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenJobRankingsAreRequestedWithJobTypeAndNoPageDefaultPageIsUsed() {
        when(service.jobRankings("warrior", Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.jobRankings("warrior");

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenJobRankingsAreRequestedWithJobTypeAndPageResponseIsProvided() {
        when(service.jobRankings(anyString(), anyLong()))
                .thenReturn(paginatedResponse);

        Response response = controller.jobRankings("warrior", 4);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenFameRankingsAreRequestedWithNoPageDefaultPageIsUsed() {
        when(service.fameRankings(Paginated.DEFAULT_PAGE))
                .thenReturn(paginatedResponse);

        Response response = controller.fameRankings();

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenFameRankingsAreRequestedWithPageResponseIsProduced() {
        when(service.fameRankings(anyLong()))
                .thenReturn(paginatedResponse);

        Response response = controller.fameRankings(3);

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(paginatedResponse, response.getEntity());
    }

    @Test
    void whenRankingSearchIsRequestedWithNoQueryNoResponseIsProvided() {
        assertNull(controller.searchRankings());
    }

    @Test
    void whenRankingSearchIsRequestedWithQueryServicePerformsSearch() {
        PlayerList list = new PlayerList();
        when(service.searchRankings(anyString()))
                .thenReturn(list);

        Response response = controller.searchRankings("sharpacex");

        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        assertEquals(list, response.getEntity());
    }
}
