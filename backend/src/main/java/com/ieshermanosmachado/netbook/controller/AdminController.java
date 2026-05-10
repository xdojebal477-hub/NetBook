package com.ieshermanosmachado.netbook.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ieshermanosmachado.netbook.dto.ComunidadRequest;
import com.ieshermanosmachado.netbook.dto.ComunidadResponse;
import com.ieshermanosmachado.netbook.dto.LibroResponse;
import com.ieshermanosmachado.netbook.dto.RegistroRequest;
import com.ieshermanosmachado.netbook.dto.ResenaResponse;
import com.ieshermanosmachado.netbook.dto.UsuarioResponse;
import com.ieshermanosmachado.netbook.model.Libro;
import com.ieshermanosmachado.netbook.model.Rol;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;
import com.ieshermanosmachado.netbook.service.AuthService;
import com.ieshermanosmachado.netbook.service.ComunidadService;
import com.ieshermanosmachado.netbook.service.LibroService;
import com.ieshermanosmachado.netbook.service.ResenaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // para conectaranos al puerto de angular y que no nos bloquee el CORS
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // TODAS estas rutas para admins
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final AuthService authService;
    private final ComunidadService comunidadService;
    private final LibroService libroService;
    private final ResenaService resenaService;

    // --- MANEJO DE USUARIOS ---

    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponse>> obtenerTodosLosUsuarios() {
        List<UsuarioResponse> usuarios = usuarioRepository.findAll().stream()
                .map(this::mapUsuarioToResponse)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody RegistroRequest request) {
        Usuario nuevoUsuario = authService.registrarDesdeAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapUsuarioToResponse(nuevoUsuario));
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> borrarUsuario(@PathVariable Integer id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/usuarios/{id}/rol")
    public ResponseEntity<UsuarioResponse> cambiarRol(@PathVariable Integer id, @RequestParam("rol") String nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        Rol enumRol = Rol.valueOf(nuevoRol.toUpperCase());
        usuario.setRol(enumRol);
        
        Usuario actualizado = usuarioRepository.save(usuario);
        return ResponseEntity.ok(mapUsuarioToResponse(actualizado));
    }

    private UsuarioResponse mapUsuarioToResponse(Usuario u) {
        return new UsuarioResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol().name(),
                u.getFechaRegistro()
        );
    }

    // --- MANEJO DE LIBROS (Moderación global) ---

    @GetMapping("/libros")
    public ResponseEntity<List<LibroResponse>> obtenerTodosLosLibros() {
        List<LibroResponse> libros = libroService.obtenerTodosLosLibros().stream()
                .map(this::mapLibroToResponse)
                .toList();
        return ResponseEntity.ok(libros);
    }

    @DeleteMapping("/libros/{id}")
    public ResponseEntity<Void> borrarLibroComoAdmin(@PathVariable Integer id) {
        libroService.eliminarLibroAdmin(id);
        return ResponseEntity.noContent().build();
    }

    private LibroResponse mapLibroToResponse( Libro l) {
        return new LibroResponse(
                l.getId(),
                l.getTitulo(),
                l.getSinopsis(),
                l.getGenero(),
                l.getArchivoUrl(),
                l.getPortadaUrl(),
                l.getEstado().name(),
                l.getAutor().getId(),
                l.getAutor().getNombre(),
                l.getFechaSubida(),
                null,
                null
        );
    }

    // --- MANEJO DE RESEÑAS (admin) ---

    @GetMapping("/resenas")
    public ResponseEntity<List<ResenaResponse>> obtenerTodasLasResenas() {
        List<ResenaResponse> resenas = resenaService.obtenerTodasResenas();
        return ResponseEntity.ok(resenas);
    }

    @DeleteMapping("/resenas/{id}")
    public ResponseEntity<Void> borrarResenaComoAdmin(@PathVariable Integer id) {
        resenaService.eliminarResenaAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // --- MANEJO DE COMUNIDADES (admin) ---

    @GetMapping("/comunidades")
    public ResponseEntity<List<ComunidadResponse>> obtenerTodasLasComunidades() {
        return ResponseEntity.ok(comunidadService.listarTodasLasComunidades());
    }

    @PostMapping("/comunidades")
    public ResponseEntity<ComunidadResponse> crearComunidad(@Valid @RequestBody ComunidadRequest request) {
        String adminEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        ComunidadResponse creada = comunidadService.crearComunidad(request, adminEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/comunidades/{id}")
    public ResponseEntity<ComunidadResponse> actualizarComunidad(@PathVariable Integer id, @Valid @RequestBody ComunidadRequest request) {
        String adminEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return ResponseEntity.ok(comunidadService.actualizarComunidad(id, request, adminEmail));
    }

    @DeleteMapping("/comunidades/{id}")
    public ResponseEntity<Void> borrarComunidad(@PathVariable Integer id) {
        String adminEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        comunidadService.eliminarComunidad(id, adminEmail);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/comunidades/{id}/usuarios-disponibles")
    public ResponseEntity<List<UsuarioResponse>> usuariosDisponiblesParaComunidad(@PathVariable Integer id) {
        return ResponseEntity.ok(comunidadService.listarUsuariosDisponiblesParaComunidad(id));
    }

    @PostMapping("/comunidades/{id}/miembros/{usuarioId}")
    public ResponseEntity<String> anadirMiembroAComunidad(@PathVariable Integer id, @PathVariable Integer usuarioId) {
        String adminEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        comunidadService.agregarMiembroAdmin(id, usuarioId, adminEmail);
        return ResponseEntity.ok("Usuario añadido correctamente a la comunidad.");
    }

    @DeleteMapping("/comunidades/{id}/miembros/{usuarioId}")
    public ResponseEntity<String> eliminarMiembroDeComunidad(@PathVariable Integer id, @PathVariable Integer usuarioId) {
        String adminEmail = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        comunidadService.eliminarMiembroAdmin(id, usuarioId, adminEmail);
        return ResponseEntity.ok("Usuario eliminado correctamente de la comunidad.");
    }
}
