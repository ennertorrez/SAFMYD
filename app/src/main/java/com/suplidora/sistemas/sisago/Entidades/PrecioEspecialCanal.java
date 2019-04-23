package com.suplidora.sistemas.sisago.Entidades;

public class PrecioEspecialCanal {
    String Id = "";
    String CodigoArticulo = "";
    String Canal = "";
    String Precio = "";

    public PrecioEspecialCanal() {
    }

    public PrecioEspecialCanal(String id, String codigoArticulo, String canal,  String precio) {
        Id = id;
        CodigoArticulo = codigoArticulo;
        Canal = canal;
        Precio = precio;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCodigoArticulo() {
        return CodigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        CodigoArticulo = codigoArticulo;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }


    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

}
