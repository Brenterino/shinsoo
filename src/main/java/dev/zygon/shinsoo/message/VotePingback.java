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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Message sent by GTOP100 to notify service that a user has
 * voted.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VotePingback {

    @JsonIgnore
    public static final int SUCCESS_CODE = 0;

    @Builder.Default
    @JsonIgnore
    private String remoteAddress = "";

    @Builder.Default
    @JsonProperty("VoterIP")
    private String voterIP = "";

    @Builder.Default
    @JsonProperty("Successful")
    private int success = 0;

    @Builder.Default
    @JsonProperty("Reason")
    private String failureReason = "";

    @Builder.Default
    @JsonProperty("pingUsername")
    private String mapleId = "";

    @Builder.Default
    @JsonProperty("pingbackURL")
    private String url = "";
}
