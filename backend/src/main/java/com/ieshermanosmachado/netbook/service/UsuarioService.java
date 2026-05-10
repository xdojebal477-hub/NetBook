package com.ieshermanosmachado.netbook.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieshermanosmachado.netbook.dto.UsuarioPerfilResponse;
import com.ieshermanosmachado.netbook.model.Comunidad;
import com.ieshermanosmachado.netbook.model.Rol;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.ComunidadRepository;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ComunidadRepository comunidadRepository;

    @Transactional(readOnly = true)
    public UsuarioPerfilResponse obtenerPerfilPublico(Integer usuarioId) {
        log.info("Obteniendo perfil publico para el usuario ID: {}", usuarioId);

        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        UsuarioPerfilResponse response = new UsuarioPerfilResponse();
        response.setId(usuario.getId());
        response.setNombre(usuario.getNombre());
        response.setAvatarUrl(usuario.getAvatarUrl());
        response.setRol(usuario.getRol().name());
        response.setFechaRegistro(usuario.getFechaRegistro());

        // Mapear Colecciones Públicas
        List<UsuarioPerfilResponse.ColeccionResumenDTO> coleccionesDTO = usuario.getColecciones().stream()
            .filter(c -> Boolean.TRUE.equals(c.getEsPublica()))
            .map(c -> {
                List<UsuarioPerfilResponse.LibroResumenDTO> librosDTO = c.getLibros().stream()
                    .map(l -> new UsuarioPerfilResponse.LibroResumenDTO(
                        l.getId(), l.getTitulo(), l.getPortadaUrl(), l.getGenero()))
                    .collect(Collectors.toList());
                return new UsuarioPerfilResponse.ColeccionResumenDTO(
                    c.getId(), c.getNombre(), c.getFechaCreacion(), librosDTO);
            })
            .collect(Collectors.toList());
        response.setColeccionesPublicas(coleccionesDTO);

        // Si es autor, mapear Obras Publicadas
        if (usuario.getRol() == Rol.AUTOR) {
            List<UsuarioPerfilResponse.LibroResumenDTO> obrasDTO = usuario.getLibrosPublicados().stream()
                .map(l -> new UsuarioPerfilResponse.LibroResumenDTO(
                    l.getId(), l.getTitulo(), l.getPortadaUrl(), l.getGenero()))
                .collect(Collectors.toList());
            response.setObrasPublicadas(obrasDTO);
        }

        // Mapear Comunidades del usuario
        List<Comunidad> comunidades = comunidadRepository.findComunidadesByMiembro(usuarioId);
        List<UsuarioPerfilResponse.ComunidadResumenDTO> comunidadesDTO = comunidades.stream()
            .map(com -> new UsuarioPerfilResponse.ComunidadResumenDTO(
                com.getId(), 
                com.getNombre(), 
                com.getDescripcion(), 
                com.getImagenUrl(), 
                com.getFechaCreacion(), 
                com.getPropietario().getNombre(), 
                com.getMiembros().size()
            ))
            .collect(Collectors.toList());
        response.setComunidades(comunidadesDTO);

        return response;
    }
}