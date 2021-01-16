package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Ruta {

    String RUTA;
    String IDRUTA;
    public Ruta(String IDRUTA,String RUTA) {
        this.IDRUTA = IDRUTA;
        this.RUTA = RUTA;
    }

    public Ruta() {

    }

    public String getIDRUTA() {
        return IDRUTA;
    }

    public void setIDRUTA(String IDRUTA) {
        this.IDRUTA = IDRUTA;
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
