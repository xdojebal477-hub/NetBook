package com.ieshermanosmachado.netbook.dto;

import lombok.Data;

@Data
public class LibroUpdateRequest {
    private String titulo;
    private String sinopsis;
    private String genero;
    private String estado;
    private String portadaUrl;
}
