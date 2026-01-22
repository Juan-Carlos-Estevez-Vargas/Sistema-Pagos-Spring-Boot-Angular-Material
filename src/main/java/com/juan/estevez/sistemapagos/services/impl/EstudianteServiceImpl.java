package com.juan.estevez.sistemapagos.services.impl;

import com.juan.estevez.sistemapagos.dto.*;
import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.entities.Programa;
import com.juan.estevez.sistemapagos.exceptions.BadRequestException;
import com.juan.estevez.sistemapagos.exceptions.NotFoundException;
import com.juan.estevez.sistemapagos.repositories.EstudianteRepository;
import com.juan.estevez.sistemapagos.repositories.ProgramaRepository;
import com.juan.estevez.sistemapagos.services.EstudianteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final ProgramaRepository programaRepository;

    @Override
    public EstudianteResponse crearEstudiante(EstudianteRequest request) {
        log.info("Creando estudiante: {}", request.getCodigo());

        // Validar código único
        if (estudianteRepository.existsByCodigo(request.getCodigo())) {
            throw new BadRequestException("El código de estudiante ya existe");
        }

        // Validar email único
        if (request.getEmail() != null && estudianteRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Obtener programa
        Programa programa = programaRepository.findById(request.getProgramaId())
                .orElseThrow(() -> new NotFoundException("Programa no encontrado"));

        // Crear estudiante
        Estudiante estudiante = Estudiante.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .codigo(request.getCodigo())
                .email(request.getEmail())
                .telefono(request.getTelefono())
                .programa(programa)
                .activo(request.isActivo())
                .build();

        estudiante = estudianteRepository.save(estudiante);
        log.info("Estudiante creado exitosamente: {}", estudiante.getId());

        return mapToResponse(estudiante);
    }

    @Override
    public EstudianteResponse actualizarEstudiante(String id, EstudianteRequest request) {
        log.info("Actualizando estudiante: {}", id);

        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        // Validar email único si cambia
        if (request.getEmail() != null && !request.getEmail().equals(estudiante.getEmail())) {
            if (estudianteRepository.existsByEmail(request.getEmail())) {
                throw new BadRequestException("El email ya está registrado");
            }
            estudiante.setEmail(request.getEmail());
        }

        // Actualizar otros campos
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());
        estudiante.setTelefono(request.getTelefono());
        estudiante.setActivo(request.isActivo());

        // Cambiar programa si es necesario
        if (!estudiante.getPrograma().getId().equals(request.getProgramaId())) {
            Programa programa = programaRepository.findById(request.getProgramaId())
                    .orElseThrow(() -> new NotFoundException("Programa no encontrado"));
            estudiante.setPrograma(programa);
        }

        estudiante = estudianteRepository.save(estudiante);
        log.info("Estudiante actualizado: {}", id);

        return mapToResponse(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponse obtenerEstudiantePorId(String id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        return mapToResponse(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponse obtenerEstudiantePorCodigo(String codigo) {
        Estudiante estudiante = estudianteRepository.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        return mapToResponse(estudiante);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstudianteResponse> listarEstudiantes(Pageable pageable) {
        return estudianteRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EstudianteResponse> buscarEstudiantes(String nombre, String apellido,
            String codigo, String programaId,
            Pageable pageable) {
        return estudianteRepository.buscarEstudiantes(nombre, apellido, codigo, programaId, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public void desactivarEstudiante(String id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        estudiante.setActivo(false);
        estudianteRepository.save(estudiante);
        log.info("Estudiante desactivado: {}", id);
    }

    @Override
    public void activarEstudiante(String id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Estudiante no encontrado"));

        estudiante.setActivo(true);
        estudianteRepository.save(estudiante);
        log.info("Estudiante activado: {}", id);
    }

    @Override
    public boolean existeEstudiante(String codigo) {
        return estudianteRepository.existsByCodigo(codigo);
    }

    private EstudianteResponse mapToResponse(Estudiante estudiante) {
        return EstudianteResponse.builder()
                .id(estudiante.getId())
                .nombre(estudiante.getNombre())
                .apellido(estudiante.getApellido())
                .codigo(estudiante.getCodigo())
                .email(estudiante.getEmail())
                .telefono(estudiante.getTelefono())
                .activo(estudiante.isActivo())
                .programaId(estudiante.getPrograma().getId())
                .programaNombre(estudiante.getPrograma().getNombre())
                .fechaRegistro(estudiante.getFechaRegistro())
                .pagos(estudiante.getPagos().stream()
                        .map(pago -> PagoResponse.builder()
                                .id(pago.getId())
                                .cantidad(pago.getCantidad())
                                .fecha(pago.getFecha())
                                .tipoPago(pago.getTipoPago())
                                .estadoPago(pago.getEstadoPago())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}