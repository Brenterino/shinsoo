package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.core.message.CookieHoldingUserStatus;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.SessionRepository;
import dev.zygon.shinsoo.security.NonceGenerator;
import dev.zygon.shinsoo.security.SecuredSession;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.CookieParam;
import javax.ws.rs.core.NewCookie;
import java.util.Optional;

@Slf4j
@RequestScoped
public class CookieSecuredSession implements SecuredSession {

    private static final String SESSION_NONCE = "SESSION_NONCE";

    @CookieParam(SESSION_NONCE)
    String nonce;

    @Inject
    SessionRepository session;

    @Inject
    NonceGenerator generator;

    @Override
    public UserStatus status() {
        if (nonce != null)
            return fromRepository().orElse(new UserStatus());
        else
            return new UserStatus();
    }

    private Optional<UserStatus> fromRepository() {
        try {
            if (session.sessionActive(nonce))
                return Optional.ofNullable(session.session(nonce));
        } catch (Exception ex) {
            log.error("Something went wrong when retrieving active session from repository.", ex);
        }
        return Optional.empty();
    }

    @Override
    public UserStatus begin(UserStatus status) {
        UserStatus current = status();
        if (current.isLoggedIn())
            return current;
        else
            return beginSession(status);
    }

    private UserStatus beginSession(UserStatus status) {
        String cookieNonce = generator.nonce(status.getUsername());
        try {
            session.beginSession(cookieNonce, status);
            status.setLoggedIn(true);
            return holdCookie(cookieNonce, status);
        } catch (Exception ex) {
            log.error("Something went wrong when beginning session with repository.", ex);
            status.setLoggedIn(false);
            return status;
        }
    }

    @Override
    public UserStatus end() {
        UserStatus status = status();
        if (status.isLoggedIn())
            return endSession(status);
        else
            return status;
    }

    private UserStatus endSession(UserStatus status) {
        try {
            session.endSession(nonce);
            return holdCookie("", new UserStatus());
        } catch (Exception ex) {
            log.error("Something went wrong when ending session with repository.", ex);
            return status;
        }
    }

    private CookieHoldingUserStatus holdCookie(String nonce, UserStatus status) {
        return CookieHoldingUserStatus.builder()
                .username(status.getUsername())
                .mapleId(status.getMapleId())
                .gmLevel(status.getGmLevel())
                .loggedIn(status.isLoggedIn())
                .cookie(new NewCookie(SESSION_NONCE, nonce))
                .build();
    }
}
