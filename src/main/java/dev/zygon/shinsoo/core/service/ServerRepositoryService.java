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

import dev.zygon.shinsoo.message.ServerList;
import dev.zygon.shinsoo.repository.ServerRepository;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.service.ServerService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Implementation for {@link ServerService} which utilizes
 * {@link UserRepository} to retrieve online user count
 * and {@link ServerRepository} to retrieve server status
 * information.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@ApplicationScoped
public class ServerRepositoryService implements ServerService {

    @ConfigProperty(name = "shinsoo.banner.message")
    Optional<String> message;

    @Inject
    UserRepository userRepository;

    @Inject
    ServerRepository serverRepository;

    @Override
    public ServerList servers() {
        try {
            return ServerList.builder()
                    .online(userRepository.usersOnline())
                    .bannerMessage(message.orElse(null))
                    .statuses(serverRepository.servers())
                    .build();
        } catch (Exception ex) {
            log.error("Unable to load server data from repository.", ex);
            return new ServerList();
        }
    }
}
