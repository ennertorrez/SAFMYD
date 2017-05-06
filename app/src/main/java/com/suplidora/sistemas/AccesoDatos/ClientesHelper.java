package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Cliente;

public class ClientesHelper {

//    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public  ClientesHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarTotalClientes( String IdCliente ,
                                      String CodCv ,
                                      String Nombre ,
                                      String FechaCreacion ,
                                      String Telefono ,
                                      String Direccion ,
                                      String IdDepartamento ,
                                      String IdMunicipio ,
                                      String Ciudad ,
                                      String Ruc ,
                                      String Cedula ,
                                      String LimiteCredito ,
                                      String IdFormaPago ,
                                      String IdVendedor ,
                                      String Excento ,
                                      String CodigoLetra ,
                                      String Ruta ,
                                      String Frecuencia ,
                                      String PrecioEspecial ,
                                      String FechaUltimaCompra) {
        long rows =0;

        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdCliente ,IdCliente );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodCv ,CodCv );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Nombre , Nombre );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion ,FechaCreacion );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Telefono ,Telefono );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Direccion ,Direccion );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento ,IdDepartamento );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio ,IdMunicipio );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ciudad ,Ciudad );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ruc ,Ruc );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cedula ,Cedula );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito ,LimiteCredito );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago ,IdFormaPago );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdVendedor ,IdVendedor );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Excento ,Excento );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra ,CodigoLetra );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ruta ,Ruta );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Frecuencia ,Frecuencia );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial ,PrecioEspecial );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,FechaUltimaCompra);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        database.insert(variables_publicas.TABLE_CLIENTES, null, contentValues);
    }
    public Cursor ObtenerListaClientesCodigo(String Busqueda) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES, null);
    }
    public Cursor ObtenerListaClientesNombre(String Busqueda) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES, null);
    }
    public Cursor BuscarClientesCount() {
        return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_CLIENTES + "", null);
    }
    public  void EliminaClientes() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+";");
        Log.d("clientes_elimina", "Datos eliminados");
    }

    public Cliente BuscarCliente(String Codigo){
        Cliente cli= new Cliente();
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES + " Where IdCliente ='"+Codigo +"'", null);
        if(c.moveToFirst()){
            do {
                cli = new Cliente(c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_CodCv)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_FechaCreacion)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Telefono)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Direccion)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdDepartamento)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdMunicipio)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Ciudad)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Ruc)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Cedula)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_LimiteCredito)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdFormaPago)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdVendedor)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Excento)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_CodigoLetra)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Ruta)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Frecuencia)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_PrecioEspecial)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra))
                );
            }while (c.moveToNext());
        }

        return cli;
    }

}