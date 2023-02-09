package com.saf.sistemas.safcafenorteno.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Vendedor {

    String CODIGO;
    String NOMBRE;
    String IDRUTA;
    String RUTA;
    String EMPRESA;
    String codsuper;
    String Supervisor;
    String Status;

    public Vendedor(String CODIGO, String NOMBRE,  String IDRUTA, String RUTA, String EMPRESA, String codsuper, String Supervisor, String status) {
        this.CODIGO = CODIGO;
        this.NOMBRE = NOMBRE;
        this.IDRUTA = IDRUTA;
        this.RUTA = RUTA;
        this.EMPRESA=EMPRESA;
        this.codsuper = codsuper;
        this.Supervisor = Supervisor;
        Status = status;
    }

    public Vendedor() {

    }

    public String getCODIGO() {
        return  CODIGO;
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

    public String getIDRUTA() {
        return IDRUTA;
    }

    public void setIDRUTA(String IDRUTA) {
        this.IDRUTA = IDRUTA;
    }

    public String getEMPRESA() {
        return EMPRESA;
    }

    public void setEMPRESA(String EMPRESA) {
        this.EMPRESA = EMPRESA;
    }

    public String getRUTA() {
        return RUTA;
    }

    public void setRUTA(String RUTA) {
        this.RUTA = RUTA;
    }

    public String getCodsuper() {
        return codsuper;
    }

    public void setCodsuper(String codsuper) {
        this.codsuper = codsuper;
    }

    public  String getSupervisor(){return Supervisor ;}

    public  void setSupervisor(String Supervisor){this.Supervisor = Supervisor;}

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public String toString(){
        return this.getNOMBRE();
    }
}
