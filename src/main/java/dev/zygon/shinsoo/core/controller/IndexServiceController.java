package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.IndexController;
import dev.zygon.shinsoo.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class IndexServiceController implements IndexController {

    @Inject
    UserService service;

    @Override
    @GET
    public Response index() {
        return Response.ok(service.session())
                .build();
    }
}
