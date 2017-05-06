package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class ConfiguracionSistemaHelper {

    private SQLiteDatabase database;

    public ConfiguracionSistemaHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarConfiguracionSistema(String Id ,
                                      String Sistema ,
                                      String Configuracion ,
                                      String Valor ,
                                      String Activo ) {

        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Id, Id);
         contentValues.put(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Sistema, Sistema);
         contentValues.put(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Configuracion, Configuracion);
         contentValues.put(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Valor, Valor);
         contentValues.put(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Activo, Activo);

        database.insert(variables_publicas.TABLE_CONFIGURACION_SISTEMA, null, contentValues);
    }
    public Cursor BuscarPrecioEspecial() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CONFIGURACION_SISTEMA +" ", null);
    }
    public  void EliminaPrecioEspecial() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONFIGURACION_SISTEMA+";");
        Log.d("PrecioEspecial_elimina", "Datos eliminados");
    }
    }
