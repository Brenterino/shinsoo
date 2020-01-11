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
import dev.zygon.shinsoo.security.TokenValidator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * Implementation for {@link TokenValidator} which utilizes the
 * reCAPTCHA API in order to verify a token is valid.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class RecaptchaTokenValidator implements TokenValidator {

    @ConfigProperty(name = "recaptcha.secret")
    String secret;

    @Inject
    @RestClient
    RecaptchaService service;

    @Override
    public boolean valid(String token) {
        RecaptchaResponse response = service.validateToken(secret, token, "");
        return response.isSuccess();
    }
}
