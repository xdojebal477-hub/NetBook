package com.ieshermanosmachado.netbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponse {
    private Integer id;
    private String titulo;
    private String sinopsis;
    private String genero;
    private String archivoUrl;
    private String portadaUrl;
    private String estado;
    private String autorNombre;
    private LocalDateTime fechaSubida;
    private Double avgRating; // Rating promedio del libro
    private Long reviewCount; // Cantidad de reseñas
}
