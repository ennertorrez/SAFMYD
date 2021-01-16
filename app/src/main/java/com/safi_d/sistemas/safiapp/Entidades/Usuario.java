package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Usuario {
    private String Codigo = "" ;
    private String nombre = "" ;
    private String Usuario = "" ;
    private String Contrasenia = "" ;
    private String Tipo = "" ;
    private String Ruta = "" ;
    private String Canal = "" ;
    private String TasaCambio = "";
    private String RutaForanea="";
    private String FechaActualiza="";
    private String EsVendedor="";
    private String Empresa_ID="";

    public Usuario(String codigo, String nombre, String usuario, String contrasenia, String tipo, String ruta, String canal, String tasaCambio,String rutaForanea,String fechaActualiza,String esvendedor,String empresa) {
        Codigo = codigo;
        this.nombre = nombre;
        Usuario = usuario;
        Contrasenia = contrasenia;
        Tipo = tipo;
        Ruta = ruta;
        Canal = canal;
        TasaCambio = tasaCambio;
        RutaForanea=rutaForanea;
        FechaActualiza = fechaActualiza;
        EsVendedor = esvendedor;
        Empresa_ID = empresa;
    }

    public Usuario() {

    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        Codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContrasenia() {
        return Contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        Contrasenia = contrasenia;
    }

    public String getTipo() {
        return Tipo;
    }

    public void setTipo(String tipo) {
        Tipo = tipo;
    }

    public String getRuta() {
        return Ruta;
    }

    public void setRuta(String ruta) {
        Ruta = ruta;
    }

    public String getCanal() {
        return Canal;
    }

    public void setCanal(String canal) {
        Canal = canal;
    }

    public String getTasaCambio() {
        return TasaCambio;
    }

    public void setTasaCambio(String tasaCambio) {
        TasaCambio = tasaCambio;
    }

    public String ToString(){
        return this.getNombre();
    }

    public String getRutaForanea() {
        return RutaForanea;
    }

    public void setRutaForanea(String rutaForanea) {
        RutaForanea = rutaForanea;
    }

    public String getFechaActualiza() {
        return FechaActualiza;
    }

    public void setFechaActualiza(String fechaActualiza) {
        FechaActualiza = fechaActualiza;
    }

    public String getEsVendedor() {
        return EsVendedor;
    }

    public void setEsVendedor(String esVendedor) {
        EsVendedor = esVendedor;
    }

    public String getEmpresa_ID() {
        return Empresa_ID;
    }

    public void setEmpresa_ID(String empresa) {
        Empresa_ID = empresa;
    }
}
