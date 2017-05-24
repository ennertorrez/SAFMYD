package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;

import java.util.ArrayList;
import java.util.List;

public class FormaPagoHelper {

    //    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public FormaPagoHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarTotalFormaPago(String CODIGO,
                                      String NOMBRE,
                                      String DIAS,
                                      String EMPRESA
    ) {
        long rows = 0;
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
    public void EliminaFormaPago() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_FORMA_PAGO + ";");
        Log.d("formaPago_elimina", "Datos eliminados");
    }

    public List<FormaPago> ObtenerListaFormaPago() {
        List<FormaPago> list = new ArrayList<FormaPago>();
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_FORMA_PAGO;
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                list.add(new FormaPago(c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_CODIGO)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_NOMBRE)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_DIAS)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_EMPRESA))
                ));
            } while (c.moveToNext());
        }
        // closing connection
        c.close();
//        database.close();
        return list;
    }

    public FormaPago ObtenerFormaPago(String IdFormaPago) {
        FormaPago formaPago= new FormaPago();
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_FORMA_PAGO+ " where "+ variables_publicas.FORMA_PAGO_COLUMN_CODIGO+" = "+IdFormaPago + " ORDER BY "+ variables_publicas.FORMA_PAGO_COLUMN_DIAS;
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                formaPago=(new FormaPago(c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_CODIGO)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_NOMBRE)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_DIAS)),
                        c.getString(c.getColumnIndex(variables_publicas.FORMA_PAGO_COLUMN_EMPRESA))
                ));
            } while (c.moveToNext());
        }
        // closing connection
        c.close();
//        database.close();
        return formaPago;
    }

}