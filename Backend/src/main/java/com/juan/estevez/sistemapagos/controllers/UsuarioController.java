package com.juan.estevez.sistemapagos.controllers;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema")
public class UsuarioController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping
    @Operation(summary = "Crear nuevo usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioService.crearUsuario(request));
    }
    
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario actual")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(username));
    }
    
    @PatchMapping("/cambiar-password")
    @Operation(summary = "Cambiar contraseña del usuario actual")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> cambiarPassword(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal String username) {
        
        String nuevaPassword = request.get("nuevaPassword");
        if (nuevaPassword == null || nuevaPassword.length() < 6) {
            return ResponseEntity.badRequest().build();
        }
        
        usuarioService.cambiarPassword(username, nuevaPassword);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/desactivar")
    @Operation(summary = "Desactivar usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> desactivarUsuario(@PathVariable String id) {
        usuarioService.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/activar")
    @Operation(summary = "Activar usuario")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> activarUsuario(@PathVariable String id) {
        usuarioService.activarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}