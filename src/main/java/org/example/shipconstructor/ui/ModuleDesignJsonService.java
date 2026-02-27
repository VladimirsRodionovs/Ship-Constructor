package org.example.shipconstructor.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModuleDesignJsonService {
    private final ObjectMapper objectMapper;

    public ModuleDesignJsonService() {
        this.objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(Path path, Map<String, Object> moduleData) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path is required");
        }
        if (moduleData == null) {
            throw new IllegalArgumentException("moduleData is required");
        }
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Map<String, Object> wrapper = new LinkedHashMap<String, Object>();
        wrapper.put("schema", "ship-module-design");
        wrapper.put("version", Integer.valueOf(1));
        wrapper.put("savedAtUtc", Instant.now().toString());
        wrapper.put("module", moduleData);

        try (OutputStream out = Files.newOutputStream(path)) {
            objectMapper.writeValue(out, wrapper);
        }
    }

    public Map<String, Object> load(Path path) throws IOException {
        try (InputStream in = Files.newInputStream(path)) {
            Map<String, Object> wrapper = objectMapper.readValue(in, new TypeReference<Map<String, Object>>() { });
            Object module = wrapper.get("module");
            if (!(module instanceof Map)) {
                throw new IOException("JSON file does not contain 'module' object");
            }
            return new LinkedHashMap<String, Object>((Map<String, Object>) module);
        }
    }

    public String toPrettyJson(Map<String, Object> moduleData) throws IOException {
        return objectMapper.writeValueAsString(moduleData);
    }

    public String toPrettyJsonValue(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }

    public void validateJsonText(String text) throws IOException {
        objectMapper.readTree(text);
    }

    public Object parseJsonValue(String text) throws IOException {
        return objectMapper.readValue(text, Object.class);
    }
}
