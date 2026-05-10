package com.ieshermanosmachado.netbook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaResponse {
    private Integer id;
    private Integer usuarioId;
    private String usuarioNombre;
    private Integer libroId;
    private Double puntuacion;
    private String comentario;
    private String sentimientoIA;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
