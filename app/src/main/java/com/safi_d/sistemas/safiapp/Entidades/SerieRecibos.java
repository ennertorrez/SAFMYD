package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Sistemas on 27/3/2018.
 */

public class SerieRecibos {
    private String IdSerie;
    private String CodVendedor;
    private String nInicial;
    private String nFinal;
    private String Numero;

    public SerieRecibos(String codVendedor, String ninicial,String nfinal, String numero, String idSerie) {
        this.IdSerie = idSerie;
        this.CodVendedor = codVendedor;
        this.nInicial = ninicial;
        this.nFinal = nfinal;
        this.Numero = numero;
    }

    public String getIdSerie() {
        return IdSerie;
    }

    public void setIdSerie(String idSerie) {
        this.IdSerie = idSerie;
    }

    public String getCodVendedor() {
        return CodVendedor;
    }

    public void setCodVendedor(String codVendedor) {
        this.CodVendedor = codVendedor;
    }

    public String getnInicial() {
        return nInicial;
    }

    public void setnInicial(String ninicial) {
        this.nInicial = ninicial;
    }

    public String getnFinal() {
        return nFinal;
    }

    public void setnFinal(String nfinal) {
        this.nFinal = nfinal;
    }
    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        this.Numero = numero;
    }

}
