package com.suplidora.sistemas.Entidades;

/**
 * Created by usuario on 18/5/2017.
 */

public class Configuraciones {
    String Id="";
    String Sistema="";
    String Configuracion ="";
    String Valor ="";
    String Activo="";

    public Configuraciones() {
    }

    public Configuraciones(String id, String sistema, String configuracion, String valor, String activo) {
        Id = id;
        Sistema = sistema;
        Configuracion = configuracion;
        Valor = valor;
        Activo = activo;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSistema() {
        return Sistema;
    }

    public void setSistema(String sistema) {
        Sistema = sistema;
    }

    public String getConfiguracion() {
        return Configuracion;
    }

    public void setConfiguracion(String configuracion) {
        Configuracion = configuracion;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }

    public String getActivo() {
        return Activo;
    }

    public void setActivo(String activo) {
        Activo = activo;
    }
}