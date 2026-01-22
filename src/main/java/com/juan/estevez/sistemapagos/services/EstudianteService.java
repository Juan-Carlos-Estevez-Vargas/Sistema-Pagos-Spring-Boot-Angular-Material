package com.juan.estevez.sistemapagos.services;

import com.juan.estevez.sistemapagos.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstudianteService {

    EstudianteResponse crearEstudiante(EstudianteRequest request);

    EstudianteResponse actualizarEstudiante(String id, EstudianteRequest request);

    EstudianteResponse obtenerEstudiantePorId(String id);

    EstudianteResponse obtenerEstudiantePorCodigo(String codigo);

    Page<EstudianteResponse> listarEstudiantes(Pageable pageable);

    Page<EstudianteResponse> buscarEstudiantes(String nombre, String apellido,
            String codigo, String programaId,
            Pageable pageable);

    void desactivarEstudiante(String id);

    void activarEstudiante(String id);

    boolean existeEstudiante(String codigo);
}