package com.juan.estevez.sistemapagos.mapper;

import org.springframework.stereotype.Component;

import com.juan.estevez.sistemapagos.dtos.response.EstudianteResponse;
import com.juan.estevez.sistemapagos.entities.Estudiante;

@Component
public class EstudianteMapper {

    public EstudianteResponse toResponse(Estudiante e) {
        return EstudianteResponse.builder()
                .codigo(e.getCodigo())
                .nombre(e.getNombre())
                .apellido(e.getApellido())
                .programaId(e.getProgramaId())
                .build();
    }
}
