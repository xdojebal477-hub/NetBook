package com.ieshermanosmachado.netbook.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ieshermanosmachado.netbook.dto.ComunidadRequest;
import com.ieshermanosmachado.netbook.dto.ComunidadResponse;
import com.ieshermanosmachado.netbook.dto.MensajeChatRequest;
import com.ieshermanosmachado.netbook.dto.MensajeChatResponse;
import com.ieshermanosmachado.netbook.dto.UsuarioResponse;
import com.ieshermanosmachado.netbook.model.Comunidad;
import com.ieshermanosmachado.netbook.model.MensajeChat;
import com.ieshermanosmachado.netbook.model.Usuario;
import com.ieshermanosmachado.netbook.repository.ComunidadRepository;
import com.ieshermanosmachado.netbook.repository.MensajeChatRepository;
import com.ieshermanosmachado.netbook.repository.UsuarioRepository;

@Service
public class ComunidadService {

    private final ComunidadRepository comunidadRepository;
    private final MensajeChatRepository mensajeChatRepository;
    private final UsuarioRepository usuarioRepository;
    private final SseChatService sseChatService;

    public ComunidadService(ComunidadRepository comunidadRepository, 
            MensajeChatRepository mensajeChatRepository, 
            UsuarioRepository usuarioRepository,
            SseChatService sseChatService) {
        this.comunidadRepository = comunidadRepository;
        this.mensajeChatRepository = mensajeChatRepository;
        this.usuarioRepository = usuarioRepository;
        this.sseChatService = sseChatService;
    }

    @Transactional(readOnly = true)
    public Page<ComunidadResponse> listarComunidades(String genero, Pageable pageable, String emailUsuario) {
        Usuario usuarioActual = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Page<Comunidad> comunidades;
        if (genero != null && !genero.trim().isEmpty()) {
            comunidades = comunidadRepository.findByGenero(genero, pageable);
        } else {
            comunidades = comunidadRepository.findAll(pageable);
        }
        return comunidades.map(comunidad -> mapToComunidadResponse(comunidad, usuarioActual.getId()));
    }

    @Transactional(readOnly = true)
    public ComunidadResponse obtenerComunidad(Integer id) {
        Comunidad c = comunidadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada con ID: " + id));
        return mapToComunidadResponse(c, null);
    }

    @Transactional
    public ComunidadResponse crearComunidad(ComunidadRequest req, String emailPropietario) {
        Usuario propietario = usuarioRepository.findByEmail(emailPropietario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        Comunidad c = new Comunidad(req.getNombre(), req.getDescripcion(), propietario);
        c.setImagenUrl(req.getImagenUrl());
        
        if (req.getGeneros() != null) {
            c.setGeneros(req.getGeneros());
        }
        
        // Al crearla, el dueño se une automáticamente
        c.addMiembro(propietario);
        
        Comunidad saved = comunidadRepository.save(c);
        return mapToComunidadResponse(saved, propietario.getId());
    }

        @Transactional
    public ComunidadResponse actualizarComunidad(Integer idComunidad, ComunidadRequest req, String emailAdmin) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        // Verificar que solo el admin o el propietario pueda editar
        Usuario admin = usuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!admin.getRol().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permiso para editar esta comunidad.");
        }
        comunidad.setNombre(req.getNombre());
        comunidad.setDescripcion(req.getDescripcion());
        comunidad.setImagenUrl(req.getImagenUrl());

        if (req.getGeneros() != null) {
            comunidad.setGeneros(req.getGeneros());
        }

        return mapToComunidadResponse(comunidadRepository.save(comunidad), null);
    }

    @Transactional
    public void eliminarComunidad(Integer idComunidad, String emailAdmin) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        
        Usuario admin = usuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!admin.getRol().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permiso para eliminar esta comunidad.");
        }
        comunidadRepository.delete(comunidad);
    }

    @Transactional(readOnly = true)
    public List<ComunidadResponse> listarTodasLasComunidades() {
        return comunidadRepository.findAll().stream()
            .map(comunidad -> mapToComunidadResponse(comunidad, null))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuariosDisponiblesParaComunidad(Integer idComunidad) {
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
            .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));

        Set<Integer> miembrosIds = comunidad.getMiembros().stream()
            .map(Usuario::getId)
            .collect(Collectors.toSet());

        return usuarioRepository.findAll().stream()
            .filter(usuario -> !miembrosIds.contains(usuario.getId()))
            .map(this::mapToUsuarioResponse)
            .toList();
    }

    @Transactional
    public void agregarMiembroAdmin(Integer idComunidad, Integer idUsuario, String emailAdmin) {
        Usuario admin = usuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!admin.getRol().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permiso para gestionar miembros.");
        }
        
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        comunidad.addMiembro(usuario);
        comunidadRepository.save(comunidad);
    }

    @Transactional
    public void eliminarMiembroAdmin(Integer idComunidad, Integer idUsuario, String emailAdmin) {
        Usuario admin = usuarioRepository.findByEmail(emailAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!admin.getRol().name().equals("ADMIN")) {
            throw new RuntimeException("No tienes permiso para gestionar miembros.");
        }
        
        Comunidad comunidad = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (comunidad.getPropietario() != null && comunidad.getPropietario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No se puede expulsar al propietario de la comunidad.");
        }

        comunidad.removeMiembro(usuario);
        comunidadRepository.save(comunidad);
    }

    @Transactional
    public String unirseComunidad(Integer idComunidad, String emailUsuario) {
        Comunidad c = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        Usuario u = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        if (c.getMiembros().contains(u)) {
            return "Ya eres miembro de la comunidad.";
        }
        c.addMiembro(u);
        comunidadRepository.save(c);
        return "Unido correctamente a la comunidad " + c.getNombre();
    }

    @Transactional(readOnly = true)
    public Page<UsuarioResponse> obtenerMiembros(Integer idComunidad, Pageable pageable) {
        Comunidad c = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
                
        
        List<UsuarioResponse> miembrosResponse = c.getMiembros().stream()
                .map(u -> new UsuarioResponse(
                    u.getId(),
                    u.getNombre(),
                    u.getEmail(),  
                    u.getAvatarUrl(),
                    u.getFechaRegistro()
                )).collect(Collectors.toList());

        // Simulamos la paginación a partir de la lista
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), miembrosResponse.size());
        
        List<UsuarioResponse> subList = start < end ? miembrosResponse.subList(start, end) : List.of();
        
        return new org.springframework.data.domain.PageImpl<>(subList, pageable, miembrosResponse.size());
    }

    @Transactional(readOnly = true)
    public Page<MensajeChatResponse> obtenerHistorial(Integer idComunidad, Pageable pageable) {
        // Obtenemos del más antiguo al más reciente
        return mensajeChatRepository.findByComunidadIdOrderByFechaPublicacionAsc(idComunidad, pageable)
                .map(this::mapToMensajeResponse);
    }

    @Transactional
    public MensajeChatResponse enviarMensaje(Integer idComunidad, MensajeChatRequest req, String emailAutor) {
        Comunidad c = comunidadRepository.findById(idComunidad)
                .orElseThrow(() -> new RuntimeException("Comunidad no encontrada"));
        Usuario u = usuarioRepository.findByEmail(emailAutor)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
        if (!c.getMiembros().contains(u)) {
            throw new RuntimeException("Debes ser miembro para escribir en esta comunidad.");
        }
        
        MensajeChat msg = new MensajeChat(req.getContenido(), c, u);
        MensajeChat saved = mensajeChatRepository.save(msg);
        
        MensajeChatResponse responseDto = mapToMensajeResponse(saved);
        
        // ¡Magia! Emitimos el evento por la tubería
        sseChatService.emitirMensaje(idComunidad, responseDto);
        
        return responseDto;
    }

    // -- Mappers
    private ComunidadResponse mapToComunidadResponse(Comunidad c, Integer currentUserId) {
        ComunidadResponse r = new ComunidadResponse();
        r.setId(c.getId());
        r.setNombre(c.getNombre());
        r.setDescripcion(c.getDescripcion());
        r.setImagenUrl(c.getImagenUrl());
        r.setFechaCreacion(c.getFechaCreacion());
        r.setPropietarioId(c.getPropietario().getId());
        r.setPropietarioNombre(c.getPropietario().getNombre());
        // Hibernate carga los miembros haciendo un LAZY cuando se accede a la propiedad, así que aquí ya los tenemos disponibles
        r.setNumeroMiembros(c.getMiembros() != null ? c.getMiembros().size() : 0);
        r.setEsMiembro(currentUserId != null && c.getMiembros() != null && c.getMiembros().stream().anyMatch(u -> u.getId().equals(currentUserId)));
        r.setGeneros(c.getGeneros() != null ? c.getGeneros() : Set.of());
        return r;
    }

    private UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getRol().name(),
                usuario.getFechaRegistro()
        );
    }

    private MensajeChatResponse mapToMensajeResponse(MensajeChat m) {
        MensajeChatResponse r = new MensajeChatResponse();
        r.setId(m.getId());
        r.setContenido(m.getContenido());
        r.setFechaPublicacion(m.getFechaPublicacion());
        r.setAutorId(m.getAutor().getId());
        r.setAutorNombre(m.getAutor().getNombre());
        r.setAutorAvatarUrl(m.getAutor().getAvatarUrl());
        return r;
    }
}
