package com.juan.estevez.sistemapagos.services;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import com.juan.estevez.sistemapagos.repositories.EstudianteRepository;
import com.juan.estevez.sistemapagos.repositories.PagoRepository;

@Service
@Transactional
public class PagoService {

    private static final Path PAGOS_DIR = Paths.get(
            System.getProperty("user.home"),
            "enset-data",
            "pagos");

    private final PagoRepository pagoRepository;
    private final EstudianteRepository estudianteRepository;

    public PagoService(PagoRepository pagoRepository,
            EstudianteRepository estudianteRepository) {
        this.pagoRepository = pagoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    public Pago guardarPago(MultipartFile archivo, NuevoPagoDTO pagoDTO) throws IOException {
        validarArchivo(archivo);
        Estudiante estudiante = estudianteRepository.buscarPorCodigo(pagoDTO.getCodigoEstudiante());

        if (estudiante == null) {
            throw new IllegalArgumentException("Estudiante no encontrado");
        }

        Files.createDirectories(PAGOS_DIR);

        Path rutaArchivo = guardarArchivo(archivo);

        Pago pago = new Pago(pagoDTO);
        pago.setEstudiante(estudiante);
        pago.setArchivo(rutaArchivo.toUri().toString());

        return pagoRepository.save(pago);
    }

    public byte[] obtenerArchivoPorId(Long pagoId) throws IOException {
        Pago pago = obtenerPagoPorId(pagoId);
        return Files.readAllBytes(Path.of(URI.create(pago.getArchivo())));
    }

    public Pago actualizarEstadoPago(Long pagoId, EstadoPago estadoPago) {
        Pago pago = obtenerPagoPorId(pagoId);
        pago.setEstadoPago(estadoPago);
        return pagoRepository.save(pago);
    }

    public Pago obtenerPagoPorId(Long idPago) {
        return pagoRepository.findById(idPago).orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));
    }

    private Path guardarArchivo(MultipartFile archivo) throws IOException {
        String nombreArchivo = UUID.randomUUID() + ".pdf";
        Path rutaArchivo = PAGOS_DIR.resolve(nombreArchivo);

        Files.copy(
                archivo.getInputStream(),
                rutaArchivo,
                StandardCopyOption.REPLACE_EXISTING);

        return rutaArchivo;
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo es obligatorio");
        }
    }

    public List<Pago> obtenerPagos() {
        return pagoRepository.findAll();
    }

    public List<Pago> obtenerPagosPorCodigoEstudiante(String codigoEstudiante) {
        return pagoRepository.buscarPorCodigoEstudiante(codigoEstudiante);
    }

    public List<Pago> obtenerPagosPorEstado(EstadoPago estadoPago) {
        return pagoRepository.buscarPorEstado(estadoPago);
    }

    public List<Pago> obtenerPagosPorTipo(TipoPago tipoPago) {
        return pagoRepository.buscarByTipoPago(tipoPago);
    }
}
