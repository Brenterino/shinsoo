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

import dev.zygon.shinsoo.message.Server;
import dev.zygon.shinsoo.message.ServerList;
import dev.zygon.shinsoo.message.Settings;
import dev.zygon.shinsoo.repository.ServerRepository;
import dev.zygon.shinsoo.repository.SettingsRepository;
import dev.zygon.shinsoo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ServerRepositoryServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SettingsRepository settingsRepository;

    @Mock
    private ServerRepository serverRepository;

    @InjectMocks
    private ServerRepositoryService service;

    private Settings settings;
    private List<Server> servers;

    @BeforeEach
    void setup() {
        settings = Settings.builder()
                .bannerMessage("hi there!")
                .build();
        servers = new LinkedList<>();
        servers.add(new Server());
    }

    @Test
    void whenServersAreRequestedAndSettingsRepositoryThrowsExceptionDefaultServerListIsReturned() throws Exception {
        when(settingsRepository.settings())
                .thenThrow(new Exception());

        assertEquals(new ServerList(), service.servers());
    }

    @Test
    void whenServersAreRequestedAndUserRepositoryThrowsExceptionDefaultServerListIsReturned() throws Exception {
        when(settingsRepository.settings())
                .thenReturn(settings);
        when(userRepository.usersOnline())
                .thenThrow(new Exception());

        assertEquals(new ServerList(), service.servers());
    }

    @Test
    void whenServersAreRequestedAndServerRepositoryThrowsExceptionDefaultServerListIsReturned() throws Exception {
        when(settingsRepository.settings())
                .thenReturn(settings);
        when(userRepository.usersOnline())
                .thenReturn(11L);
        when(serverRepository.servers())
                .thenThrow(new Exception());

        assertEquals(new ServerList(), service.servers());
    }

    @Test
    void whenServersAreRequestedAndServerListIsBuiltSuccessfullyItIsReturned() throws Exception {
        when(settingsRepository.settings())
                .thenReturn(settings);
        when(userRepository.usersOnline())
                .thenReturn(69L);
        when(serverRepository.servers())
                .thenReturn(servers);

        ServerList expected = ServerList.builder()
                .online(69L)
                .bannerMessage(settings.getBannerMessage())
                .statuses(servers)
                .build();

        assertEquals(expected, service.servers());
    }
}
