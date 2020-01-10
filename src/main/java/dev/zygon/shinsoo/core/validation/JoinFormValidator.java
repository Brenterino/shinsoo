package dev.zygon.shinsoo.core.validation;

import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.message.JoinCredentials;
import dev.zygon.shinsoo.security.TokenValidator;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

import java.util.regex.Matcher;

@AllArgsConstructor
public class JoinFormValidator implements FormValidator {

    private JoinCredentials credentials;
    private TokenValidator tokenValidator;

    @Override
    public FormFailures validate() {
        return verifyNoFieldsMissing()
                .or(this::verifyFieldLengths)
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
