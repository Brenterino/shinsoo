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

/**
 * Data object which represents a post that contains information
 * such as updates, announcements, and other news.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Builder.Default
    private long id = 0;

    @Builder.Default
    private String type = "";

    @Builder.Default
    private long views = 0;

    @Builder.Default
    private String title = "";

    @Builder.Default
    private String author = "";

    @Builder.Default
    @JsonProperty("created_at")
    private String createdTime = "";

    @Builder.Default
    @JsonProperty("updated_at")
    private String updatedTime = "";

    @Builder.Default
    private String content = "";
}
