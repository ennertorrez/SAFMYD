package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Enner Torrez on 29/10/2019.
 */

public class SubZona {
    private String CodSubZona="";
    private String SubZona="";

    public SubZona(String codSubZona, String subZona) {
        this.CodSubZona = codSubZona;
        this.SubZona = subZona;
    }

    public SubZona() {
    }
    public String getCodSubZona() {
        return CodSubZona;
    }
    public void setCodSubZona(String codSubZona) {
        this.CodSubZona = codSubZona;}

    public String getSubZona() {
        return SubZona;
    }
    public void setSubZona(String subZona) {
        this.SubZona = subZona;}

    public String toString(){
        return this.getSubZona();
    }
}
