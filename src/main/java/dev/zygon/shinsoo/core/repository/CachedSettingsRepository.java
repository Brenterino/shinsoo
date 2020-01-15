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
package dev.zygon.shinsoo.core.repository;

import dev.zygon.shinsoo.message.Settings;
import dev.zygon.shinsoo.repository.SettingsRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.infinispan.Cache;
import org.infinispan.manager.EmbeddedCacheManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Implementation for {@link SettingsRepository} which utilizes
 * {@link EmbeddedCacheManager} to store settings loaded from
 * config and allows mutation during runtime.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@ApplicationScoped
public class CachedSettingsRepository implements SettingsRepository {

    private static final String SETTINGS_CACHE = "SettingsCache";

    @Inject
    EmbeddedCacheManager manager;

    @ConfigProperty(name = "shinsoo.banner.message")
    Optional<String> message;

    @Override
    public Settings settings() throws Exception {
        Cache<String, Settings> cache = manager.getCache(SETTINGS_CACHE);
        if (cache.containsKey(SETTINGS_CACHE))
            return cache.get(SETTINGS_CACHE);

        Settings settings = Settings.builder()
                .bannerMessage(message.orElse(null))
                .build();
        cache.put(SETTINGS_CACHE, settings);
        return settings;
    }

    @Override
    public boolean update(Settings settings) throws Exception {
        Cache<String, Settings> cache = manager.getCache(SETTINGS_CACHE);
        cache.put(SETTINGS_CACHE, settings);
        return true;
    }
}
