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

import dev.zygon.shinsoo.controller.VoteController;
import dev.zygon.shinsoo.message.VotePingback;
import dev.zygon.shinsoo.service.VoteService;
import org.jboss.resteasy.spi.HttpRequest;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Implementation for {@link VoteController} which utilizes
 * {@link VoteService} to process the pingback request from
 * GTOP100.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Path("/vote")
@ApplicationScoped
public class VoteServiceController implements VoteController {

    @Context
    HttpRequest request;

    @Inject
    VoteService service;

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response vote(
            @FormParam("VoterIP") @DefaultValue("") String voterIP,
            @FormParam("Successful") @DefaultValue("0") int success,
            @FormParam("Reason") @DefaultValue("") String reason,
            @FormParam("pingUsername") @DefaultValue("") String mapleId,
            @FormParam("pingbackURL") @DefaultValue("") String url) {
        VotePingback pingback = VotePingback.builder()
                .voterIP(voterIP)
                .success(success)
                .failureReason(reason)
                .mapleId(mapleId)
                .url(url)
                .remoteAddress(request.getRemoteAddress())
                .build();
        if (service.process(pingback))
            return Response.ok().build();
        else
            return Response.status(Status.BAD_REQUEST).build();
    }
}
