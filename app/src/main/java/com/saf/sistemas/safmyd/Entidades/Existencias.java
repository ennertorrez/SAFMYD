package com.saf.sistemas.safmyd.Entidades;

public class Existencias {
    private String Codigo="";
    private String Bodega ="";
    private String Existencia ="";

    public Existencias(String Codigo,String Bodega,String Existencia) {
        this.Codigo = Codigo;
        this.Bodega = Bodega;
        this.Existencia = Existencia;
    }

    public Existencias() {

    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getBodega() {
        return Bodega;
    }

    public void setBodega(String Bodega) {
        this.Bodega = Bodega;
    }

    public String getExistencia() {
        return Existencia;
    }

    public void setExistencia(String Existencia) {
        this.Existencia = Existencia;
    }
}
