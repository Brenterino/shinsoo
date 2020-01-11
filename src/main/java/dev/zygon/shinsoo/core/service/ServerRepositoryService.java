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
import dev.zygon.shinsoo.service.ServerService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Optional;

/**
 * @todo To-Be-Implemented
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class ServerRepositoryService implements ServerService {

    @ConfigProperty(name = "shinsoo.banner.message")
    Optional<String> message;

    @Override
    public ServerList servers() {
        ServerList status = new ServerList();
        status.setBannerMessage(message.orElse(null));
        status.setStatuses(new ArrayList<>(4));
        status.getStatuses().add(new Server("Login", true));
        status.getStatuses().add(new Server("Channel 1", true));
        status.getStatuses().add(new Server("Channel 2", false));
        status.getStatuses().add(new Server("Yeet", false));
        return status;
    }
}
