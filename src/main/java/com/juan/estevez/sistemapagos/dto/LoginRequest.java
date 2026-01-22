package com.juan.estevez.sistemapagos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginRequest {

    @NotBlank(message = "El username es requerido")
    private String username;

    @NotBlank(message = "La contraseña es requerida")
    private String password;
}