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

import java.util.regex.Pattern;

/**
 * Utility to validate if a form is valid or not.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public interface FormValidator {

    long MINIMUM_FIELD_SIZE = 4;
    long MAXIMUM_FIELD_SIZE = 12;
    long MAXIMUM_EMAIL_SIZE = 320;

    Pattern EMAIL_PATTERN = Pattern.compile(
            "^([\\p{L}-_\\.]+){1,64}@([\\p{L}-_\\.]+){2,255}.[a-z]{2,}$"
    );

    FormFailures validate();
}
