package com.saf.sistemas.safmyd.Entidades;

public class Model2 {
    private String codigo;
    private String precio;
    private String nombre;
    private String existencia;
    private boolean selected;

    public Model2(String codigo,String precio,String nombre,String existencia) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
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
    public String getExistencia() {
        return existencia;
    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
