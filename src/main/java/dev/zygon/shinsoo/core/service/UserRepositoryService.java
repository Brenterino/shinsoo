package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.core.validation.LoginFormValidator;
import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.message.UserStatus;
import dev.zygon.shinsoo.message.UserStatusPayload;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Checker;
import dev.zygon.shinsoo.service.UserService;
import dev.zygon.shinsoo.security.SecuredSession;
import dev.zygon.shinsoo.validation.FormFailures;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

import static java.util.Collections.singletonList;

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
    public UserStatusPayload login(LoginCredentials credentials) {
        LoginFormValidator validator = new LoginFormValidator(credentials);
        FormFailures formFailures = validator.validate();
        if (formFailures == FormFailures.NONE)
            return attemptLogin(credentials);
        else
            return createFailedResponse(formFailures.getMessage());
    }

    private UserStatusPayload attemptLogin(LoginCredentials credentials) {
        String email = credentials.getEmail();
        String password = credentials.getPassword();
        Optional<UserDetails> retrievedDetails = retrieveDetailsForEmail(email);

        if (retrievedDetails.isPresent()) {
            UserDetails details = retrievedDetails.get();
            if (checker.check(password, details.getPassword()))
                return doLogin(details);
            else
                return createFailedResponse("Incorrect password.");
        } else {
            return createFailedResponse("No account with this email exists.");
        }
    }

    private UserStatusPayload doLogin(UserDetails details) {
        UserStatus status = session.begin(createStatusFromDetails(details));

        if (status.isLoggedIn())
            return createSuccessfulResponse(status);
        else
            return createFailedResponse("Unable to begin session. Please try again.");
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
    public UserStatusPayload logout() {
        if (session.status().isLoggedIn())
            return doLogout();
        else
            return createFailedResponse("Currently not logged in.");
    }

    private UserStatusPayload doLogout() {
        UserStatus status = session.end();
        if (status.isLoggedIn())
            return createFailedResponse("Something went wrong when logging out. Please try again.");
        else
            return createSuccessfulResponse(status);
    }

    private static UserStatusPayload createFailedResponse(String message) {
        return UserStatusPayload.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private static UserStatusPayload createSuccessfulResponse(UserStatus status) {
        return UserStatusPayload.builder()
                .success(true)
                .status(status)
                .build();
    }
}
