package com.saf.sistemas.safmyd.Entidades;

/**
 * Created by Enner Torrez on 29/10/2019.
 */

public class ClienteCategoria {
    String Cod_Cat;
    String Categoria;
    public ClienteCategoria(String cod_Cat,String categoria) {
        this.Cod_Cat = cod_Cat;
        this.Categoria = categoria;
    }

    public ClienteCategoria() {

    }

    public String getCod_Cat() {
        return Cod_Cat;
    }

    public void setCod_Cat(String cod_Cat) {
        this.Cod_Cat = cod_Cat;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        this.Categoria = categoria;
    }

    public String toString(){
        return this.getCategoria();
    }
}
