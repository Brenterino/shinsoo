package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.NewsController;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.service.NewsService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/news")
@Produces(MediaType.APPLICATION_JSON)
public class NewsServiceController implements NewsController {

    @Inject
    NewsService service;

    @Override
    @GET
    public Response news() {
        return news(Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/{page}")
    public Response news(@PathParam("page") long page) {
        return Response.ok(service.news(page))
                .build();
    }
}
