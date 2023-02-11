package com.saf.sistemas.safcafenorteno.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Entidades.Ruta;
import com.saf.sistemas.safcafenorteno.Entidades.Supervisor;
import com.saf.sistemas.safcafenorteno.Entidades.Vendedor;

import java.util.ArrayList;
import java.util.List;

public class VendedoresHelper {

    //    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public VendedoresHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarTotalVendedores(String CODIGO,
                                       String NOMBRE,
                                       String IDRUTA,
                                       String RUTA,
                                       String EMPRESA,
                                       String codsuper,
                                       String Supervisor,
                                       String Status) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.VENDEDORES_COLUMN_CODIGO, CODIGO);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_NOMBRE, NOMBRE);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_IDRUTA, IDRUTA);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_RUTA, RUTA);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_EMPRESA, EMPRESA);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_codsuper, codsuper);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_Supervisor, Supervisor);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_Status, Status);

        long rowInserted=database.insert(variables_publicas.TABLE_VENDEDORES, null, contentValues);
    }

    //    public Cursor ObtenerListaVendedores() {
//        return database.rawQuery("select * from " + variables_publicas.TABLE_VENDEDORES, null);
//    }
    public void EliminaVendedores() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_VENDEDORES + ";");
        Log.d("vendedores_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public List<Vendedor> ObtenerListaVendedores() {
        List<Vendedor> list = new ArrayList<Vendedor>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_NOMBRE ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            //list.add(new Vendedor("999","TODOS","1","RT-Todos","1","1","TODOS","1"));
            do {
                list.add(new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("EMPRESA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status"))
));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

        @SuppressLint("Range")
        public List<Ruta> ObtenerListaRutas(String CodigoSupervisor) {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_IDRUTA+","+variables_publicas.VENDEDORES_COLUMN_RUTA+" FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+ variables_publicas.VENDEDORES_COLUMN_codsuper + "= '" + CodigoSupervisor + "' ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            list.add(new Ruta("99","TODOS"));
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                        ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    @SuppressLint("Range")
    public List<Ruta> ObtenerRutavendedor(String CodigoVendedor) {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_IDRUTA+","+variables_publicas.VENDEDORES_COLUMN_RUTA+" FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+ variables_publicas.VENDEDORES_COLUMN_CODIGO + "= '" + CodigoVendedor + "' ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    @SuppressLint("Range")
    public List<Ruta> ObtenerTodasRutas() {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_IDRUTA+", "+variables_publicas.VENDEDORES_COLUMN_RUTA +" FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            list.add(new Ruta("99","TODOS"));
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
    @SuppressLint("Range")
    public List<Supervisor> ObtenerTodosSupervisores() {
        List<Supervisor> list = new ArrayList<Supervisor>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_codsuper+" , "+ variables_publicas.VENDEDORES_COLUMN_Supervisor +" FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_Supervisor ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
           //list.add(new Supervisor("TODOS"," "));
            do {
                list.add(new Supervisor(
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor"))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    @SuppressLint("Range")
    public Vendedor ObtenerVendedor(String Codigo) {
        Vendedor vendedor= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+variables_publicas.VENDEDORES_COLUMN_CODIGO+ " = "+Codigo;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                vendedor=new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("EMPRESA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status")
                        ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return vendedor;
    }
    @SuppressLint("Range")
    public List<Vendedor> ObtenerVendedorxSup(String vCodSupervisor) {
        List<Vendedor> list = new ArrayList<Vendedor>();
//        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+ variables_publicas.VENDEDORES_COLUMN_codsuper +"= "+ vCodSupervisor +" ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_NOMBRE ;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_NOMBRE ;
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            list.add(new Vendedor("0","TODOS","0","RT-Todos","1","1","TODOS","1"));
            do {
                list.add(new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("IDRUTA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("EMPRESA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status")
                        )));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}