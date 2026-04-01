package com.ieshermanosmachado.netbook.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity // Permite usar @PreAuthorize en los Controladores
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Desactivamos CSRF porque no usamos cookies de sesión (usamos JWT)
            .authorizeHttpRequests(auth -> auth
                // Dejamos pasar todo lo que vaya a la ruta de registro/login (Auth) y a Swagger si lo tuviéramos
                .requestMatchers("/api/auth/**").permitAll()
                // Cualquier otra ruta requiere estar autenticado (haber pasado el filtro JWT con éxito)
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                // Sesiones Stateless, es decir, el backend NO guarda quién está logueado. El token es la única prueba.
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
