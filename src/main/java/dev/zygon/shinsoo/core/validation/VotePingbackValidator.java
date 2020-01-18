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
package dev.zygon.shinsoo.core.validation;

import dev.zygon.shinsoo.message.VotePingback;
import dev.zygon.shinsoo.validation.Failures;
import dev.zygon.shinsoo.validation.Validator;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * Utility which implements {@link Validator} in order
 * to verify if the pingback request from a voting service
 * is valide or not.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@AllArgsConstructor
public class VotePingbackValidator implements Validator {

    private VotePingback pingback;
    private String authorizedSources;

    @Override
    public Failures validate() {
        return isAuthorizedSource(pingback.getRemoteAddress())
                .or(this::hasValidContents);
    }

    private Failures isAuthorizedSource(String ip) {
        if (checkAuthorization(ip))
            return Failures.NONE;
        else
            return Failures.BAD_REQUEST_SOURCE;
    }

    private boolean checkAuthorization(String ip) {
        if (authorizedSources.isEmpty())
            return true;
        return Arrays.asList(authorizedSources
                .split(",")).contains(ip);
    }

    private Failures hasValidContents() {
        if (pingback.getMapleId().isEmpty())
            return Failures.MAPLE_ID_EMPTY;
        else if (pingback.getSuccess() != VotePingback.SUCCESS_CODE)
            return Failures.VOTE_FAILED;
        else
            return Failures.NONE;
    }
}
