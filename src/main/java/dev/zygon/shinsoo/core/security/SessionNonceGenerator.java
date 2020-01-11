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
package dev.zygon.shinsoo.core.security;

import dev.zygon.shinsoo.security.NonceGenerator;

import javax.enterprise.context.RequestScoped;
import java.math.BigInteger;

/**
 * Implementation for {@link NonceGenerator} which utilizes
 * metadata from the user and the current time in order to
 * generate a unique id that should not be repeatable again.
 * This implementation may be naive and require revisiting
 * in the future.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@RequestScoped
public class SessionNonceGenerator implements NonceGenerator {

    @Override
    public String nonce(String metadata) {
        return numericNonce(metadata).toString();
    }

    private BigInteger numericNonce(String metadata) {
        return mashed(System.currentTimeMillis(),
            metadata.hashCode());
    }

    private BigInteger mashed(long left, long right) {
        BigInteger bigLeft = BigInteger.valueOf(left);
        BigInteger bigRight = BigInteger.valueOf(right);

        return bigLeft
                .shiftLeft(Long.SIZE)
                .or(bigRight);
    }
}
