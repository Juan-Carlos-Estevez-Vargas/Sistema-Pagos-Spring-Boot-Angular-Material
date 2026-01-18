package com.juan.estevez.sistemapagos.entities;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Juan Carlos Estevez Vargas
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    private BigDecimal cantidad;
    private TipoPago tipoPago;
    private EstadoPago estadoPago;
    private String archivo;

    @ManyToOne
    private Estudiante estudiante;

}
