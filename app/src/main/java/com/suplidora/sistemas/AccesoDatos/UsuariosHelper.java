package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class UsuariosHelper {

    private SQLiteDatabase database;

    public  UsuariosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarUsuario(String Codigo, String nombre, String Usuario,
                                   String Contrasenia, String Tipo, String Ruta,
                                      String Canal,String TasaCambio) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_nombre, nombre);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Usuario, Usuario);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Contrasenia, Contrasenia);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Tipo, Tipo);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Ruta, Ruta);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_Canal, Canal);
         contentValues.put(variables_publicas.USUARIOS_COLUMN_TasaCambio, TasaCambio);
        database.insert(variables_publicas.TABLE_USUARIOS, null, contentValues);
    }
    public Cursor BuscarUsuarios(String Usuario,String Contrasenia) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_USUARIOS +" where "+variables_publicas.USUARIOS_COLUMN_Usuario+" = '"+Usuario+"' and "+variables_publicas.USUARIOS_COLUMN_Contrasenia+" = '"+Contrasenia+"'", null);
    }
    public Cursor BuscarUsuariosCount() {
         return database.rawQuery("select * from " + variables_publicas.TABLE_USUARIOS + "", null);
    }
    public  void EliminaUsuarios() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_USUARIOS+";");
        Log.d("Usuario_elimina", "Datos eliminados");
    }
    }
