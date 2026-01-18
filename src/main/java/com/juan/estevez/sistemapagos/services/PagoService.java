package com.juan.estevez.sistemapagos.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.repositories.EstudianteRepository;
import com.juan.estevez.sistemapagos.repositories.PagoRepository;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Service
@Transactional
public class PagoService {

    private final PagoRepository pagoRepository;
    private final EstudianteRepository estudianteRepository;

    public PagoService(PagoRepository pagoRepository, EstudianteRepository estudianteRepository) {
        this.pagoRepository = pagoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    public Pago guardarPago(MultipartFile archivo, NuevoPagoDTO pagoDTO) throws IOException {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }

        Estudiante estudiante = estudianteRepository.buscarPorCodigo(pagoDTO.getCodigoEstudiante());

        if (estudiante == null) {
            throw new IllegalArgumentException("Estudiante no encontrado");
        }

        Path directorioPagos = Paths.get(
                System.getProperty("user.home"),
                "enset-data",
                "pagos");

        Files.createDirectories(directorioPagos);

        String nombreArchivo = UUID.randomUUID() + ".pdf";
        Path rutaArchivo = directorioPagos.resolve(nombreArchivo);

        Files.copy(
                archivo.getInputStream(),
                rutaArchivo,
                StandardCopyOption.REPLACE_EXISTING);

        Pago pago = new Pago(pagoDTO);
        pago.setEstudiante(estudiante);
        pago.setArchivo(rutaArchivo.toString());

        return pagoRepository.save(pago);
    }

}
