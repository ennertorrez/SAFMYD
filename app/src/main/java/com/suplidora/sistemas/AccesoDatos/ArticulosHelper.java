package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Articulo;
import com.suplidora.sistemas.Entidades.FormaPago;

public class ArticulosHelper {

    private SQLiteDatabase database;

    public ArticulosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre,
                                      String COSTO,String UNIDAD,String UnidadCaja,String ISC,String PorIVA,String PrecioSuper,
                                   String PrecioDetalle, String PrecioForaneo, String PrecioMayorista,
                                      String Bonificable,String AplicaPrecioDetalle,String DESCUENTO_MAXIMO,String detallista
                                      ) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Costo , COSTO);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Unidad , UNIDAD);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja , UnidadCaja);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Isc , ISC);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_PorIva , PorIVA);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, PrecioSuper);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, PrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, PrecioForaneo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, PrecioMayorista);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Bonificable, Bonificable);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, AplicaPrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, DESCUENTO_MAXIMO);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Detallista, detallista);

        database.insert(variables_publicas.TABLE_ARTICULOS, null, contentValues);
    }


    public Articulo BuscarArticulo(String Codigo) {
        String selectQuery = "select * from " + variables_publicas.TABLE_ARTICULOS + " where " + variables_publicas.ARTICULO_COLUMN_Codigo + " like '%" + Codigo + "' LIMIT 1";
        Cursor c = database.rawQuery(selectQuery, null);
        Articulo articulo=null;
        if (c.moveToFirst()) {
            do {
                articulo = (new Articulo(c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Codigo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Costo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Unidad)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCaja)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Isc)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PorIva)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioSuper)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioDetalle)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioForaneo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioMayorista)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Bonificable)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Detallista))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }
    public Cursor BuscarTotalArticulo() {
        return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_ARTICULOS +"", null);
    }
    public  void EliminaArticulos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ARTICULOS+";");
        Log.d("cursos_elimina", "Datos eliminados");
    }
}