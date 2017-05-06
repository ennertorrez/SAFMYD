package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.List;

public class FormaPagoHelper {

//    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public FormaPagoHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarTotalFormaPago(String CODIGO ,
                                     String NOMBRE ,
                                     String DIAS ,
                                     String EMPRESA
                                      )
    {
        long rows =0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.FORMA_PAGO_COLUMN_CODIGO, CODIGO);
        contentValues.put(variables_publicas.FORMA_PAGO_COLUMN_NOMBRE, NOMBRE);
        contentValues.put(variables_publicas.FORMA_PAGO_COLUMN_DIAS, DIAS);
        contentValues.put(variables_publicas.FORMA_PAGO_COLUMN_EMPRESA, EMPRESA);

        database.insert(variables_publicas.TABLE_FORMA_PAGO, null, contentValues);
    }
//    public Cursor ObtenerListaVendedores() {
//        return database.rawQuery("select * from " + variables_publicas.TABLE_VENDEDORES, null);
//    }
    public  void EliminaFormaPago() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_FORMA_PAGO+";");
        Log.d("formaPago_elimina", "Datos eliminados");
    }
    public List<String> ObtenerListaFormaPago(){
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_FORMA_PAGO;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        database.close();

        return list;
    }

}