package com.suplidora.sistemas.sisago.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCarga;
import com.suplidora.sistemas.sisago.Entidades.DtoConsolidadoCargaFacturas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class ConsolidadoCargaHelper {

    private SQLiteDatabase database;

    public ConsolidadoCargaHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarConsolidadoCarga(String IdConsolidado,
                                String Factura ,
                               String Cliente ,
                               String Vendedor ,
                               String Direccion ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado, IdConsolidado);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura, Factura);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente, Cliente);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor, Vendedor);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion, Direccion);

        database.insert(variables_publicas.TABLE_CONSOLIDADO_CARGA, null, contentValues);
    }
   /* public Cursor BuscarConsolidadoCarga() {
        return database.rawQuery("select DISTINCT "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" from " + variables_publicas.TABLE_CONSOLIDADO_CARGA+" ", null);
    }*/
    public List<ConsolidadoCarga> BuscarConsolidadoCarga() {
        List<ConsolidadoCarga> list = new ArrayList<ConsolidadoCarga>();
        String selectQuery = "select DISTINCT "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" from " + variables_publicas.TABLE_CONSOLIDADO_CARGA;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new ConsolidadoCarga(
                        cursor.getString(cursor.getColumnIndex("IdConsolidado"))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
    public java.util.ArrayList<String> BuscarConsolidadoCargaFacturas() {
        java.util.ArrayList<String> list = new java.util.ArrayList<String>();
        String selectQuery = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                list.add(new String(
//                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado)),
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura))
//                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente)),
//                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor)),
//                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
    public ArrayList<HashMap<String, String>> ObtenerCcarga(String Factura) {

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_CONSOLIDADO_CARGA +" where "+ variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura +" = '"+ Factura +"'" ;

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> Ccarga = new HashMap<>();
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion)));
                lst.add(Ccarga);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public  void EliminaConsolidadoCarga() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONSOLIDADO_CARGA+";");
        Log.d("ConsoliCarga_elimina", "Datos eliminados");
    }



    }
