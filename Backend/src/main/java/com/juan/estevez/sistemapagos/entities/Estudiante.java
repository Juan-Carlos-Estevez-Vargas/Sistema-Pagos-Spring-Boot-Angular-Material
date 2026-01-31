package com.juan.estevez.sistemapagos.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellido;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(unique = true, length = 50)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(nullable = false)
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "programa_id", nullable = false)
    private Programa programa;

    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    @Builder.Default
    @OrderBy("fecha DESC")
    private List<Pago> pagos = new ArrayList<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime fechaRegistro;
    
}