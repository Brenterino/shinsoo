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

import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.Player;
import dev.zygon.shinsoo.message.PlayerList;
import dev.zygon.shinsoo.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RankingRepositoryServiceTest {

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private RankingRepositoryService service;

    @Test
    void whenPageOfRankingsIsRequestedAndItIsLessThanZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.rankings(-1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfRankingsIsRequestedAndItIsEqualToZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.rankings(0);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfRankingsIsRequestedAndItIsGreaterThanTheTotalPageCountFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.rankings(10);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfRankingsIsRequestedAndPostCountThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenThrow(new Exception());

        Paginated<?> response = service.rankings(2);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfRankingsIsRequestedAndItIsValidAndRetrievalIsAttemptAndExceptionIsThrownFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);
        when(repository.players(anyLong(), anyLong()))
                .thenThrow(new Exception());

        Paginated<?> response = service.rankings(1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfRankingsIsRequestedAndItIsValidAndRetrievedFromRepositorySuccessfulResponseIsReturned() throws Exception {
        List<Player> players = new LinkedList<>();
        when(repository.count())
                .thenReturn(1L);
        when(repository.players(anyLong(), anyLong()))
                .thenReturn(players);

        Paginated<?> response = service.rankings(1);

        assertTrue(response.isSuccess());
        assertEquals(players, response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndTheProvidedJobIsUnknownFailureResponseIsReturned() {
        Paginated<?> response = service.jobRankings("yeeter", -1L);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndItIsLessThanZeroFailureResponseIsReturned() throws Exception {
        when(repository.countJob(any(JobRange.class)))
                .thenReturn(1L);

        Paginated<?> response = service.jobRankings("warrior", -1L);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndItIsEqualToZeroFailureResponseIsReturned() throws Exception {
        when(repository.countJob(any(JobRange.class)))
                .thenReturn(1L);

        Paginated<?> response = service.jobRankings("warrior", 0L);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndItIsGreaterThanTheTotalPageCountFailureResponseIsReturned() throws Exception {
        when(repository.countJob(any(JobRange.class)))
                .thenReturn(1L);

        Paginated<?> response = service.jobRankings("warrior", 10);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndPostCountThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.countJob(any(JobRange.class)))
                .thenThrow(new Exception());

        Paginated<?> response = service.jobRankings("warrior", 2);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndItIsValidAndRetrievalIsAttemptedAndExceptionIsThrownFailureResponseIsReturned() throws Exception {
        when(repository.countJob(any(JobRange.class)))
                .thenReturn(1L);
        when(repository.playersByJob(any(JobRange.class), anyLong(), anyLong()))
                .thenThrow(new Exception());

        Paginated<?> response = service.jobRankings("warrior", 1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfJobRankingsIsRequestedAndItIsValidAndRetrievedFromRepositorySuccessfulResponseIsReturned() throws Exception {
        List<Player> players = new LinkedList<>();
        when(repository.countJob(any(JobRange.class)))
                .thenReturn(1L);
        when(repository.playersByJob(any(JobRange.class), anyLong(), anyLong()))
                .thenReturn(players);

        Paginated<?> response = service.jobRankings("warrior", 1);

        assertTrue(response.isSuccess());
        assertEquals(players, response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndItIsLessThanZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.fameRankings(-1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndItIsEqualToZeroFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.fameRankings(0);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndItIsGreaterThanTheTotalPageCountFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);

        Paginated<?> response = service.fameRankings(10);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndPostCountThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenThrow(new Exception());

        Paginated<?> response = service.fameRankings(2);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndItIsValidAndRetrievalIsAttemptAndExceptionIsThrownFailureResponseIsReturned() throws Exception {
        when(repository.count())
                .thenReturn(1L);
        when(repository.playersByFame(anyLong(), anyLong()))
                .thenThrow(new Exception());

        Paginated<?> response = service.fameRankings(1);

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenPageOfFameRankingsIsRequestedAndItIsValidAndRetrievedFromRepositorySuccessfulResponseIsReturned() throws Exception {
        List<Player> players = new LinkedList<>();
        when(repository.count())
                .thenReturn(1L);
        when(repository.playersByFame(anyLong(), anyLong()))
                .thenReturn(players);

        Paginated<?> response = service.fameRankings(1);

        assertTrue(response.isSuccess());
        assertEquals(players, response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Nested
    class SearchRankingsTests {

        @BeforeEach
        void setup() {
            service.minimumQuerySize = 4;
            service.maximumQuerySize = 12;
        }

        @Test
        void whenSearchQueryForRankingSearchIsTooSmallFailureResponseIsReturned() {
            PlayerList response = service.searchRankings("ye");

            assertFalse(response.isSuccess());
            assertTrue(response.getPlayers().isEmpty());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenSearchQueryForRankingSearchIsTooBigFailureResponseIsReturned() {
            PlayerList response = service.searchRankings("ReallyLongSearchQuery");

            assertFalse(response.isSuccess());
            assertTrue(response.getPlayers().isEmpty());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenSearchQueryInRepositoryForRankingSearchThrowsExceptionFailureResponseIsReturned() throws Exception {
            when(repository.searchByQuery(anyString()))
                    .thenThrow(new Exception());

            PlayerList response = service.searchRankings("JustRight");

            assertFalse(response.isSuccess());
            assertTrue(response.getPlayers().isEmpty());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenSearchQueryInRepositoryForRankingSearchReturnsNoResultsFailureResponseIsReturned() throws Exception {
            when(repository.searchByQuery(anyString()))
                    .thenReturn(Collections.emptyList());

            PlayerList response = service.searchRankings("JustRight");

            assertFalse(response.isSuccess());
            assertTrue(response.getPlayers().isEmpty());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenSearchQueryInRepositoryForRankingSearchReturnsResultsSuccessResponseIsReturned() throws Exception {
            List<Player> players = new LinkedList<>();
            players.add(new Player());

            when(repository.searchByQuery(anyString()))
                    .thenReturn(players);

            PlayerList response = service.searchRankings("JustRight");

            assertTrue(response.isSuccess());
            assertFalse(response.getPlayers().isEmpty());
            assertTrue(response.getError().isEmpty());
        }
    }
}
