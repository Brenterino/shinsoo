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

import dev.zygon.shinsoo.controller.RankingController;
import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Paginated;
import dev.zygon.shinsoo.service.RankingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Implementation for {@link RankingController} which utilizes
 * {@link RankingService} to find various ranking configurations
 * and produce the payloads which contain player information.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
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
        return Response.ok(service.jobRankings(JobRange.UNKNOWN.name(), Paginated.DEFAULT_PAGE))
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
