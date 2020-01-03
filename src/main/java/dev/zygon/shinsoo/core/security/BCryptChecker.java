package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.Checker;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BCryptChecker implements Checker {

    @Override
    public boolean check(String provided, String saved) {
        return BCrypt.checkpw(provided, saved);
    }
}
