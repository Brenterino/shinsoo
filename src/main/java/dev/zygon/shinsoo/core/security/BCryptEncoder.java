package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.Encoder;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BCryptEncoder implements Encoder {

    @ConfigProperty(name = "bcrypt.encoder.rounds", defaultValue = "8")
    int rounds;

    @Override
    public String encode(String input) {
        return BCrypt.hashpw(input, BCrypt.gensalt(rounds));
    }
}
