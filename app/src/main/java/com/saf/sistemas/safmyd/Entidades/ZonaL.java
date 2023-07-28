package com.saf.sistemas.safmyd.Entidades;

/**
 * Created by Enner Torrez on 29/10/2019.
 */

public class ZonaL {
    private String CodZona="";
    private String Zona="";

    public ZonaL(String codZona, String zona) {
        this.CodZona = codZona;
        this.Zona = zona;
    }

    public ZonaL() {
    }
    public String getCodZona() {
        return CodZona;
    }
    public void setCodZona(String codZona) {
        this.CodZona = codZona;}

    public String getZona() {
        return Zona;
    }
    public void setZona(String zona) {
        this.Zona = zona;}

    public String toString(){
        return this.getZona();
    }
}
