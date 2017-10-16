package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class ConsolidadoCargaDetalle {

    String IdVehiculo = "";
    String Factura = "";
    String ITEM = "";
    String Item_Descripcion = "";
    String CANTIDAD = "";
    String PRECIO = "";
    String TOTAL = "";
    String IVA = "";
    String DESCUENTO = "";

    public ConsolidadoCargaDetalle() {
    }

    public ConsolidadoCargaDetalle(String idVehiculo, String factura, String item, String item_Descripcion, String cantidad, String precio, String total, String iva, String descuento) {
        this.IdVehiculo = idVehiculo;
        this.Factura = factura;
        this.ITEM = item;
        this.Item_Descripcion = item_Descripcion;
        this.CANTIDAD = cantidad;
        this.PRECIO = precio;
        this.TOTAL = total;
        this.IVA = iva;
        this.DESCUENTO = descuento;
    }

    public String getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        IdVehiculo = idVehiculo;
    }

    public String getFactura() {
        return Factura;
    }

    public void setFactura(String factura) {
        Factura = factura;
    }

    public String getITEM() {
        return ITEM;
    }

    public void setITEM(String ITEM) {
        this.ITEM = ITEM;
    }

    public String getItem_Descripcion() {
        return Item_Descripcion;
    }

    public void setItem_Descripcion(String item_Descripcion) {
        Item_Descripcion = item_Descripcion;
    }

    public String getCANTIDAD() {
        return CANTIDAD;
    }

    public void setCANTIDAD(String CANTIDAD) {
        this.CANTIDAD = CANTIDAD;
    }

    public String getPRECIO() {
        return PRECIO;
    }

    public void setPRECIO(String PRECIO) {
        this.PRECIO = PRECIO;
    }

    public String getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(String TOTAL) {
        this.TOTAL = TOTAL;
    }

    public String getIVA() {
        return IVA;
    }

    public void setIVA(String IVA) {
        this.IVA = IVA;
    }

    public String getDESCUENTO() {
        return DESCUENTO;
    }

    public void setDESCUENTO(String DESCUENTO) {
        this.DESCUENTO = DESCUENTO;
    }
}
