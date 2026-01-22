package com.juan.estevez.sistemapagos.utils;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@UtilityClass
public class FileUtils {

    private static final String UPLOAD_DIR = "uploads";

    public static String guardarArchivo(MultipartFile archivo) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + archivo.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(archivo.getInputStream(), filePath);

        return fileName;
    }

    public static byte[] obtenerArchivo(String fileName) throws IOException {
        Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        return Files.readAllBytes(filePath);
    }

    public static String calcularHash(MultipartFile archivo) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(archivo.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular hash", e);
        }
    }
}