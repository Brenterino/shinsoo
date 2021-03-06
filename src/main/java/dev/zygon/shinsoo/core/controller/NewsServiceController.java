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

import dev.zygon.shinsoo.controller.NewsController;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.service.PostService;
import dev.zygon.shinsoo.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Implementation for {@link NewsController} which utilizes
 * {@link PostService} to produce a paginated list of posts
 * based on a requested page. In order to load all of the
 * posts in the database in one list the user must be
 * authorized and this is checked via consulting the active
 * session via {@link UserService}.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsServiceController implements NewsController {

    @Inject
    PostService postService;

    @Inject
    UserService userService;

    @Override
    @GET
    public Response news() {
        return news(Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/{page}")
    public Response news(@PathParam("page") long page) {
        return Response.ok(postService.posts(page))
                .build();
    }

    @Override
    @GET
    @Path("/all")
    public Response all() {
        UserStatus status = userService.session();
        if (status.isAuthorizedUser())
            return Response.ok(postService.posts())
                    .build();
        else
            return Response.status(Status.UNAUTHORIZED)
                    .build();
    }
}
