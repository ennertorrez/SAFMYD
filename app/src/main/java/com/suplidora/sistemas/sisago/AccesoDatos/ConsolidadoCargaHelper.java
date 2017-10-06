package com.suplidora.sistemas.sisago.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCarga;

import java.util.ArrayList;
import java.util.List;

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
    public List<ConsolidadoCarga> BuscarConsolidadoCargaFacturas() {
        List<ConsolidadoCarga> list = new ArrayList<ConsolidadoCarga>();
        String selectQuery = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                list.add(new ConsolidadoCarga(
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado)),
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura)),
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente)),
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor)),
                        cursor.getString(cursor.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
    public  void EliminaConsolidadoCarga() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONSOLIDADO_CARGA+";");
        Log.d("ConsoliCarga_elimina", "Datos eliminados");
    }



    }
