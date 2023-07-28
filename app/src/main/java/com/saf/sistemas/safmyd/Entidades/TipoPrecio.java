package com.saf.sistemas.safmyd.Entidades;

/**
 * Created by Enner Torrez on 29/10/2019.
 */

public class TipoPrecio {
    private String Cod_Tipo_Precio="";
    private String Tipo_Precio="";

    public TipoPrecio(String cod_Tipo_Precio, String tipo_Precio) {
        this.Cod_Tipo_Precio = cod_Tipo_Precio;
        this.Tipo_Precio = tipo_Precio;
    }

    public TipoPrecio() {
    }
    public String getCod_Tipo_Precio() {
        return Cod_Tipo_Precio;
    }
    public void setCod_Tipo_Precio(String cod_Tipo_Precio) {
        this.Cod_Tipo_Precio = cod_Tipo_Precio;}

    public String getTipo_Precio() {
        return Tipo_Precio;
    }
    public void setTipo_Precio(String tipo_Precio) {
        this.Tipo_Precio = tipo_Precio;}

    public String toString(){
        return this.getTipo_Precio();
    }
}
