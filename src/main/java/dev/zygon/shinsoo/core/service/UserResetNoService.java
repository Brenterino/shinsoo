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
package dev.zygon.shinsoo.core.service;

import dev.zygon.shinsoo.message.ResetCredentials;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.service.UserResetService;

import javax.enterprise.context.ApplicationScoped;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * Implementation for {@link UserResetService} that
 * provides no actual service to reset a password as
 * it is not supported.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class UserResetNoService implements UserResetService {

    @Override
    public SimpleResponse<?> reset(ResetCredentials credentials) {
        return SimpleResponse.<List<String>>builder()
                .success(false)
                .error(singletonList("Feature is not supported."))
                .build();
    }
}
