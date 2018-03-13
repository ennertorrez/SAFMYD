package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 14/2/2018.
 */

public class Barrios {
    private String Codigo_Barrio="";
    private String Nombre_Barrio="";

    public Barrios( String Codigo_Barrio, String Nombre_Barrio) {
        this.Codigo_Barrio = Codigo_Barrio;
        this.Nombre_Barrio = Nombre_Barrio;
    }

    public Barrios() {
    }

    public String getCodigo_Barrio() {
        return Codigo_Barrio;
    }
    public void setCodigo_Barrio(String Codigo_Barrio) {
        this.Codigo_Barrio = Codigo_Barrio;
    }
    public String getNombre_Barrio() {
        return Nombre_Barrio;
    }
    public void setNombre_Barrio(String Nombre_Barrio) {
        this.Nombre_Barrio = Nombre_Barrio;
    }
    public String toString(){
        return this.getNombre_Barrio();
    }
}
