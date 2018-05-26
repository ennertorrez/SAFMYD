package com.suplidora.sistemas.sisago.Entidades;

import org.jsoup.nodes.Entities;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Cliente {

    private String IdCliente = "";
    private String CodCv= "";
    private String Nombre= "";
    private String NombreCliente="";
    private String FechaCreacion = "";
    private String Telefono = "";
    private String Direccion = "";
    private String IdDepartamento= "";
    private String IdMunicipio = "";
    private String Ciudad = "";
    private String Ruc= "";
    private String Cedula= "";
    private String LimiteCredito= "";
    private String IdFormaPago= "";
    private String IdVendedor= "";
    private String Excento= "";
    private String CodigoLetra= "";
    private String Ruta= "";
    private String Frecuencia= "";
    private String PrecioEspecial= "";
    private String FechaUltimaCompra= "";
    private String Tipo="";
    private String CodigoGalatea="";
    private String Descuento="";
    private String Empleado="";
    private String Detallista="";
    private String RutaForanea="";
    private String EsClienteVarios="";
    private String IdBarrio= "";
    private String TipoNegocio= "";
    public Cliente() {

    }

    public String getEsClienteVarios() {
        return EsClienteVarios;
    }

    public void setEsClienteVarios(String esClienteVarios) {
        EsClienteVarios = esClienteVarios;
    }

    public Cliente(String Ruta) {
        this.Ruta = Ruta;
    }
    public Cliente(String idCliente, String codCv, String nombre, String nombreCliente, String fechaCreacion, String telefono, String direccion, String idDepartamento, String idMunicipio, String ciudad, String ruc, String cedula, String limiteCredito, String idFormaPago, String idVendedor, String excento, String codigoLetra, String ruta, String frecuencia, String precioEspecial, String fechaUltimaCompra, String tipo, String codigoGalatea, String descuento, String empleado, String detallista, String rutaForanea, String esClienteVarios, String idbarrio, String tipoNegocio) {
        IdCliente = idCliente;
        CodCv = codCv;
        Nombre = nombre;
        NombreCliente=nombreCliente;
        FechaCreacion = fechaCreacion;
        Telefono = telefono;
        Direccion = direccion;
        IdDepartamento = idDepartamento;
        IdMunicipio = idMunicipio;
        Ciudad = ciudad;
        Ruc = ruc;
        Cedula = cedula;
        LimiteCredito = limiteCredito;
        IdFormaPago = idFormaPago;
        IdVendedor = idVendedor;
        Excento = excento;
        CodigoLetra = codigoLetra;
        Ruta = ruta;
        Frecuencia = frecuencia;
        PrecioEspecial = precioEspecial;
        FechaUltimaCompra = fechaUltimaCompra;
        Tipo=tipo;
        CodigoGalatea = codigoGalatea;
        Descuento = descuento;
        Empleado = empleado;
        Detallista=detallista;
        RutaForanea=rutaForanea;
        EsClienteVarios=esClienteVarios;
        IdBarrio= idbarrio;
        TipoNegocio =tipoNegocio;
    }

    public String getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(String idCliente) {
        IdCliente = idCliente;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getCodCv() {
        return CodCv;
    }

    public void setCodCv(String codCv) {
        CodCv = codCv;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getIdDepartamento() {
        return IdDepartamento;
    }

    public void setIdDepartamento(String idDepartamento) {
        IdDepartamento = idDepartamento;
    }

    public String getIdMunicipio() {
        return IdMunicipio;
    }

    public void setIdMunicipio(String idMunicipio) {
        IdMunicipio = idMunicipio;
    }

    public String getCiudad() {
        return Ciudad;
    }

    public void setCiudad(String ciudad) {
        Ciudad = ciudad;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getLimiteCredito() {
        return LimiteCredito;
    }

    public void setLimiteCredito(String limiteCredito) {
        LimiteCredito = limiteCredito;
    }

    public String getIdFormaPago() {
        return IdFormaPago;
    }

    public void setIdFormaPago(String idFormaPago) {
        IdFormaPago = idFormaPago;
    }

    public String getIdVendedor() {
        return IdVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        IdVendedor = idVendedor;
    }

    public String getExcento() {
        return Excento;
    }

    public void setExcento(String excento) {
        Excento = excento;
    }

    public String getCodigoLetra() {
        return CodigoLetra;
    }

    public void setCodigoLetra(String codigoLetra) {
        CodigoLetra = codigoLetra;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public String getFrecuencia() {
        return Frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        Frecuencia = frecuencia;
    }

    public String getPrecioEspecial() {
        return PrecioEspecial;
    }

    public void setPrecioEspecial(String precioEspecial) {
        PrecioEspecial = precioEspecial;
    }

    public String getFechaUltimaCompra() {
        return FechaUltimaCompra;
    }

    public void setFechaUltimaCompra(String fechaUltimaCompra) {
        FechaUltimaCompra = fechaUltimaCompra;
    }

    public String getCodigoGalatea() {
        return CodigoGalatea;
    }

    public void setCodigoGalatea(String codigoGalatea) {
        CodigoGalatea = codigoGalatea;
    }

    public String getDescuento() {
        return Descuento;
    }

    public void setDescuento(String descuento) {
        Descuento = descuento;
    }

    public String getEmpleado() {
        return Empleado;
    }

    public void setEmpleado(String empleado) {
        Empleado = empleado;
    }

    public String getDetallista() {
        return Detallista;
    }

    public void setDetallista(String detallista) {
        Detallista = detallista;
    }

    public String getIdBarrio() {
        return IdBarrio;
    }

    public void setIdBarrio(String idBarrio) {
        IdBarrio = idBarrio;
    }

    public String getTipoNegocio() {
        return TipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        TipoNegocio = tipoNegocio;
    }

    public String getRutaForanea() {
        return RutaForanea;
    }

    public void setRutaForanea(String rutaForanea) {
        RutaForanea = rutaForanea;
    }

    public String toString(){

        return  this.getNombre();
    }

    public String getNombreCliente() {
        return NombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        NombreCliente = nombreCliente;
    }
}
