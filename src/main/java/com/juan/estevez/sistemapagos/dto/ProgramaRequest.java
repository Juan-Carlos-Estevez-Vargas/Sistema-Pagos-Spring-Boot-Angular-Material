package com.juan.estevez.sistemapagos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProgramaRequest {

    @NotBlank(message = "El ID es requerido")
    @Size(min = 2, max = 20, message = "El ID debe tener entre 2 y 20 caracteres")
    private String id;

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @Size(max = 10, message = "El código no puede exceder 10 caracteres")
    private String codigo;

    private boolean activo;
}