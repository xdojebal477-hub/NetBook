package com.ieshermanosmachado.netbook.dto;

import java.time.LocalDateTime;

public record UsuarioResponse(
    Integer id,
    String nombre,
    String email,
    String rol,
    LocalDateTime fechaRegistro
) {}
