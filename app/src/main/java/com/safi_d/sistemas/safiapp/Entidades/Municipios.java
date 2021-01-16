package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 14/2/2018.
 */

public class Municipios {
    private String Codigo_Municipio="";
    private String Nombre_Municipio="";

    public Municipios(String Codigo_Municipio, String Nombre_Municipio) {
        this.Codigo_Municipio = Codigo_Municipio;
        this.Nombre_Municipio = Nombre_Municipio;
    }

    public Municipios() {
    }
    public String getCodigo_Municipio() {
        return Codigo_Municipio;
    }
    public void setCodigo_Municipio(String Codigo_Municipio) {
        this.Codigo_Municipio = Codigo_Municipio;}

    public String getNombre_Municipio() {
        return Nombre_Municipio;
    }
    public void setNombre_Municipio(String Nombre_Municipio) {
        this.Nombre_Municipio = Nombre_Municipio;}

    public String toString(){
        return this.getNombre_Municipio();
    }
}
