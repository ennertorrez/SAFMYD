package com.saf.sistemas.safmyd.Entidades;

public class MotivosNoVenta {
    private String Codigo;
    private String Motivo;

    public MotivosNoVenta(String codigo, String motivo) {
        this.Codigo = codigo;
        this.Motivo = motivo;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String codigo) {
        this.Codigo = codigo;
    }

    public String getMotivo() {
        return Motivo;
    }

    public void setMotivo(String motivo) {
        this.Motivo = motivo;
    }

    public String toString(){
        return this.Motivo;
    }
}
