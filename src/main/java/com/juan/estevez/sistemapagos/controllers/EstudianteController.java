package com.juan.estevez.sistemapagos.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.service.IEstudianteService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Juan Carlos Estevez Vargas
 */
@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EstudianteController {

    private IEstudianteService estudianteService;

    @GetMapping
    public List<Estudiante> listar() {
        return estudianteService.obtenerListadoEstudiantes();
    }

    @GetMapping("/{codigo}")
    public Estudiante obtener(@PathVariable String codigo) {
        return estudianteService.obtenerEstudiantePorCodigo(codigo);
    }

    @GetMapping("/programa/{programaId}")
    public List<Estudiante> listarEstudiantesPorPrograma(@RequestParam String programaId) {
        return estudianteService.obtenerEstudiantesPorPrograma(programaId);
    }

}
