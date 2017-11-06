package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class ConsolidadoCarga {

    String IdConsolidado = "";
    String Factura = "";
    String IdCliente="";
    String Cliente = "";
    String IdVendedor="";
    String Vendedor = "";
    String Direccion = "";
    String Guardada="";

    public ConsolidadoCarga() {
    }

    public ConsolidadoCarga(String idConsolidado){
        this.IdConsolidado = idConsolidado;
    }
    public ConsolidadoCarga(String idConsolidado, String factura, String cliente, String vendedor, String direccion,String idCliente,String idVendedor,String guardada) {
        this.IdConsolidado = idConsolidado;
       this.Factura = factura;
        this.Cliente = cliente;
        this.Vendedor = vendedor;
        this.Direccion = direccion;
        this.IdCliente=idCliente;
        this.IdVendedor=idVendedor;
        this.Guardada=guardada;
    }

    public String getGuardada() {
        return Guardada;
    }

    public void setGuardada(String guardada) {
        Guardada = guardada;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getIdConsolidado() {
        return IdConsolidado;
    }

    public void setIdConsolidado(String idConsolidado) {
        IdConsolidado = idConsolidado;
    }

    public String getFactura() {
        return Factura;
    }

    public void setFactura(String factura) {
        Factura = factura;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getVendedor() {
        return Vendedor;
    }

    public void setVendedor(String vendedor) {
        Vendedor = vendedor;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String toString(){
        return this.getIdConsolidado();
    }
}
