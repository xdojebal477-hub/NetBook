package com.ieshermanosmachado.netbook.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ieshermanosmachado.netbook.dto.UsuarioPerfilResponse;
import com.ieshermanosmachado.netbook.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/{id}/perfil")
    public ResponseEntity<UsuarioPerfilResponse> obtenerPerfilPublico(@PathVariable Integer id) {
        log.info("Peticion REST para obtener el perfil publico del usuario: {}", id);
        try {
            UsuarioPerfilResponse perfil = usuarioService.obtenerPerfilPublico(id);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            log.error("Error al obtener perfil: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}