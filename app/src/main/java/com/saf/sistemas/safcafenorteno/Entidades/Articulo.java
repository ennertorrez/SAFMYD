package com.saf.sistemas.safcafenorteno.Entidades;

/**
 * Created by Sistemas on 11/5/2017.
 */

public class Articulo {

    String Codigo= "";
    String Nombre = "";
    String Costo = "";
    String Unidad = "";
    String UnidadCaja = "";
    String Precio = "";
    String Precio2 = "";
    String Precio3 = "";
    String Precio4 = "";
    String CodUM = "";
    String PorIva = "";
    String DescuentoMaximo = "";
    String Existencia="";
    String UnidadCajaVenta="";
    String UnidadCajaVenta2="";
    String UnidadCajaVenta3="";
    String IdProveedor="";
    String Escala="";
    public Articulo() {
    }

    public Articulo(String codigo, String nombre, String costo, String unidad, String unidadCaja, String precio,
                    String precio2,String precio3,String precio4,String codUM,String porIva,
                    String descuentoMaximo, String existencia, String unidadCajaVenta, String unidadCajaVenta2, String unidadCajaVenta3,String idProveedor,String escala) {
        Codigo = codigo;
        Nombre = nombre;
        Costo = costo;
        Unidad = unidad;
        UnidadCaja = unidadCaja;
        Precio = precio;
        Precio2 = precio2;
        Precio3 = precio3;
        Precio4 = precio4;
        CodUM = codUM;
        PorIva = porIva;
        DescuentoMaximo = descuentoMaximo;
        Existencia=existencia;
        UnidadCajaVenta=unidadCajaVenta;
        UnidadCajaVenta2=unidadCajaVenta2;
        UnidadCajaVenta3=unidadCajaVenta3;
        IdProveedor=idProveedor;
        Escala=escala;
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

    public String getUnidadCajaVenta() {
        return UnidadCajaVenta;
    }

    public void setUnidadCajaVenta(String unidadCajaVenta) {
        UnidadCajaVenta = unidadCajaVenta;
    }

    public String getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(String idProveedor) {
        IdProveedor = idProveedor;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getPrecio2() {
        return Precio2;
    }

    public void setPrecio2(String precio2) {
        Precio2 = precio2;
    }

    public String getPrecio3() {
        return Precio3;
    }

    public void setPrecio3(String precio3) {
        Precio3 = precio3;
    }

    public String getPrecio4() {
        return Precio4;
    }

    public void setPrecio4(String precio4) {
        Precio4 = precio4;
    }

    public String getCodUM() {
        return CodUM;
    }

    public void setCodUM(String codUM) {
        CodUM = codUM;
    }

    public String getPorIva() {
        return PorIva;
    }

    public void setPorIva(String porIva) {
        PorIva = porIva;
    }

    public String getDescuentoMaximo() {
        return DescuentoMaximo;
    }

    public void setDescuentoMaximo(String descuentoMaximo) {
        DescuentoMaximo = descuentoMaximo;
    }

    public String getUnidadCajaVenta2() {
        return UnidadCajaVenta2;
    }

    public void setUnidadCajaVenta2(String unidadCajaVenta2) {
        UnidadCajaVenta2 = unidadCajaVenta2;
    }
    public String getUnidadCajaVenta3() {
        return UnidadCajaVenta3;
    }

    public void setUnidadCajaVenta3(String unidadCajaVenta3) {
        UnidadCajaVenta3 = unidadCajaVenta3;
    }

    public String getEscala() {
        return Escala;
    }

    public void setEscala(String escala) {
        Escala = escala;
    }

    public String toString() {
        return this.getNombre();
    }

}