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

import dev.zygon.shinsoo.message.VotePingback;
import dev.zygon.shinsoo.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoteRepositoryServiceTest {

    @Mock
    private VoteRepository repository;

    @InjectMocks
    private VoteRepositoryService service;

    @BeforeEach
    void setup() {
        service.authorizedSources = Optional.empty();
        service.delayBetweenVotesHours = 24L;
    }

    @Test
    void whenInvalidPingbackIsProvidedVoteFails() {
        assertFalse(service.process(new VotePingback()));
    }

    @Nested
    class WithValidPingback {

        private VotePingback pingback;

        @BeforeEach
        void setup() {
            pingback = VotePingback.builder()
                    .remoteAddress("127.0.0.1")
                    .url("/vote")
                    .success(VotePingback.SUCCESS_CODE)
                    .failureReason("")
                    .voterIP("127.0.0.1")
                    .mapleId("Shavit")
                    .build();
        }

        @Test
        void whenCheckingIfPlayerCanVoteAndExceptionIsThrownVoteCannotBeProcessed() throws Exception {
            when(repository.canVote(anyString(), anyLong()))
                    .thenThrow(new Exception());

            assertFalse(service.process(pingback));
        }

        @Test
        void whenCheckingIfPlayerCanVoteAndItResultsThatTheyCannotVoteCannotBeProcessed() throws Exception {
            when(repository.canVote(anyString(), anyLong()))
                    .thenReturn(false);

            assertFalse(service.process(pingback));
        }

        @Test
        void whenAddingVoteToPlayerWhoCanVoteAndExceptionIsThrownVoteCannotBeProcessed() throws Exception {
            when(repository.canVote(anyString(), anyLong()))
                    .thenReturn(true);
            when(repository.addVote(anyString()))
                    .thenThrow(new Exception());

            assertFalse(service.process(pingback));
        }

        @Test
        void whenPlayerCanVoteButAddingVoteDoesNotSucceedVoteCannotBeProcessed() throws Exception {
            when(repository.canVote(anyString(), anyLong()))
                    .thenReturn(true);
            when(repository.addVote(anyString()))
                    .thenReturn(false);

            assertFalse(service.process(pingback));
        }

        @Test
        void whenPlayerCanVoteAndAddingVoteSucceedsVoteIsProcessed() throws Exception {
            when(repository.canVote(anyString(), anyLong()))
                    .thenReturn(true);
            when(repository.addVote(anyString()))
                    .thenReturn(true);

            assertTrue(service.process(pingback));
        }
    }
}
