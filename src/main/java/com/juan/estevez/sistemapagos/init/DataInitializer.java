package com.juan.estevez.sistemapagos.init;

import com.juan.estevez.sistemapagos.entities.Programa;
import com.juan.estevez.sistemapagos.entities.Usuario;
import com.juan.estevez.sistemapagos.enums.Rol;
import com.juan.estevez.sistemapagos.repositories.ProgramaRepository;
import com.juan.estevez.sistemapagos.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final ProgramaRepository programaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear programas iniciales
        if (programaRepository.count() == 0) {
            log.info("Creando programas iniciales...");

            Programa ingenieria = Programa.builder()
                    .id("ING")
                    .nombre("Ingeniería de Sistemas")
                    .codigo("IS001")
                    .activo(true)
                    .build();

            Programa medicina = Programa.builder()
                    .id("MED")
                    .nombre("Medicina")
                    .codigo("MD001")
                    .activo(true)
                    .build();

            Programa derecho = Programa.builder()
                    .id("DER")
                    .nombre("Derecho")
                    .codigo("DR001")
                    .activo(true)
                    .build();

            programaRepository.save(ingenieria);
            programaRepository.save(medicina);
            programaRepository.save(derecho);

            log.info("Programas creados exitosamente");
        }

        // Crear usuarios iniciales
        if (usuarioRepository.count() == 0) {
            log.info("Creando usuarios iniciales...");

            Usuario admin = Usuario.builder()
                    .username("admin")
                    .nombre("Administrador")
                    .email("admin@sistema.com")
                    .password(passwordEncoder.encode("admin123"))
                    .rol(Rol.ADMIN)
                    .activo(true)
                    .build();

            Usuario finanzas = Usuario.builder()
                    .username("finanzas")
                    .nombre("Departamento de Finanzas")
                    .email("finanzas@sistema.com")
                    .password(passwordEncoder.encode("finanzas123"))
                    .rol(Rol.FINANZAS)
                    .activo(true)
                    .build();

            Usuario profesor = Usuario.builder()
                    .username("profesor")
                    .nombre("Juan Pérez")
                    .email("profesor@sistema.com")
                    .password(passwordEncoder.encode("profesor123"))
                    .rol(Rol.PROFESOR)
                    .activo(true)
                    .build();

            Usuario estudiante = Usuario.builder()
                    .username("estudiante")
                    .nombre("Carlos Gómez")
                    .email("estudiante@sistema.com")
                    .password(passwordEncoder.encode("estudiante123"))
                    .rol(Rol.ESTUDIANTE)
                    .activo(true)
                    .build();

            usuarioRepository.save(admin);
            usuarioRepository.save(finanzas);
            usuarioRepository.save(profesor);
            usuarioRepository.save(estudiante);

            log.info("Usuarios creados exitosamente");
            log.info("=== Credenciales para pruebas ===");
            log.info("Admin: admin / admin123");
            log.info("Finanzas: finanzas / finanzas123");
            log.info("Profesor: profesor / profesor123");
            log.info("Estudiante: estudiante / estudiante123");
        }
    }
}