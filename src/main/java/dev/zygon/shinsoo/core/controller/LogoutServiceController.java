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

import dev.zygon.shinsoo.controller.LogoutController;
import dev.zygon.shinsoo.core.message.CookieHoldingUserStatus;
import dev.zygon.shinsoo.message.UserStatusPayload;
import dev.zygon.shinsoo.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Implementation for {@link LogoutController} which utilizes
 * {@link UserService} to process logging out of the service.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
@Path("/logout")
@Produces(MediaType.APPLICATION_JSON)
public class LogoutServiceController implements LogoutController {

    @Inject
    UserService service;

    @Override
    @GET
    public Response logout() {
        UserStatusPayload payload = service.logout();
        CookieHoldingUserStatus status = (CookieHoldingUserStatus) payload.getStatus();
        ResponseBuilder builder = Response.ok(payload);
        if (status != null)
            builder.cookie(status.getCookie());
        return builder.build();
    }
}
