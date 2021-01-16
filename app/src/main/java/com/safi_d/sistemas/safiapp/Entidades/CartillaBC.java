package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class CartillaBC {

    String id = "";
    String codigo = "";
    String fechaini = "";
    String fechafinal = "";
    String tipo = "";
    String aprobado = "";

    public CartillaBC() {
    }

    public CartillaBC(String id, String codigo, String fechaini, String fechafinal, String tipo, String aprobado) {
        this.id = id;
        this.codigo = codigo;
        this.fechaini = fechaini;
        this.fechafinal = fechafinal;
        this.tipo = tipo;
        this.aprobado = aprobado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFechaini() {
        return fechaini;
    }

    public void setFechaini(String fechaini) {
        this.fechaini = fechaini;
    }

    public String getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(String fechafinal) {
        this.fechafinal = fechafinal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAprobado() {
        return aprobado;
    }

    public void setAprobado(String aprobado) {
        this.aprobado = aprobado;
    }
}
