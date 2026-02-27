package org.example.shipconstructor.db;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class EnvFileLoader {
    private EnvFileLoader() {
    }

    public static Map<String, String> load(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path is required");
        }
        if (!Files.exists(path)) {
            throw new IOException(".env file not found: " + path.toAbsolutePath());
        }

        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        Map<String, String> values = new LinkedHashMap<String, String>();

        for (String rawLine : lines) {
            if (rawLine == null) {
                continue;
            }
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            int idx = line.indexOf('=');
            if (idx <= 0) {
                continue;
            }

            String key = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();
            values.put(key, stripQuotes(value));
        }
        return values;
    }

    private static String stripQuotes(String value) {
        if (value == null || value.length() < 2) {
            return value;
        }
        if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
