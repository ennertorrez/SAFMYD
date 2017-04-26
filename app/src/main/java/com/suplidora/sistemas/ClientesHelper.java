package com.suplidora.sistemas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ClientesHelper {

   
    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public ClientesHelper(Context context){
        openHelper = new ClientesOpenHelper(context);
        database = openHelper.getWritableDatabase();
    }
    public void GuardarTotalClientes(String IdCliente ,
                                      String CodCv ,
                                      String Cliente ,
                                      String Nombre ,
                                      String FechaIngreso ,
                                      String ClienteNuevo ,
                                      String Ruta ,
                                      String Direccion ,
                                      String Cedula ,
                                      String IdVendedor ,
                                      String Vendedor ,
                                      String IdSupervisor ,
                                      String Supervisor ,
                                      String Subruta ,
                                      String FechaUltimaCompra) {
        long rows =0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodCv, CodCv);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cliente, Cliente);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaIngreso, FechaIngreso);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_ClienteNuevo, ClienteNuevo);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ruta, Ruta);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Direccion, Direccion);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Direccion, Cedula);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Vendedor, Vendedor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, IdSupervisor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Supervisor, Supervisor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Subruta, Subruta);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,FechaUltimaCompra);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        database.insert(variables_publicas.TABLE_CLIENTES, null, contentValues);
    }
    public Cursor ObtenerListaClientes() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES, null);
    }
    public  void EliminaClientes() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+";");
        Log.d("clientes_elimina", "Datos eliminados");
    }
    private class ClientesOpenHelper extends SQLiteOpenHelper {
        public ClientesOpenHelper(Context context) {
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
                    + variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra + " TEXT ) " );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_CLIENTES);
            onCreate(db);
        }

    }
}