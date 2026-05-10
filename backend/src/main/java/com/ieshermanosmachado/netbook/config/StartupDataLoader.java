package com.ieshermanosmachado.netbook.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ieshermanosmachado.netbook.dto.RegistroRequest;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;
import com.ieshermanosmachado.netbook.service.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupDataLoader implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;

    @Value("${ADMIN_EMAIL:admin@netbook.com}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:Admin12345}")
    private String adminPassword;

    @Value("${ADMIN_NAME:Admin Netbook}")
    private String adminName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            if (usuarioRepository.existsByEmail(adminEmail)) {
                log.info("[StartupDataLoader] Admin user already exists: {}", adminEmail);
                return;
            }

            RegistroRequest req = new RegistroRequest();
            req.setEmail(adminEmail);
            req.setPassword(adminPassword);
            req.setNombre(adminName);
            // No setRol -> AuthService.registrarDesdeAdmin will create with default or allow admin? We need ADMIN role.
            // registrarDesdeAdmin only restricts creating ADMIN from non-admin UI; here we call server-side so set role.
            req.setRol(com.ieshermanosmachado.netbook.model.Rol.ADMIN);

            authService.registrarDesdeAdmin(req);
            log.info("[StartupDataLoader] Admin user created: {}", adminEmail);
        } catch (Exception ex) {
            log.error("[StartupDataLoader] Error ensuring admin user exists", ex);
            
        }
    }
}
