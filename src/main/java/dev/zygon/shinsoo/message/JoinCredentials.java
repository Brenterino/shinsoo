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

import lombok.*;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import javax.ws.rs.FormParam;
import javax.ws.rs.core.MediaType;

/**
 * Message which is sent by the front end when the user submits
 * the registration form.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinCredentials {

    @Builder.Default
    @FormParam("name")
    @PartType(MediaType.TEXT_PLAIN)
    private String name = "";

    @Builder.Default
    @FormParam("email")
    @PartType(MediaType.TEXT_PLAIN)
    private String email = "";

    @Builder.Default
    @FormParam("mapleid")
    @PartType(MediaType.TEXT_PLAIN)
    private String mapleId = "";

    @Builder.Default
    @FormParam("password")
    @PartType(MediaType.TEXT_PLAIN)
    private String password = "";

    @Builder.Default
    @FormParam("password_confirmation")
    @PartType(MediaType.TEXT_PLAIN)
    private String confirmation = "";

    @Builder.Default
    @FormParam("g-recaptcha-response")
    @PartType(MediaType.TEXT_PLAIN)
    private String token = "";
}
