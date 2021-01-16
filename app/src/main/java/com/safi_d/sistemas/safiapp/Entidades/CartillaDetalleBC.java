package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class CartillaDetalleBC {

    String id = "";
    String itemV = "";
    String descripcionV = "";
    String cantidad = "";
    String itemB = "";
    String descripcionB = "";
    String cantidadB = "";
    String codigo = "";
    String tipo = "";
    String activo = "";
    String codUMV = "";
    String codUMB = "";
    String unidadesV = "";
    String unidadesB = "";
    String umB = "";
    public CartillaDetalleBC() {
    }

    public CartillaDetalleBC(String id, String itemV, String descripcionV, String cantidad, String itemB, String descripcionB, String cantidadB, String codigo, String tipo, String activo, String codumv, String codumb, String unidadesV, String unidadesB, String umb) {
        this.id = id;
        this.itemV = itemV;
        this.descripcionV = descripcionV;
        this.cantidad = cantidad;
        this.itemB = itemB;
        this.descripcionB = descripcionB;
        this.cantidadB = cantidadB;
        this.codigo = codigo;
        this.tipo = tipo;
        this.activo = activo;
        this.codUMV = codumv;
        this.codUMB = codumb;
        this.unidadesV = unidadesV;
        this.unidadesB = unidadesB;
        this.umB=umb;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemV() {
        return itemV;
    }

    public void setItemV(String itemV) {
        this.itemV = itemV;
    }

    public String getDescripcionV() {
        return descripcionV;
    }

    public void setDescripcionV(String descripcionV) {
        this.descripcionV = descripcionV;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemB) {
        this.itemB = itemB;
    }

    public String getDescripcionB() {
        return descripcionB;
    }

    public void setDescripcionB(String descripcionB) {
        this.descripcionB = descripcionB;
    }

    public String getCantidadB() {
        return cantidadB;
    }

    public void setCantidadB(String cantidadB) {
        this.cantidadB = cantidadB;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getActivo() {
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getCodUMV() {
        return codUMV;
    }

    public void setCodUMV(String codUMV) {
        this.codUMV = codUMV;
    }

    public String getCodUMB() {
        return codUMB;
    }

    public void setCodUMB(String codUMB) {
        this.codUMB = codUMB;
    }

    public String getUnidadesV() {
        return unidadesV;
    }

    public void setUnidadesV(String unidadesV) {
        this.unidadesV = unidadesV;
    }

    public String getUnidadesB() {
        return unidadesB;
    }

    public void setUnidadesB(String unidadesB) {
        this.unidadesB = unidadesB;
    }

    public String getUmB() {
        return umB;
    }

    public void setUmB(String umB) {
        this.umB = umB;
    }

}



