package com.saf.sistemas.safcafenorteno.Entidades;

import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Pedido {
   String CodigoPedido ="";
   String IdVendedor = "";
   String IdCliente = "";
    String Tipo="";
   String Observacion = "";
   String IdFormaPago = "";
   String IdSucursal = "";
   String Fecha = "";
   String Usuario = "";
   String IMEI ="" ;
   String Subtotal="";
    String Total="";
    String TCambio="";
    String Empresa="";

    public Pedido() {
    }

    public Pedido(String codigoPedido,String idVendedor, String idCliente,String tipo, String observacion, String idFormaPago, String idSucursal, String fecha, String usuario, String IMEI,String subtotal,String total,String tasa, String empresa) {
        CodigoPedido=codigoPedido;
        IdVendedor = idVendedor;
        IdCliente = idCliente;
        Tipo=tipo;
        Observacion = observacion;
        IdFormaPago = idFormaPago;
        IdSucursal = idSucursal;
        Fecha = fecha;
        Usuario = usuario;
        this.IMEI = IMEI;
        this.Subtotal=subtotal;
        this.Total=total;
        this.TCambio=tasa;
        this.Empresa=empresa;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getObservacion() {
        return Funciones.Decodificar( Observacion);
    }

    public void setObservacion(String observacion) {
        Observacion = observacion;
    }

    public String getIdFormaPago() {
        return IdFormaPago;
    }

    public void setIdFormaPago(String idFormaPago) {
        IdFormaPago = idFormaPago;
    }

    public String getIdSucursal() {
        return IdSucursal;
    }

    public void setIdSucursal(String idSucursal) {
        IdSucursal = idSucursal;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getCodigoPedido() {
        return CodigoPedido;
    }


    public void setCodigoPedido(String codigoPedido) {


        CodigoPedido = codigoPedido;
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

    public String getTCambio() {
        return TCambio;
    }

    public void setTCambio(String tasa) {
        TCambio = tasa;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String toString(){
        return  this.getCodigoPedido();
    }
}
