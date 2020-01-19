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

import dev.zygon.shinsoo.message.Settings;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.repository.SettingsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SettingsRepositoryServiceTest {

    @Mock
    private SettingsRepository repository;

    @InjectMocks
    private SettingsRepositoryService service;

    @Test
    void whenSettingsAreRetrievedAndRepositoryThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.settings())
                .thenThrow(new Exception());

        SimpleResponse<?> response = service.settings();

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenSettingsAreRetrievedSuccessfullyResponseIsReturned() throws Exception {
        when(repository.settings())
                .thenReturn(new Settings());

        SimpleResponse<?> response = service.settings();

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getError().isEmpty());
    }

    @Test
    void whenSettingsAreUpdatingAndRepositoryThrowsExceptionFailureResponseIsReturned() throws Exception {
        when(repository.update(any(Settings.class)))
                .thenThrow(new Exception());

        SimpleResponse<?> response = service.update(new Settings());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenSettingsAreUpdatingAndUpdateFailsThenFailureResponseIsReturned() throws Exception {
        when(repository.update(any(Settings.class)))
                .thenReturn(false);

        SimpleResponse<?> response = service.update(new Settings());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }

    @Test
    void whenSettingsAreUpdatedSuccessfulResponseIsReturned() throws Exception {
        when(repository.update(any(Settings.class)))
                .thenReturn(true);

        SimpleResponse<?> response = service.update(new Settings());

        assertTrue(response.isSuccess());
        assertNotNull(response.getData());
        assertTrue(response.getError().isEmpty());
    }
}
