package com.saf.sistemas.safasuncion.AccesoDatos;

/**
 * Created by Enner Torrez on 22/10/2019.
 */
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;
import com.saf.sistemas.safasuncion.Entidades.SubZona;
import com.saf.sistemas.safasuncion.Entidades.ZonaL;

import java.util.ArrayList;
import java.util.List;

public class ZonasHelper {
    private SQLiteDatabase database;

    public ZonasHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarZonas(String empresa,
                                   String codZona ,
                                   String zona ,
                                   String codSubZona ,
                                   String subZona  ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.ZONAS_COLUMN_EMPRESA, empresa);
        contentValues.put(variables_publicas.ZONAS_COLUMN_CODZONA, codZona);
        contentValues.put(variables_publicas.ZONAS_COLUMN_ZONA, zona);
        contentValues.put(variables_publicas.ZONAS_COLUMN_CODSUBZONA, codSubZona);
        contentValues.put(variables_publicas.ZONAS_COLUMN_SUBZONA, subZona);

        database.insert(variables_publicas.TABLE_ZONAS, null, contentValues);
    }

    public Cursor BuscarZonas(String Empresa) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_ZONAS +" WHERE "+ variables_publicas.ZONAS_COLUMN_EMPRESA + " = "+ Empresa +" ", null);
    }
    public  void EliminaZonas() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ZONAS+";");
        Log.d("Zonas_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public List<ZonaL> ObtenerListaZonas() {
        List<ZonaL> list = new ArrayList<ZonaL>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.ZONAS_COLUMN_CODZONA  + "," + variables_publicas.ZONAS_COLUMN_ZONA  + " FROM " + variables_publicas.TABLE_ZONAS  + " ";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new ZonaL(
                        c.getString(c.getColumnIndex("Cod_Zona")),
                        c.getString(c.getColumnIndex("Zona"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @SuppressLint("Range")
    public List<SubZona> ObtenerListaSubZonas(String zona) {
        List<SubZona> list = new ArrayList<SubZona>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.ZONAS_COLUMN_CODSUBZONA  + "," + variables_publicas.ZONAS_COLUMN_SUBZONA  + " FROM " + variables_publicas.TABLE_ZONAS  + " WHERE " + variables_publicas.ZONAS_COLUMN_ZONA  + "='"+ zona +"'";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new SubZona(
                        c.getString(c.getColumnIndex("Cod_SubZona")),
                        c.getString(c.getColumnIndex("SubZona"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}
