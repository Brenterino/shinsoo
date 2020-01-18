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
package dev.zygon.shinsoo.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * Listing of all possible failures associated with the application
 * and messages displayed to the user if that failure is applicable.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Getter
@AllArgsConstructor
public enum Failures {

    NAME_EMPTY("Name must not be empty."),
    EMAIL_EMPTY("Email must not be empty."),
    MAPLE_ID_EMPTY("MapleID must not be empty."),
    PASSWORD_EMPTY("Password must not be empty."),
    CONFIRMATION_EMPTY("Password confirmation must not be empty."),
    TOKEN_CHALLENGE_INCOMPLETE("Please complete the reCAPTCHA challenge."),
    TOKEN_CHALLENGE_INVALID("reCAPTCHA challenge is invalid. Please try again."),
    NAME_SIZE_OUT_OF_BOUNDS("Provided name is either too long or too short."),
    MAPLE_ID_SIZE_OUT_OF_BOUNDS("Provided MapleID is either too long or too short."),
    PASSWORD_SIZE_OUT_OF_BOUNDS("Provided password is either too long or too short."),
    INVALID_NAME("Provided name is not valid."),
    INVALID_EMAIL("Provided email is not valid."),
    INVALID_MAPLE_ID("Provided MapleID is not valid."),
    INVALID_PASSWORD("Provided password is not valid."),
    INVALID_CONFIRMATION("Provided confirmation is not valid."),
    CONFIRMATION_FAILED("Passwords do not match."),
    INVALID_POST_TYPE("Post type is too short or invalid."),
    INVALID_POST_TITLE("Post title is too short or invalid."),
    INVALID_POST_CONTENT("Post content is too short or invalid."),
    BAD_REQUEST_SOURCE("Source of request is not authorized."),
    VOTE_FAILED("Vote for user was not successful."),
    NONE("");

    private String message;

    public Failures or(Supplier<Failures> checker) {
        if (this == NONE)
            return checker.get();
        return this;
    }
}
