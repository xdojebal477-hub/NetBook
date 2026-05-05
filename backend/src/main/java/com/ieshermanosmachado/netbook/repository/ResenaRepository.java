package com.ieshermanosmachado.netbook.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ieshermanosmachado.netbook.model.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {

    // Buscar la reseña de un usuario específico sobre un libro
    Optional<Resena> findByUsuarioIdAndLibroId(Integer usuarioId, Integer libroId);

    // Obtener todas las reseñas de un libro ( por fecha descendente)
    List<Resena> findByLibroIdOrderByFechaCreacionDesc(Integer libroId);

    // Obtener todas las reseñas de un usuario
    List<Resena> findByUsuarioIdOrderByFechaCreacionDesc(Integer usuarioId);

    // Calcular la puntuación promedio de un libro
    @Query("SELECT AVG(r.puntuacion) FROM Resena r WHERE r.libro.id = :libroId")
    Optional<Double> getAverageRatingByLibroId(@Param("libroId") Integer libroId);

    // Contar reseñas de un libro
    long countByLibroId(Integer libroId);

    // Eliminar reseña por usuario e idLibro
    int deleteByUsuarioIdAndLibroId(Integer usuarioId, Integer libroId);
}
