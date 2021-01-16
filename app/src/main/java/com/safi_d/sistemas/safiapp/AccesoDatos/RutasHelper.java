package com.safi_d.sistemas.safiapp.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Ruta;
import com.safi_d.sistemas.safiapp.Entidades.SubZona;
import com.safi_d.sistemas.safiapp.Entidades.ZonaL;
import com.safi_d.sistemas.safiapp.Entidades.Zonas;

import java.util.ArrayList;
import java.util.List;
public class RutasHelper {
    private SQLiteDatabase database;

    public RutasHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarRutas(String IdRuta,
                             String Ruta,
                             String Vendedor) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.RUTA_COLUMN_idRuta, IdRuta);
        contentValues.put(variables_publicas.RUTA_COLUMN_ruta, Ruta);
        contentValues.put(variables_publicas.RUTA_COLUMN_vendedor, Vendedor);
        database.insert(variables_publicas.TABLE_RUTAS, null, contentValues);
    }

    public  void EliminaRutas() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_RUTAS+";");
        Log.d("Rutas_elimina", "Datos eliminados");
    }
    public List<Ruta> ObtenerListaRutas() {
        List<Ruta> list = new ArrayList<Ruta>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_idRuta  + "," + variables_publicas.RUTA_COLUMN_ruta  + " FROM " + variables_publicas.TABLE_RUTAS  + " ";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new Ruta(
                        c.getString(c.getColumnIndex("IDRUTA")),
                        c.getString(c.getColumnIndex("RUTA"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
    public List<Ruta> ObtenerRutaVendedor(int idVendedor) {
        List<Ruta> list = new ArrayList<Ruta>();
        String sqlQuery ="SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_idRuta  + "," + variables_publicas.RUTA_COLUMN_ruta  + " FROM " + variables_publicas.TABLE_RUTAS  + " WHERE " + variables_publicas.RUTA_COLUMN_vendedor  + "="+ idVendedor +"";
        Cursor c = database.rawQuery(sqlQuery,null);
        if (c.moveToFirst()) {
            do {
                list.add(new Ruta(
                        c.getString(c.getColumnIndex("IDRUTA")),
                        c.getString(c.getColumnIndex("RUTA"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}
