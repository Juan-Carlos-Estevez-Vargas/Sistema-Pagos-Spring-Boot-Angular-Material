package com.juan.estevez.sistemapagos.services;

import com.juan.estevez.sistemapagos.dto.LoginRequest;
import com.juan.estevez.sistemapagos.dto.LoginResponse;
import com.juan.estevez.sistemapagos.dto.UsuarioRequest;
import com.juan.estevez.sistemapagos.dto.UsuarioResponse;

public interface UsuarioService {

    LoginResponse login(LoginRequest request);

    UsuarioResponse crearUsuario(UsuarioRequest request);

    void cambiarPassword(String username, String nuevaPassword);

    void desactivarUsuario(String id);

    void activarUsuario(String id);

    UsuarioResponse obtenerPerfil(String username);
}