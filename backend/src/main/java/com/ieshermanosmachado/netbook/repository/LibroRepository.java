package com.ieshermanosmachado.netbook.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ieshermanosmachado.netbook.model.EstadoLibro;
import com.ieshermanosmachado.netbook.model.Libro;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Integer> {

    // Buscar libros por su estado (ej. Solo mostrar los PUBLICOS en el catálogo)
    List<Libro> findByEstado(EstadoLibro estado);

    // Buscar todos los libros de un autor en particular (Para el panel del Autor)
    List<Libro> findByAutorId(Integer idAutor);

    // Buscar libros por título que contengan un texto (Ignorando mayúsculas) (Buscador)
    List<Libro> findByTituloContainingIgnoreCaseAndEstado(String titulo, EstadoLibro estado);

    // Filtrar por género
    List<Libro> findByGeneroAndEstado(String genero, EstadoLibro estado);

}
