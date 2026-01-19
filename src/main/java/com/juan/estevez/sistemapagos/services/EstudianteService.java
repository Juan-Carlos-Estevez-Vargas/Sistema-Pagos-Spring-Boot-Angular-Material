package com.juan.estevez.sistemapagos.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.repositories.EstudianteRepository;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Service
@Transactional
public class EstudianteService {

    private EstudianteRepository estudianteRepository;

    public EstudianteService(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    public List<Estudiante> obtenerListadoEstudiantes() {
        return estudianteRepository.findAll();
    }

    public Estudiante obtenerEstudiantePorCodigo(String codigoEstudiante) {
        return estudianteRepository.buscarPorCodigo(codigoEstudiante);
    }

    public List<Estudiante> obtenerEstudiantesPorPrograma(String programaId) {
        return estudianteRepository.buscarPorProgramaID(programaId);
    }
}
