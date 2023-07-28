package com.saf.sistemas.safmyd.Entidades;

public class Bancos {
    private String Codigo;
    private String Nombre;

    public Bancos(String codigo, String nombre) {
        this.Codigo = codigo;
        this.Nombre = nombre;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        this.Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        this.Nombre = nombre;
    }

    public String toString(){
        return this.Nombre;
    }
}
