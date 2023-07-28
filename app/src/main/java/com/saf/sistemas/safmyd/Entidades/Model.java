package com.saf.sistemas.safmyd.Entidades;

public class Model {
    private String codigo;
    private String precio;
    private String nombre;
    private String existencia;
    private boolean selected;

    public Model(String codigo,String precio,String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public String getPrecio() {
        return precio;
    }
    public String getCodigo() {
        return codigo;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
