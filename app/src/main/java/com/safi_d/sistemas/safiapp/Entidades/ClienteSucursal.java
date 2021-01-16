package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class ClienteSucursal {

    String CodSuc = "" ;
    String CodCliente = "" ;
    String Sucursal = "" ;
    String Ciudad = "" ;
    String DeptoID = "" ;
    String Direccion = "" ;
    String FormaPagoID = "" ;
    String Descuento ="";

    public ClienteSucursal() {
    }

    public ClienteSucursal(String codSuc, String codCliente, String sucursal, String ciudad, String deptoID, String direccion, String formaPagoID,String descuento) {

        CodSuc = codSuc;
        CodCliente = codCliente;
        Sucursal = sucursal;
        Ciudad = ciudad;
        DeptoID = deptoID;
        Direccion = direccion;
        FormaPagoID = formaPagoID;
        Descuento=descuento;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getCodSuc() {
        return CodSuc;
    }

    public void setCodSuc(String codSuc) {
        CodSuc = codSuc;
    }

    public String getCodCliente() {
        return CodCliente;
    }

    public void setCodCliente(String codCliente) {
        CodCliente = codCliente;
    }

    public String getSucursal() {
        return Sucursal;
    }

    public void setSucursal(String sucursal) {
        Sucursal = sucursal;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getDeptoID() {
        return DeptoID;
    }

    public void setDeptoID(String deptoID) {
        DeptoID = deptoID;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getFormaPagoID() {
        return FormaPagoID;
    }

    public void setFormaPagoID(String formaPagoID) {
        FormaPagoID = formaPagoID;
    }
    public String toString(){
        return this.getSucursal();
    }
}

