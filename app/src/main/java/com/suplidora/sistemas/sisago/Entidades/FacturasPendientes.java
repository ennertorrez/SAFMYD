package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 26/3/2018
 */

public class FacturasPendientes {

    String codvendedor ="";
    String No_Factura ="";
    String Cliente ="";
    String CodigoCliente ="";
    String Fecha ="";
    String IVA="";
    String Tipo="";
    String SubTotal ="";
    String Descuento ="";
    String Total ="";
    String Abono="";
    String Saldo="";

    public FacturasPendientes() {
    }
    public FacturasPendientes(String idvendedor, String factura, String cliente, String idCliente, String fecha, String iva, String tipo, String subtotal, String descuento,
                              String total, String abono, String saldo) {
        codvendedor = idvendedor;
        No_Factura = factura;
        Cliente = cliente;
        CodigoCliente = idCliente;
        Fecha=fecha;
        IVA = iva;
        Tipo = tipo;
        SubTotal = subtotal;
        Descuento = descuento;
        Total = total;
        Abono = abono;
        Saldo = saldo;
    }

    public String getcodvendedor() {
        return codvendedor;
    }

    public void setcodvendedor(String idvendedor) {
        codvendedor = idvendedor;
    }

    public String getNo_Factura() {
        return No_Factura;
    }

    public void setNo_Factura(String factura) {
        No_Factura = factura;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getCodigoCliente() {
        return CodigoCliente;
    }

    public void setCodigoCliente(String idCliente) {
        CodigoCliente = idCliente;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getIVA() {
        return IVA;
    }

    public void setIVA(String iva) {
        IVA = iva;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(String subtotal) {
        SubTotal = subtotal;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getAbono() {
        return Abono;
    }

    public void setAbono(String abono) {
        Abono = abono;
    }

    public String getSaldo() {
        return Saldo;
    }

    public void setSaldo(String saldo  ) {
        Saldo =saldo;
    }

}
