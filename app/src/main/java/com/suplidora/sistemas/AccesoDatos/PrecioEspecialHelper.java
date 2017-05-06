package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class PrecioEspecialHelper {

    private SQLiteDatabase database;

    public PrecioEspecialHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarPrecioEspecial(String Id ,
                                      String CodigoArticulo ,
                                      String IdCliente ,
                                      String Descuento ,
                                      String Precio ) {

        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Id, Id);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo, CodigoArticulo);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente, IdCliente);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento, Descuento);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio, Precio);

        database.insert(variables_publicas.TABLE_PRECIO_ESPECIAL, null, contentValues);
    }
    public Cursor BuscarPrecioEspecial() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_PRECIO_ESPECIAL +" ", null);
    }
    public  void EliminaPrecioEspecial() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PRECIO_ESPECIAL+";");
        Log.d("PrecioEspecial_elimina", "Datos eliminados");
    }
    }
