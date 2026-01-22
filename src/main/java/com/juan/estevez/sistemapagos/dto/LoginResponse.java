package com.juan.estevez.sistemapagos.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String username;
    private String nombre;
    private String email;
    private String rol;
    private LocalDateTime fechaExpiracion;
}