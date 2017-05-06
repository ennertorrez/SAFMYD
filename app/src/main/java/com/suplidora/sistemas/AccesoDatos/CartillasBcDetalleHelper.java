package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class CartillasBcDetalleHelper {

    private SQLiteDatabase database;

    public CartillasBcDetalleHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarCartillasBcDetalle(String id ,
                                            String itemV ,
                                          String descripcionV ,
                                          String cantidad ,
                                          String itemB ,
                                          String descripcionB ,
                                          String cantidadB ,
                                          String codigo ,
                                          String tipo ,
                                          String activo  ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_id, id);
         contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV, itemV);
         contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionV, descripcionV);
         contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad, cantidad);
         contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB, itemB);
         contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB, descripcionB);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB, cantidadB);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codigo, codigo);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo, tipo);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_activo, activo);

        database.insert(variables_publicas.TABLE_DETALLE_CARTILLAS_BC, null, contentValues);
    }
    public Cursor BuscarCartillasBcDetalle() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_DETALLE_CARTILLAS_BC +" ", null);
    }
    public  void EliminaCartillasBcDetalle() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+";");
        Log.d("CartillasBc_elimina", "Datos eliminados");
    }
    }
