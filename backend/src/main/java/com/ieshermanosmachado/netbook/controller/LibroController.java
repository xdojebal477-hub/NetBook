package com.ieshermanosmachado.netbook.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ieshermanosmachado.netbook.dto.LibroResponse;
import com.ieshermanosmachado.netbook.model.EstadoLibro;
import com.ieshermanosmachado.netbook.model.Libro;
import com.ieshermanosmachado.netbook.service.LibroService;
import com.ieshermanosmachado.netbook.service.ResenaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Fundamental para conectar con Angular
public class LibroController {

    private final LibroService libroService;
    private final ResenaService resenaService;

    // metodo post con multipart/form-data para subir archivos aparte de JSON
    @PostMapping(consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ROLE_AUTOR')") // valida que solo los usuarios con rol AUTOR puedan acceder a este endpoint
    public ResponseEntity<LibroResponse> subirLibro(
            @RequestParam("titulo") String titulo,
            @RequestParam(value = "sinopsis", required = false) String sinopsis,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "portada", required = false) MultipartFile portada,
            @RequestParam("archivo") MultipartFile archivo,
            Authentication authentication) { // Spring Security inyecta automáticamente quién eres por tu Token
        
        try {
            
            String emailAutor = authentication.getName();
            
            
            Libro libroGuardado = libroService.publicarLibro(emailAutor, titulo, sinopsis, genero, archivo, portada);
            
            // Transformamos a un DTO para no exponer información 
            LibroResponse response = new LibroResponse(
                    libroGuardado.getId(),
                    libroGuardado.getTitulo(),
                    libroGuardado.getSinopsis(),
                    libroGuardado.getGenero(),
                    libroGuardado.getArchivoUrl(),
                    libroGuardado.getPortadaUrl(),
                    libroGuardado.getEstado().name(),
                    libroGuardado.getAutor().getId(),
                    libroGuardado.getAutor().getNombre(), // Solo devolvemos el nombre del autor
                    libroGuardado.getFechaSubida(),
                    resenaService.obtenerRatingPromedio(libroGuardado.getId()),
                    resenaService.contarResenasPorLibro(libroGuardado.getId())
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            // Si la validación del PDF falla u otro error, devolvemos un 400 
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Listar todos los libros públicos (Catálogo General - Permitido a todos)
    @GetMapping
    public ResponseEntity<List<LibroResponse>> obtenerCatalogoGeneral() {
        List<LibroResponse> libros = libroService.obtenerCatalogoGeneral().stream()
                .map(this::mapToResponse)
                .toList();
        return ResponseEntity.ok(libros);
    }

    // Listar solo los libros del Autor que esta logeado
    @GetMapping("/mis-obras")
    @PreAuthorize("hasAuthority('ROLE_AUTOR')")
    public ResponseEntity<List<LibroResponse>> obtenerMisObras(Authentication authentication) {
        try {
            List<LibroResponse> obras = libroService.obtenerMisObras(authentication.getName()).stream()
                    .map(this::mapToResponse)
                    .toList();
            return ResponseEntity.ok(obras);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // Detalle de un libro en particular
    @GetMapping("/{id}")
    public ResponseEntity<LibroResponse> obtenerLibroPorId(@PathVariable Integer id) {
        try {
            Libro libro = libroService.obtenerLibroPorId(id);
            return ResponseEntity.ok(mapToResponse(libro));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para el streaming físico del archivo (PDF o TXT)
    @GetMapping("/{id}/leer")
    public ResponseEntity<Resource> descargarLibro(@PathVariable Integer id) {
        try {
            Resource recurso = libroService.descargarLibro(id);
            
            String contentType = "application/pdf";
            if(recurso.getFilename() != null && recurso.getFilename().endsWith(".txt")) {
                contentType = "text/plain";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
.body(recurso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Endpoint para obtener fotos (portadas)
    @GetMapping("/archivos/{filename:.+}")
    public ResponseEntity<Resource> obtenerImagen(@PathVariable String filename) {
        try {
            Resource recurso = libroService.descargarLibroPorNombre(filename);
            
            String contentType = "application/octet-stream";
            if (filename.toLowerCase().endsWith(".png")) {
                contentType = "image/png";
            } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (filename.toLowerCase().endsWith(".webp")) {
                contentType = "image/webp";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline")
                    .body(recurso);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Actualizar metadatos de la obra (Solo por el propio Autor)
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('ROLE_AUTOR')")
    public ResponseEntity<LibroResponse> actualizarLibro(
            @PathVariable Integer id,
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "sinopsis", required = false) String sinopsis,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "portada", required = false) MultipartFile portada,
            Authentication authentication) {

        EstadoLibro estadoEnum = null;
        if (estado != null && !estado.isEmpty()) {
            try {
                estadoEnum = EstadoLibro.valueOf(estado.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        try {
            Libro actualizado = libroService.actualizarLibro(
                id, 
                authentication.getName(), 
                titulo, 
                sinopsis, 
                genero,
                estadoEnum,
                portada
            );
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (RuntimeException e) {
            // Si intenta editar un libro que no es suyo, dará este error
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // Borrar la obra (Solo por el propio Autor)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_AUTOR')")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Integer id, Authentication authentication) {
        try {
            libroService.eliminarLibro(id, authentication.getName());
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }
    }

    // metodo  Helper : Convierte la Entidad Libro a DTO LibroResponse
    private LibroResponse mapToResponse(Libro libro) {
        Double avgRating = resenaService.obtenerRatingPromedio(libro.getId());
        Long reviewCount = resenaService.contarResenasPorLibro(libro.getId());
        
        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getGenero(),
                libro.getArchivoUrl(),
                libro.getPortadaUrl(),
                libro.getEstado().name(),
                libro.getAutor().getId(),
                libro.getAutor().getNombre(),
                libro.getFechaSubida(),
                avgRating,
                reviewCount
        );
    }
}