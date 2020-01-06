package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.core.message.RecaptchaResponse;
import dev.zygon.shinsoo.security.TokenValidator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
