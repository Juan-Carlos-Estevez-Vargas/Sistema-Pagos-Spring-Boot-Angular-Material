package com.juan.estevez.sistemapagos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.services.EstudianteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Juan Carlos Estevez Vargas
 */
@RestController
@CrossOrigin("*")
public class EstudianteController {

    private EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping("/estudiantes")
    public List<Estudiante> obtenerListadoEstudiantes() {
        return estudianteService.obtenerListadoEstudiantes();
    }

    @GetMapping("/estudiantes/{codigo}")
    public Estudiante listarEstudiantePorCodigo(@PathVariable String codigoEstudiante) {
        return estudianteService.obtenerEstudiantePorCodigo(codigoEstudiante);
    }

    @GetMapping("/estudiantes/programa")
    public List<Estudiante> listarEstudiantesPorPrograma(@RequestParam String programaId) {
        return estudianteService.obtenerEstudiantesPorPrograma(programaId);
    }

}
