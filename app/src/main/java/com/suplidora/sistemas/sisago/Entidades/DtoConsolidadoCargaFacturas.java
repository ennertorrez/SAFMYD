package com.suplidora.sistemas.sisago.Entidades;

import ir.mirrajabi.searchdialog.core.Searchable;

/**
 * Created by Sistemas on 16/10/2017.
 */

public class DtoConsolidadoCargaFacturas implements Searchable {

    String IdConsolidado = "";
    String mTitle ;
    String Cliente = "";
    String Vendedor = "";
    String Direccion = "";


    public DtoConsolidadoCargaFacturas() {
    }

    public DtoConsolidadoCargaFacturas(String idConsolidado) {
        this.IdConsolidado = idConsolidado;
    }

    public DtoConsolidadoCargaFacturas(String idConsolidado, String factura, String cliente, String vendedor, String direccion) {
        this.IdConsolidado = idConsolidado;
        this.mTitle = factura;
        this.Cliente = cliente;
        this.Vendedor = vendedor;
        this.Direccion = direccion;
    }

    public String getIdConsolidado() {
        return IdConsolidado;
    }

    public void setIdConsolidado(String idConsolidado) {
        IdConsolidado = idConsolidado;
    }

//    public String getFactura() {
//        return Factura;
//    }
//
//    public void setFactura(String factura) {
//        Factura = factura;
//    }

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

    public String toString() {
        return this.getTitle();
    }

    //    public void setFactura(String factura) {
//        this.Factura = factura;
//    }
    public void setTitle(String factura) {
        mTitle = factura;
    }

    @Override
    public String getTitle() {
        // mTitle = this.getFactura();
        return mTitle;
    }

}

