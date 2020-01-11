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

import dev.zygon.shinsoo.message.JobRange;
import dev.zygon.shinsoo.message.Player;

import java.util.List;

/**
 * Represents a data source that contains player information that
 * can be/is organized and ranked in a variety of ways.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public interface PlayerRepository {

    long count() throws Exception;

    List<Player> players(long offset, long limit) throws Exception;

    List<Player> playersByFame(long offset, long limit) throws Exception;

    long countJob(JobRange range) throws Exception;

    List<Player> playersByJob(JobRange range, long offset, long limit) throws Exception;

    List<Player> searchByQuery(String query) throws Exception;
}
