package com.suplidora.sistemas.sisago.Entidades;

/**
 * Created by Sistemas on 6/5/2017.
 */

public class Vehiculo {

    String IdVehiculo;
    String Vehiculo;
    String Capacidad;
    String clave;
    String estado;


    public Vehiculo(String IdVehiculo, String Vehiculo, String Capacidad, String clave) {
        this.IdVehiculo = IdVehiculo;
        this.Vehiculo = Vehiculo;
        this.Capacidad = Capacidad;
        this.clave = clave;

    }

    public Vehiculo() {

    }

    public String getIdVehiculo() {
        return IdVehiculo;
    }

    public void setIdVehiculo(String idVehiculo) {
        IdVehiculo = idVehiculo;
    }

    public String getVehiculo() {
        return Vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        Vehiculo = vehiculo;
    }

    public String getCapacidad() {
        return Capacidad;
    }

    public void setCapacidad(String capacidad) {
        Capacidad = capacidad;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String toString(){
        return this.getVehiculo();
    }
}
