package com.saf.sistemas.safcafenorteno.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Entidades.Ruta;

import java.util.ArrayList;
import java.util.List;
public class RutasHelper {
    private SQLiteDatabase database;

    public RutasHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarRutas(String IdRuta,
                             String Ruta,
                             String Vendedor,
                             String Serie,
                             String Orden,String Recibo,String UltRecibo) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.RUTA_COLUMN_idRuta, IdRuta);
        contentValues.put(variables_publicas.RUTA_COLUMN_ruta, Ruta);
        contentValues.put(variables_publicas.RUTA_COLUMN_vendedor, Vendedor);
        contentValues.put(variables_publicas.RUTA_COLUMN_serie, Serie);
        contentValues.put(variables_publicas.RUTA_COLUMN_orden, Orden);
        contentValues.put(variables_publicas.RUTA_COLUMN_recibo, Recibo);
        contentValues.put(variables_publicas.RUTA_COLUMN_ultrecibo, UltRecibo);
        database.insert(variables_publicas.TABLE_RUTAS, null, contentValues);
    }

    public  void EliminaRutas() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_RUTAS+";");
        Log.d("Rutas_elimina", "Datos eliminados");
    }
    @SuppressLint("Range")
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
    @SuppressLint("Range")
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
    @SuppressLint("Range")
    public String  ObtenerVendedorRuta(String ruta) {

        String selectQuery = "SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_vendedor  + "  FROM " + variables_publicas.TABLE_RUTAS  + " WHERE " + variables_publicas.RUTA_COLUMN_idRuta  + "="+ ruta +" LIMIT 1;";
        Cursor c = database.rawQuery(selectQuery,null);
        String vIdVendedor="1";
        if (c.moveToFirst()) {
            do {
                vIdVendedor= c.getString(c.getColumnIndex(variables_publicas.RUTA_COLUMN_vendedor));
            } while (c.moveToNext());
        }
        c.close();

        return vIdVendedor;
    }
    @SuppressLint("Range")
    public String ObtenerSerieReciboRuta(int ruta,int idVendedor) {

        String sqlQuery ="SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_recibo  + " FROM " + variables_publicas.TABLE_RUTAS  + " WHERE " + variables_publicas.RUTA_COLUMN_vendedor  + "="+ idVendedor +" AND " + variables_publicas.RUTA_COLUMN_idRuta  + "="+ ruta +"";
        Cursor c = database.rawQuery(sqlQuery,null);
        String vSerieRecibo="A";
        if (c.moveToFirst()) {
            do {
                vSerieRecibo=c.getString(c.getColumnIndex("RECIBO"));
            } while (c.moveToNext());
        }
        c.close();
        return vSerieRecibo;
    }
    @SuppressLint("Range")
    public String ObtenerUltReciboRuta(int ruta,int idVendedor) {

        String sqlQuery ="SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_ultrecibo + " FROM " + variables_publicas.TABLE_RUTAS  + " WHERE " + variables_publicas.RUTA_COLUMN_vendedor  + "="+ idVendedor +" AND " + variables_publicas.RUTA_COLUMN_idRuta  + "="+ ruta +"";
        Cursor c = database.rawQuery(sqlQuery,null);
        String vUltimoRecibo="00000";
        if (c.moveToFirst()) {
            do {
                vUltimoRecibo=c.getString(c.getColumnIndex("ULTRECIBO"));
            } while (c.moveToNext());
        }
        c.close();
        return vUltimoRecibo;
    }
    @SuppressLint("Range")
    public String ObtenerSerieRuta(int ruta,int idVendedor) {

        String sqlQuery ="SELECT DISTINCT " + variables_publicas.RUTA_COLUMN_serie  + " FROM " + variables_publicas.TABLE_RUTAS  + " WHERE " + variables_publicas.RUTA_COLUMN_vendedor  + "="+ idVendedor +" AND " + variables_publicas.RUTA_COLUMN_idRuta  + "="+ ruta +"";
        Cursor c = database.rawQuery(sqlQuery,null);
        String vSerie="01";
        if (c.moveToFirst()) {
            do {
                vSerie=c.getString(c.getColumnIndex("SERIE"));
            } while (c.moveToNext());
        }
        c.close();
        return vSerie;
    }
}
