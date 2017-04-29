package com.suplidora.sistemas.AccesoDatos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class ArticulosHelper {


    ArticulosOpenHelper openHelper;
    private SQLiteDatabase database;

    public ArticulosHelper(Context context){
        openHelper = new ArticulosOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre, String PrecioSuper,
                                   String PrecioDetalle, String PrecioForaneo, String PrecioMayorista,
                                      String Bonificable,String AplicaPrecioDetalle,String DESCUENTO_MAXIMO,String detallista) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
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
        return database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS +" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Codigo+"'", null);
    }
    public Cursor BuscarTotalArticulo() {
        return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_ARTICULOS +"", null);
    }
    public  void EliminaArticulos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ARTICULOS+";");
        Log.d("cursos_elimina", "Datos eliminados");
    }
    private class ArticulosOpenHelper extends SQLiteOpenHelper {
        public ArticulosOpenHelper(Context context) {
            // TODO Auto-generated constructor stub
            super(context, variables_publicas.DATABASE_NAME, null, variables_publicas.DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_ARTICULOS + "( "
                   // + ARTICULO_COLUMN_ID + " INTEGER PRIMARY KEY , "
                    + variables_publicas.ARTICULO_COLUMN_Codigo + " TEXT , "
                    + variables_publicas.ARTICULO_COLUMN_Nombre + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioSuper + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioDetalle + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioForaneo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioMayorista + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Bonificable + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_DESCUENTO_MAXIMO + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_detallista + " TEXT )" );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
           db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_ARTICULOS);
            onCreate(db);
        }

    }
}