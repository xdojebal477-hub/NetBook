package com.ieshermanosmachado.netbook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UsuarioRepository usuarioRepository;

    // Le decimos a Spring cómo buscar al usuario en la base de datos (por el Email)
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByEmail(username)
                // Hacemos un adaptador al vuelo (Spring Security necesita UserDetails y nuestra entidad no lo implementa aún)
                .map(SecurityUsuario::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));
    }

    // Quién va a realizar la autenticación real cruzando datos (BD vs Contraseña introducida)
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // Manejador central de autenticaciones
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Cómo vamos a encriptar las contraseñas (BCrypt es el gold standard actual)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
