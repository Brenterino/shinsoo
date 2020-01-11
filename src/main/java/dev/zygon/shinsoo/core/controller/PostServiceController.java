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
import dev.zygon.shinsoo.message.PostPayload;
import dev.zygon.shinsoo.service.PostService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Implementation for {@link PostController} which utilizes
 * {@link PostService} to find the requested post and produce
 * the payload which contains it.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
public class PostServiceController implements PostController {

    @Inject
    PostService service;

    @Override
    @GET
    public Response post() {
        return post(PostPayload.UNKNOWN_POST);
    }

    @Override
    @GET
    @Path("/{id}")
    public Response post(@PathParam("id") long id) {
        return Response.ok(service.post(id))
                .build();
    }
}
