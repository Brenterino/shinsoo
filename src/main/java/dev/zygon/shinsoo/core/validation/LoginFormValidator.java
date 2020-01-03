package dev.zygon.shinsoo.core.validation;

import dev.zygon.shinsoo.message.LoginCredentials;
import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginFormValidator implements FormValidator {

    private LoginCredentials credentials;

    @Override
    public FormFailures validate() {
        return null;
    }
}
