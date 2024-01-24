package com.crm.negocios.sql.model;

public class UnidadMedida {
    private long Cod;
    private String Nombre;
    private String EstadoRegistro;

    public UnidadMedida( long cod, String nombre, String estadoRegistro) {
        Nombre = nombre;
        EstadoRegistro = estadoRegistro;
        Cod = cod;
    }

    public UnidadMedida(String nombre) {
        Nombre = nombre;
        EstadoRegistro = "A";
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getEstadoRegistro() {
        return EstadoRegistro;
    }

    public void setEstadoRegistro(String estadoRegistro) {
        EstadoRegistro = estadoRegistro;
    }

    public long getCod() {
        return Cod;
    }

    public void setCod(long cod) {
        Cod = cod;
    }
}
