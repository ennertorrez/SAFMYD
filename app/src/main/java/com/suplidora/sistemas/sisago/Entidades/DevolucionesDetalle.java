package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class DevolucionesDetalle {
   String    ID  = "";
   String   ndevolucion = "";
   String  item  = "";
    String item_descripcion="";
    String   cantidad ="";
   String  precio  = "";
   String   iva  = "";
   String  subtotal  = "";
   String  total  = "";
   String   poriva  = "";
   String    descuento  ="" ;
   String  tipo ="";
   String numero="";
   String factura="";



    public DevolucionesDetalle() {
    }

    public DevolucionesDetalle(String iD, String Ndevolucion, String Item,String Item_descripcion, String Cantidad, String Precio, String Iva, String Subtotal,
                               String Total, String Poriva, String Descuento, String Tipo, String Numero, String Factura) {
        ID=iD;
        ndevolucion = Ndevolucion;
        item = Item;
        cantidad = Cantidad;
        precio=Precio;
        iva = Iva;
        subtotal = Subtotal;
        total = Total;
        poriva = Poriva;
        descuento = Descuento;
        tipo = Tipo;
        numero = Numero;
        factura = Factura;
        item_descripcion=Item_descripcion;

    }

    public String getItem_descripcion() {
        return item_descripcion;
    }

    public void setItem_descripcion(String item_descripcion) {
        this.item_descripcion = item_descripcion;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNdevolucion() {
        return ndevolucion;
    }

    public void setNdevolucion(String ndevolucion) {
        this.ndevolucion = ndevolucion;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPoriva() {
        return poriva;
    }

    public void setPoriva(String poriva) {
        this.poriva = poriva;
    }

    public String getDescuento() {
        return descuento;
    }

    public void setDescuento(String descuento) {
        this.descuento = descuento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }
}
