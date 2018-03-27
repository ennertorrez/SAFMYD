package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Pedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InformesHelper {


    private SQLiteDatabase database;

    public InformesHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarInforme(String CodInforme,
                                  String Fecha,
                                  String IdVendedor,
                                 String Aprobada,
                                 String Anulada,
                                 String FechaCreacion,
                                 String Usuario) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.INFORMES_COLUMN_CodInforme, CodInforme);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Fecha, Fecha);
        contentValues.put(variables_publicas.INFORMES_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Aprobada, Aprobada);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Anulada, Anulada);
        contentValues.put(variables_publicas.INFORMES_COLUMN_FechaCreacion, FechaCreacion);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Usuario, Usuario);
        long rowInserted = database.insert(variables_publicas.TABLE_INFORMES, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean ActualizarInforme(String CodInforme) {
        ContentValues con = new ContentValues();
        con.put("CodInforme", CodInforme);
        long rowInserted = database.update(variables_publicas.TABLE_INFORMES, con, variables_publicas.INFORMES_COLUMN_CodInforme + "= '" + CodInforme+"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerListaInformes() {
        HashMap<String, String> informe = null;
        List<HashMap<String, String>> lst = new ArrayList<>();
        String Query = "SELECT * FROM " + variables_publicas.TABLE_INFORMES + ";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                informe = new HashMap<>();
                informe.put(variables_publicas.INFORMES_COLUMN_CodInforme, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_CodInforme)));
                informe.put(variables_publicas.INFORMES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Fecha)));
                informe.put(variables_publicas.INFORMES_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_IdVendedor)));
                informe.put(variables_publicas.INFORMES_COLUMN_Aprobada, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Aprobada)));
                informe.put(variables_publicas.INFORMES_COLUMN_Anulada, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Anulada)));
                informe.put(variables_publicas.INFORMES_COLUMN_FechaCreacion, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_FechaCreacion)));
                informe.put(variables_publicas.INFORMES_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Usuario)));
                lst.add(informe);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean EliminaInforme(String codInforme) {
        long rowInserted = database.delete(variables_publicas.TABLE_INFORMES, variables_publicas.INFORMES_COLUMN_CodInforme + "= '" + codInforme +"'", null);
        if (rowInserted != -1)
                return true;
            else return false;
    }

    public int ObtenerNuevoCodigoInforme() {

        String selectQuery = "SELECT IFNULL(MAX("+ variables_publicas.INFORMES_COLUMN_CodInforme +"),0) as Cantidad FROM " + variables_publicas.TABLE_INFORMES;
        Cursor c = database.rawQuery(selectQuery, null);
        int numero = 0;
        if (c.moveToFirst()) {
            do {
                numero = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        return numero + 1;
    }

    public HashMap<String, String> ObtenerInforme(String CodigoInforme) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_INFORMES  + " Where " + variables_publicas.INFORMES_COLUMN_CodInforme + " = ?", new String[]{CodigoInforme});
        HashMap<String, String> informe = null;
        if (c.moveToFirst()) {
            do {
                informe = new HashMap<>();
                informe.put(variables_publicas.INFORMES_COLUMN_CodInforme, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_CodInforme)));
                informe.put(variables_publicas.INFORMES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Fecha)));
                informe.put(variables_publicas.INFORMES_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_IdVendedor)));
                informe.put(variables_publicas.INFORMES_COLUMN_Aprobada, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Aprobada)));
                informe.put(variables_publicas.INFORMES_COLUMN_Anulada, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Anulada)));
                informe.put(variables_publicas.INFORMES_COLUMN_FechaCreacion, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_FechaCreacion)));
                informe.put(variables_publicas.INFORMES_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Usuario)));
            } while (c.moveToNext());
        }
        c.close();
        return informe;
    }

    public boolean EliminarBancos() {
        long deletedrows=  database.delete( variables_publicas.TABLE_BANCOS,null,null);
        Log.d("bancos_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }
    public boolean GuardarBancos(String cod, String banco){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.BANCOS_COLUMN_codigo, cod);
        contentValues.put(variables_publicas.BANCOS_COLUMN_nombre, banco);
        long rowInserted = database.insert(variables_publicas.TABLE_BANCOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
}