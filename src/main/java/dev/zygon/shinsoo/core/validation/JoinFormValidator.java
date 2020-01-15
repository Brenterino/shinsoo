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
package dev.zygon.shinsoo.core.validation;

import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.security.TokenValidator;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;

import static dev.zygon.shinsoo.core.validation.ValidationConstants.*;

/**
 * Utility which implements {@link FormValidator} in order
 * to verify if the form used for registration was completed
 * successfully by the user or if an error exists that would
 * prevent registration.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@AllArgsConstructor
public class JoinFormValidator implements FormValidator {

    private JoinCredentials credentials;
    private TokenValidator tokenValidator;

    @Override
    public FormFailures validate() {
        return verifyNoFieldsMissing()
                .or(this::verifyFieldLengths)
                .or(this::verifyFieldsContainNoWhitespace)
                .or(this::verifyEmail)
                .or(this::verifyPasswordConfirmation)
                .or(this::verifyToken);
    }

    private FormFailures verifyNoFieldsMissing() {
        if (credentials.getName().isEmpty())
            return FormFailures.NAME_EMPTY;
        else if (credentials.getEmail().isEmpty())
            return FormFailures.EMAIL_EMPTY;
        else if (credentials.getMapleId().isEmpty())
            return FormFailures.MAPLE_ID_EMPTY;
        else if (credentials.getPassword().isEmpty())
            return FormFailures.PASSWORD_EMPTY;
        else if (credentials.getConfirmation().isEmpty())
            return FormFailures.CONFIRMATION_EMPTY;
        else if (credentials.getToken().isEmpty())
            return FormFailures.TOKEN_CHALLENGE_INCOMPLETE;
        else
            return FormFailures.NONE;
    }

    private FormFailures verifyFieldsContainNoWhitespace() {
        Matcher nameMatcher = WHITESPACE_PATTERN.matcher(credentials.getName());
        Matcher emailMatcher = WHITESPACE_PATTERN.matcher(credentials.getEmail());
        Matcher mapleIdMatcher = WHITESPACE_PATTERN.matcher(credentials.getMapleId());
        Matcher passwordMatcher = WHITESPACE_PATTERN.matcher(credentials.getPassword());
        Matcher confirmationMatcher = WHITESPACE_PATTERN.matcher(credentials.getConfirmation());

        if (nameMatcher.matches())
            return FormFailures.INVALID_NAME;
        else if (emailMatcher.matches())
            return FormFailures.INVALID_EMAIL;
        else if (mapleIdMatcher.matches())
            return FormFailures.INVALID_MAPLE_ID;
        else if (passwordMatcher.matches())
            return FormFailures.INVALID_PASSWORD;
        else if (confirmationMatcher.matches())
            return FormFailures.INVALID_CONFIRMATION;
        else
            return FormFailures.NONE;
    }

    private FormFailures verifyFieldLengths() {
        String name = credentials.getName();
        String email = credentials.getEmail();
        String mapleId = credentials.getMapleId();
        String password = credentials.getPassword();
        String confirmation = credentials.getConfirmation();

        if (name.length() < MINIMUM_FIELD_SIZE || name.length() > MAXIMUM_FIELD_SIZE)
            return FormFailures.NAME_SIZE_OUT_OF_BOUNDS;
        else if (email.length() > MAXIMUM_EMAIL_SIZE)
            return FormFailures.INVALID_EMAIL;
        else if (mapleId.length() < MINIMUM_FIELD_SIZE || mapleId.length() > MAXIMUM_FIELD_SIZE)
            return FormFailures.MAPLE_ID_SIZE_OUT_OF_BOUNDS;
        else if (password.length() < MINIMUM_FIELD_SIZE || password.length() > MAXIMUM_FIELD_SIZE ||
                confirmation.length() < MINIMUM_FIELD_SIZE || confirmation.length() > MAXIMUM_FIELD_SIZE)
            return FormFailures.PASSWORD_SIZE_OUT_OF_BOUNDS;
        else
            return FormFailures.NONE;
    }

    private FormFailures verifyEmail() {
        Matcher matcher = EMAIL_PATTERN.matcher(credentials.getEmail());
        if (matcher.matches())
            return FormFailures.NONE;
        else
            return FormFailures.INVALID_EMAIL;
    }

    private FormFailures verifyPasswordConfirmation() {
        String password = credentials.getPassword();
        String confirm  = credentials.getConfirmation();

        if (password.equals(confirm))
            return FormFailures.NONE;
        else
            return FormFailures.CONFIRMATION_FAILED;
    }

    private FormFailures verifyToken() {
        String token = credentials.getToken();
        if (tokenValidator.valid(token))
            return FormFailures.NONE;
        else
            return FormFailures.TOKEN_CHALLENGE_INVALID;
    }
}
