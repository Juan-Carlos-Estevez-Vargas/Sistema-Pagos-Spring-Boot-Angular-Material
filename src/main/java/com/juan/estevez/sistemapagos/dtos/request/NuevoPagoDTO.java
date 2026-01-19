package com.juan.estevez.sistemapagos.dtos.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.juan.estevez.sistemapagos.enums.TipoPago;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NuevoPagoDTO {

    private BigDecimal cantidad;
    private TipoPago tipoPago;
    private LocalDate fecha;
    private String codigoEstudiante;

}
