package com.ieshermanosmachado.netbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioPerfilResponse {
    private Integer id;
    private String nombre;
    private String avatarUrl;
    private String rol;
    private LocalDateTime fechaRegistro;
    
    private List<ColeccionResumenDTO> coleccionesPublicas;
    private List<LibroResumenDTO> obrasPublicadas; // Solo si el usuario es AUTOR
    private List<ComunidadResumenDTO> comunidades;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ColeccionResumenDTO {
        private Integer id;
        private String nombre;
        private LocalDateTime fechaCreacion;
        private List<LibroResumenDTO> libros;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LibroResumenDTO {
        private Integer id;
        private String titulo;
        private String portadaUrl;
        private String genero;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComunidadResumenDTO {
        private Integer id;
        private String nombre;
        private String descripcion;
        private String imagenUrl;
        private LocalDateTime fechaCreacion;
        private String propietarioNombre;
        private Integer numeroMiembros;
    }
}