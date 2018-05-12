package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Informe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import clojure.asm.commons.LocalVariablesSorter;

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
                                 String Imei,
                                 String Usuario) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.INFORMES_COLUMN_CodInforme, CodInforme);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Fecha, Fecha);
        contentValues.put(variables_publicas.INFORMES_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Aprobada, Aprobada);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Anulada, Anulada);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Imei, Imei);
        contentValues.put(variables_publicas.INFORMES_COLUMN_Usuario, Usuario);
        long rowInserted = database.insert(variables_publicas.TABLE_INFORMES, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean ActualizarInforme(String vcodInforme, String NoInforme) {
        ContentValues con = new ContentValues();
        con.put("CodInforme", NoInforme);
        long rowInserted = database.update(variables_publicas.TABLE_INFORMES, con, variables_publicas.INFORMES_COLUMN_CodInforme + "= '" + vcodInforme+"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean AnularInforme(String NoInforme) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.INFORMES_COLUMN_Anulada, "true");
        con.put(variables_publicas.INFORMES_COLUMN_Aprobada, "false");
        long rowInserted = database.update(variables_publicas.TABLE_INFORMES, con, variables_publicas.INFORMES_COLUMN_CodInforme + "= " + NoInforme+"", null);
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
                informe.put(variables_publicas.INFORMES_COLUMN_Imei, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Imei)));
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

    public int  BuscarMinimoRecibo(String informe) {
        String Query = "SELECT MIN("+ variables_publicas.DETALLEINFORMES_COLUMN_Recibo +") minimo FROM "+ variables_publicas.TABLE_DETALLE_INFORMES +" WHERE "+ variables_publicas.DETALLEINFORMES_COLUMN_CodInforme +"= "+ informe +"";
        int recibominimo=0;
        Cursor c = database.rawQuery(Query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                recibominimo=c.getInt(c.getColumnIndex("minimo"));
            } while (c.moveToNext());
        }
        c.close();
        return recibominimo;
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
                informe.put(variables_publicas.INFORMES_COLUMN_Imei, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Imei)));
                informe.put(variables_publicas.INFORMES_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Usuario)));
            } while (c.moveToNext());
        }
        c.close();
        return informe;
    }

    public ArrayList<HashMap<String, String>> ObtenerInformeDet(String CodigoInforme) {
        ArrayList<HashMap<String,String>> lst= new ArrayList<>();
        String selectQuery="SELECT DISTINCT "+ variables_publicas.DETALLEINFORMES_COLUMN_Recibo +", "+ variables_publicas.DETALLEINFORMES_COLUMN_IdCliente +", "+ variables_publicas.DETALLEINFORMES_COLUMN_Cliente +", printf(\"%.2f\",SUM(" + variables_publicas.DETALLEINFORMES_COLUMN_Abono + ")) as Monto, group_concat("+ variables_publicas.DETALLEINFORMES_COLUMN_Factura +") Facturas  " +
                " from " + variables_publicas.TABLE_DETALLE_INFORMES  + " Where " + variables_publicas.DETALLEINFORMES_COLUMN_CodInforme + " = " + CodigoInforme +
                " GROUP BY "+ variables_publicas.DETALLEINFORMES_COLUMN_Recibo +","+ variables_publicas.DETALLEINFORMES_COLUMN_IdCliente +","+ variables_publicas.DETALLEINFORMES_COLUMN_Cliente +";";
        Cursor c= database.rawQuery(selectQuery , null);

        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detinforme = new HashMap<>();
                detinforme.put("Recibo", c.getString(c.getColumnIndex("Recibo")));
                detinforme.put("Id", c.getString(c.getColumnIndex("IdCliente")));
                detinforme.put("Cliente", c.getString(c.getColumnIndex("Cliente")));
                detinforme.put("Monto", c.getString(c.getColumnIndex("Monto")));
                detinforme.put("Facturas", c.getString(c.getColumnIndex("Facturas")));
                lst.add(detinforme);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean EliminarBancos() {
        long deletedrows=  database.delete( variables_publicas.TABLE_BANCOS,null,null);
        Log.d("bancos_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }

    public boolean EliminarSeries() {
        long deletedrows=  database.delete( variables_publicas.TABLE_SERIE_RECIBOS,null,null);
        Log.d("series_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }

    public boolean GuardarSeries(String vid, String vvendedor, String vinicio, String vfin, String vnumero){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.SERIERECIBOS_COLUMN_IdSerie, vid);
        contentValues.put(variables_publicas.SERIERECIBOS_COLUMN_CodVendedor, vvendedor);
        contentValues.put(variables_publicas.SERIERECIBOS_COLUMN_nInicial, vinicio);
        contentValues.put(variables_publicas.SERIERECIBOS_COLUMN_nFinal, vfin);
        contentValues.put(variables_publicas.SERIERECIBOS_COLUMN_Numero, vnumero);
        long rowInserted = database.insert(variables_publicas.TABLE_SERIE_RECIBOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
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
    public ArrayList<HashMap<String, String>> ObtenerInformesLocales(String Fecha, String informe) {

        String selectQuery = "SELECT D.codigo Informe,D.vend Vendedor,D.fech Fecha,group_concat(D.recib) as Recibos,printf(\"%.2f\",SUM(D.total)) Monto,D.Est Status FROM "+
                            "(SELECT  I."+ variables_publicas.INFORMES_COLUMN_CodInforme +" codigo,DI."+ variables_publicas.DETALLEINFORMES_COLUMN_Vendedor +" vend,DATE(I."+ variables_publicas.INFORMES_COLUMN_Fecha +") as fech,DI."+ variables_publicas.DETALLEINFORMES_COLUMN_Recibo +" recib,printf(\"%.2f\",SUM(DI." + variables_publicas.DETALLEINFORMES_COLUMN_Abono + ")) total,'NO ENVIADO' as Est " +
                            "FROM "+ variables_publicas.TABLE_INFORMES +" I INNER JOIN "+ variables_publicas.TABLE_DETALLE_INFORMES +" DI ON I."+ variables_publicas.INFORMES_COLUMN_CodInforme +"=DI."+ variables_publicas.DETALLEINFORMES_COLUMN_CodInforme +" " +
                            "WHERE LENGTH(I."+ variables_publicas.INFORMES_COLUMN_CodInforme +")>=13 and DATE(I." + variables_publicas.PEDIDOS_COLUMN_Fecha + ") = DATE('" + Fecha + "') AND I." + variables_publicas.INFORMES_COLUMN_CodInforme + " LIKE '%" + informe + "%' " +
                            "GROUP BY I." + variables_publicas.INFORMES_COLUMN_CodInforme + ",DI."+ variables_publicas.DETALLEINFORMES_COLUMN_Vendedor +",DATE(I."+ variables_publicas.INFORMES_COLUMN_Fecha +"),DI."+ variables_publicas.DETALLEINFORMES_COLUMN_Recibo +") D "+
                            "GROUP BY  D.codigo,D.vend,D.fech,D.Est;";

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> informes = new HashMap<>();
                informes.put("Informe", c.getString(c.getColumnIndex("Informe")));
                informes.put("Vendedor", c.getString(c.getColumnIndex("Vendedor")));
                informes.put("Fecha", c.getString(c.getColumnIndex("Fecha")));
                informes.put("Recibos", c.getString(c.getColumnIndex("Recibos")));
                informes.put("Monto", c.getString(c.getColumnIndex("Monto")));
                informes.put("Estado", c.getString(c.getColumnIndex("Status")));
                lst.add(informes);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public Informe BuscarInformesSinconizar( ) {
        Informe informes = null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_INFORMES+"";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                informes = (new Informe(c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_CodInforme)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Fecha)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_IdVendedor)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Imei)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Aprobada)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Anulada)),
                        c.getString(c.getColumnIndex(variables_publicas.INFORMES_COLUMN_Usuario))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return informes;
    }
}