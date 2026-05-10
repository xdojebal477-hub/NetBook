package com.ieshermanosmachado.netbook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieshermanosmachado.netbook.dto.ColeccionRequest;
import com.ieshermanosmachado.netbook.dto.ColeccionResponse;
import com.ieshermanosmachado.netbook.service.ColeccionService;

@RestController
@RequestMapping("/api/colecciones")
public class ColeccionController {

    private final ColeccionService coleccionService;

    public ColeccionController(ColeccionService coleccionService) {
        this.coleccionService = coleccionService;
    }

    @GetMapping
    public ResponseEntity<List<ColeccionResponse>> obtenerColecciones(Authentication authentication) {
        String email = authentication.getName();
        List<ColeccionResponse> respuesta = coleccionService.obtenerColeccionesDelUsuario(email);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping
    public ResponseEntity<ColeccionResponse> crearColeccion(@RequestBody ColeccionRequest request, Authentication authentication) {
        String email = authentication.getName();
        ColeccionResponse respuesta = coleccionService.crearColeccion(request, email);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    @PostMapping("/{id}/libros/{libroId}")
    public ResponseEntity<Void> anadirLibroAColeccion(
            @PathVariable("id") Integer coleccionId,
            @PathVariable("libroId") Integer libroId,
            Authentication authentication) {
        
        String email = authentication.getName();
        coleccionService.anadirLibroAColeccion(coleccionId, libroId, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ColeccionResponse> actualizarColeccion(
            @PathVariable("id") Integer coleccionId,
            @RequestBody ColeccionRequest request,
            Authentication authentication) {
        String email = authentication.getName();
        ColeccionResponse respuesta = coleccionService.actualizarColeccion(coleccionId, request, email);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarColeccion(
            @PathVariable("id") Integer coleccionId,
            Authentication authentication) {
        String email = authentication.getName();
        coleccionService.eliminarColeccion(coleccionId, email);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/libros/{libroId}")
    public ResponseEntity<Void> quitarLibroDeColeccion(
            @PathVariable("id") Integer coleccionId,
            @PathVariable("libroId") Integer libroId,
            Authentication authentication) {
        String email = authentication.getName();
        coleccionService.quitarLibroDeColeccion(coleccionId, libroId, email);
        return ResponseEntity.noContent().build();
    }
}
