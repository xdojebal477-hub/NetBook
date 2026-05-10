package com.ieshermanosmachado.netbook.security;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // creamos buckets en memoria para cada IP, asi el usuario tiene un limite por minuto
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //  la IP del cliente
        String remoteIpInfo = request.getRemoteAddr();

        // Obtener o crear el bucket para esa IP
        Bucket bucket = buckets.computeIfAbsent(remoteIpInfo, k -> createNewBucket());

        // Intentar consumir 1 "token"
        if (bucket.tryConsume(1)) {
            // Hay capacidad, dejamos pasar la petición
            filterChain.doFilter(request, response);
        } else {
            // Se ha excedido el límite ("Too Many Requests")
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value()); // HTTP 429
            response.getWriter().write("Has superado el limite de peticiones. Intentalo mas tarde.");
        }
    }

    // Configuración del Rate Limiting: 10 peticiones cada 1 minuto por IP.
    private Bucket createNewBucket() {
        
        Bandwidth limit = Bandwidth.builder()
            .capacity(10)
            .refillGreedy(10, Duration.ofMinutes(1))
            .build();
        
        return Bucket.builder().addLimit(limit).build();
    }
}
