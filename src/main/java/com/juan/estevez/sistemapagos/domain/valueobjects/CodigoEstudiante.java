package com.juan.estevez.sistemapagos.domain.valueobjects;

import jakarta.validation.constraints.Pattern;

public record CodigoEstudiante(
        @Pattern(regexp = "^[A-Z]{2}\\d{5}$", message = "Formato inválido: AA12345") String valor)
        implements ValueObject {
    public CodigoEstudiante {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("Código de estudiante no puede ser nulo");
        }
        if (!valor.matches("^[A-Z]{2}\\d{5}$")) {
            throw new IllegalArgumentException("Formato de código inválido");
        }
    }
}