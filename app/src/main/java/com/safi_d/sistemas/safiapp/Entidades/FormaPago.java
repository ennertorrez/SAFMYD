package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class FormaPago {
    String CODIGO = "" ;
    String NOMBRE = "" ;
    String DIAS = "" ;
    String EMPRESA = "" ;

    public FormaPago() {
    }

    public FormaPago(String CODIGO, String NOMBRE, String DIAS, String EMPRESA) {
        this.CODIGO = CODIGO;
        this.NOMBRE = NOMBRE;
        this.DIAS = DIAS;
        this.EMPRESA = EMPRESA;
    }

    public String getCODIGO() {
        return CODIGO;
    }

    public void setCODIGO(String CODIGO) {
        this.CODIGO = CODIGO;
    }

    public String getNOMBRE() {
        return NOMBRE;
    }

    public void setNOMBRE(String NOMBRE) {
        this.NOMBRE = NOMBRE;
    }

    public String getDIAS() {
        return DIAS;
    }

    public void setDIAS(String DIAS) {
        this.DIAS = DIAS;
    }

    public String getEMPRESA() {
        return EMPRESA;
    }

    public void setEMPRESA(String EMPRESA) {
        this.EMPRESA = EMPRESA;
    }
    public String toString(){
        return this.getNOMBRE();
    }
}
