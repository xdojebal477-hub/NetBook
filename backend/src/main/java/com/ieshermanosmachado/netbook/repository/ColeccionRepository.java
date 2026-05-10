package com.ieshermanosmachado.netbook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.ieshermanosmachado.netbook.model.Coleccion;
import com.ieshermanosmachado.netbook.model.Usuario;

@Repository
public interface ColeccionRepository extends JpaRepository<Coleccion, Integer> {
    List<Coleccion> findByPropietario(Usuario propietario);
}
