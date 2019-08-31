package com.suplidora.sistemas.sisago.Entidades;

public class DescuentoEspecial {
    String IdCliente = "";
    String CodigoArticulo = "";
    String Porcentaje = "";
    String Canal="";

    public DescuentoEspecial() {
    }

    public DescuentoEspecial(String idCliente,String codigoArticulo,  String porcentaje, String canal) {
        IdCliente = idCliente;
        CodigoArticulo = codigoArticulo;
        Porcentaje = porcentaje;
        Canal = canal;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getCodigoArticulo() {
        return CodigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        CodigoArticulo = codigoArticulo;
    }

    public String getPorcentaje() {
        return Porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        Porcentaje = porcentaje;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }

}
