package com.suplidora.sistemas.sisago.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

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
                                               String SUBTOTAL ,
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
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_SUBTOTAL, SUBTOTAL);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA, IVA);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO, DESCUENTO);

        database.insert(variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE, null, contentValues);
    }
    public Cursor BuscarConsolidadoCargaDetalle(String Usuario,String Contrasenia) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+" ", null);
    }
    public  void EliminaConsolidadoCargaDetalle() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE+";");
        Log.d("ConsoliCargaDet_elimina", "Datos eliminados");
    }



    }
