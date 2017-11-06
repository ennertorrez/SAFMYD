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
                               String Direccion,String IdCliente,String IdVendedor,String Guardada ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado, IdConsolidado);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura, Factura);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente, Cliente);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor, Vendedor);
         contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion, Direccion);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdCliente,IdCliente);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdVendedor,IdVendedor);
        contentValues.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada,Guardada);

        database.insert(variables_publicas.TABLE_CONSOLIDADO_CARGA, null, contentValues);
    }
   /* public Cursor BuscarConsolidadoCarga() {
        return database.rawQuery("select DISTINCT "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" from " + variables_publicas.TABLE_CONSOLIDADO_CARGA+" ", null);
    }*/
    public List<ConsolidadoCarga> BuscarConsolidadoCarga() {
        List<ConsolidadoCarga> list = new ArrayList<ConsolidadoCarga>();
        String selectQuery = "select DISTINCT "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" from " + variables_publicas.TABLE_CONSOLIDADO_CARGA+" where "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada+" = 'false'";
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
    public java.util.ArrayList<String> BuscarConsolidadoCargaFacturas(String IdConsolidado) {
        java.util.ArrayList<String> list = new java.util.ArrayList<String>();
        String selectQuery = "select * from " + variables_publicas.TABLE_CONSOLIDADO_CARGA+" where "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" = '"+IdConsolidado+"'"+ " AND "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada+ " = 'false'";
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
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdCliente)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdVendedor)));
                Ccarga.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada, c.getString(c.getColumnIndex(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada)));
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

    public boolean ActualizarConsolidadoCarga(String rango,String factura ) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Guardada, "true");
        long rowsUpdated = database.update(variables_publicas.TABLE_CONSOLIDADO_CARGA, con, variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura + "= '" + factura+"'"+" AND "+variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado+" = '"+rango+"'", null);
        if (rowsUpdated != -1)
            return true;
        else return false;
    }



    }
