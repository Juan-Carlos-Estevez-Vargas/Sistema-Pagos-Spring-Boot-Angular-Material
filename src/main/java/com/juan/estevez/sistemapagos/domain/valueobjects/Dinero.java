package com.juan.estevez.sistemapagos.domain.valueobjects;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Getter
@EqualsAndHashCode
@ToString
public class Dinero implements ValueObject {

    private final BigDecimal cantidad;
    private final Currency moneda;
    private static final Currency MONEDA_DEFAULT = Currency.getInstance("MXN");

    public Dinero(BigDecimal cantidad) {
        this(cantidad, MONEDA_DEFAULT);
    }

    public Dinero(BigDecimal cantidad, Currency moneda) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.cantidad = cantidad.setScale(2);
        this.moneda = moneda;
    }

    public Dinero sumar(Dinero otro) {
        if (!this.moneda.equals(otro.moneda)) {
            throw new IllegalArgumentException("No se pueden sumar diferentes monedas");
        }
        return new Dinero(this.cantidad.add(otro.cantidad), this.moneda);
    }

    public Dinero multiplicar(BigDecimal factor) {
        return new Dinero(this.cantidad.multiply(factor), this.moneda);
    }

    public boolean esMayorQue(Dinero otro) {
        return this.cantidad.compareTo(otro.cantidad) > 0;
    }
}