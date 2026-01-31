package com.juan.estevez.sistemapagos.services;

import com.juan.estevez.sistemapagos.dto.*;

import java.util.List;

public interface ProgramaService {

    ProgramaResponse crearPrograma(ProgramaRequest request);

    ProgramaResponse actualizarPrograma(String id, ProgramaRequest request);

    ProgramaResponse obtenerPrograma(String id);

    ProgramaResponse obtenerProgramaPorCodigo(String codigo);

    List<ProgramaResponse> listarProgramas();

    List<ProgramaResponse> listarProgramasActivos();

    void desactivarPrograma(String id);

    void activarPrograma(String id);
}