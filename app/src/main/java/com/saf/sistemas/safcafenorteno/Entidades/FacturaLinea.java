package com.saf.sistemas.safcafenorteno.Entidades;

public class FacturaLinea {
    String Factura= "";
    String Item= "";
    String Cantidad= "";
    String Precio= "";
    String Iva= "";
    String Total= "";
    String TipoArt= "";
    String PorcentajeIva= "";
    String Descripcion= "";
    String PorDescuento= "";
    String Presentacion= "";
    String Subtotal= "";
    String Costo= "";
    String BonificaA= "";
    String CodUM= "";
    String Unidades= "";
    String Barra= "";
    String Empresa= "";

    public FacturaLinea() {
    }

    public FacturaLinea(String factura,String item,String cantidad,String precio,String iva,String total,String tipoart,String porcentajeiva,String descripcion,String pordescuento,String presentacion,String subtotal,String costo,String bonificaa,String codum,String unidades,String barra,String empresa) {
        Factura = factura;
        Item = item;
        Cantidad = cantidad;
        Precio = precio;
        Iva = iva;
        Total = total;
        TipoArt = tipoart;
        PorcentajeIva = porcentajeiva;
        Descripcion = descripcion;
        PorDescuento = pordescuento;
        Presentacion = presentacion;
        Subtotal = subtotal;
        Costo = costo;
        BonificaA = bonificaa;
        CodUM = codum;
        Unidades = unidades;
        Barra = barra;
        Empresa = empresa;

    }

    public String geFactura() {
        return Factura;
    }
    public String getItem() {
        return Item;
    }
    public String getCantidad() {
        return Cantidad;
    }
    public String getPrecio() {
        return Precio;
    }
    public String getIva() {
        return Iva;
    }
    public String getTotal() {
        return Total;
    }
    public String getTipoArt() {
        return TipoArt;
    }
    public String getPorcentajeIva() {
        return PorcentajeIva;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public String getPorDescuento() {
        return PorDescuento;
    }
    public String getPresentacion() {
        return Presentacion;
    }
    public String getSubtotal() {
        return Subtotal;
    }
    public String getCosto() {
        return Costo;
    }
    public String getBonificaA() {
        return BonificaA;
    }
    public String getCodUM() {
        return CodUM;
    }
    public String getUnidades() {
        return Unidades;
    }
    public String getBarra() {
        return Barra;
    }
    public String getEmpresa() {
        return Empresa;
    }

    public void setFactura(String factura) {
        Factura = factura;
    }
    public void setItem(String item) {
        Item = item;
    }
    public void setCantidad(String cantidad) {
        Cantidad = cantidad;
    }
    public void setPrecio(String precio) {
        Precio = precio;
    }
    public void setIva(String iva) {
        Iva = iva;
    }
    public void setTotal(String total) {
        Total = total;
    }
    public void setTipoArt(String tipoart) {
        TipoArt = tipoart;
    }
    public void setPorcentajeIva(String porcentajeiva) {
        PorcentajeIva = porcentajeiva;
    }
    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
    public void setPorDescuento(String pordescuento) {
        PorDescuento = pordescuento;
    }
    public void setPresentacion(String presentacion) {
        Presentacion = presentacion;
    }
    public void setSubtotal(String subtotal) {
        Subtotal = subtotal;
    }
    public void setCosto(String costo) {
        Costo = costo;
    }
    public void setBonificaA(String bonificaa) {
        BonificaA = bonificaa;
    }
    public void setCodUM(String codum) {
        CodUM = codum;
    }
    public void setUnidades(String unidades) {
        Unidades = unidades;
    }
    public void setBarra(String barra) {
        Barra = barra;
    }
    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }
}
