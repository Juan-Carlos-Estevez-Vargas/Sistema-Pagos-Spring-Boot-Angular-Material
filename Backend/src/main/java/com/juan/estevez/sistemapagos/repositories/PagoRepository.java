package com.juan.estevez.sistemapagos.repositories;

import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    Page<Pago> findByEstudianteCodigo(String codigo, Pageable pageable);

    Page<Pago> findByEstadoPago(EstadoPago estado, Pageable pageable);

    Page<Pago> findByTipoPago(TipoPago tipo, Pageable pageable);

    @Query("SELECT p FROM Pago p WHERE " +
            "p.estudiante.codigo = :codigo AND " +
            "YEAR(p.fecha) = YEAR(CURRENT_DATE) AND " +
            "MONTH(p.fecha) = MONTH(CURRENT_DATE)")
    List<Pago> findPagosMesActual(@Param("codigo") String codigo);

    @Query("SELECT SUM(p.cantidad) FROM Pago p WHERE " +
            "p.estudiante.codigo = :codigo AND " +
            "p.estadoPago = 'APROBADO'")
    BigDecimal sumPagosAprobadosPorEstudiante(@Param("codigo") String codigo);

    @Query("SELECT p FROM Pago p WHERE " +
            "(:codigoEstudiante IS NULL OR p.estudiante.codigo = :codigoEstudiante) AND " +
            "(:estado IS NULL OR p.estadoPago = :estado) AND " +
            "(:tipo IS NULL OR p.tipoPago = :tipo) AND " +
            "(:fechaDesde IS NULL OR p.fecha >= :fechaDesde) AND " +
            "(:fechaHasta IS NULL OR p.fecha <= :fechaHasta)")
    Page<Pago> buscarPagos(
            @Param("codigoEstudiante") String codigoEstudiante,
            @Param("estado") EstadoPago estado,
            @Param("tipo") TipoPago tipo,
            @Param("fechaDesde") LocalDate fechaDesde,
            @Param("fechaHasta") LocalDate fechaHasta,
            Pageable pageable);
}