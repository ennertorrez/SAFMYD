package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Ruta {

    String RUTA;

    public Ruta(String RUTA) {

        this.RUTA = RUTA;
    }

    public Ruta() {

    }

    public String getRUTA() {
        return RUTA;
    }

    public void setRUTA(String RUTA) {
        this.RUTA = RUTA;
    }

    public String toString(){
        return this.getRUTA();
    }
}
