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

import dev.zygon.shinsoo.controller.PostController;
import dev.zygon.shinsoo.message.Post;
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
 * Implementation for {@link PostController} which utilizes
 * {@link PostService} to find the requested post and produce
 * the payload which contains it.  For updates, deletes, and
 * creates the user must be authorized and this is checked
 * via consulting the active session via {@link UserService}.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostServiceController implements PostController {

    static final int UNKNOWN_POST = 0;

    @Inject
    PostService postService;

    @Inject
    UserService userService;

    @Override
    @GET
    public Response post() {
        return post(UNKNOWN_POST);
    }

    @Override
    @GET
    @Path("/{id}")
    public Response post(@PathParam("id") long id) {
        return Response.ok(postService.post(id))
                .build();
    }

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Post post) {
        UserStatus status = userService.session();
        post.setAuthor(status.getUsername());
        if (status.isAuthorizedUser())
            return Response.ok(postService.create(post))
                    .build();
        else
            return Response.status(Status.UNAUTHORIZED)
                    .build();
    }

    @Override
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        UserStatus status = userService.session();
        if (status.isAuthorizedUser())
            return Response.ok(postService.delete(id))
                    .build();
        else
            return Response.status(Status.UNAUTHORIZED)
                    .build();
    }

    @Override
    @PATCH
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Post post) {
        UserStatus status = userService.session();
        if (status.isAuthorizedUser())
            return Response.ok(postService.update(id, post))
                    .build();
        else
            return Response.status(Status.UNAUTHORIZED)
                    .build();
    }
}
