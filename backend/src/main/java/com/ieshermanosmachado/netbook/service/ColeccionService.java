package com.ieshermanosmachado.netbook.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieshermanosmachado.netbook.dto.ColeccionRequest;
import com.ieshermanosmachado.netbook.dto.ColeccionResponse;
import com.ieshermanosmachado.netbook.dto.LibroResponse;
import com.ieshermanosmachado.netbook.model.Coleccion;
import com.ieshermanosmachado.netbook.model.Libro;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.ColeccionRepository;
import com.ieshermanosmachado.netbook.repository.LibroRepository;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

@Service
public class ColeccionService {

    private final ColeccionRepository coleccionRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;

    public ColeccionService(ColeccionRepository coleccionRepository, UsuarioRepository usuarioRepository, LibroRepository libroRepository) {
        this.coleccionRepository = coleccionRepository;
        this.usuarioRepository = usuarioRepository;
        this.libroRepository = libroRepository;
    }

    public List<ColeccionResponse> obtenerColeccionesDelUsuario(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Coleccion> colecciones = coleccionRepository.findByPropietario(usuario);
        return colecciones.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public ColeccionResponse crearColeccion(ColeccionRequest request, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Coleccion coleccion = new Coleccion(request.getNombre(), Boolean.TRUE.equals(request.getEsPublica()), usuario);
        Coleccion guardada = coleccionRepository.save(coleccion);
        return mapToResponse(guardada);
    }

    @Transactional
    public void anadirLibroAColeccion(Integer coleccionId, Integer libroId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        
        if (!coleccion.getPropietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos sobre esta colección");
        }

        Libro libro = libroRepository.findById(libroId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        if (!coleccion.getLibros().contains(libro)) {
            coleccion.getLibros().add(libro);
            coleccionRepository.save(coleccion);
        }
    }

    @Transactional
    public ColeccionResponse actualizarColeccion(Integer coleccionId, ColeccionRequest request, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        
        if (!coleccion.getPropietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos sobre esta colección");
        }

        if(request.getNombre() != null) {
            coleccion.setNombre(request.getNombre());
        }
        if(request.getEsPublica() != null) {
            coleccion.setEsPublica(request.getEsPublica());
        }
        
        Coleccion guardada = coleccionRepository.save(coleccion);
        return mapToResponse(guardada);
    }

    @Transactional
    public void eliminarColeccion(Integer coleccionId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        
        if (!coleccion.getPropietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos sobre esta colección");
        }

        coleccionRepository.delete(coleccion);
    }

    @Transactional
    public void quitarLibroDeColeccion(Integer coleccionId, Integer libroId, String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Coleccion coleccion = coleccionRepository.findById(coleccionId).orElseThrow(() -> new RuntimeException("Colección no encontrada"));
        
        if (!coleccion.getPropietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No tienes permisos sobre esta colección");
        }

        Libro libro = libroRepository.findById(libroId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        
        if (coleccion.getLibros().contains(libro)) {
            coleccion.getLibros().remove(libro);
            coleccionRepository.save(coleccion);
        }
    }

    private ColeccionResponse mapToResponse(Coleccion c) {
        ColeccionResponse res = new ColeccionResponse();
        res.setId(c.getId());
        res.setNombre(c.getNombre());
        res.setEsPublica(c.getEsPublica());
        res.setFechaCreacion(c.getFechaCreacion());
        res.setNumeroLibros(c.getLibros() != null ? c.getLibros().size() : 0);
        res.setPropietarioId(c.getPropietario().getId());
        if (c.getLibros() != null) {
            res.setLibros(c.getLibros().stream().map(this::mapLibroToResponse).collect(Collectors.toList()));
        }
        return res;
    }

    private LibroResponse mapLibroToResponse(Libro libro) {
        return new LibroResponse(
                libro.getId(),
                libro.getTitulo(),
                libro.getSinopsis(),
                libro.getGenero(),
                libro.getArchivoUrl(),
                libro.getPortadaUrl(),
                libro.getEstado() != null ? libro.getEstado().name() : null,
                libro.getAutor() != null ? libro.getAutor().getNombre() : null,
                libro.getFechaSubida(),
                null,
                null
        );
    }
}
