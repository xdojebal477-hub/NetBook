package com.ieshermanosmachado.netbook.dto;

public class ColeccionRequest {
    private String nombre;
    private Boolean esPublica = false;

    public ColeccionRequest() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Boolean getEsPublica() { return esPublica; }
    public void setEsPublica(Boolean esPublica) { this.esPublica = esPublica; }
}
