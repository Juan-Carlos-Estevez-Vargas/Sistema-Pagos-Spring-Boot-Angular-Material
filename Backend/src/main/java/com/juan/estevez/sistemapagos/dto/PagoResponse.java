package com.juan.estevez.sistemapagos.dto;

import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class PagoResponse {

    private Long id;
    private BigDecimal cantidad;
    private LocalDate fecha;
    private TipoPago tipoPago;
    private EstadoPago estadoPago;
    private String observaciones;
    private String archivoNombre;
    private LocalDateTime fechaCreacion;
    private String estudianteNombre;
    private String estudianteCodigo;
    private String programaNombre;
    private String procesadoPor;
}