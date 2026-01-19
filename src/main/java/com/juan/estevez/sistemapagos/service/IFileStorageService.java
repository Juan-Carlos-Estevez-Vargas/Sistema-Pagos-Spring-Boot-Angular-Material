package com.juan.estevez.sistemapagos.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IFileStorageService {

    String guardar(MultipartFile archivo) throws IOException;

    byte[] leer(String nombreArchivo) throws IOException;

}
