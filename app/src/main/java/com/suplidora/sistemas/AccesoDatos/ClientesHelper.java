package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

public class ClientesHelper {

//    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public  ClientesHelper(SQLiteDatabase db){
        database = db;
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
                                      String FechaUltimaCompra,
                                     String Frecuencia) {
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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cedula, Cedula);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Vendedor, Vendedor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, IdSupervisor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Supervisor, Supervisor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Subruta, Subruta);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,FechaUltimaCompra);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Frecuencia,Frecuencia);
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

}