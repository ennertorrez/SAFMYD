package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;

import java.util.HashMap;

public class EscalaPreciosHelper {
    private SQLiteDatabase database;

    public EscalaPreciosHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarEscalaPrecios(String codigo,
                               String lista,
                               String escala1,
                               String escala2,
                               String escala3,
                               String precio1,
                               String precio2,
                               String precio3) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA, codigo);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_LISTAARTICULOS, lista);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA1, escala1);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA2, escala2);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA3, escala3);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO1, precio1);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO2, precio2);
        contentValues.put(variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO3, precio3);

        long rowInserted = database.insert(variables_publicas.TABLE_ESCALAPRECIOS, null, contentValues);
    }

    public void EliminaEscalaPrecios() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_ESCALAPRECIOS + ";");
        Log.d("escalaprecios_elimina", "Datos eliminados");
    }


    @SuppressLint("Range")
    public HashMap<String, String> ObtenerEscala(int escala){
        HashMap<String,String> escalaprecios = new HashMap<String, String>();

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_ESCALAPRECIOS + " WHERE " + variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA + " = " + escala + " ";
        Cursor c = database.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            do {
                escalaprecios.put("ListaArticulos",c.getString(c.getColumnIndex("ListaArticulos")));
                escalaprecios.put("Escala1",c.getString(c.getColumnIndex("Escala1")));
                escalaprecios.put("Escala2", c.getString(c.getColumnIndex("Escala2")));
                escalaprecios.put("Escala3", c.getString(c.getColumnIndex("Escala3")));
                escalaprecios.put("Precio1",c.getString(c.getColumnIndex("Precio1")));
                escalaprecios.put("Precio2", c.getString(c.getColumnIndex("Precio2")));
                escalaprecios.put("Precio3", c.getString(c.getColumnIndex("Precio3")));
            } while (c.moveToNext());
        }
        c.close();

        return escalaprecios;
    }
    @SuppressLint("Range")
    public int  BuscarCodEscala(String producto) {

        String selectQuery = "SELECT pr."+ variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA +" FROM "+variables_publicas.TABLE_ESCALAPRECIOS+" PR " +
                "WHERE PR."+variables_publicas.ESCALAPRECIOS_COLUMN_LISTAARTICULOS+" LIKE '%"+producto+"%' ORDER BY cast(PR."+variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA+" as integer) DESC LIMIT 1";
        Cursor c = database.rawQuery(selectQuery,null);
        int vIdEscala=0;
        if (c.moveToFirst()) {
            do {
                vIdEscala= Integer.parseInt(c.getString(c.getColumnIndex(variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA)));
            } while (c.moveToNext());
        }
        c.close();

        return vIdEscala;
    }
}
