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
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Represents a response which has its full results split across
 * multiple pages.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Paginated<E> extends SimpleResponse<E> {

    @JsonIgnore
    public static final int DEFAULT_PAGE = 1;

    @JsonIgnore
    public static final int DEFAULT_PAGE_SIZE = 5;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long prev = 0;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long current = 0;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long next = 0;

    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private long last = 0;
}
