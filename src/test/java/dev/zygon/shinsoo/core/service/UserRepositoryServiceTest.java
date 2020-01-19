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

import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Checker;
import dev.zygon.shinsoo.security.SecuredSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryServiceTest {

    @Mock
    private SecuredSession session;

    @Mock
    private UserRepository repository;

    @Mock
    private Checker checker;

    @InjectMocks
    private UserRepositoryService service;

    @Test
    void sessionIsRetrievedFromSecuredSession() {
        UserStatus status = new UserStatus();

        when(session.status())
                .thenReturn(status);

        assertEquals(status, service.session());
    }

    @Test
    void whenInvalidLoginCredentialsAreProvidedFailureMessageIsReturned() {
        SimpleResponse<?> response = service.login(new LoginCredentials());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Nested
    class WithValidLoginCredentials {

        private LoginCredentials credentials;

        @BeforeEach
        void setup() {
            credentials = LoginCredentials.builder()
                    .email("arnah@playphoenix.ca")
                    .password("produper")
                    .build();
        }

        @Test
        void whenCheckingForEmailExceptionIsThrownFailureMessageIsReturned() throws Exception {
            when(repository.emailExists(anyString()))
                .thenThrow(new Exception());

            SimpleResponse<?> response = service.login(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenCheckingForEmailAndNoEmailIsFoundFailureMessageIsReturned() throws Exception {
            when(repository.emailExists(anyString()))
                    .thenReturn(false);

            SimpleResponse<?> response = service.login(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenEmailIsFoundButUserDetailsCannotBeRetrievedDueToException() throws Exception {
            when(repository.emailExists(anyString()))
                    .thenReturn(true);
            when(repository.detailsForEmail(anyString()))
                    .thenThrow(new Exception());

            SimpleResponse<?> response = service.login(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenEmailIsFoundButUserDetailsCannotBeRetrieved() throws Exception {
            when(repository.emailExists(anyString()))
                    .thenReturn(true);
            when(repository.detailsForEmail(anyString()))
                    .thenReturn(null);

            SimpleResponse<?> response = service.login(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Nested
        class WithUserDetailsRetrieved {

            @BeforeEach
            void setup() throws Exception {
                UserDetails details = UserDetails.builder()
                        .email("arnah@playphoenix.ca")
                        .gmLevel(1)
                        .mapleId("ilovealan")
                        .username("ilovealan")
                        .password("plaintext.pw")
                        .build();
                when(repository.emailExists(anyString()))
                        .thenReturn(true);
                when(repository.detailsForEmail(anyString()))
                        .thenReturn(details);
            }

            @Test
            void whenPasswordDoesNotMatchFailureMessageIsReturned() {
                when(checker.check(anyString(), anyString()))
                        .thenReturn(false);

                SimpleResponse<?> response = service.login(credentials);

                assertFalse(response.isSuccess());
                assertNull(response.getData());
                assertFalse(response.getError().isEmpty());
            }

            @Nested
            class WithPasswordsMatching {

                @BeforeEach
                void setup() {
                    when(checker.check(anyString(), anyString()))
                            .thenReturn(true);
                }

                @Test
                void whenUserIsNotLoggedInFailureMessageIsReturned() {
                    UserStatus status = UserStatus.builder()
                            .loggedIn(false)
                            .build();
                    when(session.begin(any(UserStatus.class)))
                            .thenReturn(status);

                    SimpleResponse<?> response = service.login(credentials);

                    assertFalse(response.isSuccess());
                    assertNull(response.getData());
                    assertFalse(response.getError().isEmpty());
                }

                @Test
                void whenUserIsFoggedInSuccessMessageIsReturned() {
                    UserStatus status = UserStatus.builder()
                            .loggedIn(true)
                            .build();
                    when(session.begin(any(UserStatus.class)))
                            .thenReturn(status);

                    SimpleResponse<?> response = service.login(credentials);

                    assertTrue(response.isSuccess());
                    assertNotNull(response.getData());
                    assertTrue(response.getError().isEmpty());
                }
            }
        }
    }

    @Test
    void whenAttemptingToLogoutAndUserIsNotLoggedInFailureMessageIsReturned() {
        UserStatus loggedOut = UserStatus.builder()
                .loggedIn(false)
                .build();
        when(session.status())
                .thenReturn(loggedOut);

        SimpleResponse<?> response = service.logout();

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenAttemptingToLogoutAndUserIsStillLoggedInAfterSessionIsEndedFailureMessageIsReturned() {
        UserStatus loggedIn = UserStatus.builder()
                .loggedIn(true)
                .build();
        when(session.status())
                .thenReturn(loggedIn);
        when(session.end())
                .thenReturn(loggedIn);

        SimpleResponse<?> response = service.logout();

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenAttemptingToLogoutAndUserIsLoggedOutAfterSessionIsEndedSuccessMessageIsReturned() {
        UserStatus loggedIn = UserStatus.builder()
                .loggedIn(true)
                .build();
        UserStatus loggedOut = UserStatus.builder()
                .loggedIn(false)
                .build();
        when(session.status())
                .thenReturn(loggedIn);
        when(session.end())
                .thenReturn(loggedOut);

        SimpleResponse<?> response = service.logout();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getError().isEmpty());
    }
}
