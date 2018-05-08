package com.suplidora.sistemas.sisago.Entidades;

import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

/**
 * Created by Sistemas on 16/3/2018.
 */

public class Informe {
   String CodInforme ="";
    String Fecha="";
   String IdVendedor = "";
   String Aprobada = "";
    String Anulada="";
   String Imei = "";
    String Usuario="";

    public Informe() {
    }

    public Informe(String codigoInforme, String fecha, String idVendedor, String aprobado, String anulado, String imei, String usuario) {
        CodInforme =codigoInforme;
        Fecha=fecha;
        IdVendedor =idVendedor;
        Aprobada = aprobado;
        Anulada=anulado;
        Imei = imei;
        Usuario=usuario;
    }

    public String getCodigoInforme() {
        return CodInforme;
    }

    public void setCodigoInforme(String codigo) {
        CodInforme = codigo;
    }

    public String getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getAprobada() {
        return Aprobada;
    }

    public void setAprobada(String aprobada) {
        Aprobada = aprobada;
    }

    public String getAnulada() {
        return Anulada;
    }

    public void setAnulada(String anulada) {
        Anulada = anulada;
    }

    public String getImei() {
        return Imei;
    }

    public void setImei(String imei) {
        Imei = imei;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String toString(){
        return  this.getCodigoInforme();
    }
}
