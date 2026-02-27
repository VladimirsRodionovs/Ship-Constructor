package org.example.shipconstructor.db;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class DatabaseConfig {
    private final String jdbcUrl;
    private final String username;
    private final String password;

    public DatabaseConfig(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public static DatabaseConfig fromEnvFile(Path envPath) throws Exception {
        Map<String, String> env = EnvFileLoader.load(envPath);

        String url = trimToNull(env.get("DB_URL"));
        String user = required(env, "DB_USER");
        String password = required(env, "DB_PASSWORD");

        if (url == null) {
            String host = required(env, "DB_HOST");
            String port = env.containsKey("DB_PORT") ? env.get("DB_PORT") : "3306";
            String dbName = required(env, "DB_NAME");
            url = "jdbc:mysql://" + host + ":" + port + "/" + dbName
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }

        return new DatabaseConfig(url, user, password);
    }

    public static Path defaultEnvPath() {
        Path dotEnv = Paths.get(System.getProperty("user.dir"), ".env");
        if (Files.exists(dotEnv)) {
            return dotEnv;
        }
        Path projectEnv = Paths.get(System.getProperty("user.dir"), "project.env");
        return projectEnv;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    private static String required(Map<String, String> env, String key) {
        String value = trimToNull(env.get(key));
        if (value == null) {
            throw new IllegalArgumentException("Missing required .env key: " + key);
        }
        return value;
    }

    private static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
