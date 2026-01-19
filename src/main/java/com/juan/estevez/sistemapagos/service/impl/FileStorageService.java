package com.juan.estevez.sistemapagos.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.service.IFileStorageService;

@Service
public class FileStorageService implements IFileStorageService {

    private static final Path ROOT = Paths.get(System.getProperty("user.home"), "enset-data", "pagos");

    public String guardar(MultipartFile archivo) throws IOException {
        Files.createDirectories(ROOT);
        String nombre = UUID.randomUUID() + ".pdf";
        Files.copy(archivo.getInputStream(), ROOT.resolve(nombre));
        return nombre;
    }

    public byte[] leer(String nombreArchivo) throws IOException {
        return Files.readAllBytes(ROOT.resolve(nombreArchivo));
    }

}
