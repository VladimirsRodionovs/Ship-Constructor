package org.example.shipconstructor.chassis.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ChassisCoefficientLibraryLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChassisCoefficientLibrary load(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path is required");
        }
        try (InputStream in = Files.newInputStream(path)) {
            ChassisCoefficientLibrary library = objectMapper.readValue(in, ChassisCoefficientLibrary.class);
            if (library == null) {
                throw new IOException("Coefficient library is empty");
            }
            return library;
        }
    }

    public static Path defaultSamplePath() {
        return Paths.get(System.getProperty("user.dir"), "data", "reference", "chassis_coefficients.sample.json");
    }
}
