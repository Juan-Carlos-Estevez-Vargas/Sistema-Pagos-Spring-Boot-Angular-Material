package com.juan.estevez.sistemapagos.services;

import com.juan.estevez.sistemapagos.dto.PagoRequest;
import com.juan.estevez.sistemapagos.dto.PagoResponse;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PagoService {

    PagoResponse crearPago(PagoRequest request, MultipartFile archivo, String username);

    PagoResponse actualizarEstado(Long id, EstadoPago estado, String observaciones, String username);

    PagoResponse obtenerPago(Long id);

    Page<PagoResponse> listarPagos(Pageable pageable);

    Page<PagoResponse> buscarPagos(String codigoEstudiante, EstadoPago estado,
            String tipo, LocalDate fechaDesde,
            LocalDate fechaHasta, Pageable pageable);

    Page<PagoResponse> listarPagosPorEstudiante(String codigo, Pageable pageable);

    byte[] descargarArchivo(Long id);

    BigDecimal obtenerTotalPagado(String codigoEstudiante);
}