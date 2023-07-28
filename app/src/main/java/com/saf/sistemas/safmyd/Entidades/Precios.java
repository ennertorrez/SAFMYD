package com.saf.sistemas.safmyd.Entidades;

/**
 * Created by Enner Torrez on 25/10/2019.
 */

public class Precios {
    String Id = "";
    String CodigoArticulo = "";
    String IdCliente = "";
    String Descuento = "";
    String Precio = "";
    String Facturar="";

    public Precios(String id, String codigoArticulo, String idCliente, String descuento, String precio,String facturar) {
        Id = id;
        CodigoArticulo = codigoArticulo;
        IdCliente = idCliente;
        Descuento = descuento;
        Precio = precio;
        Facturar=facturar;
    }

    public Precios() {

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
