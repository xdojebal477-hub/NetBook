package com.ieshermanosmachado.netbook.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        String jwt = null;
        final String userEmail;

        // Leer de query param para compatibilidad con EventSource (SSE)
        if (request.getParameter("token") != null) {
            jwt = request.getParameter("token");
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraemos el correo desde el token 
        userEmail = jwtService.extractUsername(jwt);

        // Si el correo existe y el contexto de seguridad aún no está autenticado...
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Validamos que el token no haya expirado y pertenezca al usuario
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Le decimos a Spring Security: "Oye, este usuario está validado, déjalo pasar".
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        
        // Continuamos con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}
