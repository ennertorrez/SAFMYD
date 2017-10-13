package com.suplidora.sistemas.sisago.Entidades;

import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Devoluciones {
   String    ndevolucion  = "";
   String   cliente = "";
   String  horagraba  = "";
    String   usuario ="";
   String  subtotal  = "";
   String   iva  = "";
   String  total  = "";
   String  estado  = "";
   String   rango  = "";
   String    motivo  ="" ;
   String  factura ="";
   String procesado="";
   String useranula="";
   String horaanula="";
    String   tipo ="";
    String ejecutada="";
    String IMEI="";
    String IdVehiculo="";

    public Devoluciones() {
    }

    public Devoluciones(String Ndevolucion, String Cliente, String Horagraba, String Usuario, String Subtotal, String Iva, String Total,
                        String Estado, String Rango, String Motivo, String Factura,String Procesado,String Useranula,String Horaanula, String Tipo,String Ejecutada,String IMEI,String IdVehiculo) {
        ndevolucion=Ndevolucion;
        cliente = Cliente;
        horagraba = Horagraba;
        usuario = Usuario;
        subtotal=Subtotal;
        iva = Iva;
        total = Total;
        estado = Estado;
        rango = Rango;
        motivo = Motivo;
        factura = Factura;
        procesado = Procesado;
        useranula = Useranula;
        horaanula = Horaanula;
        tipo=Tipo;
        ejecutada=Ejecutada;
        this.IMEI = IMEI;
        IdVehiculo = IdVehiculo;

    }

    public String getNdevolucion() {
        return ndevolucion;
    }

    public void setNdevolucion(String ndevolucion) {
        this.ndevolucion = ndevolucion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getHoragraba() {
        return horagraba;
    }

    public void setHoragraba(String horagraba) {
        this.horagraba = horagraba;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getIva() {
        return iva;
    }

    public void setIva(String iva) {
        this.iva = iva;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    public String getProcesado() {
        return procesado;
    }

    public void setProcesado(String procesado) {
        this.procesado = procesado;
    }

    public String getUseranula() {
        return useranula;
    }

    public void setUseranula(String useranula) {
        this.useranula = useranula;
    }

    public String getHoraanula() {
        return horaanula;
    }

    public void setHoraanula(String horaanula) {
        this.horaanula = horaanula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEjecutada() {
        return ejecutada;
    }

    public void setEjecutada(String ejecutada) {
        this.ejecutada = ejecutada;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(String IdVehiculo) {
        this.IdVehiculo = IdVehiculo;
    }

    public String toString(){
        return  this.getNdevolucion();
    }
}
