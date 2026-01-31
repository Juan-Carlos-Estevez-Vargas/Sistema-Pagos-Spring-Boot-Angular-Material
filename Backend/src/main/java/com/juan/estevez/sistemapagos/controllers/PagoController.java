package com.juan.estevez.sistemapagos.controllers;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.services.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@Tag(name = "Pagos", description = "Gestión de pagos")
public class PagoController {

    private final PagoService pagoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Crear pago")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'ESTUDIANTE')")
    public ResponseEntity<PagoResponse> crearPago(
            @Valid @RequestPart("data") PagoRequest request,
            @RequestPart("archivo") MultipartFile archivo,
            @AuthenticationPrincipal String username) {

        log.info("Creando pago para estudiante: {}", request.getCodigoEstudiante());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pagoService.crearPago(request, archivo, username));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener pago por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'ESTUDIANTE')")
    public ResponseEntity<PagoResponse> obtenerPago(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPago(id));
    }

    @GetMapping
    @Operation(summary = "Listar pagos")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS')")
    public ResponseEntity<Page<PagoResponse>> listarPagos(
            @PageableDefault(size = 20, sort = "fecha,desc") Pageable pageable) {
        return ResponseEntity.ok(pagoService.listarPagos(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar pagos")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS')")
    public ResponseEntity<Page<PagoResponse>> buscarPagos(
            @RequestParam(required = false) String codigoEstudiante,
            @RequestParam(required = false) EstadoPago estado,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @PageableDefault(size = 20, sort = "fecha,desc") Pageable pageable) {

        return ResponseEntity.ok(
                pagoService.buscarPagos(codigoEstudiante, estado, tipo, fechaDesde, fechaHasta, pageable));
    }

    @GetMapping("/estudiante/{codigo}")
    @Operation(summary = "Listar pagos por estudiante")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'ESTUDIANTE')")
    public ResponseEntity<Page<PagoResponse>> listarPagosPorEstudiante(
            @PathVariable String codigo,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(pagoService.listarPagosPorEstudiante(codigo, pageable));
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de pago")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS')")
    public ResponseEntity<PagoResponse> actualizarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPago estado,
            @RequestParam(required = false) String observaciones,
            @AuthenticationPrincipal String username) {

        return ResponseEntity.ok(pagoService.actualizarEstado(id, estado, observaciones, username));
    }

    @GetMapping("/{id}/archivo")
    @Operation(summary = "Descargar archivo de pago")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'ESTUDIANTE')")
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable Long id) {
        byte[] archivo = pagoService.descargarArchivo(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "pago_" + id + ".pdf");

        return new ResponseEntity<>(archivo, headers, HttpStatus.OK);
    }

    @GetMapping("/estudiante/{codigo}/total")
    @Operation(summary = "Obtener total pagado por estudiante")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'ESTUDIANTE')")
    public ResponseEntity<BigDecimal> obtenerTotalPagado(@PathVariable String codigo) {
        return ResponseEntity.ok(pagoService.obtenerTotalPagado(codigo));
    }
}
