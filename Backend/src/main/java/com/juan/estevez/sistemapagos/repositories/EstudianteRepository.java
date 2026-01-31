package com.juan.estevez.sistemapagos.repositories;

import com.juan.estevez.sistemapagos.entities.Estudiante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, String> {

    Optional<Estudiante> findByCodigo(String codigo);

    Optional<Estudiante> findByEmail(String email);

    Page<Estudiante> findByProgramaId(String programaId, Pageable pageable);

    Page<Estudiante> findByActivo(boolean activo, Pageable pageable);

    @Query("SELECT e FROM Estudiante e WHERE " +
            "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
            "(:apellido IS NULL OR LOWER(e.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))) AND " +
            "(:codigo IS NULL OR LOWER(e.codigo) LIKE LOWER(CONCAT('%', :codigo, '%'))) AND " +
            "(:programaId IS NULL OR e.programa.id = :programaId)")
    Page<Estudiante> buscarEstudiantes(
            @Param("nombre") String nombre,
            @Param("apellido") String apellido,
            @Param("codigo") String codigo,
            @Param("programaId") String programaId,
            Pageable pageable);

    boolean existsByCodigo(String codigo);

    boolean existsByEmail(String email);
}
