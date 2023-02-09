package com.saf.sistemas.safcafenorteno.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacturasLineasHelper {
    private SQLiteDatabase database;

    public FacturasLineasHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarFacturasLineas(String Factura,
                                      String Item,
                                      String Cantidad,
                                      String Precio,
                                      String Iva,
                                      String Total,
                                      String TipoArt,
                                      String PorcentajeIva,
                                      String Descripcion,
                                      String PorDescuento,
                                      String Presentacion,
                                      String Subtotal,
                                      String Costo,
                                      String BonificaA,
                                      String CodUM,
                                      String Unidades,
                                      String Barra,
                                      String Empresa
                                      ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura, Factura);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_item, Item);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, Cantidad);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_precio, Precio);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_iva, Iva);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_total, Total);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart, TipoArt);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva, PorcentajeIva);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion, Descripcion);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento, PorDescuento);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion, Presentacion);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal, Subtotal);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_costo, Costo);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa, BonificaA);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_codum, CodUM);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades, Unidades);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_barra, Barra);
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa, Empresa);
        database.insert(variables_publicas.TABLE_FACTURAS_LINEAS, null, contentValues);
    }


    public boolean GuardarFacturasLineas(HashMap<String,String> lstArticulos) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_item, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_item));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_precio, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_precio));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_iva, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_iva));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_total, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_total));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_costo, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_costo));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_codum, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_codum));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_barra, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_barra));
        contentValues.put(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa, lstArticulos.get(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa));
        long rowInserted=database.insert(variables_publicas.TABLE_FACTURAS_LINEAS, null, contentValues);
        if(rowInserted != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public List<HashMap<String, String>> ObtenerFacturasLineas(String NoFactura) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        String sqlQuery ="SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_LINEAS + " WHERE " + variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura + " = '" + NoFactura + "'";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_item, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_item)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_precio, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_precio)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_iva)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_total)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_costo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_costo)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_codum, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_codum)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_barra, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_barra)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa)));

                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerFacturasListasArrayList(String NoFactura) {
        ArrayList<HashMap<String,String>> lst= new ArrayList<>();
        String sqlQuery="SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_LINEAS + " WHERE " + variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura + " = '" + NoFactura + "'";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_item, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_item)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_precio, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_precio)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_iva)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_total)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_porcentajeiva)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_descripcion)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_pordescuento)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_presentacion)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_subtotal)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_costo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_costo)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_bonificaa)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_codum, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_codum)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_unidades)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_barra, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_barra)));
                detalle.put(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_empresa)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean EliminarFacturasLineas(String NoFactura) {
        int rowsAffected =database.delete( variables_publicas.TABLE_FACTURAS_LINEAS, variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura+ "='" +NoFactura+"'",null) ;
        if(rowsAffected != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerCantidadActualFactura(String factura,String item, String tipo) {

        String selectQuery = "SELECT "+ variables_publicas.FACTURAS_LINEAS_COLUMN_item +","+ variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad +","+   variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart +" FROM " + variables_publicas.TABLE_FACTURAS_LINEAS +
                " WHERE "+ variables_publicas.FACTURAS_LINEAS_COLUMN_noFactura +"='"+ factura +"' AND "+ variables_publicas.FACTURAS_LINEAS_COLUMN_item +"='"+ item +"' AND "+ variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart +"='"+ tipo +"';";
        Cursor c = database.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> fact = new HashMap<>();
                fact.put(variables_publicas.FACTURAS_LINEAS_COLUMN_item, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_item)));
                fact.put(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad)));
                fact.put(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_tipoart)));
                lst.add(fact);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
}
