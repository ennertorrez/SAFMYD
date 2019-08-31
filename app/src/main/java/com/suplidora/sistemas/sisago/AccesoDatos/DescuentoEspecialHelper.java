package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.DescuentoEspecial;

public class DescuentoEspecialHelper {
    private SQLiteDatabase database;

    public DescuentoEspecialHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarDescuentoEspecial(String IdCliente ,
                                      String CodigoArticulo ,
                                      String Porcentaje ,
                                      String Canal ) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_IdCliente, IdCliente);
        contentValues.put(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_CodigoArticulo, CodigoArticulo);
        contentValues.put(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Porcentaje, Porcentaje);
        contentValues.put(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Canal, Canal);

        database.insert(variables_publicas.TABLE_DESCUENTO_ESPECIAL, null, contentValues);
    }

    public DescuentoEspecial BuscarDescuentoEspecial(String IdCliente, String CodigoArticulo, String Canal) {
        DescuentoEspecial descuentoEspecial= null;
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_DESCUENTO_ESPECIAL+ " where "+ variables_publicas.DESCUENTO_ESPECIAL_COLUMN_IdCliente+" = "+IdCliente
                + " AND "+variables_publicas.DESCUENTO_ESPECIAL_COLUMN_CodigoArticulo +" = '"+CodigoArticulo+"' AND "+variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Canal + "= '"+Canal+"'";
        Cursor c = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                descuentoEspecial=(new DescuentoEspecial(c.getString(c.getColumnIndex(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_CodigoArticulo)),
                        c.getString(c.getColumnIndex(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Porcentaje)),
                        c.getString(c.getColumnIndex(variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Canal))
                ));
            } while (c.moveToNext());
        }
        c.close();

        return descuentoEspecial;
    }
    public  void EliminaDescuentoEspecial() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_DESCUENTO_ESPECIAL+";");
        Log.d("DescEspecial_elimina", "Datos eliminados");
    }

    public boolean ValidaCoDistribuidor(String IdCliente, String Canal){
        Cursor c = database.rawQuery("SELECT IFNULL(COUNT(*),0) Cantidad  FROM " + variables_publicas.TABLE_DESCUENTO_ESPECIAL + " WHERE " + variables_publicas.DESCUENTO_ESPECIAL_COLUMN_IdCliente + " = " + IdCliente + " AND " + variables_publicas.DESCUENTO_ESPECIAL_COLUMN_Canal + " = '" + Canal + "' ", null);
        int cant =0;
        if (c.moveToFirst()) {
            do {
                cant = c.getInt(c.getColumnIndex("Cantidad"));
            } while (c.moveToNext());
        }
        c.close();
        if (cant==0){
            return false;
        }else {
            return true;
        }
    }
}
