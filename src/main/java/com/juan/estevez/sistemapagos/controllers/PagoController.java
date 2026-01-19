package com.juan.estevez.sistemapagos.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.juan.estevez.sistemapagos.services.EstudianteService;
import com.juan.estevez.sistemapagos.services.PagoService;

/**
 * @author Juan Carlos Estevez Vargas
 */
@RestController
@CrossOrigin("*")
public class PagoController {

    private PagoService pagoService;
    private EstudianteService estudianteService;

    public PagoController(PagoService pagoService, EstudianteService estudianteService) {
        this.pagoService = pagoService;
        this.estudianteService = estudianteService;
    }

}
