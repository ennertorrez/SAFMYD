package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Enner Torrez on 22/10/2019.
 */

public class Zonas {
    String Empresa = "";
    String Cod_Zona = "";
    String Zona = "";
    String Cod_SubZona = "";
    String SubZona = "";

    public Zonas() {
    }

    public Zonas(String empresa, String cod_Zona, String zona, String cod_SubZona, String subZona) {
        this.Empresa = empresa;
        this.Cod_Zona = cod_Zona;
        this.Zona = zona;
        this.Cod_SubZona = cod_SubZona;
        this.SubZona = subZona;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        this.Empresa = empresa;
    }

    public String getCod_Zona() {
        return Cod_Zona;
    }

    public void setCod_Zona(String cod_Zona) {
        this.Cod_Zona = cod_Zona;
    }

    public String getZona() {
        return Zona;
    }

    public void setZona(String zona) { this.Zona = zona; }

    public String getCod_SubZona() {
        return Cod_SubZona;
    }

    public void setCod_SubZona(String cod_SubZona) {
        this.Cod_SubZona = cod_SubZona;
    }

    public String getSubZona() {
        return SubZona;
    }

    public void setSubZona(String subZona) {
        this.SubZona = subZona;
    }
}
