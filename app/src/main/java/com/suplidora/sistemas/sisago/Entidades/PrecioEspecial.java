package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class PrecioEspecial {
    String Id = "";
    String CodigoArticulo = "";
    String IdCliente = "";
    String Descuento = "";
    String Precio = "";
    String Facturar="";

    public PrecioEspecial() {
    }

    public PrecioEspecial(String id, String codigoArticulo, String idCliente, String descuento, String precio,String facturar) {
        Id = id;
        CodigoArticulo = codigoArticulo;
        IdCliente = idCliente;
        Descuento = descuento;
        Precio = precio;
        Facturar=facturar;
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

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getFacturar() {
        return Facturar;
    }

    public void setFacturar(String facturar) {
        Facturar = facturar;
    }
}
