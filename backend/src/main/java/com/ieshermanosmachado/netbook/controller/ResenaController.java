package com.ieshermanosmachado.netbook.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieshermanosmachado.netbook.dto.CreateResenaRequest;
import com.ieshermanosmachado.netbook.dto.ResenaResponse;
import com.ieshermanosmachado.netbook.service.ResenaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
@Slf4j
public class ResenaController {

    private final ResenaService resenaService;

    @PostMapping("/{libroId}")
    public ResponseEntity<ResenaResponse> crearOActualizarResena(@PathVariable Integer libroId,
            @RequestBody CreateResenaRequest request,
            Authentication authentication) {
        log.info("CREAR O ACTUALIZAR RESEÑA: libroId={}, email={}, puntuacion={}", libroId, authentication.getName(), request.getPuntuacion());
        String email = authentication.getName();
        ResenaResponse respuesta = resenaService.crearOActualizarResena(email, libroId, request);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/libro/{libroId}")
    public ResponseEntity<List<ResenaResponse>> obtenerResenasPorLibro(@PathVariable Integer libroId) {
        log.info("OBTENER RESEÑAS POR LIBRO: libroId={}", libroId);
        List<ResenaResponse> lista = resenaService.obtenerResenasPorLibro(libroId);
        log.info(" - Encontradas {} reseñas para el libro {}", lista.size(), libroId);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/mia/{libroId}")
    public ResponseEntity<ResenaResponse> obtenerMiResena(@PathVariable Integer libroId,
            Authentication authentication) {
        log.info("OBTENER MI RESEÑA: libroId={}, email={}", libroId, authentication.getName());
        String email = authentication.getName();
        try {
            ResenaResponse resena = resenaService.obtenerResenaDelUsuario(email, libroId);
            return ResponseEntity.ok(resena);
        } catch (RuntimeException ex) {
            log.info(" - El usuario no tiene reseña guardada para el libro: {}", libroId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/mis-resenas")
    public ResponseEntity<List<ResenaResponse>> obtenerMisResenas(
            Authentication authentication) {
        log.info("OBTENER MIS RESEÑAS: email={}", authentication.getName());
        String email = authentication.getName();
        List<ResenaResponse> lista = resenaService.obtenerResenasDelUsuario(email);
        log.info(" - Encontradas {} reseñas del usuario {}", lista.size(), email);
        return ResponseEntity.ok(lista);
    }

    @DeleteMapping("/{libroId}")
    public ResponseEntity<Void> eliminarMiResena(@PathVariable Integer libroId,
            Authentication authentication) {
        log.info("ELIMINAR MI RESEÑA: libroId={}, email={}", libroId, authentication.getName());
        String email = authentication.getName();
        resenaService.eliminarResena(email, libroId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/libro/{libroId}/rating")
    public ResponseEntity<Double> obtenerRatingPromedio(@PathVariable Integer libroId) {
        log.info("OBTENER RATING PROMEDIO: libroId={}", libroId);
        Double rating = resenaService.obtenerRatingPromedio(libroId);
        log.info(" - Rating devuelto: {}", rating);
        return ResponseEntity.ok(rating);
    }

    @GetMapping("/libro/{libroId}/count")
    public ResponseEntity<Long> contarResenas(@PathVariable Integer libroId) {
        log.info("CONTAR RESEÑAS: libroId={}", libroId);
        long count = resenaService.contarResenasPorLibro(libroId);
        log.info(" - Cantidad devuelta: {}", count);
        return ResponseEntity.ok(count);
    }
}
