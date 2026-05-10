package com.ieshermanosmachado.netbook.dto;

import java.util.Set;

public class ComunidadRequest {
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private Set<String> generos;

    public ComunidadRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public Set<String> getGeneros() { return generos; }
    public void setGeneros(Set<String> generos) { this.generos = generos; }
}
