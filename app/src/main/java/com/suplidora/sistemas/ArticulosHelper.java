package com.suplidora.sistemas;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ArticulosHelper {


    //public static final String ARTICULO_COLUMN_ID = "Id";
    public static final String ARTICULO_COLUMN_CODIGO = "Codigo";
    public static final String ARTICULO_COLUMN_NAME = "Nombre";
    public static final String  ARTICULO_COLUMN_PS= "PrecioSuper";
    public static final String ARTICULO_COLUMN_PD = "PrecioDetalle";
    public static final String ARTICULO_COLUMN_PF = "PrecioForaneo";
    public static final String ARTICULO_COLUMN_PM = "PrecioMayorista";
    ArticulosOpenHelper openHelper;
    private SQLiteDatabase database;

    public ArticulosHelper(Context context){
        openHelper = new ArticulosOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre, String PrecioSuper,
                                   String PrecioDetalle, String PrecioForaneo, String PrecioMayorista) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ARTICULO_COLUMN_CODIGO, Codigo);
        contentValues.put(ARTICULO_COLUMN_NAME, Nombre);
        contentValues.put(ARTICULO_COLUMN_PS, PrecioSuper);
        contentValues.put(ARTICULO_COLUMN_PD, PrecioDetalle);
        contentValues.put(ARTICULO_COLUMN_PF, PrecioForaneo);
        contentValues.put(ARTICULO_COLUMN_PM, PrecioMayorista);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        database.insert(variables_publicas.TABLE_ARTICULOS, null, contentValues);
    }
    public Cursor getTimeRecordList(String Codigo) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS +" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Codigo+"'", null);
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
                    + ARTICULO_COLUMN_CODIGO + " TEXT , "
                    + ARTICULO_COLUMN_NAME + " TEXT, "
                    + ARTICULO_COLUMN_PS + " TEXT, "
                    + ARTICULO_COLUMN_PD + " TEXT, "
                    + ARTICULO_COLUMN_PF + " TEXT, "
                    + ARTICULO_COLUMN_PM + " TEXT )" );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_ARTICULOS);
            onCreate(db);
        }

    }
}