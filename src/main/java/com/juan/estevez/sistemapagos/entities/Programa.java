package com.juan.estevez.sistemapagos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "programas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Programa {

    @Id
    @Column(length = 20)
    private String id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 10)
    private String codigo;

    @Column(nullable = false)
    private boolean activo;

    @OneToMany(mappedBy = "programa", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Estudiante> estudiantes = new ArrayList<>();
}
