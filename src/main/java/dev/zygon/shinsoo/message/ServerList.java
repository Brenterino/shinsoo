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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Payload which contains a list of players when the request was
 * successful or an error message when it failed.  Resulting list
 * is not paginated and payload may be very large.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerList {

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("alert")
    private String bannerMessage = null;

    @Builder.Default
    @JsonProperty("online_count")
    private int online = 0;

    @Builder.Default
    @JsonProperty("server_status")
    private List<Server> statuses = Collections.emptyList();
}
