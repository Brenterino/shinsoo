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

/**
 * Implementation for {@link SecuredSession} which utilizes
 * {@link SessionRepository} and {@link NonceGenerator} to
 * manage sessions and generate a unique identifier for
 * started sessions.  Utilizes cookies to identify if a
 * client knows about the session.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
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
