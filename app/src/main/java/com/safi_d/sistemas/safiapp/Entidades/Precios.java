package com.safi_d.sistemas.safiapp.Entidades;

/**
 * Created by Enner Torrez on 25/10/2019.
 */

public class Precios {
    String EMPRESA;
    String CODIGO;
    String COD_TIPO_PRECIO;
    String TIPO_PRECIO;
    String COD_UM;
    String UM;
    String UNIDADES;
    String MONTO;

    public Precios(String empresa, String codigo, String codtipoprecio, String tipoprecio, String codum, String um, String unidades, String monto) {
        this.EMPRESA =empresa;
        this.CODIGO=codigo;
        this.COD_TIPO_PRECIO = codtipoprecio;
        this.TIPO_PRECIO =tipoprecio;
        this.COD_UM=codum;
        this.UM=um;
        this.UNIDADES=unidades;
        this.MONTO=monto;
    }

    public Precios() {

    }

    public String getCODIGO() {
        return  CODIGO;
    }

    public void setCODIGO(String codigo) {
        this.CODIGO = codigo;
    }

    public String getEMPRESA() {
        return  EMPRESA;
    }

    public void setEMPRESA(String empresa) {
        this.EMPRESA = empresa;
    }

    public String getCOD_TIPO_PRECIO() {
        return  COD_TIPO_PRECIO;
    }

    public void setCOD_TIPO_PRECIO(String cod_tipo_precio) {
        this.COD_TIPO_PRECIO = cod_tipo_precio;
    }
    public String getTIPO_PRECIO() {
        return  TIPO_PRECIO;
    }

    public void setTIPO_PRECIO(String tipo_precio) {
        this.TIPO_PRECIO = tipo_precio;
    }

    public String getUM() {
        return  UM;
    }

    public void setUM(String um ) {
        this.UM = um;
    }

    public String getCOD_UM() {
        return  COD_UM;
    }

    public void setCOD_UM(String cod_um ) {
        this.COD_UM = cod_um;
    }

    public String getUNIDADES() {
        return  UNIDADES;
    }

    public void setUNIDADES(String unidades ) {
        this.UNIDADES = unidades;
    }
    public String getMONTO() {
        return  MONTO;
    }

    public void setMONTO(String monto ) {
        this.MONTO = monto;
    }
}
