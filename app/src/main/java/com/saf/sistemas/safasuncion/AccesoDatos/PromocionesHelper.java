package com.saf.sistemas.safasuncion.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;

import java.util.HashMap;

public class PromocionesHelper {
    private SQLiteDatabase database;
    public PromocionesHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarPromociones(String codpromo ,
                                   String itemv ,
                                   String cantv ,
                                   String itemb ,
                                   String cantb ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_codPromo, codpromo);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_itemV, itemv);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_cantV, cantv);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_itemB, itemb);
        contentValues.put(variables_publicas.PROMOCIONES_COLUMN_cantB, cantb);

        database.insert(variables_publicas.TABLE_PROMOCIONES, null, contentValues);
    }
    public  void EliminaPromociones() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PROMOCIONES+";");
        Log.d("Promociones_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public HashMap<String, String> BuscarPromocion(int promo){
        HashMap<String,String> promocionDetalle = new HashMap<String, String>();

            String selectQuery3 = "SELECT DISTINCT GROUP_CONCAT(pr."+ variables_publicas.PROMOCIONES_COLUMN_itemV +") AS Productos, PR."+ variables_publicas.PROMOCIONES_COLUMN_cantV +" as CantidadV " +
                    ", PR."+ variables_publicas.PROMOCIONES_COLUMN_itemB +" as ItemB,PR."+ variables_publicas.PROMOCIONES_COLUMN_cantB +" as CantidadB FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                    "WHERE PR."+variables_publicas.PROMOCIONES_COLUMN_codPromo+"= "+ promo +"";
            Cursor c = database.rawQuery(selectQuery3,null);
            if (c.moveToFirst()) {
                do {
                    promocionDetalle.put("Articulos",c.getString(c.getColumnIndex("Productos")));
                    promocionDetalle.put("CantidadV",c.getString(c.getColumnIndex("CantidadV")));
                    promocionDetalle.put("ItemB", c.getString(c.getColumnIndex("ItemB")));
                    promocionDetalle.put("CantidadB", c.getString(c.getColumnIndex("CantidadB")));

                } while (c.moveToNext());
            }
            c.close();

        return promocionDetalle;
    }

    @SuppressLint("Range")
    public int  BuscarCodPromocion(String producto) {

        String selectQuery = "SELECT pr."+ variables_publicas.PROMOCIONES_COLUMN_codPromo +" FROM "+variables_publicas.TABLE_PROMOCIONES+" PR " +
                "WHERE PR."+variables_publicas.PROMOCIONES_COLUMN_itemV+"= '"+producto+"' ORDER BY cast(PR."+variables_publicas.PROMOCIONES_COLUMN_codPromo+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQuery,null);
        int vIdPromo=0;
        if (c.moveToFirst()) {
            do {
                vIdPromo= Integer.parseInt(c.getString(c.getColumnIndex(variables_publicas.PROMOCIONES_COLUMN_codPromo)));
            } while (c.moveToNext());
        }
        c.close();

        return vIdPromo;
    }

}
