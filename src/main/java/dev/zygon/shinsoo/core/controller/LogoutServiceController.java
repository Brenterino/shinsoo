package dev.zygon.shinsoo.core.controller;

import dev.zygon.shinsoo.controller.LogoutController;
import dev.zygon.shinsoo.core.message.CookieHoldingUserStatus;
import dev.zygon.shinsoo.message.UserStatusPayload;
import dev.zygon.shinsoo.service.UserService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

@ApplicationScoped
@Path("/logout")
@Produces(MediaType.APPLICATION_JSON)
public class LogoutServiceController implements LogoutController {

    @Inject
    UserService service;

    @Override
    @GET
    public Response logout() {
        UserStatusPayload payload = service.logout();
        CookieHoldingUserStatus status = (CookieHoldingUserStatus) payload.getStatus();
        ResponseBuilder builder = Response.ok(payload);
        if (status != null)
            builder.cookie(status.getCookie());
        return builder.build();
    }
}
