package com.suplidora.sistemas.Entidades;

/**
 * Created by Sistemas on 11/5/2017.
 */

public class PedidoDetalle {

    String CodigoPedido ="";
    String CodigoArticulo ="";
    String Descripcion ="";
    String Cantidad ="";
    String BonificaA ="";
    String TipoArt="";
    String PorDescuento="";
    String Descuento ="";
    String Isc ="";
    String Costo ="";
    String TipoPrecio="";
    String Precio="";
    String PorcentajeIva="";
    String Iva="";
    String Um="";
    String Subtotal ="";
    String Total ="";



    public PedidoDetalle() {
    }

    public PedidoDetalle(String codigoPedido, String codigoArticulo, String descripcion, String cantidad, String bonificaA,String tipoArt, String descuento, String isc, String costo, String precio,String tipoPrecio, String porcentajeIva, String iva, String um, String subtotal, String total,String porDescuento) {
        CodigoPedido = codigoPedido;
        CodigoArticulo = codigoArticulo;
        Descripcion = descripcion;
        Cantidad = cantidad;
        TipoArt=tipoArt;
        BonificaA = bonificaA;
        Descuento = descuento;
        Isc = isc;
        Costo = costo;
        Precio = precio;
        PorcentajeIva = porcentajeIva;
        Iva = iva;
        Um = um;
        Subtotal = subtotal;
        Total = total;
        PorDescuento=porDescuento;
        TipoPrecio=tipoPrecio;
    }

    public String getCodigoPedido() {
        return CodigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        CodigoPedido = codigoPedido;
    }

    public String getCodigoArticulo() {
        return CodigoArticulo;
    }

    public void setCodigoArticulo(String codigoArticulo) {
        CodigoArticulo = codigoArticulo;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getCantidad() {
        return Cantidad;
    }

    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }

    public String getBonificaA() {
        return BonificaA;
    }

    public void setBonificaA(String bonificaA) {
        BonificaA = bonificaA;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getIsc() {
        return Isc;
    }

    public void setIsc(String isc) {
        Isc = isc;
    }

    public String getCosto() {
        return Costo;
    }

    public void setCosto(String costo) {
        Costo = costo;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getPorcentajeIva() {
        return PorcentajeIva;
    }

    public void setPorcentajeIva(String porcentajeIva) {
        PorcentajeIva = porcentajeIva;
    }

    public String getIva() {
        return Iva;
    }

    public void setIva(String iva) {
        Iva = iva;
    }

    public String getUm() {
        return Um;
    }

    public void setUm(String um) {
        Um = um;
    }

    public String getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getTipoArt() {
        return TipoArt;
    }

    public void setTipoArt(String tipoArt) {
        TipoArt = tipoArt;
    }

    public String getPorDescuento() {
        return PorDescuento;
    }

    public void setPorDescuento(String porDescuento) {
        PorDescuento = porDescuento;
    }

    public String getTipoPrecio() {
        return TipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        TipoPrecio = tipoPrecio;
    }
}
