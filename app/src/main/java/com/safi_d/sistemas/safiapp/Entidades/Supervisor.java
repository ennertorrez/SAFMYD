package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Supervisor {

    String Supervisor;
    String codsuper;

    public Supervisor(String codsuper,String Supervisor) {

        this.codsuper = codsuper;
        this.Supervisor = Supervisor;

    }

    public Supervisor() {

    }

    public String getSupervisor() {
        return Supervisor;
    }

    public void setSupervisor(String supervisor) {
        Supervisor = supervisor;
    }
    public String getCodsuper() {
        return codsuper;
    }

    public void setCodsuper(String codsuper) {
        codsuper = codsuper;
    }



    @Override
    public String toString() {
        return this.getSupervisor();
    }
//    public String toStringCodSup() {
//        return this.getCodsuper();
//    }
}
