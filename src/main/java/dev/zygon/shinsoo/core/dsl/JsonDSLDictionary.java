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
        }
        initialized = true;
    }
}
