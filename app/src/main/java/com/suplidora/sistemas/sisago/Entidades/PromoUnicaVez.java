package com.suplidora.sistemas.sisago.Entidades;

public class PromoUnicaVez {
    private String Cliente;
    private String Item;
    private String CodCV;

    public PromoUnicaVez(String cliente, String item, String codcv) {
        this.Cliente = cliente;
        this.Item = item;
        this.CodCV = codcv;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        this.Cliente = cliente;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        this.Item = item;
    }

    public String getCodCV() {
        return CodCV;
    }

    public void setCodCV(String codcv) {
        this.CodCV = codcv;
    }

}
