package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.CartillaDetalleBC;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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


    public HashMap<String, String> BuscarBonificacion(String ItemV, String Canal, String Fecha,String Cantidad){
        HashMap<String,String> cartillaDetalle = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM "+variables_publicas.TABLE_CARTILLAS_BC+" cb INNER JOIN "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+" db ON cb.codigo= db.codigo " +
                "WHERE db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV+"= ? AND DB."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo+" = ? COLLATE NOCASE " +
                "AND db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad+" <= ? AND  ( ? BETWEEN cb.fechaini AND cb.fechafinal)" +
                " AND db.activo = 'true'";
        Cursor c = database.rawQuery(selectQuery, new String[]{ItemV,Canal,Cantidad,Fecha});
        if (c.moveToFirst()) {
            do {
              cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_id,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_id)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionV,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionV)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codigo,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codigo)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo)));
            } while (c.moveToNext());
        }
        c.close();
        return cartillaDetalle;
    }

    }
