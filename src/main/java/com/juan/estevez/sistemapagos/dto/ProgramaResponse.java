package com.juan.estevez.sistemapagos.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProgramaResponse {

    private String id;
    private String nombre;
    private String codigo;
    private boolean activo;
    private int totalEstudiantes;
    private List<EstudianteSimpleResponse> estudiantes;
}
