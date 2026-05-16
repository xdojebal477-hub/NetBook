package com.ieshermanosmachado.netbook.model;

import java.time.LocalDateTime;

import com.ieshermanosmachado.netbook.config.AppUrlConfig;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro")
    private Integer id;

    // Relación N:1 con Usuario (Muchos libros pertenecen a un autor)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_autor", nullable = false)
    private Usuario autor;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    @Column(length = 50)
    private String genero;

    @Column(name = "portada_url")
    private String portadaUrl;

    // Ruta física del archivo dentro de nuestro servidor
    @Column(name = "archivo_url", nullable = false)
    private String archivoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoLibro estado = EstadoLibro.BORRADOR;

    @Column(name = "resumen_ia", columnDefinition = "TEXT")
    private String resumenIa;

    @Column(name = "fecha_subida", insertable = false, updatable = false)
    private LocalDateTime fechaSubida;

    public Libro() {
    }

    public Libro(Usuario autor, String titulo, String sinopsis, String genero, String archivoUrl, EstadoLibro estado) {
        this.autor = autor;
        this.titulo = titulo;
        this.sinopsis = sinopsis;
        this.genero = genero;
        this.archivoUrl = archivoUrl;
        this.estado = estado;
    }

    // --- GETTERS Y SETTERS ---

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getAutor() { return autor; }
    public void setAutor(Usuario autor) { this.autor = autor; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getPortadaUrl() { 
        if (portadaUrl != null && portadaUrl.startsWith("/api/")) {
            return AppUrlConfig.getPublicBaseUrl() + portadaUrl;
        }
        return portadaUrl; 
    }
    public void setPortadaUrl(String portadaUrl) { this.portadaUrl = portadaUrl; }

    public String getArchivoUrl() { return archivoUrl; }
    public void setArchivoUrl(String archivoUrl) { this.archivoUrl = archivoUrl; }

    public EstadoLibro getEstado() { return estado; }
    public void setEstado(EstadoLibro estado) { this.estado = estado; }

    public String getResumenIa() { return resumenIa; }
    public void setResumenIa(String resumenIa) { this.resumenIa = resumenIa; }

    public LocalDateTime getFechaSubida() { return fechaSubida; }
    public void setFechaSubida(LocalDateTime fechaSubida) { this.fechaSubida = fechaSubida; }
}
