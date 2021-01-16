package com.safi_d.sistemas.safiapp.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;

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
                                          String activo,
                                          String codumv,
                                          String codumb,
                                          String unidadesv,
                                          String unidadesb,
                                          String umb) {

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
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMV, codumv);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMB, codumb);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesV, unidadesv);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesB, unidadesb);
        contentValues.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_umB, umb);

        database.insert(variables_publicas.TABLE_DETALLE_CARTILLAS_BC, null, contentValues);
    }
    public Cursor BuscarCartillasBcDetalle() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_DETALLE_CARTILLAS_BC +" ", null);
    }
    public  void EliminaCartillasBcDetalle() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+";");
        Log.d("CartillasBc_elimina", "Datos eliminados");
    }


    public HashMap<String, String> BuscarBonificacion(String ItemV, String Canal, String Fecha,String Cantidad,String codUMV){
        HashMap<String,String> cartillaDetalle = new HashMap<String, String>();

        String selectQuery = "SELECT * FROM "+variables_publicas.TABLE_CARTILLAS_BC+" cb INNER JOIN "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+" db ON cb.codigo= db.codigo " +
                "WHERE db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV+"= '"+ItemV+"' AND DB."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo+" = '"+Canal+"' COLLATE NOCASE " +
                "AND cast(db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad+" as integer) <= cast("+Cantidad+" as integer) AND  ( DATE('"+Fecha+"') BETWEEN DATE(cb.fechaini) AND DATE(cb.fechafinal) )" +
                " AND db.activo = 'true'  ORDER BY cast(db."+variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQuery,null);
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
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMV,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMV)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMB)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesV,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesV)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesB)));
                cartillaDetalle.put(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_umB,c.getString(c.getColumnIndex(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_umB)));
            } while (c.moveToNext());
        }
        c.close();
        return cartillaDetalle;
    }

        public ArrayList<HashMap<String, String>> ListaBonificacionesCanal(String Canal)  {

        String selectQuery = "SELECT db.itemV,db.descripcionV,db.cantidad,db.itemB, db.descripcionB,db.cantidadB FROM "+variables_publicas.TABLE_CARTILLAS_BC+" cb INNER JOIN "+variables_publicas.TABLE_DETALLE_CARTILLAS_BC+" db ON cb.codigo= db.codigo " +
                "WHERE  DATE(cb.fechafinal) >= date('now') AND db.tipo='"+ Canal +"' ORDER BY cb.codigo";
        Cursor c = database.rawQuery(selectQuery,null);
            ArrayList<HashMap<String, String>> lista = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> promocion = new HashMap<>();
                promocion.put("itemV",c.getString(c.getColumnIndex("itemV")).split("-")[c.getString(c.getColumnIndex("itemV")).split("-").length -1]);
                promocion.put("descripcionV",c.getString(c.getColumnIndex("descripcionV")));
                promocion.put("cantidad",c.getString(c.getColumnIndex("cantidad")));
                promocion.put("itemB",c.getString(c.getColumnIndex("itemB")).split("-")[c.getString(c.getColumnIndex("itemB")).split("-").length -1]);
                promocion.put("descripcionB",c.getString(c.getColumnIndex("descripcionB")));
                promocion.put("cantidadB",c.getString(c.getColumnIndex("cantidadB")));
                promocion.put("codUMV",c.getString(c.getColumnIndex("codUMV")));
                promocion.put("codUMB",c.getString(c.getColumnIndex("codUMB")));
                promocion.put("unidadesV",c.getString(c.getColumnIndex("unidadesV")));
                promocion.put("unidadesB",c.getString(c.getColumnIndex("unidadesB")));
                promocion.put("umB",c.getString(c.getColumnIndex("umB")));
                lista.add(promocion);
            } while (c.moveToNext());
        }
        c.close();
        return lista;
    }
    }
