package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {

    private final String baseDirectory;
    private final ConcurrentHashMap<String, Path> fileMap; // Changed to Path

    public FileOutputStrategy(String baseDirectory) {
        if (baseDirectory == null || baseDirectory.trim().isEmpty()) {
            throw new IllegalArgumentException("Base directory cannot be null or empty");
        }
        this.baseDirectory = baseDirectory;
        this.fileMap = new ConcurrentHashMap<>();

        // Create base directory on initialization
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create base directory: " + baseDirectory, e);
        }
    }

    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (label == null || data == null) {
            System.err.println("Error: Label or data cannot be null");
            return;
        }

        // Use Path instead of String
        Path filePath = fileMap.computeIfAbsent(label, k -> {
            Path newFilePath = Paths.get(baseDirectory, label + "_" + timestamp + ".txt"); // include timestamp in file name
            return newFilePath;
        });

        try { // try block here
            PrintWriter out = new PrintWriter(
                    Files.newBufferedWriter(filePath, // Use Path here
                            StandardCharsets.UTF_8, // Explicitly set UTF-8 encoding
                            StandardOpenOption.CREATE,
                            StandardOpenOption.APPEND));
            try {
                out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n",
                        patientId, timestamp, label, data);
            } finally {
                out.close(); // Ensure PrintWriter is closed
            }
        } catch (IOException e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}
