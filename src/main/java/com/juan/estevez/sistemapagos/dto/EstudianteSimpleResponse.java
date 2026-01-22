package com.juan.estevez.sistemapagos.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteSimpleResponse {

    private String id;
    private String nombre;
    private String apellido;
    private String codigo;
    private boolean activo;
}