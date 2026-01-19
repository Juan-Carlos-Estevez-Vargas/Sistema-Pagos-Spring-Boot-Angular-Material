package com.juan.estevez.sistemapagos.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.request.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.dtos.response.PagoResponse;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import com.juan.estevez.sistemapagos.mapper.PagoMapper;
import com.juan.estevez.sistemapagos.service.IPagoService;

import lombok.RequiredArgsConstructor;

/**
 * @author Juan Carlos Estevez Vargas
 */
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PagoController {

    private final IPagoService pagoService;
    private final PagoMapper mapper;

    @GetMapping
    public ResponseEntity<List<PagoResponse>> listar() {
        return ResponseEntity.ok(
                pagoService.obtenerTodos()
                        .stream()
                        .map(mapper::toResponse)
                        .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPorId(@PathVariable Long id) {
        Pago pago = pagoService.obtenerPorId(id);
        return ResponseEntity.ok(mapper.toResponse(pago)); // 200
    }

    @GetMapping("/estudiante/{codigo}")
    public List<Pago> listarPagosPorCodigoEstudiante(@PathVariable String codigoEstudiante) {
        return pagoService.obtenerPorCodigoEstudiante(codigoEstudiante);
    }

    @GetMapping("/estado")
    public List<Pago> obtenerPagosPorEstado(@RequestParam EstadoPago estadoPago) {
        return pagoService.obtenerPorEstado(estadoPago);
    }

    @GetMapping("/tipo")
    public List<Pago> listarPagosPorTipo(@RequestParam TipoPago tipoPago) {
        return pagoService.obtenerPorTipo(tipoPago);
    }

    @PutMapping("/pagos/{pagoId}/actualizarPago")
    public Pago actualizarEstadoPago(@RequestParam EstadoPago EstadoPago, @PathVariable Long pagoId) {
        return pagoService.actualizarEstadoPago(pagoId, EstadoPago);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PagoResponse> crear(
            @RequestParam MultipartFile file,
            @Validated NuevoPagoDTO dto) throws IOException {

        Pago pago = pagoService.guardarPago(file, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mapper.toResponse(pago)); // 201
    }

}
