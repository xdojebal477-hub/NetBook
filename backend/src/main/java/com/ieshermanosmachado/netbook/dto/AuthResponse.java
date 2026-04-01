package com.ieshermanosmachado.netbook.dto;

import com.ieshermanosmachado.netbook.model.Rol;

public class AuthResponse {
    
    private String token;
    private String nombre;
    private String email;
    private Rol rol;

    public AuthResponse(String token, String nombre, String email, Rol rol) {
        this.token = token;
        this.nombre = nombre;
        this.email = email;
        this.rol = rol;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
