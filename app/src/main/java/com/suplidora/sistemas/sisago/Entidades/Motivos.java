package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 13/10/2017.
 */

public class Motivos {

    private String id;
    private String motivo;

    public Motivos(String id, String motivo) {
        this.id = id;
        this.motivo = motivo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String toString(){
        return this.motivo;
    }
}
