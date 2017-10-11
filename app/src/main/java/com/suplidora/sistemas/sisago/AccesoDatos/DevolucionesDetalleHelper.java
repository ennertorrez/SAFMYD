package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevolucionesDetalleHelper {


    private SQLiteDatabase database;

    public DevolucionesDetalleHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarDetalleDevolucion(String ndevolucion, String item,
                                     String cantidad,
                                     String precio,
                                     String iva,
                                     String subtotal,
                                     String total,
                                     String poriva,
                                     String descuento,
                                     String tipo,
                                     String numero,
                                     String factura
    ) {

        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion, ndevolucion);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item, item);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad, cantidad);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio, precio);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva, iva);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal, subtotal);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total, total);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva, poriva);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento, descuento);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo, tipo);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero, numero);
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura, factura);
        database.insert(variables_publicas.TABLE_DEVOLUCIONES_DETALLE, null, contentValues);
    }


    public boolean GuardarDetalleDevolucion(HashMap<String,String> lstArticulos) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero));
        contentValues.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura, lstArticulos.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura));
        long rowInserted=database.insert(variables_publicas.TABLE_DEVOLUCIONES_DETALLE, null, contentValues);
        if(rowInserted != -1)
           return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerDevolucionDetalle(String ndevolucion) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_DEVOLUCIONES_DETALLE + " WHERE " + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion + " = ? ", new String[]{ndevolucion});
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public ArrayList<HashMap<String, String>> ObtenerDevolucionDetalleArrayList(String ndevolucion) {
        ArrayList<HashMap<String,String>> lst= new ArrayList<>();
        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_DEVOLUCIONES_DETALLE + " WHERE " + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion + " = ? ", new String[]{ndevolucion});
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero)));
                detalle.put(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public void LimpiarDevolucionDetalle() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_DEVOLUCIONES_DETALLE + ";");
        Log.d("devolucionDet_elimina", "Datos eliminados");
    }
    public void EliminarDetalleDevolucion(String ndevolucion) {
        int rowsAffected =database.delete( variables_publicas.TABLE_DEVOLUCIONES_DETALLE, variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion+ "='" +ndevolucion+"'",null) ;
        Log.d("Det.DevolucionDeleted: "+ndevolucion, "Datos eliminados");
    }

    public boolean Actualizarndevolucion(String ndevolucion, String NoDevolucion){
        ContentValues con = new ContentValues();
        con.put("ndevolucion", NoDevolucion);
        long rowInserted= database.update(variables_publicas.TABLE_DEVOLUCIONES_DETALLE, con, variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion +"='"+ndevolucion+"'", null );
        if(rowInserted != -1)
            return true;
        else return false;
    }

}