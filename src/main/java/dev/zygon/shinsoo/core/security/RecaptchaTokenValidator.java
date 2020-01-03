package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.TokenValidator;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RecaptchaTokenValidator implements TokenValidator {

    @Override
    public boolean valid(String token) {
        // TODO check recaptcha token
        return true;
    }
}
