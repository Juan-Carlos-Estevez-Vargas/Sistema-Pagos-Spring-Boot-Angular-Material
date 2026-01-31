package com.juan.estevez.sistemapagos.dto;

import com.juan.estevez.sistemapagos.enums.Rol;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UsuarioResponse {

    private String id;
    private String username;
    private String nombre;
    private String email;
    private Rol rol;
    private boolean activo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
