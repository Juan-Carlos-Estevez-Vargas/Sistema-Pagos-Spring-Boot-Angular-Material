package com.juan.estevez.sistemapagos.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juan.estevez.sistemapagos.entities.Estudiante;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {

    Estudiante buscarPorCodigo(String codigo);

    List<Estudiante> buscarPorProgramaID(String programaId);

}
