package com.juan.estevez.sistemapagos.dtos.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoResponse {

    private Long id;
    private BigDecimal cantidad;
    private LocalDate fecha;
    private EstadoPago estadoPago;
    private TipoPago tipoPago;
    private String codigoEstudiante;
    
}
