package dev.zygon.shinsoo.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

@Getter
@AllArgsConstructor
public enum FormFailures {

    NAME_EMPTY("Name must not be empty."),
    EMAIL_EMPTY("Email must not be empty."),
    MAPLE_ID_EMPTY("MapleID must not be empty."),
    PASSWORD_EMPTY("Password must not be empty."),
    CONFIRMATION_EMPTY("Password confirmation must not be empty."),
    TOKEN_CHALLENGE_INCOMPLETE("Please complete the reCAPTCHA challenge."),
    TOKEN_CHALLENGE_INVALID("reCAPTCHA challenge is invalid. Please try again."),
    CONFIRMATION_FAILED("Passwords do not match."),
    NONE("")
    ;

    private String message;

    public FormFailures or(Supplier<FormFailures> checker) {
        if (this == NONE)
            return checker.get();
        return this;
    }
}
