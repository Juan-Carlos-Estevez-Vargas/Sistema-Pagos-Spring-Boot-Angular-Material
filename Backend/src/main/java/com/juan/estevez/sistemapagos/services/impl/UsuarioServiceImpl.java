package com.juan.estevez.sistemapagos.services.impl;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.entities.Usuario;
import com.juan.estevez.sistemapagos.enums.Rol;
import com.juan.estevez.sistemapagos.exceptions.BadRequestException;
import com.juan.estevez.sistemapagos.exceptions.NotFoundException;
import com.juan.estevez.sistemapagos.repositories.UsuarioRepository;
import com.juan.estevez.sistemapagos.security.JwtTokenProvider;
import com.juan.estevez.sistemapagos.services.UsuarioService;
import com.juan.estevez.sistemapagos.utils.DateConverter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("Intentando login para usuario: {}", request.getUsername());

        // Autenticar usuario
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Obtener usuario
        Usuario usuario = usuarioRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("Credenciales inválidas"));

        // Verificar que esté activo
        if (!usuario.isActivo()) {
            throw new BadRequestException("Usuario desactivado");
        }

        // Generar token JWT
        String token = jwtTokenProvider.generateToken(authentication);

        log.info("Login exitoso para usuario: {}", request.getUsername());

        return LoginResponse.builder()
                .token(token)
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol().name())
                .fechaExpiracion(DateConverter.toLocalDateTime(jwtTokenProvider.getExpirationDateFromToken(token)))
                .build();
    }

    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest request) {
        log.info("Creando usuario: {}", request.getUsername());

        // Validar username único
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("El username ya está registrado");
        }

        // Validar email único
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Validar que solo ADMIN pueda crear otros ADMIN
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (request.getRol() == Rol.ADMIN && !isAdmin) {
            throw new BadRequestException("No tiene permisos para crear usuarios ADMIN");
        }

        // Crear usuario
        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .activo(request.isActivo())
                .build();

        usuario = usuarioRepository.save(usuario);
        log.info("Usuario creado exitosamente: {}", usuario.getId());

        return mapToResponse(usuario);
    }

    @Override
    public void cambiarPassword(String username, String nuevaPassword) {
        log.info("Cambiando password para usuario: {}", username);

        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(nuevaPassword));
        usuario.setFechaActualizacion(LocalDateTime.now());

        usuarioRepository.save(usuario);
        log.info("Password cambiado exitosamente para usuario: {}", username);
    }

    @Override
    public void desactivarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        // No permitir desactivarse a sí mismo
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        if (usuario.getUsername().equals(currentUsername)) {
            throw new BadRequestException("No puede desactivar su propio usuario");
        }

        usuario.setActivo(false);
        usuario.setFechaActualizacion(LocalDateTime.now());

        usuarioRepository.save(usuario);
        log.info("Usuario desactivado: {}", id);
    }

    @Override
    public void activarUsuario(String id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        usuario.setActivo(true);
        usuario.setFechaActualizacion(LocalDateTime.now());

        usuarioRepository.save(usuario);
        log.info("Usuario activado: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPerfil(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return mapToResponse(usuario);
    }

    private UsuarioResponse mapToResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .activo(usuario.isActivo())
                .fechaCreacion(usuario.getFechaCreacion())
                .fechaActualizacion(usuario.getFechaActualizacion())
                .build();
    }
}