package com.juan.estevez.sistemapagos.repositories;

import com.juan.estevez.sistemapagos.entities.Programa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgramaRepository extends JpaRepository<Programa, String> {

    Optional<Programa> findByCodigo(String codigo);

    List<Programa> findByActivo(boolean activo);
}