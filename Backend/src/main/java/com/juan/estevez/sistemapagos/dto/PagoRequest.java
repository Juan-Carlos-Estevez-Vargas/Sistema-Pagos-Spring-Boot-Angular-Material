package com.juan.estevez.sistemapagos.dto;

import com.juan.estevez.sistemapagos.enums.TipoPago;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PagoRequest {

    @NotNull(message = "La cantidad es requerida")
    @DecimalMin(value = "0.01", message = "La cantidad debe ser mayor a 0")
    private BigDecimal cantidad;

    @NotNull(message = "El tipo de pago es requerido")
    private TipoPago tipoPago;

    @NotNull(message = "La fecha es requerida")
    private LocalDate fecha;

    @NotBlank(message = "El código del estudiante es requerido")
    @Size(min = 5, max = 20, message = "El código debe tener entre 5 y 20 caracteres")
    private String codigoEstudiante;

    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}
