package com.ieshermanosmachado.netbook.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieshermanosmachado.netbook.dto.CreateResenaRequest;
import com.ieshermanosmachado.netbook.dto.ResenaResponse;
import com.ieshermanosmachado.netbook.model.Libro;
import com.ieshermanosmachado.netbook.model.Resena;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.LibroRepository;
import com.ieshermanosmachado.netbook.repository.ResenaRepository;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    /**
     * Crear o actualizar una reseña 
     */
    @Transactional
    public ResenaResponse crearOActualizarResena(String emailUsuario, Integer libroId, CreateResenaRequest request) {
        log.info("[ResenaService] crearOActualizarResena - email: {}, libroId: {}", emailUsuario, libroId);
        // Validar puntuación
        if (request.getPuntuacion() == null || request.getPuntuacion() < 0.5 || request.getPuntuacion() > 5.0) {
            log.warn("[ResenaService] Puntuación inválida: {}", request.getPuntuacion());
            throw new IllegalArgumentException("La puntuación debe estar entre 0.5 y 5.0");
        }

        // Buscar usuario
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> {
                log.error("[ResenaService] Usuario no encontrado con email: {}", emailUsuario);
                return new RuntimeException("Usuario no encontrado");
            });

        // Buscar libro
        Libro libro = libroRepository.findById(libroId)
            .orElseThrow(() -> {
                log.error("[ResenaService] Libro no encontrado con id: {}", libroId);
                return new RuntimeException("Libro no encontrado");
            });

        // Buscar si ya existe reseña del usuario sobre el libro
        Resena resena = resenaRepository.findByUsuarioIdAndLibroId(usuario.getId(), libroId)
            .orElseGet(() -> {
                log.info("[ResenaService] Creando NUEVA reseña para usuario {} y libro {}", usuario.getId(), libroId);
                return new Resena(usuario, libro, request.getPuntuacion(), request.getComentario());
            });

        if (resena.getId() != null) {
            log.info("[ResenaService] Actualizando reseña EXISTENTE id {}", resena.getId());
        }

        // Actualizar datos
        resena.setPuntuacion(request.getPuntuacion());
        resena.setComentario(request.getComentario());
        resena.setFechaActualizacion(LocalDateTime.now());

        // Guardar
        Resena resenaGuardada = resenaRepository.save(resena);
        log.info("[ResenaService] Reseña guardada con éxito. ID: {}", resenaGuardada.getId());
        return mapToResponse(resenaGuardada);
    }

    /**
     * Obtener todas las reseñas de un libro (ordenadas por fecha descendente)
     */
    public List<ResenaResponse> obtenerResenasPorLibro(Integer libroId) {
        log.info("[ResenaService] obtenerResenasPorLibro - libroId: {}", libroId);
        // Verificar que el libro existe
        if (!libroRepository.existsById(libroId)) {
            log.error("[ResenaService] Libro no encontrado con id: {}", libroId);
            throw new RuntimeException("Libro no encontrado");
        }

        List<Resena> resenas = resenaRepository.findByLibroIdOrderByFechaCreacionDesc(libroId);
        log.info("[ResenaService] Consultada BD: devueltas {} reseñas para el libro {}", resenas.size(), libroId);
        return resenas.stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Obtener la reseña de un usuario específico sobre un libro
     */
    public ResenaResponse obtenerResenaDelUsuario(String emailUsuario, Integer libroId) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return resenaRepository.findByUsuarioIdAndLibroId(usuario.getId(), libroId)
            .map(this::mapToResponse)
            .orElseThrow(() -> new RuntimeException("Reseña no encontrada"));
    }

    /**
     * Obtener todas las reseñas de un usuario
     */
    public List<ResenaResponse> obtenerResenasDelUsuario(String emailUsuario) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return resenaRepository.findByUsuarioIdOrderByFechaCreacionDesc(usuario.getId())
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Eliminar reseña
     */
    @Transactional
    public void eliminarResena(String emailUsuario, Integer libroId) {
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int deleted = resenaRepository.deleteByUsuarioIdAndLibroId(usuario.getId(), libroId);
        if (deleted == 0) {
            throw new RuntimeException("Reseña no encontrada para eliminar");
        }
    }

    /**
     * Eliminar reseña por admin
     */
    @Transactional
    public void eliminarResenaAdmin(Integer resenaId) {
        if (!resenaRepository.existsById(resenaId)) {
            throw new RuntimeException("Reseña no encontrada");
        }
        resenaRepository.deleteById(resenaId);
    }

    /**
     * Obtener todas las reseñas para admin
     */
    public List<ResenaResponse> obtenerTodasResenas() {
        return resenaRepository.findAll()
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    /**
     * Obtener rating promedio de un libro
     */
    public Double obtenerRatingPromedio(Integer libroId) {
        if (!libroRepository.existsById(libroId)) {
            throw new RuntimeException("Libro no encontrado");
        }
        return resenaRepository.getAverageRatingByLibroId(libroId).orElse(0.0);
    }

    /**
     * Contar reseñas de un libro
     */
    public long contarResenasPorLibro(Integer libroId) {
        if (!libroRepository.existsById(libroId)) {
            throw new RuntimeException("Libro no encontrado");
        }
        return resenaRepository.countByLibroId(libroId);
    }

    /**
     * Mapear Entity a DTO
     */
    private ResenaResponse mapToResponse(Resena resena) {
        return new ResenaResponse(
            resena.getId(),
            resena.getUsuario().getId(),
            resena.getUsuario().getNombre(),
            resena.getLibro().getId(),
            resena.getPuntuacion(),
            resena.getComentario(),
            resena.getSentimientoIA(),
            resena.getFechaCreacion(),
            resena.getFechaActualizacion()
        );
    }
}
