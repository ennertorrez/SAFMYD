package com.saf.sistemas.safcafenorteno.Entidades;

/**
 * Created by Enner Torrez on 26/10/2019.
 */

public class UnidadMedida {
    private String Cod_UM="";
    private String Um="";

    public UnidadMedida(String codigo_UM, String um) {
        this.Cod_UM = codigo_UM;
        this.Um = um;
    }

    public UnidadMedida() {
    }
    public String getCod_UM() {
        return Cod_UM;
    }
    public void setCod_UM(String cod_um) {
        this.Cod_UM = cod_um;}

    public String getUm() {
        return Um;
    }
    public void setUm(String um) {
        this.Um = um;}

    public String toString(){
        return this.getUm();
    }
}
