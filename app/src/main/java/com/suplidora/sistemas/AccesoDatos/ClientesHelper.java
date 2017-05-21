package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Cliente;
import com.suplidora.sistemas.Entidades.Vendedor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientesHelper {

//    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public  ClientesHelper(SQLiteDatabase db){
        database = db;
    }

    public boolean GuardarTotalClientes( String IdCliente ,
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
                                      String FechaUltimaCompra,String Tipo,String CodigoGalatea,String Descuento,String Empleado,String Detallista) {
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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Tipo,Tipo);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, CodigoGalatea);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Descuento, Descuento);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empleado, Empleado);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Detallista, Detallista);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
       long inserted= database.insert(variables_publicas.TABLE_CLIENTES, null, contentValues);
        if(inserted!=-1)
            return true;
        else
            return false;
    }


    public void GuardarTotalClientes( HashMap<String,String> cliente) {
        long rows =0;

        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdCliente ,cliente.get(variables_publicas.CLIENTES_COLUMN_IdCliente) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodCv ,cliente.get(variables_publicas.CLIENTES_COLUMN_CodCv) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Nombre , cliente.get(variables_publicas.CLIENTES_COLUMN_Nombre)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion ,cliente.get(variables_publicas.CLIENTES_COLUMN_FechaCreacion) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Telefono ,cliente.get(variables_publicas.CLIENTES_COLUMN_Telefono)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Direccion ,cliente.get(variables_publicas.CLIENTES_COLUMN_Direccion) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento,cliente.get(variables_publicas.CLIENTES_COLUMN_IdDepartamento)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio ,cliente.get(variables_publicas.CLIENTES_COLUMN_IdMunicipio)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ciudad ,cliente.get(variables_publicas.CLIENTES_COLUMN_Ciudad)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ruc ,cliente.get(variables_publicas.CLIENTES_COLUMN_Ruc)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cedula ,cliente.get(variables_publicas.CLIENTES_COLUMN_Cedula)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito ,cliente.get(variables_publicas.CLIENTES_COLUMN_LimiteCredito)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago ,cliente.get(variables_publicas.CLIENTES_COLUMN_IdFormaPago)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdVendedor ,cliente.get(variables_publicas.CLIENTES_COLUMN_IdVendedor)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Excento ,cliente.get(variables_publicas.CLIENTES_COLUMN_Excento) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra ,cliente.get(variables_publicas.CLIENTES_COLUMN_CodigoLetra)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Ruta ,cliente.get(variables_publicas.CLIENTES_COLUMN_Ruta)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Frecuencia ,cliente.get(variables_publicas.CLIENTES_COLUMN_Frecuencia) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial ,cliente.get(variables_publicas.CLIENTES_COLUMN_PrecioEspecial)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,cliente.get(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Tipo,cliente.get(variables_publicas.CLIENTES_COLUMN_Tipo) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, cliente.get(variables_publicas.CLIENTES_COLUMN_CodigoGalatea) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Descuento, cliente.get(variables_publicas.CLIENTES_COLUMN_Descuento) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empleado, cliente.get(variables_publicas.CLIENTES_COLUMN_Empleado) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Detallista, cliente.get(variables_publicas.CLIENTES_COLUMN_Detallista) );

        database.insert(variables_publicas.TABLE_CLIENTES, null, contentValues);
    }
    public Cursor ObtenerListaClientesCodigo(String Busqueda) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES, null);
    }
    public Cursor ObtenerListaClientesNombre(String Busqueda) {
        return database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES, null);
    }

    public ArrayList<HashMap<String, String>>  BuscarClientesNombre(String Busqueda) {
        Busqueda= Busqueda.replace(" ","%");
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_Nombre+" like '%"+Busqueda+"%'", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodCv, c.getString(c.getColumnIndex("CodCv")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion, c.getString(c.getColumnIndex("FechaCreacion")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString(c.getColumnIndex("Telefono")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString(c.getColumnIndex("Direccion")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento, c.getString(c.getColumnIndex("IdDepartamento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio, c.getString(c.getColumnIndex("IdMunicipio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString(c.getColumnIndex("Ciudad")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString(c.getColumnIndex("Ruc")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString(c.getColumnIndex("Cedula")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito, c.getString(c.getColumnIndex("LimiteCredito")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago, c.getString(c.getColumnIndex("IdFormaPago")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString(c.getColumnIndex("IdVendedor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Excento, c.getString(c.getColumnIndex("Excento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString(c.getColumnIndex("CodigoLetra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString(c.getColumnIndex("Ruta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, c.getString(c.getColumnIndex("CodigoGalatea")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Detallista, c.getString(c.getColumnIndex("Detallista")));
                        lst.add(cliente);

            }while (c.moveToNext());
        }
        return  lst;
    }

    public ArrayList<HashMap<String, String>>  BuscarClientesCodigo(String Busqueda) {
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = '"+Busqueda+"'", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodCv, c.getString(c.getColumnIndex("CodCv")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion, c.getString(c.getColumnIndex("FechaCreacion")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString(c.getColumnIndex("Telefono")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString(c.getColumnIndex("Direccion")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento, c.getString(c.getColumnIndex("IdDepartamento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio, c.getString(c.getColumnIndex("IdMunicipio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString(c.getColumnIndex("Ciudad")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString(c.getColumnIndex("Ruc")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString(c.getColumnIndex("Cedula")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito, c.getString(c.getColumnIndex("LimiteCredito")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago, c.getString(c.getColumnIndex("IdFormaPago")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString(c.getColumnIndex("IdVendedor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Excento, c.getString(c.getColumnIndex("Excento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString(c.getColumnIndex("CodigoLetra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString(c.getColumnIndex("Ruta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, c.getString(c.getColumnIndex("CodigoGalatea")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Detallista, c.getString(c.getColumnIndex("Detallista")));
                lst.add(cliente);

            }while (c.moveToNext());
        }
        return  lst;
    }

    public Cursor BuscarClientesCount() {
        return database.rawQuery("select COUNT(*) from " + variables_publicas.TABLE_CLIENTES + "", null);
    }
    public  void EliminaClientes() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+";");
        Log.d("clientes_elimina", "Datos eliminados");
    }

    public  void EliminaCliente(String IdCliente) {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = '"+IdCliente+"'");
        Log.d("clientes_elimina", "Datos eliminados");
    }

    public Cliente BuscarCliente(String Codigo){
        Cliente cli= new Cliente();
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_CLIENTES + " Where IdCliente = "+Codigo +"", null);
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
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_CodigoGalatea)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Empleado)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Detallista))
                );
            }while (c.moveToNext());
        }

        return cli;
    }

}