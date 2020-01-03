package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.NonceGenerator;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class SessionNonceGenerator implements NonceGenerator {

    @Override
    public String nonce() {
        // TODO create
        return null;
    }
}
