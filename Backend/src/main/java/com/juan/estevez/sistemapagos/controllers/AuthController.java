package com.juan.estevez.sistemapagos.controllers;

import com.juan.estevez.sistemapagos.dto.LoginRequest;
import com.juan.estevez.sistemapagos.dto.LoginResponse;
import com.juan.estevez.sistemapagos.dto.UsuarioRequest;
import com.juan.estevez.sistemapagos.dto.UsuarioResponse;
import com.juan.estevez.sistemapagos.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Gestión de autenticación y usuarios")
public class AuthController {
    
    private final UsuarioService usuarioService;
    
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }
    
    @PostMapping("/registro")
    @Operation(summary = "Registrar usuario (solo admin)")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> registrarUsuario(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(usuarioService.crearUsuario(request));
    }
    
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil del usuario actual")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponse> obtenerPerfil(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(usuarioService.obtenerPerfil(username));
    }
}