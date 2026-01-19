package com.juan.estevez.sistemapagos.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EstudianteResponse {
    private String codigo;
    private String nombre;
    private String apellido;
    private String programaId;
}
