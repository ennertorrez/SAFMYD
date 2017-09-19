package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 11/5/2017.
 */

public class Articulo {

    String Codigo= "";
    String Nombre = "";
    String Costo = "";
    String Unidad = "";
    String UnidadCaja = "";
    String Isc = "";
    String PorIva = "";
    String PrecioSuper= "";
    String PrecioDetalle = "";
    String PrecioForaneo = "";
    String PrecioForaneo2 = "";
    String PrecioMayorista = "";
    String Bonificable = "";
    String AplicaPrecioDetalle = "";
    String DescuentoMaximo = "";
    String Detallista = "";
    String Existencia="";

    public Articulo() {
    }

    public Articulo(String codigo, String nombre, String costo, String unidad, String unidadCaja, String isc, String porIva, String precioSuper, String precioDetalle, String precioForaneo,String precioForaneo2, String precioMayorista, String bonificable, String aplicaPrecioDetalle, String descuentoMaximo, String detallista,String existencia) {
        Codigo = codigo;
        Nombre = nombre;
        Costo = costo;
        Unidad = unidad;
        UnidadCaja = unidadCaja;
        Isc = isc;
        PorIva = porIva;
        PrecioSuper = precioSuper;
        PrecioDetalle = precioDetalle;
        PrecioForaneo = precioForaneo;
        PrecioForaneo2= precioForaneo2;
        PrecioMayorista = precioMayorista;
        Bonificable = bonificable;
        AplicaPrecioDetalle = aplicaPrecioDetalle;
        DescuentoMaximo = descuentoMaximo;
        Detallista = detallista;
        Existencia=existencia;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCosto() {
        return Costo;
    }

    public void setCosto(String costo) {
        Costo = costo;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String unidad) {
        Unidad = unidad;
    }

    public String getUnidadCaja() {
        return UnidadCaja;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String existencia) {
        Existencia = existencia;
    }

    public void setUnidadCaja(String unidadCaja) {
        UnidadCaja = unidadCaja;
    }

    public String getIsc() {
        return Isc;
    }

    public void setIsc(String isc) {
        Isc = isc;
    }

    public String getPorIva() {
        return PorIva;
    }

    public void setPorIva(String porIva) {
        PorIva = porIva;
    }

    public String getPrecioSuper() {
        return PrecioSuper;
    }

    public void setPrecioSuper(String precioSuper) {
        PrecioSuper = precioSuper;
    }

    public String getPrecioDetalle() {
        return PrecioDetalle;
    }

    public void setPrecioDetalle(String precioDetalle) {
        PrecioDetalle = precioDetalle;
    }

    public String getPrecioForaneo() {
        return PrecioForaneo;
    }

    public void setPrecioForaneo(String precioForaneo) {
        PrecioForaneo = precioForaneo;
    }

    public String getPrecioMayorista() {
        return PrecioMayorista;
    }

    public void setPrecioMayorista(String precioMayorista) {
        PrecioMayorista = precioMayorista;
    }

    public String getBonificable() {
        return Bonificable;
    }

    public void setBonificable(String bonificable) {
        Bonificable = bonificable;
    }

    public String getAplicaPrecioDetalle() {
        return AplicaPrecioDetalle;
    }

    public void setAplicaPrecioDetalle(String aplicaPrecioDetalle) {
        AplicaPrecioDetalle = aplicaPrecioDetalle;
    }

    public String getDescuentoMaximo() {
        return DescuentoMaximo;
    }

    public void setDescuentoMaximo(String descuentoMaximo) {
        DescuentoMaximo = descuentoMaximo;
    }

    public String getDetallista() {
        return Detallista;
    }

    public void setDetallista(String detallista) {
        Detallista = detallista;
    }

    public String toString() {
        return this.getNombre();
    }

    public String getPrecioForaneo2() {
        return PrecioForaneo2;
    }

    public void setPrecioForaneo2(String precioForaneo2) {
        PrecioForaneo2 = precioForaneo2;
    }
}