package com.juan.estevez.sistemapagos.services.impl;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.entities.Programa;
import com.juan.estevez.sistemapagos.exceptions.BadRequestException;
import com.juan.estevez.sistemapagos.exceptions.NotFoundException;
import com.juan.estevez.sistemapagos.repositories.ProgramaRepository;
import com.juan.estevez.sistemapagos.services.ProgramaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgramaServiceImpl implements ProgramaService {

    private final ProgramaRepository programaRepository;

    @Override
    public ProgramaResponse crearPrograma(ProgramaRequest request) {
        log.info("Creando programa: {}", request.getNombre());

        // Validar que el ID no exista
        if (programaRepository.existsById(request.getId())) {
            throw new BadRequestException("Ya existe un programa con este ID");
        }

        // Validar que el código sea único si se proporciona
        if (request.getCodigo() != null && !request.getCodigo().isBlank()) {
            programaRepository.findByCodigo(request.getCodigo())
                    .ifPresent(p -> {
                        throw new BadRequestException("Ya existe un programa con este código");
                    });
        }

        // Crear programa
        Programa programa = Programa.builder()
                .id(request.getId())
                .nombre(request.getNombre())
                .codigo(request.getCodigo())
                .activo(request.isActivo())
                .build();

        programa = programaRepository.save(programa);
        log.info("Programa creado exitosamente: {}", programa.getId());

        return mapToResponse(programa);
    }

    @Override
    public ProgramaResponse actualizarPrograma(String id, ProgramaRequest request) {
        log.info("Actualizando programa: {}", id);

        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        // Validar que el código sea único si cambia
        if (request.getCodigo() != null && !request.getCodigo().isBlank()
                && !request.getCodigo().equals(programa.getCodigo())) {
            programaRepository.findByCodigo(request.getCodigo())
                    .ifPresent(p -> {
                        throw new BadRequestException("Ya existe un programa con este código");
                    });
        }

        // Actualizar campos
        programa.setNombre(request.getNombre());
        programa.setCodigo(request.getCodigo());
        programa.setActivo(request.isActivo());

        programa = programaRepository.save(programa);
        log.info("Programa actualizado: {}", id);

        return mapToResponse(programa);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramaResponse obtenerPrograma(String id) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        return mapToResponse(programa);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgramaResponse obtenerProgramaPorCodigo(String codigo) {
        Programa programa = programaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        return mapToResponse(programa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaResponse> listarProgramas() {
        return programaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramaResponse> listarProgramasActivos() {
        return programaRepository.findByActivo(true).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void desactivarPrograma(String id) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        programa.setActivo(false);
        programaRepository.save(programa);
        log.info("Programa desactivado: {}", id);
    }

    @Override
    public void activarPrograma(String id) {
        Programa programa = programaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        programa.setActivo(true);
        programaRepository.save(programa);
        log.info("Programa activado: {}", id);
    }

    private ProgramaResponse mapToResponse(Programa programa) {
        return ProgramaResponse.builder()
            .id(programa.getId())
            .nombre(programa.getNombre())
            .codigo(programa.getCodigo())
            .activo(programa.isActivo())
            .totalEstudiantes(programa.getEstudiantes().size())
            .estudiantes(programa.getEstudiantes().stream()
                .map(e -> EstudianteSimpleResponse.builder()
                    .id(e.getId())
                    .nombre(e.getNombre())
                    .apellido(e.getApellido())
                    .codigo(e.getCodigo())
                    .activo(e.isActivo())
                    .build())
                .collect(Collectors.toList()))
            .build();
    }
}