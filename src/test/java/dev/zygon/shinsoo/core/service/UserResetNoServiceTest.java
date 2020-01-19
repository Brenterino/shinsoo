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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserResetNoServiceTest {

    private UserResetNoService service = new UserResetNoService();

    @Test
    void whenResetIsRequestedFailureMessageIsReturnedAsFunctionalityIsNotSupported() {
        SimpleResponse<?> response = service.reset(new ResetCredentials());

        assertFalse(response.isSuccess());
        assertNull(response.getData());
        assertFalse(response.getError().isEmpty());
    }
}
