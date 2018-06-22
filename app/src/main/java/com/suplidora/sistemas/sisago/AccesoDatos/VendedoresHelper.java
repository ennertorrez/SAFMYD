package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Ruta;
import com.suplidora.sistemas.sisago.Entidades.Supervisor;
import com.suplidora.sistemas.sisago.Entidades.Usuario;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;

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
                                       String DEPARTAMENTO,
                                       String MUNICIPIO,
                                       String CIUDAD,
                                       String TELEFONO,
                                       String CELULAR,
                                       String CORREO,
                                       String COD_ZONA,
                                       String RUTA,
                                       String codsuper,
                                       String Supervisor,
                                       String Status,
                                       String detalle,
                                       String horeca,
                                       String mayorista,
                                       String Super) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.VENDEDORES_COLUMN_CODIGO, CODIGO);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_NOMBRE, NOMBRE);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_COD_ZONA, COD_ZONA);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_RUTA, RUTA);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_codsuper, codsuper);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_Supervisor, Supervisor);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_Status, Status);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_detalle, detalle);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_horeca, horeca);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_mayorista, mayorista);
        contentValues.put(variables_publicas.VENDEDORES_COLUMN_Super, Super);

        long rowInserted=database.insert(variables_publicas.TABLE_VENDEDORES, null, contentValues);
    }

    //    public Cursor ObtenerListaVendedores() {
//        return database.rawQuery("select * from " + variables_publicas.TABLE_VENDEDORES, null);
//    }
    public void EliminaVendedores() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_VENDEDORES + ";");
        Log.d("vendedores_elimina", "Datos eliminados");
    }

    public List<Vendedor> ObtenerListaVendedores() {
        List<Vendedor> list = new ArrayList<Vendedor>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_NOMBRE ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                list.add(new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("COD_ZONA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status")),
                        cursor.getString(cursor.getColumnIndex("detalle")),
                        cursor.getString(cursor.getColumnIndex("horeca")),
                        cursor.getString(cursor.getColumnIndex("mayorista")
                        )));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    public List<Vendedor> ObtenerListaVendedores2() {
        List<Vendedor> list = new ArrayList<Vendedor>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE " + variables_publicas.VENDEDORES_COLUMN_detalle + "= 'true' ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_NOMBRE ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                list.add(new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("COD_ZONA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status")),
                        cursor.getString(cursor.getColumnIndex("detalle")),
                        cursor.getString(cursor.getColumnIndex("horeca")),
                        cursor.getString(cursor.getColumnIndex("mayorista")
                        )));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    public List<Ruta> ObtenerListaRutas(String CodigoSupervisor) {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_RUTA+" FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+ variables_publicas.VENDEDORES_COLUMN_codsuper + "= '" + CodigoSupervisor + "' ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            list.add(new Ruta("TODOS"));
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                        ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    public List<Ruta> ObtenerRutavendedor(String CodigoVendedor) {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_RUTA+" FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+ variables_publicas.VENDEDORES_COLUMN_CODIGO + "= '" + CodigoVendedor + "' ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
    public List<Ruta> ObtenerTodasRutas() {
        List<Ruta> list = new ArrayList<Ruta>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_RUTA+" FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_RUTA ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            list.add(new Ruta("TODOS"));
            do {
                list.add(new Ruta(
                        cursor.getString(cursor.getColumnIndex("RUTA"))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }
    public List<Supervisor> ObtenerTodosSupervisores() {
        List<Supervisor> list = new ArrayList<Supervisor>();

        String selectQuery = "SELECT DISTINCT "+variables_publicas.VENDEDORES_COLUMN_Supervisor+" , "+ variables_publicas.VENDEDORES_COLUMN_codsuper +" FROM " + variables_publicas.TABLE_VENDEDORES+ " ORDER BY "+ variables_publicas.VENDEDORES_COLUMN_Supervisor ;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
           //list.add(new Supervisor("TODOS"," "));
            do {
                list.add(new Supervisor(
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("codsuper"))
                ));

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        // database.close();

        return list;
    }

    public Vendedor ObtenerVendedor(String Codigo) {
        Vendedor vendedor= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_VENDEDORES+ " WHERE "+variables_publicas.VENDEDORES_COLUMN_CODIGO+ " = "+Codigo;
        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                vendedor=new Vendedor(cursor.getString(cursor.getColumnIndex("CODIGO")),
                        cursor.getString(cursor.getColumnIndex("NOMBRE")),
                        cursor.getString(cursor.getColumnIndex("COD_ZONA")),
                        cursor.getString(cursor.getColumnIndex("RUTA")),
                        cursor.getString(cursor.getColumnIndex("codsuper")),
                        cursor.getString(cursor.getColumnIndex("Supervisor")),
                        cursor.getString(cursor.getColumnIndex("Status")),
                        cursor.getString(cursor.getColumnIndex("detalle")),
                        cursor.getString(cursor.getColumnIndex("horeca")),
                        cursor.getString(cursor.getColumnIndex("mayorista")
                        ));

            } while (cursor.moveToNext());
        }
        cursor.close();
        return vendedor;
    }

}