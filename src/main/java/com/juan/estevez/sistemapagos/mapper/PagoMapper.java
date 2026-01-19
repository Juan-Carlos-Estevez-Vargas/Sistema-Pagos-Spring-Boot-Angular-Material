package com.juan.estevez.sistemapagos.mapper;

import org.springframework.stereotype.Component;

import com.juan.estevez.sistemapagos.dtos.response.PagoResponse;
import com.juan.estevez.sistemapagos.entities.Pago;

@Component
public class PagoMapper {

    public PagoResponse toResponse(Pago pago) {
        return PagoResponse.builder()
                .id(pago.getId())
                .cantidad(pago.getCantidad())
                .fecha(pago.getFecha())
                .estadoPago(pago.getEstadoPago())
                .tipoPago(pago.getTipoPago())
                .codigoEstudiante(pago.getEstudiante().getCodigo())
                .build();
    }
}
