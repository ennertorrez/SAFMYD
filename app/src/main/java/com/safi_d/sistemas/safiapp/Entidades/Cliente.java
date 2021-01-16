package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Cliente {

    private String IdCliente = "";
    private String Nombre= "";
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
    private String NombreRuta= "";
    private String Frecuencia= "";
    private String PrecioEspecial= "";
    private String FechaUltimaCompra= "";
    private String Tipo="";
    private String TipoPrecio="";
    private String Descuento="";
    private String Empleado="";
    private String IdSupervisor="";
    private String Empresa="";
    private String Cod_Zona="";
    private String Cod_SubZona="";
    private String Pais_Id= "";
    private String Pais_Nombre= "";
    private String IdTipoNegocio= "";
    private String TipoNegocio= "";
    public Cliente() {

    }

    public Cliente(String Ruta) {
        this.Ruta = Ruta;
    }
    public Cliente(String idCliente, String nombre, String fechaCreacion, String telefono, String direccion, String idDepartamento, String idMunicipio, String ciudad, String ruc, String cedula, String limiteCredito,
                   String idFormaPago, String idVendedor, String excento, String codigoLetra, String ruta,  String nombreRuta,String frecuencia, String precioEspecial, String fechaUltimaCompra, String tipo,String tipoPrecio,
                   String descuento, String empleado, String idSupervisor, String empresa, String codZona, String codSubZona, String idPais,String nombrePais, String idTipoNegocio,String tipoNegocio) {
        IdCliente = idCliente;
        Nombre = nombre;
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
        NombreRuta =nombreRuta;
        Frecuencia = frecuencia;
        PrecioEspecial = precioEspecial;
        FechaUltimaCompra = fechaUltimaCompra;
        Tipo=tipo;
        TipoPrecio=tipoPrecio;
        Descuento = descuento;
        Empleado = empleado;
        IdSupervisor = idSupervisor;
        Empresa=empresa;
        Cod_Zona=codZona;
        Cod_SubZona=codSubZona;
        Pais_Id= idPais;
        Pais_Nombre=nombrePais;
        IdTipoNegocio=idTipoNegocio;
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

    public String getNombreRuta() {
        return NombreRuta;
    }

    public void setNombreRuta(String nombreRuta) {
        NombreRuta = nombreRuta;
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

    public String getTipoPrecio() {
        return TipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        TipoPrecio = tipoPrecio;
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

    public String getIdTipoNegocio() {
        return IdTipoNegocio;
    }

    public void setIdTipoNegocio(String idTipoNegocio) {
        IdTipoNegocio = idTipoNegocio;
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

    public String getIdSupervisor() {
        return IdSupervisor;
    }

    public void setIdSupervisor(String idSupervisor) {
        IdSupervisor = idSupervisor;
    }

    public String getEmpresa() {
        return Empresa;
    }

    public void setEmpresa(String empresa) {
        Empresa = empresa;
    }

    public String getTipoNegocio() {
        return TipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        TipoNegocio = tipoNegocio;
    }

    public String getCod_Zona() {
        return Cod_Zona;
    }

    public void setCod_Zona(String codZona) {
        Cod_Zona = codZona;
    }

    public String getCod_SubZona() {
        return Cod_SubZona;
    }

    public void setCod_SubZona(String codSubZona) {
        Cod_SubZona = codSubZona;
    }

    public String toString(){

        return  this.getNombre();
    }

    public String getPais_Id() {
        return Pais_Id;
    }

    public void setPais_Id(String idPais) {
        Pais_Id = idPais;
    }

    public String getPais_Nombre() {
        return Pais_Nombre;
    }

    public void setPais_Nombre(String nombrePais) {
        Pais_Nombre = nombrePais;
    }
}
