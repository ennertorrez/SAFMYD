package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Precios;
import com.saf.sistemas.safmyd.Entidades.TipoPrecio;
import com.saf.sistemas.safmyd.Entidades.UnidadMedida;

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

    public void GuardarPrecios(String Id ,
                               String CodigoArticulo ,
                               String IdCliente ,
                               String Descuento ,
                               String Precio,String Facturar ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Id, Id);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo, CodigoArticulo);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente, IdCliente);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento, Descuento);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio, Precio);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Facturar, Facturar);

        long rowInserted = database.insert(variables_publicas.TABLE_PRECIOS, null, contentValues);
    }

    public void EliminaPrecios() {
        try {
            database.execSQL("DELETE FROM " + variables_publicas.TABLE_PRECIOS + ";");
            Log.d("precios_elimina", "Datos eliminados");
        } catch (SQLException e) {

        }
    }

    @SuppressLint("Range")
    public Precios BuscarPrecioEspecial(String IdCliente,String CodigoArticulo) {
        Precios precioEspecial= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_PRECIOS + " where "+ variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente+" = "+IdCliente
                + " AND "+variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo +" = '"+CodigoArticulo+"'";
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                precioEspecial=(new Precios(c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Id)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Facturar))
                ));
            } while (c.moveToNext());
        }
        c.close();

        return precioEspecial;
    }
}
