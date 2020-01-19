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
import dev.zygon.shinsoo.core.validation.JoinFormValidator;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.validation.Failures;
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.repository.UserRepository;
import dev.zygon.shinsoo.security.Encoder;
import dev.zygon.shinsoo.security.TokenValidator;
import dev.zygon.shinsoo.service.UserJoinService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static dev.zygon.shinsoo.message.SimpleResponse.failure;
import static dev.zygon.shinsoo.message.SimpleResponse.success;

/**
 * Implementation for {@link UserJoinService} which utilizes
 * {@link UserRepository} to retrieve user data from a data
 * repository.  Generates response messages based on the
 * validity of the data submitted in the form and the
 * data retrieved from the repository.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
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
    public SimpleResponse<?> join(JoinCredentials credentials) {
        JoinFormValidator validator = new JoinFormValidator(credentials, tokenValidator);
        Failures formFailures = validator.validate();
        if (formFailures == Failures.NONE)
            return attemptJoin(credentials);
        else
            return failure(formFailures.getMessage());
    }

    private SimpleResponse<?> attemptJoin(JoinCredentials credentials) {
        String username = credentials.getName();
        String mapleId = credentials.getMapleId();
        String email = credentials.getEmail();
        try {
            if (repository.userExists(username))
                return failure("User with this username exists already!");
            else if (repository.idExists(mapleId))
                return failure("User with this Maple ID exists already!");
            else if (repository.emailExists(email))
                return failure("User with this email exists already!");
            else
                return createUser(createDetailsFromCredentials(credentials));
        } catch (Exception ex) {
            log.error("Something went wrong while trying to insert new user into repository.", ex);
            return failure("Registration failed due to database issues.");
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

    private SimpleResponse<?> createUser(UserDetails details) throws Exception {
        if (repository.create(details))
            return success("You have successfully joined!");
        else
            return failure("Something unexpected happened while saving. Please try again.");
    }
}
