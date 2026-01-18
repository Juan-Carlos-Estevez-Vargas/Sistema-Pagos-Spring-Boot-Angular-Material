package com.juan.estevez.sistemapagos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.juan.estevez.sistemapagos.entities.Pago;
import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    List<Pago> buscarPorCodigoEstudiante(String codigoEstudiante);

    List<Pago> buscarPorEstado(EstadoPago estadoPago);

    List<Pago> buscarByTipoPago(TipoPago tipoPago);

}
