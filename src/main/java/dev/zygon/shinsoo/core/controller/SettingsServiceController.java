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

import dev.zygon.shinsoo.controller.SettingsController;
import dev.zygon.shinsoo.message.Settings;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.service.SettingsService;
import dev.zygon.shinsoo.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Implementation for {@link SettingsController} which utilizes
 * {@link SettingsService} to produce a payload of settings and
 * update them as requested. All direct accesses to this routes
 * requires the session to be with an authorized user.  This is
 * verified through consulting the active session via
 * {@link UserService}.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
@Path("/settings")
@Produces(MediaType.APPLICATION_JSON)
public class SettingsServiceController implements SettingsController {

    @Inject
    SettingsService settingsService;

    @Inject
    UserService userService;

    @Override
    @GET
    public Response settings() {
        UserStatus status = userService.session();
        if (status.isAuthorizedUser())
        return Response.ok(settingsService.settings())
                .build();
        else
            return Response.status(Response.Status.UNAUTHORIZED)
                    .build();
    }

    @Override
    @PATCH
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Settings settings) {
        UserStatus status = userService.session();
        if (status.isAuthorizedUser())
            return Response.ok(settingsService.update(settings))
                    .build();
        else
            return Response.status(Response.Status.UNAUTHORIZED)
                    .build();
    }
}
