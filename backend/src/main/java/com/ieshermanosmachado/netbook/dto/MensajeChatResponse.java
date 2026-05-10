package com.ieshermanosmachado.netbook.dto;

import java.time.LocalDateTime;

public class MensajeChatResponse {
    private Integer id;
    private String contenido;
    private LocalDateTime fechaPublicacion;
    private Integer autorId;
    private String autorNombre;
    private String autorAvatarUrl;

    public MensajeChatResponse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }

    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public Integer getAutorId() { return autorId; }
    public void setAutorId(Integer autorId) { this.autorId = autorId; }

    public String getAutorNombre() { return autorNombre; }
    public void setAutorNombre(String autorNombre) { this.autorNombre = autorNombre; }

    public String getAutorAvatarUrl() { return autorAvatarUrl; }
    public void setAutorAvatarUrl(String autorAvatarUrl) { this.autorAvatarUrl = autorAvatarUrl; }
}
