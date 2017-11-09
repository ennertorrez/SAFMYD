package com.suplidora.sistemas.sisago.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCargaDetalle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConsolidadoCargaDetalleHelper {

    private SQLiteDatabase database;

    public ConsolidadoCargaDetalleHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarConsolidadoCargaDetalle(String IdVehiculo,
                                String Factura ,
                               String ITEM ,
                               String Item_Descripcion ,
                                               String CANTIDAD ,
                                               String PRECIO ,
                                               String TOTAL ,
                                               String IVA ,
                               String DESCUENTO ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo, IdVehiculo);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura, Factura);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM, ITEM);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, Item_Descripcion);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD, CANTIDAD);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO, PRECIO);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL, TOTAL);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA, IVA);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO, DESCUENTO);

        database.insert(variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE, null, contentValues);
    }
    public ArrayList<HashMap<String, String>>  BuscarConsolidadoCargaDetalleXCodigo(String Factura,String busqueda) {
        String Query = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+" WHERE  "+variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura+" = '"+ Factura +"' AND "+ variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM+ " LIKE '%"+busqueda+"'";
        HashMap<String, String> cargaDetalle = null;
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();

        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                cargaDetalle = new HashMap<>();
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO)));

                lst.add(cargaDetalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public ArrayList<HashMap<String, String>>  BuscarConsolidadoCargaDetalleXFactura(String Factura) {
        String Query = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+" WHERE  "+variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura+" = '"+ Factura +"'";
        HashMap<String, String> cargaDetalle = null;
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();

        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                cargaDetalle = new HashMap<>();
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO)));

                lst.add(cargaDetalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }


    public ConsolidadoCargaDetalle BuscarConsolidadoCargaDetalle(String Factura, String codigo) {
        String Query = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+" WHERE  "+variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura+" = '"+ Factura +"' AND "+ variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM+ " = '"+codigo+"'";
        ConsolidadoCargaDetalle cargaDetalle = null;
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();

        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {

                cargaDetalle = new ConsolidadoCargaDetalle();
                cargaDetalle.setIdVehiculo( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo)));
                cargaDetalle.setFactura( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura)));
                cargaDetalle.setITEM( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM)));
                cargaDetalle.setItem_Descripcion( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion)));
                cargaDetalle.setCANTIDAD( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD)));
                cargaDetalle.setPRECIO( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO)));
                cargaDetalle.setTOTAL(c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL)));
                cargaDetalle.setIVA( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA)));
                cargaDetalle.setDESCUENTO( c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO)));
                c.close();
                return cargaDetalle;
        }
        return cargaDetalle;
    }

    public ArrayList<HashMap<String, String>>  BuscarConsolidadoCargaDetalleXNombre(String Factura,String busqueda) {
        String Query = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+" WHERE  "+variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura+" = '"+ Factura +"' AND "+ variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion+ " LIKE '%"+busqueda+"%'";
        //String Query = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE;
        HashMap<String, String> cargaDetalle = null;
        ArrayList<HashMap<String, String>> lst = new ArrayList<>();

        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                cargaDetalle = new HashMap<>();
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA)));
                cargaDetalle.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO)));

                lst.add(cargaDetalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public  void EliminaConsolidadoCargaDetalle() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+";");
        Log.d("ConsoliCargaDet_elimina", "Datos eliminados");
    }



    }
