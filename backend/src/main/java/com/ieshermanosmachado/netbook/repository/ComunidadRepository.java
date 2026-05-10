package com.ieshermanosmachado.netbook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ieshermanosmachado.netbook.model.Comunidad;

public interface ComunidadRepository extends JpaRepository<Comunidad, Integer> {

    // Filtrar comunidades por género usando JOIN sobre la lista de géneros
    @Query("SELECT DISTINCT c FROM Comunidad c JOIN c.generos g WHERE LOWER(g) = LOWER(:genero)")
    Page<Comunidad> findByGenero(@Param("genero") String genero, Pageable pageable);

    // Encontrar comunidades donde el usuario es miembro
    @Query("SELECT c FROM Comunidad c JOIN c.miembros m WHERE m.id = :usuarioId")
    java.util.List<Comunidad> findComunidadesByMiembro(@Param("usuarioId") Integer usuarioId);

}
