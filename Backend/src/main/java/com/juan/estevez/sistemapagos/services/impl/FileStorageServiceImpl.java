package com.juan.estevez.sistemapagos.services.impl;

import com.juan.estevez.sistemapagos.exceptions.FileStorageException;
import com.juan.estevez.sistemapagos.services.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Override
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        try {
            // Crear directorio si no existe
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generar nombre único
            String fileName = UUID.randomUUID().toString() + "_" +
                    archivo.getOriginalFilename().replaceAll("[^a-zA-Z0-9.-]", "_");

            // Guardar archivo
            Path targetLocation = uploadPath.resolve(fileName);
            Files.copy(archivo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("Archivo guardado: {}", fileName);
            return fileName;

        } catch (IOException ex) {
            log.error("Error al guardar archivo", ex);
            throw new FileStorageException("Error al guardar el archivo", ex);
        }
    }

    @Override
    public byte[] obtenerArchivo(String fileName) throws IOException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();

            if (!Files.exists(filePath)) {
                throw new FileStorageException("Archivo no encontrado: " + fileName);
            }

            return Files.readAllBytes(filePath);

        } catch (IOException ex) {
            log.error("Error al leer archivo: {}", fileName, ex);
            throw new FileStorageException("Error al leer el archivo", ex);
        }
    }

    @Override
    public void eliminarArchivo(String fileName) throws IOException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();

            if (Files.exists(filePath)) {
                Files.delete(filePath);
                log.info("Archivo eliminado: {}", fileName);
            }

        } catch (IOException ex) {
            log.error("Error al eliminar archivo: {}", fileName, ex);
            throw new FileStorageException("Error al eliminar el archivo", ex);
        }
    }

    @Override
    public String calcularHash(MultipartFile archivo) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] fileBytes = archivo.getBytes();
            byte[] hashBytes = digest.digest(fileBytes);

            // Convertir a hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException ex) {
            log.error("Error al calcular hash", ex);
            throw new FileStorageException("Error al calcular hash del archivo", ex);
        }
    }
}