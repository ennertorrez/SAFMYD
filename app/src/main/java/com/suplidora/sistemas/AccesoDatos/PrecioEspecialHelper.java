package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.PrecioEspecial;

public class PrecioEspecialHelper {

    private SQLiteDatabase database;

    public PrecioEspecialHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarPrecioEspecial(String Id ,
                                      String CodigoArticulo ,
                                      String IdCliente ,
                                      String Descuento ,
                                      String Precio,String Facturar ) {

        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Id, Id);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo, CodigoArticulo);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente, IdCliente);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento, Descuento);
         contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio, Precio);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Facturar, Facturar);

        database.insert(variables_publicas.TABLE_PRECIO_ESPECIAL, null, contentValues);
    }
    public PrecioEspecial BuscarPrecioEspecial(String IdCliente,String CodigoArticulo) {
        PrecioEspecial precioEspecial= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_PRECIO_ESPECIAL+ " where "+ variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente+" = "+IdCliente
                + " AND "+variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo +" = '"+CodigoArticulo+"'";
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                precioEspecial=(new PrecioEspecial(c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Id)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_COLUMN_Facturar))
                ));
            } while (c.moveToNext());
        }
        c.close();

        return precioEspecial;
    }
    public  void EliminaPrecioEspecial() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PRECIO_ESPECIAL+";");
        Log.d("PrecioEspecial_elimina", "Datos eliminados");
    }
    }
