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

import dev.zygon.shinsoo.message.Post;
import dev.zygon.shinsoo.validation.FormFailures;
import dev.zygon.shinsoo.validation.FormValidator;
import lombok.AllArgsConstructor;

/**
 * Utility which implements {@link FormValidator} in order
 * to verify if the form used for creating or updated posts
 * was completed successfully by the user or if an error
 * exists that would prevent creating or updating the post.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@AllArgsConstructor
public class PostFormValidator implements FormValidator {

    private Post post;

    @Override
    public FormFailures validate() {
        return verifyNoFieldsMissing();
    }

    private FormFailures verifyNoFieldsMissing() {
        String type = post.getType().trim();
        String title = post.getTitle().trim();
        String content = post.getContent().trim();

        if (type.isEmpty())
            return FormFailures.INVALID_POST_TYPE;
        else if (title.isEmpty())
            return FormFailures.INVALID_POST_TITLE;
        else if (content.isEmpty())
            return FormFailures.INVALID_POST_CONTENT;
        else
            return FormFailures.NONE;
    }
}
