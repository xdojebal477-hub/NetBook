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

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registrar(RegistroRequest request) {
        
        // 1. Validar que el email no exista ya
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso"); // Mejorar con excepciones personalizadas más adelante
        }

        // 2. Determinar el rol (Si no lo pasan, es LECTOR por defecto)
        Rol rolAsignado = request.getRol() != null ? request.getRol() : Rol.LECTOR;

        // 3. Crear Entidad Usuario y encriptar contraseña
        Usuario nuevoUsuario = new Usuario(
                request.getNombre(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                rolAsignado
        );

        // 4. Guardar en Base de Datos
        usuarioRepository.save(nuevoUsuario);

        // 5. Generar token y devolver respuesta
        String jwtToken = generarToken(nuevoUsuario);
        return new AuthResponse(jwtToken, nuevoUsuario.getNombre(), nuevoUsuario.getEmail(), nuevoUsuario.getRol());
    }

    public AuthResponse login(LoginRequest request) {
        // 1. Delega en Spring Security la validación (Compara BD vs Contraseña enviada)
        // Si falla, lanza excepción automática (BadCredentialsException)
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
