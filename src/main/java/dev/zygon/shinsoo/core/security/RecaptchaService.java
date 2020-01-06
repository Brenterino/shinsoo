package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.core.message.RecaptchaResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/api")
@RegisterRestClient(configKey = "recaptcha-service")
public interface RecaptchaService {

    @POST
    @Path("/siteverify")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    RecaptchaResponse validateToken(
            @QueryParam("secret") String secret,
            @QueryParam("response") String response,
            String content); //< this argument is a hack since otherwise it will not produce a correct request
}
