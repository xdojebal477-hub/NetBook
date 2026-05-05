package com.ieshermanosmachado.netbook.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ieshermanosmachado.netbook.dto.AuthResponse;
import com.ieshermanosmachado.netbook.dto.LoginRequest;
import com.ieshermanosmachado.netbook.dto.RegistroRequest;
import com.ieshermanosmachado.netbook.model.Rol;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;
import com.ieshermanosmachado.netbook.security.JwtService;
import com.ieshermanosmachado.netbook.security.SecurityUsuario;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegistroRequest request) {
        log.info("[AuthService] Iniciando registro para el usuario: {}", request.getEmail());
        Usuario nuevoUsuario = crearUsuario(request, true);

        //  Generar token y devolver respuesta
        String jwtToken = generarToken(nuevoUsuario);
        log.info("[AuthService] Registro completado y token generado para: {}", request.getEmail());
        return new AuthResponse(jwtToken, nuevoUsuario.getNombre(), nuevoUsuario.getEmail(), nuevoUsuario.getRol());
    }

    public Usuario registrarDesdeAdmin(RegistroRequest request) {
        log.info("[AuthService] Administrador registrando nuevo usuario: {}", request.getEmail());
        return crearUsuario(request, false);
    }

    private Usuario crearUsuario(RegistroRequest request, boolean permitirRolAdmin) {
        // . Validar que el email no exista ya
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso"); 
        }

        // 2. Determinar el rol (Si no lo pasan, es LECTOR por defecto)
        Rol rolAsignado = request.getRol() != null ? request.getRol() : Rol.LECTOR;
        if (!permitirRolAdmin && rolAsignado == Rol.ADMIN) {
            throw new RuntimeException("No se puede crear una cuenta ADMIN desde el panel");
        }

        // 3. Crear Entidad Usuario y encriptar contraseña
        Usuario nuevoUsuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                rolAsignado
        );

        // 4. Guardar en Base de Datos
        return usuarioRepository.save(nuevoUsuario);
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Delega en Spring Security la validación (Compara BD vs Contraseña enviada)
        // Si falla, lanza excepción automática 
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 2. Si llega aquí, es que las credenciales son correctas. Buscamos al usuario.
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow();

        // 3. Generar token y devolver
        String jwtToken = generarToken(usuario);
        return new AuthResponse(jwtToken, usuario.getNombre(), usuario.getEmail(), usuario.getRol());
    }

    // Helper para meter el ID y el ROL dentro del payload del token
    private String generarToken(Usuario usuario) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("id", usuario.getId());
        extraClaims.put("rol", usuario.getRol().name());
        
        return jwtService.generateToken(extraClaims, new SecurityUsuario(usuario));
    }
}
