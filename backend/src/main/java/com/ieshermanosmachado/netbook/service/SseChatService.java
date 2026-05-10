package com.ieshermanosmachado.netbook.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ieshermanosmachado.netbook.dto.MensajeChatResponse;

@Service
public class SseChatService {

    // Mapa para guardar los clientes conectados (Emitters) divididos por ID de Comunidad
    private final Map<Integer, List<SseEmitter>> emittersPorComunidad = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public SseChatService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public SseEmitter suscribir(Integer comunidadId) {
        // Tiempo de timeout infinito (0) o muy largo para que la conexión no se caiga
        SseEmitter emitter = new SseEmitter(-1L);
        
        emittersPorComunidad.putIfAbsent(comunidadId, new CopyOnWriteArrayList<>());
        emittersPorComunidad.get(comunidadId).add(emitter);

        // Callbacks para eliminar el emitter cuando el cliente se desconecte o haya un error
        emitter.onCompletion(() -> removeEmitter(comunidadId, emitter));
        emitter.onTimeout(() -> removeEmitter(comunidadId, emitter));
        emitter.onError((e) -> removeEmitter(comunidadId, emitter));

        // Enviar un evento inicial para confirmar suscripción o evitar caídas por inactividad
        try {
            emitter.send(SseEmitter.event().name("init").data("Conectado a la comunidad " + comunidadId));
        } catch (IOException e) {
            removeEmitter(comunidadId, emitter);
        }

        return emitter;
    }

    public void emitirMensaje(Integer comunidadId, MensajeChatResponse mensaje) {
        List<SseEmitter> emitters = emittersPorComunidad.getOrDefault(comunidadId, new ArrayList<>());
        
        // Convertimos el DTO a JSON para mandarlo por la tubería
        String jsonMessage;
        try {
            jsonMessage = objectMapper.writeValueAsString(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name("nuevo_mensaje")
                        .data(jsonMessage));
            } catch (IOException e) {
                // Si falla el envío (el cliente cerró la pestaña), lo marcamos para borrar
                deadEmitters.add(emitter);
            }
        }
        
        // Limpiamos la memoria de los que se fueron
        emitters.removeAll(deadEmitters);
    }

    private void removeEmitter(Integer comunidadId, SseEmitter emitter) {
        List<SseEmitter> emitters = emittersPorComunidad.get(comunidadId);
        if (emitters != null) {
            emitters.remove(emitter);
            if (emitters.isEmpty()) {
                emittersPorComunidad.remove(comunidadId);
            }
        }
    }
}
