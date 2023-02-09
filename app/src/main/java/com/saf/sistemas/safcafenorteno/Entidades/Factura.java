package com.saf.sistemas.safcafenorteno.Entidades;

import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;

public class Factura {
    String Factura ="";
    String Fecha = "";
    String Cliente = "";
    String Monto="";
    String SubTotal = "";
    String Descuento = "";
    String Iva = "";
    String Total = "";
    String Vendedor = "";
    String Tipo ="" ;
    String Tipo_Factura="";
    String Empresa="";
    String Usuario="";
    String Sucursal="";
    String Ruta="";
    String Orden="";
    String Guardada="";

    public Factura() {
    }

    public Factura(String factura,String fecha, String cliente,String monto, String subTotal, String descuento, String iva, String total, String vendedor, String tipo,String tipo_Factura,String empresa,String usuario, String sucursal,String ruta, String orden,String guardada) {
        Factura=factura;
        Fecha = fecha;
        Cliente = cliente;
        Monto=monto;
        SubTotal = subTotal;
        Descuento = descuento;
        Iva = iva;
        Total = total;
        Vendedor = vendedor;
        Tipo = tipo;
        Tipo_Factura=tipo_Factura;
        Empresa=empresa;
        Usuario=usuario;
        Sucursal=sucursal;
        Ruta=ruta;
        Orden=orden;
        Guardada=guardada;
    }

    public String getFactura() {
        return Factura;
    }
    public String getFecha() {
        return Fecha;
    }
    public String getCliente() {
        return Cliente;
    }
    public String getMonto() {
        return Monto;
    }
    public String getSubTotal() {
        return SubTotal;
    }
    public String getDescuento() {
        return Descuento;
    }
    public String getIva() {
        return Iva;
    }
    public String getTotal() {
        return Total;
    }
    public String getVendedor() {
        return Vendedor;
    }
    public String getTipo() {
        return Tipo;
    }
    public String getTipo_Factura() {
        return Tipo_Factura;
    }
    public String getEmpresa() {
        return Empresa;
    }
    public String getUsuario() {
        return Usuario;
    }
    public String getSucursal() {
        return Sucursal;
    }
    public String getRuta() {
        return Ruta;
    }
    public String getOrden() {
        return Orden;
    }
    public String getGuardada() {
        return Guardada;
    }

    public void setFactura(String factura) {
        Factura = factura;
    }
    public void setFecha(String fecha) {
        Fecha = fecha;
    }
    public void setCliente(String cliente) {
        Cliente = cliente;
    }
    public void setMonto(String monto) {
        Monto = monto;
    }
    public void setSubTotal(String subtotal) {
        SubTotal = subtotal;
    }
    public void setDescuento(String descuento) {
        Descuento = descuento;
    }
    public void setIva(String iva) {
        Iva = iva;
    }
    public void setTotal(String total) {
        Total = total;
    }
    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }
    public void setTipo(String tipo) {
        Tipo = tipo;
    }
    public void setTipo_Factura(String tipo_factura) {
        Tipo_Factura = tipo_factura;
    }
    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }
    public void setUsuario(String usuario) {
        Usuario = usuario;
    }
    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }
    public void setRuta(String ruta) {
        Ruta = ruta;
    }
    public void setOrden(String orden) {
        Orden = orden;
    }
    public void setGuardada(String guardada) {
        Guardada = guardada;
    }

    public String toString(){

        return  this.getFactura();
    }
}
