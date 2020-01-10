package dev.zygon.shinsoo.validation;

import java.util.regex.Pattern;

public interface FormValidator {

    long MINIMUM_FIELD_SIZE = 4;
    long MAXIMUM_FIELD_SIZE = 12;
    long MAXIMUM_EMAIL_SIZE = 320;

    Pattern EMAIL_PATTERN = Pattern.compile(
            "^([\\p{L}-_\\.]+){1,64}@([\\p{L}-_\\.]+){2,255}.[a-z]{2,}$"
    );

    FormFailures validate();
}
