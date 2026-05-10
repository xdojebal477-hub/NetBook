package com.ieshermanosmachado.netbook.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ieshermanosmachado.netbook.model.MensajeChat;

public interface MensajeChatRepository extends JpaRepository<MensajeChat, Integer> {

    // Obtener el historial paginado de mensajes de una sala de chat ordenados por fecha ascendente
    @Query("SELECT m FROM MensajeChat m WHERE m.comunidad.id = :comunidadId ORDER BY m.fechaPublicacion ASC")
    Page<MensajeChat> findByComunidadIdOrderByFechaPublicacionAsc(@Param("comunidadId") Integer comunidadId, Pageable pageable);

    // Alternativa: Obtener todos los mensajes de una sala ordenados históricamente (cuidado si es muy grande)
    List<MensajeChat> findByComunidadIdOrderByFechaPublicacionAsc(Integer comunidadId);
}
