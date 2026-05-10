package com.ieshermanosmachado.netbook.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.ieshermanosmachado.netbook.dto.ComunidadRequest;
import com.ieshermanosmachado.netbook.dto.ComunidadResponse;
import com.ieshermanosmachado.netbook.dto.MensajeChatRequest;
import com.ieshermanosmachado.netbook.dto.MensajeChatResponse;
import com.ieshermanosmachado.netbook.dto.UsuarioResponse;
import com.ieshermanosmachado.netbook.service.ComunidadService;
import com.ieshermanosmachado.netbook.service.SseChatService;

@RestController
@RequestMapping("/api/comunidades")
public class ComunidadController {

    private final ComunidadService comunidadService;
    private final SseChatService sseChatService;

    public ComunidadController(ComunidadService comunidadService, SseChatService sseChatService) {
        this.comunidadService = comunidadService;
        this.sseChatService = sseChatService;
    }

    // 1. Exploración y Gestión de Comunidades

    @GetMapping
    public ResponseEntity<Page<ComunidadResponse>> listarComunidades(
            @RequestParam(required = false) String genero,
            @AuthenticationPrincipal UserDetails userDetails,
            Pageable pageable) {
        return ResponseEntity.ok(comunidadService.listarComunidades(genero, pageable, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunidadResponse> obtenerComunidad(@PathVariable Integer id) {
        return ResponseEntity.ok(comunidadService.obtenerComunidad(id));
    }

    @PostMapping
    public ResponseEntity<ComunidadResponse> crearComunidad(
            @RequestBody ComunidadRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(comunidadService.crearComunidad(req, userDetails.getUsername()));
    }

    @PostMapping("/{id}/unirse")
    public ResponseEntity<String> unirseComunidad(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String msg = comunidadService.unirseComunidad(id, userDetails.getUsername());
        return ResponseEntity.ok(msg);
    }

    @GetMapping("/{id}/miembros")
    public ResponseEntity<Page<UsuarioResponse>> obtenerMiembros(
            @PathVariable Integer id,
            Pageable pageable) {
        return ResponseEntity.ok(comunidadService.obtenerMiembros(id, pageable));
    }

    // 2. Chat y Mensajería

    @GetMapping("/{id}/mensajes")
    public ResponseEntity<Page<MensajeChatResponse>> historialMensajes(
            @PathVariable Integer id,
            Pageable pageable) {
        return ResponseEntity.ok(comunidadService.obtenerHistorial(id, pageable));
    }

    @PostMapping("/{id}/mensajes")
    public ResponseEntity<MensajeChatResponse> enviarMensaje(
            @PathVariable Integer id,
            @RequestBody MensajeChatRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(comunidadService.enviarMensaje(id, req, userDetails.getUsername()));
    }

    // El endpoint de Streaming de SSE
    @GetMapping(path = "/{id}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter escucharComunidad(@PathVariable Integer id) {
        return sseChatService.suscribir(id);
    }
}
