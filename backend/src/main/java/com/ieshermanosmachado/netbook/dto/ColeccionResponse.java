package com.ieshermanosmachado.netbook.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ColeccionResponse {
    private Integer id;
    private String nombre;
    private Boolean esPublica;
    private LocalDateTime fechaCreacion;
    private Integer numeroLibros;
    private Integer propietarioId;
    private List<LibroResponse> libros;

    public ColeccionResponse() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getEsPublica() { return esPublica; }
    public void setEsPublica(Boolean esPublica) { this.esPublica = esPublica; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Integer getNumeroLibros() { return numeroLibros; }
    public void setNumeroLibros(Integer numeroLibros) { this.numeroLibros = numeroLibros; }

    public Integer getPropietarioId() { return propietarioId; }
    public void setPropietarioId(Integer propietarioId) { this.propietarioId = propietarioId; }

    public List<LibroResponse> getLibros() { return libros; }
    public void setLibros(List<LibroResponse> libros) { this.libros = libros; }
}
