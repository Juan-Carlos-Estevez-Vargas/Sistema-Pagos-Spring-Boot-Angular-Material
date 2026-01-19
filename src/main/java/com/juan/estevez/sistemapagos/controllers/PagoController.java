package com.juan.estevez.sistemapagos.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import com.juan.estevez.sistemapagos.services.PagoService;

/**
 * @author Juan Carlos Estevez Vargas
 */
@RestController
@CrossOrigin("*")
public class PagoController {

    private PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @GetMapping("/pagos")
    public List<Pago> listarPagos() {
        return pagoService.obtenerPagos();
    }

    @GetMapping("/pagos/{id}")
    public Pago listarPagoPorId(@PathVariable Long idPago) {
        return pagoService.obtenerPagoPorId(idPago);
    }

    @GetMapping("/estudiante/{codigo}/pagos")
    public List<Pago> listarPagosPorCodigoEstudiante(@PathVariable String codigoEstudiante) {
        return pagoService.obtenerPagosPorCodigoEstudiante(codigoEstudiante);
    }

    @GetMapping("/pagos/estado")
    public List<Pago> obtenerPagosPorEstado(@RequestParam EstadoPago estadoPago) {
        return pagoService.obtenerPagosPorEstado(estadoPago);
    }

    @GetMapping("/pagos/tipo")
    public List<Pago> listarPagosPorTipo(@RequestParam TipoPago tipoPago) {
        return pagoService.obtenerPagosPorTipo(tipoPago);
    }

    @PutMapping("/pagos/{pagoId}/actualizarPago")
    public Pago actualizarEstadoPago(@RequestParam EstadoPago EstadoPago, @PathVariable Long pagoId) {
        return pagoService.actualizarEstadoPago(pagoId, EstadoPago);
    }

    @PostMapping(path = "/pagos", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pago guardarPago(@RequestParam("file") MultipartFile archivo, NuevoPagoDTO pagoDTO) throws IOException {
        return pagoService.guardarPago(archivo, pagoDTO);
    }

    @GetMapping(value = "/pagos/archivo/{idPago}", produces = MediaType.APPLICATION_PDF_VALUE)
    public byte[] listarArchivoPorIdPago(@PathVariable Long idPago) throws IOException {
        return pagoService.obtenerArchivoPorId(idPago);
    }

}
