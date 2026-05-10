package com.ieshermanosmachado.netbook.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class ComunidadResponse {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private LocalDateTime fechaCreacion;
    private Integer propietarioId;
    private String propietarioNombre;
    private Integer numeroMiembros;
    private boolean esMiembro;
    private Set<String> generos;

    public ComunidadResponse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUrl() { return imagenUrl; }
    public void setImagenUrl(String imagenUrl) { this.imagenUrl = imagenUrl; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Integer getPropietarioId() { return propietarioId; }
    public void setPropietarioId(Integer propietarioId) { this.propietarioId = propietarioId; }

    public String getPropietarioNombre() { return propietarioNombre; }
    public void setPropietarioNombre(String propietarioNombre) { this.propietarioNombre = propietarioNombre; }

    public Integer getNumeroMiembros() { return numeroMiembros; }
    public void setNumeroMiembros(Integer numeroMiembros) { this.numeroMiembros = numeroMiembros; }

    public boolean isEsMiembro() { return esMiembro; }
    public void setEsMiembro(boolean esMiembro) { this.esMiembro = esMiembro; }

    public Set<String> getGeneros() { return generos; }
    public void setGeneros(Set<String> generos) { this.generos = generos; }
}
