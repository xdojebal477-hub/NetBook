package com.ieshermanosmachado.netbook.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "colecciones")
public class Coleccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_coleccion")
    private Integer id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "es_publica", nullable = false)
    private Boolean esPublica = false;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_propietario", nullable = false)
    private Usuario propietario;

    @ManyToMany
    @JoinTable(
            name = "colecciones_libros",
            joinColumns = @JoinColumn(name = "id_coleccion"),
            inverseJoinColumns = @JoinColumn(name = "id_libro")
    )
    private List<Libro> libros = new ArrayList<>();

    public Coleccion() {
    }

    public Coleccion(String nombre, Boolean esPublica, Usuario propietario) {
        this.nombre = nombre;
        this.esPublica = esPublica;
        this.propietario = propietario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getEsPublica() { return esPublica; }
    public void setEsPublica(Boolean esPublica) { this.esPublica = esPublica; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public Usuario getPropietario() { return propietario; }
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }

    public List<Libro> getLibros() { return libros; }
    public void setLibros(List<Libro> libros) { this.libros = libros; }
}
