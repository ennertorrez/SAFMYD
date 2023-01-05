package com.saf.sistemas.safasuncion.Entidades;

public class EscalaPrecios {
    String CodEscala;
    String ListaArticulos;
    String Escala1;
    String Escala2;
    String Escala3;
    String Precio1;
    String Precio2;
    String Precio3;

    public EscalaPrecios(String codescala, String listaarticulos, String escala1, String escala2, String escala3, String precio1, String precio2, String precio3) {
        this.CodEscala =codescala;
        this.ListaArticulos=listaarticulos;
        this.Escala1=escala1;
        this.Escala2=escala2;
        this.Escala3=escala3;
        this.Precio1=precio1;
        this.Precio2=precio2;
        this.Precio3=precio3;
    }

    public EscalaPrecios() {

    }

    public String getCodEscala() {
        return  CodEscala;
    }

    public void setCodigo(String codEscala) {
        this.CodEscala = codEscala;
    }

    public String getListaArticulos() {
        return  ListaArticulos;
    }

    public void setListaArticulos(String listaArticulos) {
        this.ListaArticulos = listaArticulos;
    }

    public String getEscala1() {
        return  Escala1;
    }

    public void setEscala1(String escala1) {
        this.Escala1 = escala1;
    }
    public String getEscala2() {
        return  Escala2;
    }

    public void setEscala2(String escala2) {
        this.Escala2 = escala2;
    }

    public String getEscala3() {
        return  Escala3;
    }

    public void setEscala3(String escala3 ) {
        this.Escala3 = escala3;
    }

    public String getPrecio1() {
        return  Precio1;
    }

    public void setPrecio1(String precio1 ) {
        this.Precio1 = precio1;
    }

    public String getPrecio2() {
        return  Precio2;
    }

    public void setPrecio2(String precio2 ) {
        this.Precio2 = precio2;
    }
    public String getPrecio3() {
        return  Precio3;
    }

    public void setPrecio3(String precio3 ) {
        this.Precio3 = precio3;
    }
}
