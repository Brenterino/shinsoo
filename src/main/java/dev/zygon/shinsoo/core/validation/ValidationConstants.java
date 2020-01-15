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

import java.util.regex.Pattern;

/**
 * Provides constants for form validation purposes.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public class ValidationConstants {

    public static final long MINIMUM_FIELD_SIZE = 4;
    public static final long MAXIMUM_FIELD_SIZE = 12;
    public static final long MAXIMUM_EMAIL_SIZE = 320;

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^([\\p{L}-_\\.]+){1,64}@([\\p{L}-_\\.]+){2,255}.[a-z]{2,}$"
    );

    public static final Pattern WHITESPACE_PATTERN = Pattern.compile(
            "\\s"
    );
}
