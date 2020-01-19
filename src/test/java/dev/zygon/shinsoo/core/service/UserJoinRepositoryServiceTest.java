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
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Encoder;
import dev.zygon.shinsoo.security.TokenValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserJoinRepositoryServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private Encoder encoder;

    @Mock
    private TokenValidator validator;

    @InjectMocks
    private UserJoinRepositoryService service;

    @Test
    void whenFormValidationFailsFailureMessageIsReturned() {
        SimpleResponse<?> response = service.join(new JoinCredentials());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Nested
    class WithValidForm {

        private JoinCredentials credentials;

        @BeforeEach
        void setup() {
            credentials = JoinCredentials.builder()
                    .name("Zygon")
                    .email("brent@zygon.dev")
                    .mapleId("brento")
                    .password("fakepassword")
                    .confirmation("fakepassword")
                    .token("1234567890")
                    .build();
            when(validator.valid(anyString()))
                    .thenReturn(true);
        }

        @Test
        void whenCheckingIfUserExistsExceptionIsThrownFailureMessageIsReturned() throws Exception {
            when(repository.userExists(anyString()))
                    .thenThrow(new Exception());

            SimpleResponse<?> response = service.join(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Test
        void whenCheckingIfUserExistsAndUserExistsAlreadyFailureMessageIsReturned() throws Exception {
            when(repository.userExists(anyString()))
                    .thenReturn(true);

            SimpleResponse<?> response = service.join(credentials);

            assertFalse(response.isSuccess());
            assertNull(response.getData());
            assertFalse(response.getError().isEmpty());
        }

        @Nested
        class UserDoesNotExist {

            @BeforeEach
            void setup() throws Exception {
                when(repository.userExists(anyString()))
                        .thenReturn(false);
            }

            @Test
            void whenCheckingIfMapleIdExistsExceptionIsThrownFailureMessageIsReturned() throws Exception {
                when(repository.idExists(anyString()))
                        .thenThrow(new Exception());

                SimpleResponse<?> response = service.join(credentials);

                assertFalse(response.isSuccess());
                assertNull(response.getData());
                assertFalse(response.getError().isEmpty());
            }

            @Test
            void whenCheckingIfMapleIdExistsAndMapleIdExistsAlreadyFailureMessageIsReturned() throws Exception {
                when(repository.idExists(anyString()))
                        .thenReturn(true);

                SimpleResponse<?> response = service.join(credentials);

                assertFalse(response.isSuccess());
                assertNull(response.getData());
                assertFalse(response.getError().isEmpty());
            }

            @Nested
            class IdDoesNotExist {

                @BeforeEach
                void setup() throws Exception {
                    when(repository.idExists(anyString()))
                            .thenReturn(false);
                }

                @Test
                void whenCheckingIfEmailExistsExceptionIsThrownFailureMessageIsReturned() throws Exception {
                    when(repository.emailExists(anyString()))
                            .thenThrow(new Exception());

                    SimpleResponse<?> response = service.join(credentials);

                    assertFalse(response.isSuccess());
                    assertNull(response.getData());
                    assertFalse(response.getError().isEmpty());
                }

                @Test
                void whenCheckingIfEmailExistsAndEmailExistsAlreadyFailureMessageIsReturned() throws Exception {
                    when(repository.emailExists(anyString()))
                            .thenReturn(true);

                    SimpleResponse<?> response = service.join(credentials);

                    assertFalse(response.isSuccess());
                    assertNull(response.getData());
                    assertFalse(response.getError().isEmpty());
                }

                @Nested
                class EmailDoesNotExist {

                    @BeforeEach
                    void setup() throws Exception {
                        when(repository.emailExists(anyString()))
                                .thenReturn(false);
                        when(encoder.encode(anyString()))
                                .thenReturn("hi");
                    }

                    @Test
                    void whenCreatingUserAndExceptionIsThrownFailureMessageIsReturned() throws Exception {
                        when(repository.create(any(UserDetails.class)))
                                .thenThrow(new Exception());

                        SimpleResponse<?> response = service.join(credentials);

                        assertFalse(response.isSuccess());
                        assertNull(response.getData());
                        assertFalse(response.getError().isEmpty());
                    }

                    @Test
                    void whenCreatingUserAndUnableToCreateUserFailureMessageIsReturned() throws Exception {
                        when(repository.create(any(UserDetails.class)))
                                .thenReturn(false);

                        SimpleResponse<?> response = service.join(credentials);

                        assertFalse(response.isSuccess());
                        assertNull(response.getData());
                        assertFalse(response.getError().isEmpty());
                    }

                    @Test
                    void whenCreatingUserAndUserIsCreatedSuccessMessageIsReturned() throws Exception {
                        when(repository.create(any(UserDetails.class)))
                                .thenReturn(true);

                        SimpleResponse<?> response = service.join(credentials);

                        assertTrue(response.isSuccess());
                        assertNotNull(response.getData());
                        assertTrue(response.getError().isEmpty());
                    }
                }
            }
        }
    }
}
