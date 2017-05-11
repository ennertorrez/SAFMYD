package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class ArticulosHelper {

    private SQLiteDatabase database;

    public ArticulosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre, String PrecioSuper,
                                   String PrecioDetalle, String PrecioForaneo, String PrecioMayorista,
                                      String Bonificable,String AplicaPrecioDetalle,String DESCUENTO_MAXIMO,String detallista,
                                      String COSTO,String UNIDAD,String UnidadCaja,String ISC,String PorIVA) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_COSTO , COSTO);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UNIDAD , UNIDAD);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja , UnidadCaja);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_ISC , ISC);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_PorIVA , PorIVA);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, PrecioSuper);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, PrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, PrecioForaneo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, PrecioMayorista);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Bonificable, Bonificable);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, AplicaPrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_DESCUENTO_MAXIMO, DESCUENTO_MAXIMO);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_detallista, detallista);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        database.insert(variables_publicas.TABLE_ARTICULOS, null, contentValues);
    }
    public Cursor BuscarArticulo(String Codigo) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS +" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Codigo+"' LIMIT 1", null);
    }
    public Cursor BuscarTotalArticulo() {
        return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_ARTICULOS +"", null);
    }
    public  void EliminaArticulos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ARTICULOS+";");
        Log.d("cursos_elimina", "Datos eliminados");
    }
}