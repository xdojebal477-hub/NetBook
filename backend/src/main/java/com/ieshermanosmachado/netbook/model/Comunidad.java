package com.ieshermanosmachado.netbook.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ieshermanosmachado.netbook.config.AppUrlConfig;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "comunidades")
public class Comunidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comunidad")
    private Integer id;

    @Column(nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Relación con el usuario fundador
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_propietario", nullable = false)
    private Usuario propietario;

    // Los múltiples géneros que aborda esta comunidad
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "comunidad_generos", joinColumns = @JoinColumn(name = "id_comunidad"))
    @Column(name = "genero", length = 50)
    private Set<String> generos = new HashSet<>();

    // Miembros que se han unido a la sala de chat
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "comunidad_miembros",
        joinColumns = @JoinColumn(name = "id_comunidad"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<Usuario> miembros = new ArrayList<>();

    // Relación bidireccional con MensajeChat
    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MensajeChat> mensajes = new ArrayList<>();

    // Constructores genéricos
    public Comunidad() {
    }

    public Comunidad(String nombre, String descripcion, Usuario propietario) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietario = propietario;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        if (imagenUrl != null && imagenUrl.startsWith("/api/")) {
            return AppUrlConfig.getPublicBaseUrl() + imagenUrl;
        }
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    public Set<String> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<String> generos) {
        this.generos = generos;
    }

    public List<Usuario> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Usuario> miembros) {
        this.miembros = miembros;
    }

    public List<MensajeChat> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<MensajeChat> mensajes) {
        this.mensajes = mensajes;
    }

    // Métodos para asignar miembros y géneros
    public void addMiembro(Usuario usuario) {
        if (!this.miembros.contains(usuario)) {
            this.miembros.add(usuario);
        }
    }

    public void removeMiembro(Usuario usuario) {
        this.miembros.remove(usuario);
    }
}
