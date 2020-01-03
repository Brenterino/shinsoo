package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.JoinController;
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.service.UserJoinService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/join")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class JoinServiceController implements JoinController {

    @Inject
    UserJoinService service;

    @Override
    @POST
    public Response join(@MultipartForm JoinCredentials credentials) {
        return Response.ok(service.join(credentials))
                .build();
    }
}
