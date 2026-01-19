package com.juan.estevez.sistemapagos.service;

import java.util.List;

import com.juan.estevez.sistemapagos.entities.Estudiante;

/**
 * @author Juan Carlos Estevez Vargas
 */
public interface IEstudianteService {

    List<Estudiante> obtenerListadoEstudiantes();

    Estudiante obtenerEstudiantePorCodigo(String codigoEstudiante);

    List<Estudiante> obtenerEstudiantesPorPrograma(String programaId);

}
