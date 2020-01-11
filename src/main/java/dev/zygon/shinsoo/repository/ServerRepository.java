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
package dev.zygon.shinsoo.repository;

import dev.zygon.shinsoo.message.Server;

import java.util.List;

/**
 * Represents a data source that contains server information.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public interface ServerRepository {

    List<Server> servers() throws Exception;
}
