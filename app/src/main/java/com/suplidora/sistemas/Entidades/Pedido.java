package com.suplidora.sistemas.Entidades;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Pedido {

    String IdVendedor = "";
    String IdCliente = "";
    String Cod_cv = "";
    String Observacion = "";
    String IdFormaPago = "";
    String IdSucursal = "";
    String Fecha = "";
    String Usuario = "";
    String IMEI = "";

    public Pedido() {
    }

    public Pedido(String idVendedor, String idCliente, String cod_cv, String observacion, String idFormaPago, String idSucursal, String fecha, String usuario, String IMEI) {
        IdVendedor = idVendedor;
        IdCliente = idCliente;
        Cod_cv = cod_cv;
        Observacion = observacion;
        IdFormaPago = idFormaPago;
        IdSucursal = idSucursal;
        Fecha = fecha;
        Usuario = usuario;
        this.IMEI = IMEI;
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

    public String getCod_cv() {
        return Cod_cv;
    }

    public void setCod_cv(String cod_cv) {
        Cod_cv = cod_cv;
    }

    public String getObservacion() {
        return Observacion;
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
}
