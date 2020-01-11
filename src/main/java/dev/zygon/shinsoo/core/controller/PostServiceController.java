package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.PostController;
import dev.zygon.shinsoo.message.PostPayload;
import dev.zygon.shinsoo.service.PostService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
