package com.juan.estevez.sistemapagos.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.exception.ResourceNotFoundException;
import com.juan.estevez.sistemapagos.repository.EstudianteRepository;
import com.juan.estevez.sistemapagos.service.IEstudianteService;

import lombok.RequiredArgsConstructor;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EstudianteService implements IEstudianteService {

    private EstudianteRepository estudianteRepository;

    public List<Estudiante> obtenerListadoEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtenerEstudiantePorCodigo(String codigoEstudiante) {
        return estudianteRepository.findByCodigo(codigoEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
    }

    public List<Estudiante> obtenerEstudiantesPorPrograma(String programaId) {
        return estudianteRepository.findByProgramaId(programaId);
    }
}
