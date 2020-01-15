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
package dev.zygon.shinsoo.service;

import dev.zygon.shinsoo.message.ResetCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;

/**
 * Service which can process a password reset request and
 * generate a response to that request.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
public interface UserResetService {

    SimpleResponse<?> reset(ResetCredentials credentials);
}
