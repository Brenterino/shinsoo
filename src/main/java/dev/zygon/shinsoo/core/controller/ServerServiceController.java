package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.ServerController;
import dev.zygon.shinsoo.service.ServerService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class ServerServiceController implements ServerController {

    @Inject
    ServerService service;

    @Override
    @GET
    public Response server() {
        return Response.ok(service.servers())
                .build();
    }
}
