package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 9/1/2018.
 */

public class Departamentos {
    private String Codigo_Departamento="";
    private String Nombre_Departamento="";

    public Departamentos(String Codigo_Departamento, String Nombre_Departamento) {
        this.Codigo_Departamento = Codigo_Departamento;
        this.Nombre_Departamento = Nombre_Departamento;
    }

    public Departamentos() {
    }

    public String getCodigo_Departamento() {
        return Codigo_Departamento;
    }
    public void setCodigo_Departamento(String Codigo_Departamento) {
        this.Codigo_Departamento = Codigo_Departamento;
    }

    public String getNombre_Departamento() {
        return Nombre_Departamento;
    }
    public void setNombre_Departamento(String Nombre_Departamento) {
        this.Nombre_Departamento = Nombre_Departamento;
    }
   public String toString(){
        return this.getNombre_Departamento();
    }
}
