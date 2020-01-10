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

    @Override
    @GET
    @Path("/overall")
    public Response overallRankings() {
        return overallRankings(Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/overall/{page}")
    public Response overallRankings(@PathParam("page") long page) {
        return rankings(page);
    }

    @Override
    @GET
    @Path("/job")
    public Response jobRankings() {
        return Response.ok(service.jobRankings("", 0))
                .build();
    }

    @Override
    @GET
    @Path("/job/{job}")
    public Response jobRankings(@PathParam("job") String job) {
        return jobRankings(job, Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/job/{job}/{page}")
    public Response jobRankings(@PathParam("job") String job, @PathParam("page") long page) {
        return Response.ok(service.jobRankings(job, page))
                .build();
    }

    @Override
    @GET
    @Path("/fame")
    public Response fameRankings() {
        return fameRankings(Paginated.DEFAULT_PAGE);
    }

    @Override
    @GET
    @Path("/fame/{page}")
    public Response fameRankings(@PathParam("page") long page) {
        return Response.ok(service.fameRankings(page))
                .build();
    }

    @Override
    @GET
    @Path("/search")
    public Response searchRankings() {
        return null; // return nothing :)
    }

    @Override
    @GET
    @Path("/search/{query}")
    public Response searchRankings(@PathParam("query") String query) {
        return Response.ok(service.searchRankings(query))
                .build();
    }
}
