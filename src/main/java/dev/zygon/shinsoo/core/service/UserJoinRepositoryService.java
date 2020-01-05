package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.core.dto.UserDetails;
import dev.zygon.shinsoo.core.validation.JoinFormValidator;
import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.message.JoinResponse;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Encoder;
import dev.zygon.shinsoo.security.TokenValidator;
import dev.zygon.shinsoo.service.UserJoinService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static java.util.Collections.singletonList;

@Slf4j
@ApplicationScoped
public class UserJoinRepositoryService implements UserJoinService {

    @Inject
    UserRepository repository;

    @Inject
    Encoder encoder;

    @Inject
    TokenValidator tokenValidator;

    @Override
    public JoinResponse join(JoinCredentials credentials) {
        JoinFormValidator validator = new JoinFormValidator(credentials, tokenValidator);
        FormFailures formFailures = validator.validate();
        if (formFailures == FormFailures.NONE)
            return attemptJoin(credentials);
        else
            return createFailedJoinResponse(formFailures.getMessage());
    }

    private JoinResponse attemptJoin(JoinCredentials credentials) {
        String username = credentials.getName();
        String mapleId = credentials.getMapleId();
        String email = credentials.getEmail();
        try {
            if (repository.userExists(username))
                return createFailedJoinResponse("User with this username exists already!");
            else if (repository.idExists(mapleId))
                return createFailedJoinResponse("User with this Maple ID exists already!");
            else if (repository.emailExists(email))
                return createFailedJoinResponse("User with this email exists already!");
            else
                return createUser(createDetailsFromCredentials(credentials));
        } catch (Exception ex) {
            log.error("Something went wrong while trying to insert new user into database.", ex);
            return createFailedJoinResponse("Registration failed due to database issues.");
        }
    }

    private UserDetails createDetailsFromCredentials(JoinCredentials credentials) {
        // only create encoded password here to save processing time
        String password = encoder.encode(credentials.getPassword());
        return UserDetails.builder()
                .username(credentials.getName())
                .email(credentials.getEmail())
                .mapleId(credentials.getMapleId())
                .password(password)
                .build();
    }

    private JoinResponse createUser(UserDetails details) throws Exception {
        if (repository.create(details))
            return createSuccessfulJoinResponse();
        else
            return createFailedJoinResponse("Something unexpected happened while saving. Please try again.");
    }

    private static JoinResponse createFailedJoinResponse(String message) {
        return JoinResponse.builder()
                .success(false)
                .error(singletonList(message))
                .build();
    }

    private static JoinResponse createSuccessfulJoinResponse() {
        return JoinResponse.builder()
                .success(true)
                .response(singletonList("You have successfully joined!"))
                .build();
    }
}
