package com.juan.estevez.sistemapagos.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.juan.estevez.sistemapagos.dtos.request.NuevoPagoDTO;
import com.juan.estevez.sistemapagos.entities.Estudiante;
import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import com.juan.estevez.sistemapagos.exception.ResourceNotFoundException;
import com.juan.estevez.sistemapagos.repository.EstudianteRepository;
import com.juan.estevez.sistemapagos.repository.PagoRepository;
import com.juan.estevez.sistemapagos.service.IFileStorageService;
import com.juan.estevez.sistemapagos.service.IPagoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoService implements IPagoService {

    private final PagoRepository pagoRepository;
    private final EstudianteRepository estudianteRepository;
    private final IFileStorageService storageService;

    public Pago guardarPago(MultipartFile archivo, NuevoPagoDTO dto) throws IOException {
        Estudiante estudiante = estudianteRepository.findByCodigo(dto.getCodigoEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));

        Pago pago = Pago.builder()
                .fecha(dto.getFecha())
                .cantidad(dto.getCantidad())
                .tipoPago(dto.getTipoPago())
                .estadoPago(EstadoPago.CREADO)
                .archivo(storageService.guardar(archivo))
                .estudiante(estudiante)
                .build();

        return pagoRepository.save(pago);
    }

    public Pago obtenerPorId(Long id) {
        return pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado"));
    }

    public List<Pago> obtenerTodos() {
        return pagoRepository.findAll();
    }

    public List<Pago> obtenerPorCodigoEstudiante(String codigo) {
        return pagoRepository.findByEstudianteCodigo(codigo);
    }

    public Pago actualizarEstadoPago(Long id, EstadoPago estadoPago) {
        Pago pago = obtenerPorId(id);
        pago.setEstadoPago(estadoPago);
        return pago;
    }

    public byte[] obtenerArchivo(Long id) throws IOException {
        return storageService.leer(obtenerPorId(id).getArchivo());
    }

    public List<Pago> obtenerPorEstado(EstadoPago estadoPago) {
        return pagoRepository.findByEstadoPago(estadoPago);
    }

    public List<Pago> obtenerPorTipo(TipoPago tipoPago) {
        return pagoRepository.findByTipoPago(tipoPago);
    }

}
