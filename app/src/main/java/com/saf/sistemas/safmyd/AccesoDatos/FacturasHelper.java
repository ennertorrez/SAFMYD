package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Factura;

import java.util.ArrayList;
import java.util.HashMap;

public class FacturasHelper {
    private SQLiteDatabase database;

    public FacturasHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarFactura(String Factura,
                                  String Fecha,
                                  String Cliente,
                                  String Monto,
                                  String SubTotal,
                                  String Descuento,
                                  String Iva,
                                  String Total,
                                  String Vendedor,
                                  String Tipo,
                                  String TipoFactura,
                                  String Empresa,
                                  String Usuario,
                                  String Sucursal,
                                  String Ruta,
                                  String Orden,
                                  String Guardada,
                                  String Latitud,
                                  String Longitud,
                                  String DireccionGeo) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_COLUMN_noFactura, Factura);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_fecha, Fecha);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_cliente, Cliente);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_monto, Monto);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_subTotal, SubTotal);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_descuento, Descuento);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_iva, Iva);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_total, Total);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_vendedor, Vendedor);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_tipo, Tipo);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_tipoFactura, TipoFactura);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_empresa, Empresa);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_usuario, Usuario);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_sucursal, Sucursal);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_ruta, Ruta);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_orden, Orden);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_guardada, Guardada);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_latitud, Latitud);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_longitud, Longitud);
        contentValues.put(variables_publicas.FACTURAS_COLUMN_direccionGeo, DireccionGeo);
        long rowInserted = database.insert(variables_publicas.TABLE_FACTURAS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }


    public boolean EliminaFactura(String factura) {
        long rowInserted = database.delete(variables_publicas.TABLE_FACTURAS, variables_publicas.FACTURAS_COLUMN_noFactura + "= '" + factura+"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public HashMap<String, String> ObtenerFactura(String NoFactura) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_FACTURAS  + " Where " + variables_publicas.FACTURAS_COLUMN_noFactura + " = ?", new String[]{NoFactura});
        HashMap<String, String> factura = null;
        if (c.moveToFirst()) {
            do {
                factura = new HashMap<>();
                factura.put(variables_publicas.FACTURAS_COLUMN_noFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_noFactura)));
                factura.put(variables_publicas.FACTURAS_COLUMN_fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_fecha)));
                factura.put(variables_publicas.FACTURAS_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_cliente)));
                factura.put(variables_publicas.FACTURAS_COLUMN_monto, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_monto)));
                factura.put(variables_publicas.FACTURAS_COLUMN_subTotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_subTotal)));
                factura.put(variables_publicas.FACTURAS_COLUMN_descuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_descuento)));
                factura.put(variables_publicas.FACTURAS_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_iva)));
                factura.put(variables_publicas.FACTURAS_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_total)));
                factura.put(variables_publicas.FACTURAS_COLUMN_vendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_vendedor)));
                factura.put(variables_publicas.FACTURAS_COLUMN_tipo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipo)));
                factura.put(variables_publicas.FACTURAS_COLUMN_tipoFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipoFactura)));
                factura.put(variables_publicas.FACTURAS_COLUMN_empresa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_empresa)));
                factura.put(variables_publicas.FACTURAS_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_usuario)));
                factura.put(variables_publicas.FACTURAS_COLUMN_sucursal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_sucursal)));
                factura.put(variables_publicas.FACTURAS_COLUMN_ruta, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_ruta)));
                factura.put(variables_publicas.FACTURAS_COLUMN_orden, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_orden)));
                factura.put(variables_publicas.FACTURAS_COLUMN_guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_guardada)));
                factura.put(variables_publicas.FACTURAS_COLUMN_latitud, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_latitud)));
                factura.put(variables_publicas.FACTURAS_COLUMN_longitud, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_longitud)));
                factura.put(variables_publicas.FACTURAS_COLUMN_direccionGeo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_direccionGeo)));

            } while (c.moveToNext());
        }
        c.close();
        return factura;
    }

    @SuppressLint("Range")
    public Factura GetFactura(String NoFactura) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_FACTURAS + " Where " + variables_publicas.FACTURAS_COLUMN_noFactura + " = ?", new String[]{NoFactura});
        Factura factura = null;
        if (c.moveToFirst()) {
            do {
                factura = new Factura();
                factura.setFactura(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_noFactura)));
                factura.setFecha(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_fecha)));
                factura.setCliente(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_cliente)));
                factura.setMonto(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_monto)));
                factura.setSubTotal(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_subTotal)));
                factura.setDescuento(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_descuento)));
                factura.setIva(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_iva)));
                factura.setTotal(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_total)));
                factura.setVendedor(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_vendedor)));
                factura.setTipo(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipo)));
                factura.setTipo_Factura(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipoFactura)));
                factura.setEmpresa(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_empresa)));
                factura.setUsuario(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_usuario)));
                factura.setSucursal(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_sucursal)));
                factura.setRuta(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_ruta)));
                factura.setOrden(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_orden)));
                factura.setGuardada(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_guardada)));
                factura.setLatitud(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_latitud)));
                factura.setLongitud(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_longitud)));
                factura.setDireccionGeo(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_direccionGeo)));
            } while (c.moveToNext());
        }
        c.close();
        return factura;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerFacturasLocales(String Fecha,String Fecha2, String Nombre) {

        String selectQuery = "SELECT F.*,DATE(F.Fecha) as Fecha,'NO ENVIADA' as Estado,Cl.Nombre as NombreCliente, R." + variables_publicas.RUTA_COLUMN_ruta + " AS NombreRuta FROM " + variables_publicas.TABLE_FACTURAS +
                " F INNER JOIN " + variables_publicas.TABLE_CLIENTES + " Cl ON CAST( Cl." + variables_publicas.CLIENTES_COLUMN_IdCliente + " AS INT) = cast(F." + variables_publicas.FACTURAS_COLUMN_cliente + " AS INT)  " +
                " INNER JOIN "+ variables_publicas.TABLE_RUTAS +" R  ON CAST( R." + variables_publicas.RUTA_COLUMN_idRuta + " AS INT) = CAST( F." + variables_publicas.FACTURAS_COLUMN_ruta + " AS INT) WHERE F."+variables_publicas.FACTURAS_COLUMN_guardada+" = 'False' AND Cl." + variables_publicas.CLIENTES_COLUMN_Nombre + " LIKE '%" + Nombre + "%' AND DATE(F." + variables_publicas.FACTURAS_COLUMN_fecha + ") between DATE('" + Fecha + "') AND DATE('" + Fecha2 + "')"+
                " AND F."+ variables_publicas.FACTURAS_COLUMN_vendedor + " = "+variables_publicas.usuario.getCodigo();

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> factura = new HashMap<>();
                factura.put(variables_publicas.FACTURAS_COLUMN_noFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_noFactura)));
                factura.put("NombreCliente", c.getString(c.getColumnIndex("NombreCliente")));
                factura.put("Estado", c.getString(c.getColumnIndex("Estado")));
                factura.put("NombreRuta", c.getString(c.getColumnIndex("NombreRuta")));
                factura.put(variables_publicas.FACTURAS_COLUMN_fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_fecha)));
                factura.put(variables_publicas.FACTURAS_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_cliente)));
                factura.put(variables_publicas.FACTURAS_COLUMN_monto, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_monto)));
                factura.put(variables_publicas.FACTURAS_COLUMN_subTotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_subTotal)));
                factura.put(variables_publicas.FACTURAS_COLUMN_descuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_descuento)));
                factura.put(variables_publicas.FACTURAS_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_iva)));
                factura.put(variables_publicas.FACTURAS_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_total)));
                factura.put(variables_publicas.FACTURAS_COLUMN_vendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_vendedor)));
                factura.put(variables_publicas.FACTURAS_COLUMN_tipo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipo)));
                factura.put(variables_publicas.FACTURAS_COLUMN_tipoFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipoFactura)));
                factura.put(variables_publicas.FACTURAS_COLUMN_empresa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_empresa)));
                factura.put(variables_publicas.FACTURAS_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_usuario)));
                factura.put(variables_publicas.FACTURAS_COLUMN_sucursal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_sucursal)));
                factura.put(variables_publicas.FACTURAS_COLUMN_ruta, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_ruta)));
                factura.put(variables_publicas.FACTURAS_COLUMN_orden, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_orden)));
                factura.put(variables_publicas.FACTURAS_COLUMN_guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_guardada)));
                factura.put(variables_publicas.FACTURAS_COLUMN_latitud, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_latitud)));
                factura.put(variables_publicas.FACTURAS_COLUMN_longitud, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_longitud)));
                factura.put(variables_publicas.FACTURAS_COLUMN_direccionGeo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_direccionGeo)));
                lst.add(factura);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    @SuppressLint("Range")
    public Factura BuscarFacturasSinconizar( ) {
        Factura facturas = null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_FACTURAS+"";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                facturas = (new Factura(c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_noFactura)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_fecha)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_cliente)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_monto)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_subTotal)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_iva)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_total)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_vendedor)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_tipoFactura)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_empresa)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_usuario)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_sucursal)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_ruta)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_orden)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_guardada)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_latitud)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_longitud)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_COLUMN_direccionGeo))

                ));
            } while (c.moveToNext());
        }
        c.close();
        return facturas;
    }
    public boolean ActualizarFactura(String NoFactura, String Estado) {
        ContentValues con = new ContentValues();
        con.put("Guardada", Estado);
        long rowInserted = database.update(variables_publicas.TABLE_FACTURAS, con, variables_publicas.FACTURAS_COLUMN_noFactura + "= '" + NoFactura +"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public int ObtenerCosecutivoFactura(String idruta, String serie) {

        String sql="SELECT " + variables_publicas.RUTA_COLUMN_orden + " AS Orden FROM "
                + variables_publicas.TABLE_RUTAS+" WHERE "+variables_publicas.RUTA_COLUMN_idRuta+" = "+idruta+" AND "+ variables_publicas.RUTA_COLUMN_serie +"='"+ serie +"'";

/*        String sql="SELECT MAX(" + variables_publicas.FACTURAS_COLUMN_orden + ") AS Orden FROM "
                + variables_publicas.TABLE_FACTURAS+" WHERE substr("+variables_publicas.FACTURAS_COLUMN_noFactura+") = '"+Serie+"'";*/
        Cursor c= database.rawQuery(sql, null);

        int resultado = 0;
        if (c.moveToFirst()) {
            do {
                resultado = Integer.parseInt(c.getString(0));

            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }
    public int ObtenerCosecutivoFactura2(String serie) {

        String sql="SELECT MAX(" + variables_publicas.FACTURAS_COLUMN_orden + ") AS Orden FROM "
                + variables_publicas.TABLE_FACTURAS+" WHERE substr("+variables_publicas.FACTURAS_COLUMN_noFactura+",0,2) = '"+serie+"'";
        Cursor c= database.rawQuery(sql, null);

        int resultado = 0;
        if (c.moveToFirst()) {
            do {
                if(c.getString(0)!=null){
                    resultado = Integer.parseInt(c.getString(0));
                }
            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }
}
