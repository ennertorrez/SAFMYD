package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class CartillasBcHelper {

    private SQLiteDatabase database;

    public CartillasBcHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarCartillasBc(String codigo ,
                               String fechaini ,
                               String fechafinal ,
                               String tipo ,
                               String aprobado ) {

        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.CARTILLAS_BC_COLUMN_codigo, codigo);
         contentValues.put(variables_publicas.CARTILLAS_BC_COLUMN_fechaini, fechaini);
         contentValues.put(variables_publicas.CARTILLAS_BC_COLUMN_fechafinal, fechafinal);
         contentValues.put(variables_publicas.CARTILLAS_BC_COLUMN_tipo, tipo);
         contentValues.put(variables_publicas.CARTILLAS_BC_COLUMN_aprobado, aprobado);

        database.insert(variables_publicas.TABLE_CARTILLAS_BC, null, contentValues);
    }
    public Cursor BuscarCartillasBc(String Usuario,String Contrasenia) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CARTILLAS_BC +" ", null);
    }
    public  void EliminaCartillasBc() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CARTILLAS_BC+";");
        Log.d("CartillasBc_elimina", "Datos eliminados");
    }
    }
