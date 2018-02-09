package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 9/1/2018.
 */

public class DptpMuniBarrio {
    private String Codigo_Departamento;
    private String Nombre_Departamento;
    private String Codigo_Municipio;
    private String Nombre_Municipio;
    private String Codigo_Barrio;
    private String Nombre_Barrio;

    public DptpMuniBarrio() {
    }
    public DptpMuniBarrio(String Nombre_Departamento){
        this.Nombre_Departamento = Nombre_Departamento;
    }
   /* public DptpMuniBarrio(String Nombre_Municipio){
        this.Nombre_Municipio = Nombre_Municipio;
    }

    public DptpMuniBarrio(String Nombre_Barrio){
        this.Nombre_Barrio = Nombre_Barrio;
    }*/
    public DptpMuniBarrio(String Codigo_Departamento, String Nombre_Departamento, String Codigo_Municipio, String Nombre_Municipio, String Codigo_Barrio, String Nombre_Barrio) {
        this.Codigo_Departamento = Codigo_Departamento;
        this.Nombre_Departamento = Nombre_Departamento;
        this.Codigo_Municipio = Codigo_Municipio;
        this.Nombre_Municipio = Nombre_Municipio;
        this.Codigo_Barrio = Codigo_Barrio;
        this.Nombre_Barrio = Nombre_Barrio;
    }

    public String getCodigo_Departamento() {
        return Codigo_Departamento;
    }
    public void setCodigo_Departamento(String Codigo_Departamento) {
        this.Codigo_Departamento = Codigo_Departamento;
    }
    public String getCodigo_Municipio() {
        return Codigo_Municipio;
    }
    public void setCodigo_Municipio(String Codigo_Municipio) {
        this.Codigo_Municipio = Codigo_Municipio;
    }
    public String getCodigo_Barrio() {
        return Codigo_Barrio;
    }
    public void setCodigo_Barrio(String Codigo_Barrio) {
        this.Codigo_Barrio = Codigo_Barrio;
    }
    public String getNombre_Departamento() {
        return Nombre_Departamento;
    }
    public void setNombre_Departamento(String Nombre_Departamento) {
        this.Nombre_Departamento = Nombre_Departamento;
    }
    public String getNombre_Municipio() {
        return Nombre_Municipio;
    }
    public void setNombre_Municipio(String Nombre_Municipio) {
        this.Nombre_Municipio = Nombre_Municipio;
    }
    public String getNombre_Barrio() {
        return Nombre_Barrio;
    }
    public void setNombre_Barrio(String Nombre_Barrio) {
        this.Nombre_Barrio = Nombre_Barrio;
    }
    public String toString(){
        return this.getNombre_Departamento();
    }
}
