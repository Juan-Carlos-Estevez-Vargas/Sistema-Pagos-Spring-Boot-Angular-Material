package com.juan.estevez.sistemapagos.services.impl;

import com.juan.estevez.sistemapagos.dto.PagoRequest;
import com.juan.estevez.sistemapagos.dto.PagoResponse;
import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.entities.Usuario;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import com.juan.estevez.sistemapagos.exceptions.BadRequestException;
import com.juan.estevez.sistemapagos.exceptions.NotFoundException;
import com.juan.estevez.sistemapagos.repositories.EstudianteRepository;
import com.juan.estevez.sistemapagos.repositories.PagoRepository;
import com.juan.estevez.sistemapagos.repositories.UsuarioRepository;
import com.juan.estevez.sistemapagos.services.FileStorageService;
import com.juan.estevez.sistemapagos.services.PagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final EstudianteRepository estudianteRepository;
    private final UsuarioRepository usuarioRepository;
    private final FileStorageService fileStorageService;

    @Override
    public PagoResponse crearPago(PagoRequest request, MultipartFile archivo, String username) {
        log.info("Creando pago para estudiante: {}", request.getCodigoEstudiante());

        // Validar estudiante
        Estudiante estudiante = estudianteRepository.findByCodigo(request.getCodigoEstudiante())
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        if (!estudiante.isActivo()) {
            throw new BadRequestException("El estudiante no está activo");
        }

        // Validar límite de pagos mensuales
        validarLimitePagosMensuales(estudiante.getCodigo());

        // Validar archivo
        validarArchivo(archivo);

        // Guardar archivo
        String archivoNombre = "";
        try {
            archivoNombre = fileStorageService.guardarArchivo(archivo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String archivoHash = "";
        try {
            archivoHash = fileStorageService.calcularHash(archivo);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // Crear pago
        Pago pago = Pago.builder()
                .fecha(request.getFecha())
                .cantidad(request.getCantidad())
                .tipoPago(request.getTipoPago())
                .estadoPago(EstadoPago.PENDIENTE)
                .observaciones(request.getObservaciones())
                .archivoNombre(archivoNombre)
                .archivoHash(archivoHash)
                .archivoTipo(archivo.getContentType())
                .estudiante(estudiante)
                .build();

        pago = pagoRepository.save(pago);
        log.info("Pago creado exitosamente: {}", pago.getId());

        return mapToResponse(pago);
    }

    @Override
    public PagoResponse actualizarEstado(Long id, EstadoPago estado, String observaciones, String username) {
        log.info("Actualizando estado de pago {} a {}", id, estado);

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        // Validar transición de estado
        validarTransicionEstado(pago.getEstadoPago(), estado);

        // Actualizar estado
        pago.setEstadoPago(estado);
        pago.setObservaciones(observaciones);

        if (estado == EstadoPago.APROBADO || estado == EstadoPago.RECHAZADO) {
            Usuario usuario = usuarioRepository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
            pago.setProcesadoPor(usuario);
            pago.setFechaProcesamiento(LocalDateTime.now());
        }

        pago = pagoRepository.save(pago);
        log.info("Estado de pago actualizado: {}", id);

        return mapToResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public PagoResponse obtenerPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        return mapToResponse(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listarPagos(Pageable pageable) {
        return pagoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> buscarPagos(String codigoEstudiante, EstadoPago estado,
            String tipo, LocalDate fechaDesde,
            LocalDate fechaHasta, Pageable pageable) {

        TipoPago tipoPago = tipo != null ? TipoPago.valueOf(tipo) : null;

        return pagoRepository.buscarPagos(
                codigoEstudiante, estado, tipoPago, fechaDesde, fechaHasta, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listarPagosPorEstudiante(String codigo, Pageable pageable) {
        return pagoRepository.findByEstudianteCodigo(codigo, pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] descargarArchivo(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

        try {
            return fileStorageService.obtenerArchivo(pago.getArchivoNombre());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalPagado(String codigoEstudiante) {
        BigDecimal total = pagoRepository.sumPagosAprobadosPorEstudiante(codigoEstudiante);
        return total != null ? total : BigDecimal.ZERO;
    }

    private void validarLimitePagosMensuales(String codigoEstudiante) {
        List<Pago> pagosMesActual = pagoRepository.findPagosMesActual(codigoEstudiante);

        if (pagosMesActual.size() >= 10) {
            throw new BadRequestException("Límite de 10 pagos mensuales alcanzado");
        }

        BigDecimal totalMes = pagosMesActual.stream()
                .map(Pago::getCantidad)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalMes.compareTo(new BigDecimal("50000")) >= 0) {
            throw new BadRequestException("Límite mensual de $50,000 alcanzado");
        }
    }

    private void validarArchivo(MultipartFile archivo) {
        if (archivo.isEmpty()) {
            throw new BadRequestException("El archivo no puede estar vacío");
        }

        if (archivo.getSize() > 10 * 1024 * 1024) { // 10MB
            throw new BadRequestException("El archivo no puede exceder 10MB");
        }

        String contentType = archivo.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.startsWith("image/"))) {
            throw new BadRequestException("Solo se permiten archivos PDF e imágenes");
        }
    }

    private void validarTransicionEstado(EstadoPago actual, EstadoPago nuevo) {
        if (actual == EstadoPago.APROBADO || actual == EstadoPago.RECHAZADO) {
            throw new BadRequestException("No se puede cambiar el estado de un pago ya procesado");
        }

        if (actual == EstadoPago.CANCELADO) {
            throw new BadRequestException("No se puede cambiar el estado de un pago cancelado");
        }
    }

    private PagoResponse mapToResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .cantidad(pago.getCantidad())
                .fecha(pago.getFecha())
                .tipoPago(pago.getTipoPago())
                .estadoPago(pago.getEstadoPago())
                .observaciones(pago.getObservaciones())
                .archivoNombre(pago.getArchivoNombre())
                .fechaCreacion(pago.getFechaCreacion())
                .estudianteNombre(pago.getEstudiante().getNombre() + " " + pago.getEstudiante().getApellido())
                .estudianteCodigo(pago.getEstudiante().getCodigo())
                .programaNombre(pago.getEstudiante().getPrograma().getNombre())
                .procesadoPor(pago.getProcesadoPor() != null ? pago.getProcesadoPor().getNombre() : null)
                .build();
    }
}