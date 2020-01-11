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
package dev.zygon.shinsoo.core.dsl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zygon.shinsoo.dsl.DSLDictionary;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for {@link DSLDictionary} which utilizes
 * Jackson in order to load a JSON file which contains the
 * mappings of keys to Domain Specific Language (DSL) values.
 *
 * @author Brenterino
 * @since 1.0.0.1
 * @version 1.0.0.1
 */
@Slf4j
@Singleton
public class JsonDSLDictionary implements DSLDictionary {

    @ConfigProperty(name = "dsl.json.source", defaultValue = "")
    String source;

    private boolean initialized;
    private Map<String, String> dictionary;

    public JsonDSLDictionary() {
        initialized = false;
        dictionary = new HashMap<>();
    }

    @Override
    public String value(String key) {
        if (!initialized)
            init();
        return dictionary.getOrDefault(key, "");
    }

    private void init() {
        File dsl = new File(source);
        if (dsl.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                dictionary = mapper.readValue(dsl, new TypeReference<Map<String, String>>() {});
            } catch (Exception ex) {
                log.error("Unable to load DSL dictionary. Please verify it is correct and can be loaded.", ex);
            }
        } else {
            log.warn("DSL dictionary was not found. Database operations may fail.");
        }
        initialized = true;
    }
}
