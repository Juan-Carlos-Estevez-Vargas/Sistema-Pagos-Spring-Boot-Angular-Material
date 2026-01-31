package com.juan.estevez.sistemapagos.controllers;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.services.ProgramaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/programas")
@RequiredArgsConstructor
@Tag(name = "Programas", description = "Gestión de programas académicos")
public class ProgramaController {

    private final ProgramaService programaService;

    @PostMapping
    @Operation(summary = "Crear programa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProgramaResponse> crearPrograma(@Valid @RequestBody ProgramaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(programaService.crearPrograma(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar programa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProgramaResponse> actualizarPrograma(
            @PathVariable String id,
            @Valid @RequestBody ProgramaRequest request) {
        return ResponseEntity.ok(programaService.actualizarPrograma(id, request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener programa por ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<ProgramaResponse> obtenerPrograma(@PathVariable String id) {
        return ResponseEntity.ok(programaService.obtenerPrograma(id));
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Obtener programa por código")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<ProgramaResponse> obtenerProgramaPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(programaService.obtenerProgramaPorCodigo(codigo));
    }

    @GetMapping
    @Operation(summary = "Listar todos los programas")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR')")
    public ResponseEntity<List<ProgramaResponse>> listarProgramas() {
        return ResponseEntity.ok(programaService.listarProgramas());
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar programas activos")
    @PreAuthorize("hasAnyRole('ADMIN', 'FINANZAS', 'PROFESOR', 'ESTUDIANTE')")
    public ResponseEntity<List<ProgramaResponse>> listarProgramasActivos() {
        return ResponseEntity.ok(programaService.listarProgramasActivos());
    }

    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar programa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarPrograma(@PathVariable String id) {
        programaService.desactivarPrograma(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar programa")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activarPrograma(@PathVariable String id) {
        programaService.activarPrograma(id);
        return ResponseEntity.noContent().build();
    }
}