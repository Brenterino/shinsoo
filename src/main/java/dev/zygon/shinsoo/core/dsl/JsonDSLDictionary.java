package dev.zygon.shinsoo.core.dsl;

import dev.zygon.shinsoo.dsl.DSLDictionary;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

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
        // TODO implement DSL load
    }
}
