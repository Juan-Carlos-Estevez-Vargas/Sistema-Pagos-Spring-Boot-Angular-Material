package com.juan.estevez.sistemapagos.controllers;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.services.EstudianteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "Gestión de estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    @PostMapping
    @Operation(summary = "Crear estudiante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponse> crearEstudiante(
            @Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(estudianteService.crearEstudiante(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar estudiante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponse> actualizarEstudiante(
            @PathVariable String id,
            @Valid @RequestBody EstudianteRequest request) {
        return ResponseEntity.ok(estudianteService.actualizarEstudiante(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estudiante por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<EstudianteResponse> obtenerEstudiante(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantePorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener estudiante por código")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR', 'ESTUDIANTE')")
    public ResponseEntity<EstudianteResponse> obtenerEstudiantePorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(estudianteService.obtenerEstudiantePorCodigo(codigo));
    }

    @GetMapping
    @Operation(summary = "Listar estudiantes")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<Page<EstudianteResponse>> listarEstudiantes(
            @PageableDefault(size = 20, sort = "apellido,nombre") Pageable pageable) {
        return ResponseEntity.ok(estudianteService.listarEstudiantes(pageable));
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar estudiantes")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<Page<EstudianteResponse>> buscarEstudiantes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String programaId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(
                estudianteService.buscarEstudiantes(nombre, apellido, codigo, programaId, pageable));
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar estudiante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarEstudiante(@PathVariable String id) {
        estudianteService.desactivarEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar estudiante")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activarEstudiante(@PathVariable String id) {
        estudianteService.activarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}