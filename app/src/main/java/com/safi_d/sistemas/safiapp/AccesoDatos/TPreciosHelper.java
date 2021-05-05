package com.safi_d.sistemas.safiapp.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Articulo;
import com.safi_d.sistemas.safiapp.Entidades.Precios;
import com.safi_d.sistemas.safiapp.Entidades.TipoPrecio;
import java.util.ArrayList;
import java.util.List;

public class TPreciosHelper {
    private SQLiteDatabase database;

    public TPreciosHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarTPrecios(String codtipoprecio,
                               String tipoprecio) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO, codtipoprecio);
        contentValues.put(variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO, tipoprecio);

        long rowInserted = database.insert(variables_publicas.TABLE_TPRECIOS, null, contentValues);
    }

    public void EliminaTPrecios() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_TPRECIOS + ";");
        Log.d("tprecios_elimina", "Datos eliminados");
    }

        public List<TipoPrecio> ObtenerTipoPrecio() {
        List<TipoPrecio> list = new ArrayList<TipoPrecio>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO  + "," + variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO  + " FROM " + variables_publicas.TABLE_TPRECIOS  + " ";
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

    public List<TipoPrecio> ObtenerTipoPrecio2(String idCodPrecio) {
        List<TipoPrecio> list = new ArrayList<TipoPrecio>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO  + "," + variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO  + " FROM " + variables_publicas.TABLE_TPRECIOS  + " WHERE "+ variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO +" = "+ idCodPrecio +" ";
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

    public List<Articulo> ObtenerPrecioPorUM(String CodigoArticulo) {
        List<Articulo> list = new ArrayList<Articulo>();

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_ARTICULOS + " WHERE " + variables_publicas.ARTICULO_COLUMN_Codigo + "= '" + CodigoArticulo + "' ";

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new Articulo(cursor.getString(cursor.getColumnIndex("Codigo")),
                        cursor.getString(cursor.getColumnIndex("Nombre")),
                        cursor.getString(cursor.getColumnIndex("Costo")),
                        cursor.getString(cursor.getColumnIndex("Unidad")),
                        cursor.getString(cursor.getColumnIndex("UnidadCaja")),
                        cursor.getString(cursor.getColumnIndex("Precio")),
                        cursor.getString(cursor.getColumnIndex("Precio2")),
                        cursor.getString(cursor.getColumnIndex("Precio3")),
                        cursor.getString(cursor.getColumnIndex("Precio4")),
                        cursor.getString(cursor.getColumnIndex("CodUM")),
                        cursor.getString(cursor.getColumnIndex("PorIva")),
                        cursor.getString(cursor.getColumnIndex("DescuentoMaximo")),
                        cursor.getString(cursor.getColumnIndex("Existencia")),
                        cursor.getString(cursor.getColumnIndex("UnidadCajaVenta")),
                        cursor.getString(cursor.getColumnIndex("UnidadCajaVenta2")),
                        cursor.getString(cursor.getColumnIndex("UnidadCajaVenta3")),
                        cursor.getString(cursor.getColumnIndex("IdProveedor")
                        )));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
}
