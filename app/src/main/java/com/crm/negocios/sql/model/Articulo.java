package com.crm.negocios.sql.model;

public class Articulo {
    private long Cod;
    private String Nombre;
    private int UnidadMedida;
    private double PrecioUnitario;
    private int Marca;
    private String EstadoRegistro;

    public Articulo(long cod, String nombre, int unidadMedida, double precioUnitario, int marca, String estadoRegistro) {
        Nombre = nombre;
        UnidadMedida = unidadMedida;
        PrecioUnitario = precioUnitario;
        Marca = marca;
        EstadoRegistro = estadoRegistro;
        Cod = cod;
    }
    public Articulo(String nombre, int unidadMedida, double precioUnitario, int marca) {
        Nombre = nombre;
        UnidadMedida = unidadMedida;
        PrecioUnitario = precioUnitario;
        Marca = marca;
        EstadoRegistro = "A";
    }
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadMedida(int unidadMedida) {
        UnidadMedida = unidadMedida;
    }

    public double getPrecioUnitario() {
        return PrecioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        PrecioUnitario = precioUnitario;
    }

    public int getMarca() {
        return Marca;
    }

    public void setMarca(int marca) {
        Marca = marca;
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
