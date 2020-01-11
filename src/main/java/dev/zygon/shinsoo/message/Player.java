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
 * Data object which represents a player and their stats.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @JsonIgnore
    public static final long PLAYER_UNRANKED = 0;

    @Builder.Default
    private String name = "";

    @Builder.Default
    private int level = 1;

    @Builder.Default
    private long exp = 0;

    @Builder.Default
    private long fame = 0;

    @Builder.Default
    @JsonProperty("job_id")
    private long job = 0;

    @Builder.Default
    @JsonProperty("guild_name")
    private String guild = null;
}
