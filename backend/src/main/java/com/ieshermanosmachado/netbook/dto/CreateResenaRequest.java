package com.ieshermanosmachado.netbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateResenaRequest {
    private Double puntuacion; // 0.5 a 5.0
    private String comentario; // opcional, máximo 1000 caracteres
}
