package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.RankingController;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.service.RankingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/rankings")
@Produces(MediaType.APPLICATION_JSON)
public class RankingServiceController implements RankingController {

    @Inject
    RankingService service;

    @Override
    @GET
    public Response rankings() {
        return rankings(Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/{page}")
    public Response rankings(@PathParam("page") long page) {
        return Response.ok(service.rankings(page))
                .build();
    }
}
