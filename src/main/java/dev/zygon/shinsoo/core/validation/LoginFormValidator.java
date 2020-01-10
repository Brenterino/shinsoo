package dev.zygon.shinsoo.core.validation;

import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;

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
