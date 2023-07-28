package com.saf.sistemas.safmyd.Entidades;

public class Promociones {
    String codPromo = "";
    String itemV = "";
    String cantV = "";
    String itemB = "";
    String cantB = "";
    public Promociones() {
    }
    public Promociones(String codPromo,String itemV,String cantV,String itemB,String cantB) {
        this.codPromo = codPromo;
        this.itemV = itemV;
        this.cantV = cantV;
        this.itemB = itemB;
        this.cantB = cantB;
    }

    public String getCodPromo() {
        return codPromo;
    }

    public void setCodPromo(String codpromo) {
        this.codPromo = codpromo;
    }

    public String getItemV() {
        return itemV;
    }

    public void setItemV(String itemv) {
        this.itemV = itemv;
    }

    public String getCantV() {
        return cantV;
    }

    public void setCantV(String cantv) {
        this.cantV = cantv;
    }

    public String getItemB() {
        return itemB;
    }

    public void setItemB(String itemb) {
        this.itemB = itemb;
    }
    public String getCantB() {
        return cantB;
    }

    public void setCantB(String cantb) {
        this.cantB = cantb;
    }
}
