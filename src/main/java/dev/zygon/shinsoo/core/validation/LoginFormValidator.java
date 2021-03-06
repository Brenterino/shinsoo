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

import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.validation.Failures;
import dev.zygon.shinsoo.validation.Validator;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;

import static dev.zygon.shinsoo.core.validation.ValidationConstants.*;

/**
 * Utility which implements {@link Validator} in order
 * to verify if the form used for logging in was completed
 * successfully by the user or if an error exists that would
 * prevent login.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@AllArgsConstructor
public class LoginFormValidator implements Validator {

    private LoginCredentials credentials;

    @Override
    public Failures validate() {
        return verifyNoFieldsMissing()
                .or(this::verifyFieldsContainNoWhitespace)
                .or(this::verifyFieldLengths)
                .or(this::verifyEmail);
    }

    private Failures verifyNoFieldsMissing() {
        if (credentials.getEmail().isEmpty())
            return Failures.EMAIL_EMPTY;
        else if (credentials.getPassword().isEmpty())
            return Failures.PASSWORD_EMPTY;
        return Failures.NONE;
    }

    private Failures verifyFieldsContainNoWhitespace() {
        Matcher emailMatcher = WHITESPACE_PATTERN.matcher(credentials.getEmail());
        Matcher passwordMatcher = WHITESPACE_PATTERN.matcher(credentials.getPassword());

        if (emailMatcher.matches())
            return Failures.INVALID_EMAIL;
        else if (passwordMatcher.matches())
            return Failures.INVALID_PASSWORD;
        else
            return Failures.NONE;
    }

    private Failures verifyFieldLengths() {
        String email = credentials.getEmail();
        String password = credentials.getPassword();

        if (email.length() > MAXIMUM_EMAIL_SIZE)
            return Failures.INVALID_EMAIL;
        else if (password.length() < MINIMUM_FIELD_SIZE || password.length() > MAXIMUM_FIELD_SIZE)
            return Failures.PASSWORD_SIZE_OUT_OF_BOUNDS;
        else
            return Failures.NONE;
    }

    private Failures verifyEmail() {
        Matcher matcher = EMAIL_PATTERN.matcher(credentials.getEmail());
        if (matcher.matches())
            return Failures.NONE;
        else
            return Failures.INVALID_EMAIL;
    }
}
