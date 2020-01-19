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
package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.core.validation.LoginFormValidator;
import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Checker;
import dev.zygon.shinsoo.service.UserService;
import dev.zygon.shinsoo.security.SecuredSession;
import dev.zygon.shinsoo.validation.Failures;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static dev.zygon.shinsoo.message.SimpleResponse.*;

/**
 * Implementation for {@link UserService} which utilizes
 * {@link UserRepository} to retrieve user data from a data
 * repository and {@link SecuredSession} in order to check
 * and change session information. Generates response messages
 * based on the data retrieved from the repository and session
 * service.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@ApplicationScoped
public class UserRepositoryService implements UserService {

    @Inject
    SecuredSession session;

    @Inject
    UserRepository repository;

    @Inject
    Checker checker;

    @Override
    public UserStatus session() {
        return session.status();
    }

    @Override
    public SimpleResponse<?> login(LoginCredentials credentials) {
        LoginFormValidator validator = new LoginFormValidator(credentials);
        Failures formFailures = validator.validate();
        if (formFailures == Failures.NONE)
            return attemptLogin(credentials);
        else
            return failure(formFailures.getMessage());
    }

    private SimpleResponse<?> attemptLogin(LoginCredentials credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();
        Optional<UserDetails> retrievedDetails = retrieveDetailsForEmail(email);

        if (retrievedDetails.isPresent()) {
            UserDetails details = retrievedDetails.get();
            if (checker.check(password, details.getPassword()))
                return doLogin(details);
            else
                return failure("Incorrect password.");
        } else {
            return failure("No account with this email exists.");
        }
    }

    private SimpleResponse<?> doLogin(UserDetails details) {
        UserStatus status = session.begin(createStatusFromDetails(details));

        if (status.isLoggedIn())
            return success(status);
        else
            return failure("Unable to begin session. Please try again.");
    }

    private UserStatus createStatusFromDetails(UserDetails details) {
        return UserStatus.builder()
                .username(details.getUsername())
                .mapleId(details.getMapleId())
                .gmLevel(details.getGmLevel())
                .build();
    }

    private Optional<UserDetails> retrieveDetailsForEmail(String email) {
        try {
            if (repository.emailExists(email))
                return Optional.ofNullable(repository.detailsForEmail(email));
        } catch (Exception ex) {
            log.error("Unable to load user details due to database issue.", ex);
        }
        return Optional.empty();
    }

    @Override
    public SimpleResponse<?> logout() {
        if (session.status().isLoggedIn())
            return doLogout();
        else
            return failure("Currently not logged in.");
    }

    private SimpleResponse<?> doLogout() {
        UserStatus status = session.end();
        if (status.isLoggedIn())
            return failure("Something went wrong when logging out. Please try again.");
        else
            return success(status);
    }
}
