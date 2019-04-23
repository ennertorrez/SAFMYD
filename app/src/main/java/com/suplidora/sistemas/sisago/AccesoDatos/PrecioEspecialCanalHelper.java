package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.PrecioEspecialCanal;

public class PrecioEspecialCanalHelper {
    private SQLiteDatabase database;

    public PrecioEspecialCanalHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarPrecioEspecialCanal(String Id ,
                                      String CodigoArticulo ,
                                      String Canal ,
                                      String Precio ) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Id, Id);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_CodigoArticulo, CodigoArticulo);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Canal, Canal);
        contentValues.put(variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio, Precio);

        database.insert(variables_publicas.TABLE_PRECIO_ESPECIAL_CANAL, null, contentValues);
    }
    public PrecioEspecialCanal BuscarPrecioEspecialCanal(String Canal, String CodigoArticulo) {
        PrecioEspecialCanal precioEspecialcanal= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_PRECIO_ESPECIAL_CANAL + " where "+ variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Canal+" = '"+Canal
                + "' AND "+variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_CodigoArticulo +" = '"+CodigoArticulo+"'";
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                precioEspecialcanal=(new PrecioEspecialCanal(c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Id)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_CodigoArticulo)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Canal)),
                        c.getString(c.getColumnIndex(variables_publicas.PRECIO_ESPECIAL_CANAL_COLUMN_Precio))
                ));
            } while (c.moveToNext());
        }
        c.close();

        return precioEspecialcanal;
    }
    public  void EliminaPrecioEspecialCanal() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PRECIO_ESPECIAL_CANAL+";");
        Log.d("PrecioEspCanal_elimina", "Datos eliminados");
    }
}
