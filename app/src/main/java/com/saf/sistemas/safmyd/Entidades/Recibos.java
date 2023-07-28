package com.saf.sistemas.safmyd.Entidades;

public class Recibos {
    String Serie ="";
    String Recibo ="";
    String Factura ="";
    String Fecha ="";
    String Monto ="";
    String NoCheque ="";
    String BancoR ="";
    String Abono ="";
    String Moneda ="";
    String TipoPago ="";
    String Concepto ="";
    String IdVendedor ="";
    String IdCliente ="";
    String Saldo ="";
    String Usuario ="";
    String Impresion ="";

    String Guardado ="";
    String Latitud ="";
    String Longitud ="";
    String DireccionGeo ="";
    public Recibos() {
    }
    public Recibos(String serie,
                   String recibo,
                   String factura,
                   String fecha,
                   String monto,
                   String nocheque,
                   String bancor,
                   String abono,
                   String moneda,
                   String tipopago,
                   String concepto,
                   String idvendedor,
                   String idcliente,
                   String saldo,
                   String usuario,
                   String impresion,
                   String guardado,
                   String latitud,
                   String longitud,
                   String direccionGeo
                   ) {
        Serie = serie;
        Recibo = recibo;
        Factura = factura;
        Fecha = fecha;
        Monto = monto;
        NoCheque = nocheque;
        BancoR = bancor;
        Abono = abono;
        Moneda = moneda;
        TipoPago = tipopago;
        Concepto = concepto;
        IdVendedor = idvendedor;
        IdCliente = idcliente;
        Saldo = saldo;
        Usuario = usuario;
        Impresion = impresion;
        Guardado = guardado;
        Latitud = latitud;
        Longitud = longitud;
        DireccionGeo = direccionGeo;
    }
        public String getSerie(){return Serie;}
        public String getRecibo() {return Recibo;}
        public String getFactura() {  return Factura;}
        public String getFecha() {  return Fecha;     }
        public String getMonto() {  return Monto;     }
        public String getNoCheque() {  return NoCheque;     }
        public String getBancoR() {  return BancoR;     }
        public String getAbono() {  return Abono;     }
        public String getMoneda() {  return Moneda;     }
        public String getTipoPago() {  return TipoPago;     }
        public String getConcepto() {  return Concepto;     }
        public String getIdVendedor() {  return IdVendedor;     }
        public String getIdCliente() {  return IdCliente;     }
        public String getSaldo() {  return Saldo;     }
        public String getUsuario() {  return Usuario;     }
        public String getImpresion() {  return Impresion;     }
        public String getGuardado() {  return Guardado;     }
        public String getLatitud() {  return Latitud;     }
        public String getLongitud() {  return Longitud;     }
        public String getDireccionGeo() {  return DireccionGeo;     }

        public void setSerie(String serie) { Serie=serie;     }
        public void setRecibo(String recibo) { Recibo=recibo;     }
        public void setFactura(String factura) { Factura=factura;     }
        public void setFecha(String fecha) { Fecha=fecha;     }
        public void setMonto(String monto) { Monto=monto;     }
        public void setNoCheque(String nocheque) { NoCheque=nocheque;     }
        public void setBancoR(String bancor) { BancoR=bancor;     }
        public void setAbono(String abono) { Abono=abono;     }
        public void setMoneda(String moneda) { Moneda=moneda;     }
        public void setTipoPago(String tipopago) { TipoPago=tipopago;     }
        public void setConcepto(String concepto) { Concepto=concepto;     }
        public void setIdVendedor(String idvendedor) { IdVendedor=idvendedor;     }
        public void setIdCliente(String idcliente) { IdCliente=idcliente;     }
        public void setSaldo(String saldo) { Saldo=saldo;     }
        public void setUsuario(String usuario) { Usuario=usuario;     }
        public void setImpresion(String impresion) { Impresion=impresion;     }
        public void setGuardado(String guardado) { Guardado=guardado;     }
        public void setLatitud(String latitud) { Latitud=latitud;     }
        public void setLongitud(String longitud) { Longitud=longitud;     }
        public void setDireccionGeo(String direccionGeo) { DireccionGeo=direccionGeo;     }

}
