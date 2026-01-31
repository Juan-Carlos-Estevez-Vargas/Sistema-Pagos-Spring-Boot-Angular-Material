package com.juan.estevez.sistemapagos.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {

    String guardarArchivo(MultipartFile archivo) throws IOException;

    byte[] obtenerArchivo(String fileName) throws IOException;

    void eliminarArchivo(String fileName) throws IOException;

    String calcularHash(MultipartFile archivo) throws IOException;
}