package com.juan.estevez.sistemapagos.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class EstudianteResponse {

    private String id;
    private String nombre;
    private String apellido;
    private String codigo;
    private String email;
    private String telefono;
    private boolean activo;
    private String programaId;
    private String programaNombre;
    private LocalDateTime fechaRegistro;
    private List<PagoResponse> pagos;
}
