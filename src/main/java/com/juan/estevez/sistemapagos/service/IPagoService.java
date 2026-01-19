package com.juan.estevez.sistemapagos.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.request.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;

/**
 * @author Juan Carlos Estevez Vargas
 */
public interface IPagoService {

    Pago guardarPago(MultipartFile archivo, NuevoPagoDTO pagoDTO) throws IOException;

    Pago actualizarEstadoPago(Long pagoId, EstadoPago estadoPago);

    Pago obtenerPorId(Long idPago);

    List<Pago> obtenerTodos();

    List<Pago> obtenerPorCodigoEstudiante(String codigoEstudiante);

    List<Pago> obtenerPorEstado(EstadoPago estadoPago);

    List<Pago> obtenerPorTipo(TipoPago tipoPago);

}
