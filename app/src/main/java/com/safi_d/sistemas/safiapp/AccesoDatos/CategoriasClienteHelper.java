package com.safi_d.sistemas.safiapp.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.ClienteCategoria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enner Torrez on 29/10/2019.
 */

public class CategoriasClienteHelper {

    private SQLiteDatabase database;

    public CategoriasClienteHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarCategoria(String cod, String categoria){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.CATEGORIAS_COLUMN_Cod_Cat, cod);
        contentValues.put(variables_publicas.CATEGORIAS_COLUMN_Categoria, categoria);
        long rowInserted = database.insert(variables_publicas.TABLE_CATEGORIAS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    public boolean EliminarCategoria() {
        long deletedrows=  database.delete( variables_publicas.TABLE_CATEGORIAS,null,null);
        Log.d("categorias_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }

    public List<ClienteCategoria> ObtenerCategorias() {
        List<ClienteCategoria> list = new ArrayList<ClienteCategoria>();

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_CATEGORIAS+ " ORDER BY "+ variables_publicas.CATEGORIAS_COLUMN_Categoria ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new ClienteCategoria(
                        cursor.getString(cursor.getColumnIndex("Cod_Cat")),
                        cursor.getString(cursor.getColumnIndex("Categoria"))
                ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
