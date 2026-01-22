package com.juan.estevez.sistemapagos.entities;

import com.juan.estevez.sistemapagos.enums.EstadoPago;
import com.juan.estevez.sistemapagos.enums.TipoPago;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPago tipoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    @Column(length = 500)
    private String observaciones;

    @Column(nullable = false)
    private String archivoNombre;

    @Column(nullable = false)
    private String archivoHash;

    @Column(nullable = false)
    private String archivoTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario procesadoPor;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaProcesamiento;

    @PrePersist
    protected void onCreate() {
        if (fecha == null) {
            fecha = LocalDate.now();
        }
        if (estadoPago == null) {
            estadoPago = EstadoPago.PENDIENTE;
        }
    }
}