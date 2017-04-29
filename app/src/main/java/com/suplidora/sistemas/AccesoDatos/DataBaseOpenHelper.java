package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class DataBaseOpenHelper {


    OpenHelper openHelper;
    public SQLiteDatabase database;

    public DataBaseOpenHelper(Context context){
        openHelper = new OpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    private class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            // TODO Auto-generated constructor stub
            super(context, variables_publicas.DATABASE_NAME, null, variables_publicas.DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CLIENTES + "( "
                    + variables_publicas.CLIENTES_COLUMN_IdCliente + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_CodCv + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Cliente + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Nombre + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_FechaIngreso + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_ClienteNuevo + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Ruta + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Direccion + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Cedula + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdVendedor + " INTEGER , "
                    + variables_publicas.CLIENTES_COLUMN_Vendedor + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdSupervisor + " INTEGER , "
                    + variables_publicas.CLIENTES_COLUMN_Supervisor + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Subruta + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Frecuencia + " TEXT ) " );

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_USUARIOS + "( "
                    + variables_publicas.USUARIOS_COLUMN_Codigo + " TEXT , "
                    + variables_publicas.USUARIOS_COLUMN_nombre + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Usuario + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Contrasenia + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Tipo + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Ruta + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Canal + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_TasaCambio + " TEXT )");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_CLIENTES);
            db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_USUARIOS);
            onCreate(db);
        }
    }
}