package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.LoginController;
import dev.zygon.shinsoo.core.message.CookieHoldingUserStatus;
import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.message.UserStatusPayload;
import dev.zygon.shinsoo.service.UserService;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@ApplicationScoped
@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
public class LoginServiceController implements LoginController {

    @Inject
    UserService service;

    @Override
    @POST
    public Response login(@MultipartForm LoginCredentials credentials) {
        UserStatusPayload payload = service.login(credentials);
        CookieHoldingUserStatus status = (CookieHoldingUserStatus) payload.getStatus();
        ResponseBuilder builder = Response.ok(payload);
        if (status != null)
            builder.cookie(status.getCookie());
        return builder.build();
    }
}
