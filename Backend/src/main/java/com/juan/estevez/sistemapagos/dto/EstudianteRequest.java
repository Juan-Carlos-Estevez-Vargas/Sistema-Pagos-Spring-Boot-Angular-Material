package com.juan.estevez.sistemapagos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class EstudianteRequest {

    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El código es requerido")
    @Size(min = 5, max = 20, message = "El código debe tener entre 5 y 20 caracteres")
    private String codigo;

    @Email(message = "El email debe ser válido")
    @Size(max = 50, message = "El email no puede exceder 50 caracteres")
    private String email;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String telefono;

    @NotBlank(message = "El programa es requerido")
    private String programaId;

    private boolean activo;
}