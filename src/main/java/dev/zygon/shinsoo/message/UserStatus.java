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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents status of the user accessing the site.  This
 * contains session information that the backend has verified
 * for the user and allows the front end to enable certain
 * content.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

    @Builder.Default
    @JsonProperty("logged_in")
    private boolean loggedIn = false;

    @Builder.Default
    private String username = "";

    @Builder.Default
    @JsonProperty("mapleid")
    private String mapleId = "";

    @Builder.Default
    @JsonProperty("gm_level")
    private int gmLevel = 0;
}
