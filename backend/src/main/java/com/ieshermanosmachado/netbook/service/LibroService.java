package com.ieshermanosmachado.netbook.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ieshermanosmachado.netbook.model.EstadoLibro;
import com.ieshermanosmachado.netbook.model.Libro;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.LibroRepository;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibroService {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final FileStorageService fileStorageService;

    //  RF-04: Lógica para la creación de un Libro de Autor 
    public Libro publicarLibro(String emailAutor, String titulo, String sinopsis, String genero, MultipartFile archivo, MultipartFile portada) {
        log.info("[LibroService] publicarLibro - Autor: {}, Titulo: {}", emailAutor, titulo);
        // buscamos que exista el autor por email
        Usuario autor = usuarioRepository.findByEmail(emailAutor)
            .orElseThrow(() -> {
                log.error("[LibroService] Autor no encontrado para email: {}", emailAutor);
                return new RuntimeException("Usuario autor no encontrado.");
            });

        // Validamos que el usuario tiene el ROL explícito de Autor
        if (autor.getRol() != com.ieshermanosmachado.netbook.model.Rol.AUTOR) {
            log.error("[LibroService] Usuario {} no tiene rol AUTOR", emailAutor);
            throw new RuntimeException("Acceso denegado: El usuario no es un Autor.");
        }

        // guardamos el archivo físico
        String archivoUrl = fileStorageService.guardarArchivo(archivo);
        String portadaUrlGuardada = null;
        if (portada != null && !portada.isEmpty()) {
            portadaUrlGuardada = fileStorageService.guardarArchivo(portada);
            // El front necesita un src para IMG. Usamos la ruta relativa para que funcione en cualquier entorno (local o prod)
            portadaUrlGuardada = "/api/libros/archivos/" + portadaUrlGuardada;
        }

        // persistencia en la db
        Libro libro = new Libro(
            autor,
            titulo,
            sinopsis,
            genero,
            archivoUrl,
            EstadoLibro.BORRADOR
        );
        libro.setPortadaUrl(portadaUrlGuardada);

        
        return libroRepository.save(libro);
    }

    // Listar todos los libros que sean PUBLICOS (Catálogo General)
    public List<Libro> obtenerCatalogoGeneral() {
        return libroRepository.findByEstado(EstadoLibro.PUBLICO);
    }

    // (ADMIN) Listar absolutamente todos los libros de la BD
    public List<Libro> obtenerTodosLosLibros() {
        return libroRepository.findAll();
    }

    // (ADMIN) Eliminar cualquier libro sin importar el autor
    public void eliminarLibroAdmin(Integer id) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        libroRepository.delete(libro);
    }

    // Listar solo los libros correspondientes al Autor autenticado
    public List<Libro> obtenerMisObras(String emailAutor) {
        Usuario autor = usuarioRepository.findByEmail(emailAutor)
            .orElseThrow(() -> new RuntimeException("Usuario autor no encontrado o el usuario no es un autor."));
        return libroRepository.findByAutorId(autor.getId());
    }

    // Obtener el detalle de un libro en específico
    public Libro obtenerLibroPorId(Integer id) {
        return libroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
    }

    // Actualizar metadatos (Solo el autor dueño)
    public Libro actualizarLibro(Integer id, String emailAutor, String titulo, String sinopsis, String genero, EstadoLibro estado, MultipartFile portada) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para actualizar obras que no son tuyas.");
        }

        if (titulo != null && !titulo.isEmpty()) libro.setTitulo(titulo);
        if (sinopsis != null) libro.setSinopsis(sinopsis);
        if (genero != null) libro.setGenero(genero);
        if (estado != null) libro.setEstado(estado);
        if (portada != null && !portada.isEmpty()) {
            String portadaFile = fileStorageService.guardarArchivo(portada);
            libro.setPortadaUrl("/api/libros/archivos/" + portadaFile);
        }

        return libroRepository.save(libro);
    }

    // Eliminar la obra por su id (Solo el autor dueño)

    public Resource descargarLibro(Integer id) {
        Libro libro = obtenerLibroPorId(id);
        return fileStorageService.cargarArchivoComoRecurso(libro.getArchivoUrl());
    }

    public Resource descargarLibroPorNombre(String filename) {
        return fileStorageService.cargarArchivoComoRecurso(filename);
    }

    public void eliminarLibro(Integer id, String emailAutor) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro no encontrado"));

        if (!libro.getAutor().getEmail().equals(emailAutor)) {
            throw new RuntimeException("No tienes permiso para eliminar obras que no son tuyas.");
        }

        
        libroRepository.delete(libro);
    }
}
