package com.safi_d.sistemas.safiapp.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Configuraciones;

public class ConfiguracionSistemaHelper {

    private SQLiteDatabase database;

    public ConfiguracionSistemaHelper(SQLiteDatabase db){
        database = db;
    }
    public boolean GuardarConfiguracionSistema(String Id ,
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

        long Guarda = database.insert(variables_publicas.TABLE_CONFIGURACION_SISTEMA, null, contentValues);
        if (Guarda !=-1)
            return  true;
        else
            return false;
    }
    public Configuraciones BuscarValorConfig(String Configuracion) {
        Configuraciones configuraciones = null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_CONFIGURACION_SISTEMA
                + " WHERE " + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Configuracion+"= '"+Configuracion+"' LIMIT 1";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                configuraciones = (new Configuraciones(c.getString(c.getColumnIndex(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Id)),
                        c.getString(c.getColumnIndex(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Sistema)),
                        c.getString(c.getColumnIndex(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Configuracion)),
                        c.getString(c.getColumnIndex(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Valor)),
                        c.getString(c.getColumnIndex(variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Activo))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return configuraciones;
    }
    public  void EliminaConfigSistema() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CONFIGURACION_SISTEMA+";");
        Log.d("ConfigSistemal_elimina", "Datos eliminados");
    }
    }
