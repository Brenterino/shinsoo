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
package dev.zygon.shinsoo.message;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Message sent to the front end which either contains the
 * current user status/session information or an error
 * message if the request failed.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusPayload {

    @Builder.Default
    private boolean success = false;

    @Builder.Default
    @JsonProperty("data")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserStatus status = null;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> error = Collections.emptyList();
}
