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
import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;

/**
 * Utility which implements {@link FormValidator} in order
 * to verify if the form used for logging in was completed
 * successfully by the user or if an error exists that would
 * prevent login.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@AllArgsConstructor
public class LoginFormValidator implements FormValidator {

    private LoginCredentials credentials;

    @Override
    public FormFailures validate() {
        return verifyNoFieldsMissing()
                .or(this::verifyFieldLengths)
                .or(this::verifyEmail);
    }

    private FormFailures verifyNoFieldsMissing() {
        if (credentials.getEmail().isEmpty())
            return FormFailures.EMAIL_EMPTY;
        else if (credentials.getPassword().isEmpty())
            return FormFailures.PASSWORD_EMPTY;
        return FormFailures.NONE;
    }

    private FormFailures verifyFieldLengths() {
        String email = credentials.getEmail();
        String password = credentials.getPassword();

        if (email.length() > MAXIMUM_EMAIL_SIZE)
            return FormFailures.INVALID_EMAIL;
        else if (password.length() < MINIMUM_FIELD_SIZE || password.length() > MAXIMUM_FIELD_SIZE)
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
}
