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

import dev.zygon.shinsoo.message.Settings;
import dev.zygon.shinsoo.message.SimpleResponse;
import dev.zygon.shinsoo.repository.SettingsRepository;
import dev.zygon.shinsoo.service.SettingsService;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static dev.zygon.shinsoo.message.SimpleResponse.*;

/**
 * Implementation for {@link SettingsService} which utilizes
 * {@link SettingsRepository} to retrieve settings from the
 * repository as well as update them.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@ApplicationScoped
public class SettingsRepositoryService implements SettingsService {

    @Inject
    SettingsRepository repository;

    @Override
    public SimpleResponse<?> settings() {
        try {
            return success(repository.settings());
        } catch (Exception ex) {
            log.error("Something went wrong when trying to retrieve settings from the repository.", ex);
            return failure("Unable to load settings from repository. Try again later.");
        }
    }

    @Override
    public SimpleResponse<?> update(Settings settings) {
        try {
            if (repository.update(settings))
                return success("Successfully updated settings.");
            else
                return failure("Something unexpected happened when updating settings. Please try again.");
        } catch (Exception ex) {
            log.error("Something went wrong when trying to update settings in the repository.", ex);
            return failure("Unable to update settings in repository. Try again later.");
        }
    }
}
