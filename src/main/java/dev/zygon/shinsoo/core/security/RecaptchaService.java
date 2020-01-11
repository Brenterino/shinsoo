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
package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.core.message.RecaptchaResponse;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * RestClient template for usage in accessing the reCAPTCHA API
 * to verify a token is valid and that a user is not a robot.
 * Actual request code is automatically generated and is not
 * explicitly user implemented.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
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
