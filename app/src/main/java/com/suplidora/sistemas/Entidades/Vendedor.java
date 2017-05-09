package com.suplidora.sistemas.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Vendedor {

    String CODIGO;
    String NOMBRE;
    String COD_ZONA;
    String RUTA;
    String codsuper;
    String Status;
    String detalle;
    String horeca;
    String mayorista;

    public Vendedor(String CODIGO, String NOMBRE, String COD_ZONA, String RUTA, String codsuper, String status, String detalle, String horeca, String mayorista) {
        this.CODIGO = CODIGO;
        this.NOMBRE = NOMBRE;
        this.COD_ZONA = COD_ZONA;
        this.RUTA = RUTA;
        this.codsuper = codsuper;
        Status = status;
        this.detalle = detalle;
        this.horeca = horeca;
        this.mayorista = mayorista;
    }

    public Vendedor() {

    }

    public int getCODIGO() {
        return Integer.parseInt( CODIGO);
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

    public String getCOD_ZONA() {
        return COD_ZONA;
    }

    public void setCOD_ZONA(String COD_ZONA) {
        this.COD_ZONA = COD_ZONA;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getHoreca() {
        return horeca;
    }

    public void setHoreca(String horeca) {
        this.horeca = horeca;
    }

    public String getMayorista() {
        return mayorista;
    }

    public void setMayorista(String mayorista) {
        this.mayorista = mayorista;
    }

    public String toString(){
        return this.getNOMBRE();
    }
}
