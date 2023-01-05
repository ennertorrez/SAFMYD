package com.saf.sistemas.safasuncion.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;
import com.saf.sistemas.safasuncion.Entidades.Precios;
import com.saf.sistemas.safasuncion.Entidades.TipoPrecio;
import com.saf.sistemas.safasuncion.Entidades.UnidadMedida;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Enner Torrez on 25/10/2019.
 */

public class PreciosHelper {
    private SQLiteDatabase database;

    public PreciosHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarPrecios(String empresa,
                               String codigo,
                               String codtipoprecio,
                               String tipoprecio,
                               String codum,
                               String um,
                               String unidades,
                               String monto) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.PRECIOS_COLUMN_EMPRESA, empresa);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_CODIGO, codigo);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO, codtipoprecio);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_TIPO_PRECIO, tipoprecio);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_COD_UM, codum);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_UM, um);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_UNIDADES, unidades);
        contentValues.put(variables_publicas.PRECIOS_COLUMN_MONTO, monto);

        long rowInserted = database.insert(variables_publicas.TABLE_PRECIOS, null, contentValues);
    }

    public void EliminaPrecios() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_PRECIOS + ";");
        Log.d("precios_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public List<Precios> ObtenerPrecioPorUM(String CodigoArticulo, String TipoCliente, String codUM) {
        List<Precios> list = new ArrayList<Precios>();

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_PRECIOS + " WHERE " + variables_publicas.PRECIOS_COLUMN_CODIGO + "= '" + CodigoArticulo + "' AND " + variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO + "= " + TipoCliente + " AND " + variables_publicas.PRECIOS_COLUMN_COD_UM + "= " + codUM + "";

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new Precios(cursor.getString(cursor.getColumnIndex("EMPRESA")),
                        cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("COD_TIPO_PRECIO")),
                        cursor.getString(cursor.getColumnIndex("TIPO_PRECIO")),
                        cursor.getString(cursor.getColumnIndex("COD_UM")),
                        cursor.getString(cursor.getColumnIndex("UM")),
                        cursor.getString(cursor.getColumnIndex("UNIDADES")),
                        cursor.getString(cursor.getColumnIndex("MONTO")
                        )));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    @SuppressLint("Range")
    public List<UnidadMedida> ObtenerUM(String CodigoArt, String CodTipoPrecio) {
        List<UnidadMedida> list = new ArrayList<UnidadMedida>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.PRECIOS_COLUMN_COD_UM  + "," + variables_publicas.PRECIOS_COLUMN_UM  + " FROM " + variables_publicas.TABLE_PRECIOS  + " Where " + variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO + " = " + CodTipoPrecio + " and  " + variables_publicas.PRECIOS_COLUMN_CODIGO + " = '" + CodigoArt + "'";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new UnidadMedida(
                        c.getString(c.getColumnIndex("COD_UM")),
                        c.getString(c.getColumnIndex("UM"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @SuppressLint("Range")
    public List<UnidadMedida> ObtenerListaUM(String CodTipoPrecio) {
        List<UnidadMedida> list = new ArrayList<UnidadMedida>();
        String Query = "SELECT DISTINCT " + variables_publicas.PRECIOS_COLUMN_COD_UM  + "," + variables_publicas.PRECIOS_COLUMN_UM  + " FROM " + variables_publicas.TABLE_PRECIOS  + " Where " + variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO + " = " + CodTipoPrecio + "";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new UnidadMedida(
                        c.getString(c.getColumnIndex("COD_UM")),
                        c.getString(c.getColumnIndex("UM"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    @SuppressLint("Range")
    public List<UnidadMedida> ObtenerUMxDescripcion(String vUm) {
        List<UnidadMedida> list = new ArrayList<UnidadMedida>();
        String Query = "SELECT DISTINCT " + variables_publicas.PRECIOS_COLUMN_COD_UM  + "," + variables_publicas.PRECIOS_COLUMN_UM  + " FROM " + variables_publicas.TABLE_PRECIOS  + " Where " + variables_publicas.PRECIOS_COLUMN_UM + " = '" + vUm + "'";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new UnidadMedida(
                        c.getString(c.getColumnIndex("COD_UM")),
                        c.getString(c.getColumnIndex("UM"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @SuppressLint("Range")
    public List<TipoPrecio> ObtenerTipoPrecio() {
        List<TipoPrecio> list = new ArrayList<TipoPrecio>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO  + "," + variables_publicas.PRECIOS_COLUMN_TIPO_PRECIO  + " FROM " + variables_publicas.TABLE_PRECIOS  + " ";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new TipoPrecio(
                        c.getString(c.getColumnIndex("COD_TIPO_PRECIO")),
                        c.getString(c.getColumnIndex("TIPO_PRECIO"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}
