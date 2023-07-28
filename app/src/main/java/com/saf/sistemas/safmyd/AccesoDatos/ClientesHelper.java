package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kenmeidearu.searchablespinnerlibrary.mListString;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Cliente;
import com.saf.sistemas.safmyd.Entidades.Departamentos;
import com.saf.sistemas.safmyd.Entidades.Municipios;
import com.saf.sistemas.safmyd.Entidades.MotivosNoVenta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClientesHelper {

    private SQLiteDatabase database;

    public  ClientesHelper(SQLiteDatabase db){
        database = db;
    }

    public boolean GuardarClientes( String IdCliente ,
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
                                      String NombreRuta ,
                                      String Frecuencia ,
                                      String PrecioEspecial ,
                                      String FechaUltimaCompra,
                                      String Tipo,
                                      String TipoPrecio ,
                                      String Descuento,
                                      String Empleado,
                                      String IdSupervisor,
                                      String Empresa,
                                      String Cod_Zona,
                                      String Cod_SubZona,
                                      String Pais_Id,
                                      String Pais_Nombre,
                                      String IdTipoNegocio,
                                      String tipoNegocio,
                                      String saldo,
                                      String noenruta,
                                      String latitud,
                                      String longitud,
                                      String visita) {
        long rows =0;

        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdCliente ,IdCliente );
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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NombreRuta ,NombreRuta );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Frecuencia ,Frecuencia );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial ,PrecioEspecial );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,FechaUltimaCompra);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Tipo,Tipo);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio , TipoPrecio );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Descuento, Descuento);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empleado, Empleado);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, IdSupervisor);
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empresa, Empresa) ;
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, Cod_Zona );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, Cod_SubZona );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, Pais_Id );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, Pais_Nombre );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, IdTipoNegocio );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, tipoNegocio );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Saldo, saldo );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, noenruta );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Lat, latitud );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Lng, longitud );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Visita, visita );

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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NombreRuta,cliente.get(variables_publicas.CLIENTES_COLUMN_NombreRuta) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Frecuencia ,cliente.get(variables_publicas.CLIENTES_COLUMN_Frecuencia) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial ,cliente.get(variables_publicas.CLIENTES_COLUMN_PrecioEspecial)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra,cliente.get(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Tipo,cliente.get(variables_publicas.CLIENTES_COLUMN_Tipo) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, cliente.get(variables_publicas.CLIENTES_COLUMN_TipoPrecio) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Descuento, cliente.get(variables_publicas.CLIENTES_COLUMN_Descuento) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empleado, cliente.get(variables_publicas.CLIENTES_COLUMN_Empleado) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor , cliente.get(variables_publicas.CLIENTES_COLUMN_IdSupervisor)  );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Empresa, cliente.get(variables_publicas.CLIENTES_COLUMN_Empresa) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, cliente.get(variables_publicas.CLIENTES_COLUMN_Cod_Zona) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, cliente.get(variables_publicas.CLIENTES_COLUMN_Cod_SubZona));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, cliente.get(variables_publicas.CLIENTES_COLUMN_Pais_Id));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, cliente.get(variables_publicas.CLIENTES_COLUMN_Pais_Nombre));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, cliente.get(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, cliente.get(variables_publicas.CLIENTES_COLUMN_TipoNegocio));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Saldo, cliente.get(variables_publicas.CLIENTES_COLUMN_Saldo));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, cliente.get(variables_publicas.CLIENTES_COLUMN_NoEnRuta));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Lat, cliente.get(variables_publicas.CLIENTES_COLUMN_Lat));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Lng, cliente.get(variables_publicas.CLIENTES_COLUMN_Lng));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Visita, cliente.get(variables_publicas.CLIENTES_COLUMN_Visita));
        database.insert(variables_publicas.TABLE_CLIENTES, null, contentValues);
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>>  BuscarClientesNombre(String Busqueda) {
        Busqueda= Busqueda.replace(" ","%");
        Cursor c= database.rawQuery("SELECT * FROM "
                + variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_Nombre+" like '%"+Busqueda+"%' ORDER BY CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +" AS INT)  ", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex("NombreRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex("TipoPrecio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString(c.getColumnIndex("IdSupervisor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString(c.getColumnIndex("Empresa")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString(c.getColumnIndex("Cod_Zona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString(c.getColumnIndex("Cod_SubZona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString(c.getColumnIndex("Pais_Id")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString(c.getColumnIndex("Pais_Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString(c.getColumnIndex("IdTipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex("Saldo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, c.getString(c.getColumnIndex("NoEnRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex("Latitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex("Longitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Visita, c.getString(c.getColumnIndex("Visita")));
                lst.add(cliente);

            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>>  BuscarClientesCodigo(String Busqueda) {
        Cursor c= database.rawQuery("SELECT * FROM "
                + variables_publicas.TABLE_CLIENTES+" WHERE "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = CASE WHEN '' = '"+Busqueda+"' THEN "+variables_publicas.CLIENTES_COLUMN_IdCliente+" ELSE '"+Busqueda+"' END ORDER BY CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +" AS INT) ", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex("NombreRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex("TipoPrecio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString(c.getColumnIndex("IdSupervisor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString(c.getColumnIndex("Empresa")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString(c.getColumnIndex("Cod_Zona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString(c.getColumnIndex("Cod_SubZona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString(c.getColumnIndex("Pais_Id")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString(c.getColumnIndex("Pais_Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString(c.getColumnIndex("IdTipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex("Saldo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, c.getString(c.getColumnIndex("NoEnRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex("Latitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex("Longitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Visita, c.getString(c.getColumnIndex("Visita")));
                lst.add(cliente);

            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>>  BuscarClientesCodigoDia(String Busqueda,String dia) {
        Cursor c= database.rawQuery("SELECT * FROM "
                + variables_publicas.TABLE_CLIENTES+" WHERE "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = CASE WHEN '' = '"+Busqueda+"' THEN "+variables_publicas.CLIENTES_COLUMN_IdCliente+" ELSE '"+Busqueda+"' END AND "+ variables_publicas.CLIENTES_COLUMN_Frecuencia +"='"+ dia  +"' ORDER BY CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +" AS INT) ", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex("NombreRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex("TipoPrecio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString(c.getColumnIndex("IdSupervisor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString(c.getColumnIndex("Empresa")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString(c.getColumnIndex("Cod_Zona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString(c.getColumnIndex("Cod_SubZona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString(c.getColumnIndex("Pais_Id")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString(c.getColumnIndex("Pais_Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString(c.getColumnIndex("IdTipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex("Saldo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, c.getString(c.getColumnIndex("NoEnRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex("Latitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex("Longitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Visita, c.getString(c.getColumnIndex("Visita")));
                lst.add(cliente);

            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>>  BuscarClientesNombreDia(String Busqueda, String dia) {
        Busqueda= Busqueda.replace(" ","%");
        Cursor c= database.rawQuery("SELECT * FROM "
                + variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_Nombre+" like '%"+Busqueda+"%' AND "+ variables_publicas.CLIENTES_COLUMN_Frecuencia +"='"+ dia  +"' ORDER BY CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +" AS INT)  ", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex("NombreRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex("TipoPrecio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString(c.getColumnIndex("IdSupervisor")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString(c.getColumnIndex("Empresa")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString(c.getColumnIndex("Cod_Zona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString(c.getColumnIndex("Cod_SubZona")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString(c.getColumnIndex("Pais_Id")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString(c.getColumnIndex("Pais_Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString(c.getColumnIndex("IdTipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex("Saldo")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, c.getString(c.getColumnIndex("NoEnRuta")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex("Latitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex("Longitud")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Visita, c.getString(c.getColumnIndex("Visita")));
                lst.add(cliente);

            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }
    @SuppressLint("Range")
    public HashMap<String, String>  ObtenerClienteGuardado(String Busqueda) {
        String sql="SELECT * FROM "
                + variables_publicas.TABLE_CLIENTES+" WHERE "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = CASE WHEN '' = '"+Busqueda+"' THEN "+variables_publicas.CLIENTES_COLUMN_IdCliente+" ELSE '"+Busqueda+"' END ";
        Cursor c= database.rawQuery(sql, null);
       // ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;
        HashMap<String, String> vCliente = null;

        if(c.moveToFirst()){
            do{
               // HashMap<String,String> cliente= new HashMap<>();
                vCliente = new HashMap<>();
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion, c.getString(c.getColumnIndex("FechaCreacion")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString(c.getColumnIndex("Telefono")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString(c.getColumnIndex("Direccion")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento, c.getString(c.getColumnIndex("IdDepartamento")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio, c.getString(c.getColumnIndex("IdMunicipio")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString(c.getColumnIndex("Ciudad")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString(c.getColumnIndex("Ruc")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString(c.getColumnIndex("Cedula")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito, c.getString(c.getColumnIndex("LimiteCredito")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago, c.getString(c.getColumnIndex("IdFormaPago")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString(c.getColumnIndex("IdVendedor")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Excento, c.getString(c.getColumnIndex("Excento")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString(c.getColumnIndex("CodigoLetra")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString(c.getColumnIndex("Ruta")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex("NombreRuta")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex("Frecuencia")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString(c.getColumnIndex("PrecioEspecial")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString(c.getColumnIndex("FechaUltimaCompra")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex("Tipo")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex("TipoPrecio")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex("Descuento")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString(c.getColumnIndex("Empleado")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString(c.getColumnIndex("IdSupervisor")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString(c.getColumnIndex("Empresa")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString(c.getColumnIndex("Cod_Zona")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString(c.getColumnIndex("Cod_SubZona")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString(c.getColumnIndex("Pais_Id")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString(c.getColumnIndex("Pais_Nombre")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString(c.getColumnIndex("IdTipoNegocio")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex("Saldo")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_NoEnRuta, c.getString(c.getColumnIndex("NoEnRuta")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex("Latitud")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex("Longitud")));
                vCliente.put(variables_publicas.CLIENTES_COLUMN_Visita, c.getString(c.getColumnIndex("Visita")));

            }while (c.moveToNext());
        }
        c.close();
        return  vCliente;
    }

    public  void EliminaClientes() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+";");
        Log.d("clientes_elimina", "Datos eliminados");
    }

    public  void EliminaCliente(String IdCliente) {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = '"+IdCliente+"'");
        Log.d("clientes_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public Cliente BuscarCliente(String Codigo){
        Cliente cli= new Cliente();
        String sql="select * from " + variables_publicas.TABLE_CLIENTES + " Where IdCliente = "+Codigo +" ";
        Cursor c= database.rawQuery(sql, null);
        if(c.moveToFirst()){
            do {
                cli = new Cliente(c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdCliente)),
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
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_NombreRuta)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Frecuencia)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_PrecioEspecial)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_TipoPrecio)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Empleado)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdSupervisor)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Empresa)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Cod_Zona)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Cod_SubZona)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Pais_Id)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Pais_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_TipoNegocio)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Saldo)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_NoEnRuta)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Lat)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Lng)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Visita))
                );
            }while (c.moveToNext());
        }
        c.close();
        return cli;
    }

    @SuppressLint("Range")
    public List<Departamentos> ObtenerListaDepartamentos() {
        List<Departamentos> list = new ArrayList<Departamentos>();
        String Query = "SELECT DISTINCT " + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento + " ," + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + " FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Departamentos(
                        c.getString(c.getColumnIndex("Codigo_Departamento")),
                        c.getString(c.getColumnIndex("Nombre_Departamento"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    @SuppressLint("Range")
    public List<Municipios> ObtenerListaMunicipios(String DesDepto) {
        List<Municipios> list = new ArrayList<Municipios>();
        String Query = "SELECT DISTINCT " + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio + " ," + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio + " FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " WHERE "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + "= '"+ DesDepto + "' ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio+";";
        //String Query = "SELECT DISTINCT * FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " WHERE "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + "= '"+ DesDepto + "' ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Municipios(
                        c.getString(c.getColumnIndex("Codigo_Municipio")),
                        c.getString(c.getColumnIndex("Nombre_Municipio"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }


    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> BuscarCedulaClientes(String vCedula) {
        String Query = "SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_IdCliente + ", " + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_Cedula + "= '"+ vCedula + "' LIMIT 1;";
        Cursor c = database.rawQuery(Query, null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtCliente= new HashMap<>();
                dtCliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                dtCliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                lst.add(dtCliente);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public String ObtenerDescripcion(String campoResultado, String tabla, String campoFiltro, String valorFiltro) {

        String Query = "SELECT DISTINCT " + campoResultado + " FROM " + tabla + " WHERE "+ campoFiltro + "= "+ valorFiltro  +";";
        //String Query = "SELECT count(*) FROM " + variables_publicas.TABLE_CLIENTES + " ;";
        Cursor c = database.rawQuery(Query, null);
        String resultado = "";
        if (c.moveToFirst()) {
            do {
                resultado = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }

    public boolean GuardarDptosMuniBarrios(String codDpto, String DesDepto,String codMun, String DesMun){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento, codDpto);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento, DesDepto);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio, codMun);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio, DesMun);
        long rowInserted = database.insert(variables_publicas.TABLE_DPTOMUNIBARRIOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    public boolean EliminarDptosMuniBarrios() {
        long deletedrows=  database.delete( variables_publicas.TABLE_DPTOMUNIBARRIOS,null,null);
        Log.d("DPTOMUNIBARRIOS_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }

    public String ObtenerCodLetra(String valorFiltro) {

        String sql="SELECT " + variables_publicas.CLIENTES_COLUMN_CodigoLetra + " AS codletra FROM "
                + variables_publicas.TABLE_CLIENTES+" WHERE "+variables_publicas.CLIENTES_COLUMN_IdCliente+" = CASE WHEN '' = '"+valorFiltro+"' THEN "+variables_publicas.CLIENTES_COLUMN_IdCliente+" ELSE '"+valorFiltro+"' END ";
        Cursor c= database.rawQuery(sql, null);

        String resultado = "";
        if (c.moveToFirst()) {
            do {
                resultado = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }
    @SuppressLint("Range")
    public HashMap<String, String> ObtenerDatosClientesParcial(String Codigo) {

        String sql="select IdCliente,CodCv,Nombre,NombreCliente,Telefono,Direccion,Cedula,IdVendedor,Ruta,Frecuencia from " + variables_publicas.TABLE_CLIENTES + " Where IdCliente = "+Codigo +" ";
        Cursor c = database.rawQuery(sql,null);
        HashMap<String, String> cliente = null;
        if (c.moveToFirst()) {
            do {
                cliente = new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdCliente)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Nombre)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Telefono)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Direccion)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Cedula)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdVendedor)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_TipoPrecio)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_NombreRuta)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Frecuencia)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lat, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Lat)));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Lng, c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Lng)));
            } while (c.moveToNext());
        }
        c.close();
        return cliente;
    }

    @SuppressLint("Range")
    public ArrayList<mListString> ObtenerListaClientes(String Ruta) {
        final ArrayList<mListString> listStringsCustomer = new ArrayList<>();
        String Query = "SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_IdCliente + " ," + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_Ruta + "= "+ Ruta + " ORDER BY cast("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta+" as int);";
        Cursor c = database.rawQuery(Query, null);
        int contador=0;
        if (c.moveToFirst()) {
            do {
                contador=contador+1;
                listStringsCustomer.add(new mListString(c.getInt(c.getColumnIndex("IdCliente")) , c.getString( c.getColumnIndex("Nombre"))));
            } while (c.moveToNext());
        }
        c.close();
        return listStringsCustomer;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerCodigoCteAnterior(int NoEnRuta) {
        ArrayList<HashMap<String, String>> cliente= new ArrayList<HashMap<String, String>> () ;
        String Query = "SELECT  " + variables_publicas.CLIENTES_COLUMN_IdCliente + " ," + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta + " AS integer) < "+ NoEnRuta + " ORDER BY cast("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta+" as Integer) DESC LIMIT 1;";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtCte= new HashMap<>();
                dtCte.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                dtCte.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.add(dtCte);
            } while (c.moveToNext());
        }
        c.close();
        return cliente;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerCodigoCteActual(int NoEnRuta) {
        ArrayList<HashMap<String, String>> cliente= new ArrayList<HashMap<String, String>> () ;
        String Query = "SELECT  " + variables_publicas.CLIENTES_COLUMN_IdCliente + " ," + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta + " AS integer) = "+ NoEnRuta + " ;";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtCte= new HashMap<>();
                dtCte.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                dtCte.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.add(dtCte);
            } while (c.moveToNext());
        }
        c.close();
        return cliente;
    }
    @SuppressLint("Range")
    public boolean ValidarSiEsPrimerCliente(int NoEnRuta) {
        ArrayList<HashMap<String, String>> cliente= new ArrayList<HashMap<String, String>> () ;
        String Query = "SELECT  " + variables_publicas.CLIENTES_COLUMN_NoEnRuta+ "  FROM " + variables_publicas.TABLE_CLIENTES + " ORDER BY cast("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta+" as Integer) ASC LIMIT 1;";
        Cursor c = database.rawQuery(Query, null);
        int noenrutaprimero=0;
        boolean resultado=true;
        if (c.moveToFirst()) {
            do {
                noenrutaprimero=Integer.parseInt(c.getString(c.getColumnIndex("NoEnRuta")));
                if (NoEnRuta<=noenrutaprimero){
                    resultado= true;
                }else{
                    resultado= false;
                }
            } while (c.moveToNext());
        }else{
            resultado  =false;
        }
        c.close();
        return resultado;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerCodigoCteUltimo(int Ruta) {
        ArrayList<HashMap<String, String>> cliente= new ArrayList<HashMap<String, String>> () ;
        String Query = "SELECT  " + variables_publicas.CLIENTES_COLUMN_IdCliente + " ," + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE CAST("+ variables_publicas.CLIENTES_COLUMN_Ruta + " AS integer) = "+ Ruta + " ORDER BY cast("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta+" as Integer) DESC LIMIT 1;";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtCte= new HashMap<>();
                dtCte.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                dtCte.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.add(dtCte);
            } while (c.moveToNext());
        }
        c.close();
        return cliente;
    }
    @SuppressLint("Range")
    public String ObtenerNoEnRutaCteAnterior( String IdCliente) {
        String Query = "SELECT  " + variables_publicas.CLIENTES_COLUMN_NoEnRuta + "  FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdCliente + " = "+ IdCliente + " LIMIT 1";
        Cursor c = database.rawQuery(Query, null);
        String valor="0";
        if (c.moveToFirst()) {
            do {
                valor=c.getString(c.getColumnIndex("NoEnRuta"));
            } while (c.moveToNext());
        }
        c.close();
        return valor;
    }
    @SuppressLint("Range")
    public boolean ActualizarNoEnRutaClientes(String Cliente,String NoEnRuta) {
        String Query = "SELECT "+ variables_publicas.CLIENTES_COLUMN_IdCliente +" FROM " + variables_publicas.TABLE_CLIENTES + " WHERE CAST("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta + " AS INT)  >= CAST("+ NoEnRuta + " AS INT) AND CAST("+ variables_publicas.CLIENTES_COLUMN_IdCliente +" AS INT)<> CAST("+ Cliente +" AS INT) ORDER BY cast("+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +" as int); ";
        Cursor c = database.rawQuery(Query, null);
        int valor=0;
        int vcliente=0;
        valor=Integer.parseInt(NoEnRuta)+1;
        try{
            if (c.moveToFirst()) {
                do {
                    vcliente=Integer.parseInt(c.getString(c.getColumnIndex("IdCliente")));
                    database.execSQL("UPDATE "+ variables_publicas.TABLE_CLIENTES +" SET "+ variables_publicas.CLIENTES_COLUMN_NoEnRuta +"="+ valor +"  WHERE "+ variables_publicas.CLIENTES_COLUMN_IdCliente +" = "+ vcliente +";");
                    valor+=1;
                } while (c.moveToNext());
            }
            c.close();
            return true;
        }    catch (Exception ex) {
            Log.e("Error:", ex.getMessage());
            return false;
        }

    }

    public boolean ActualizarCoordenadas(String Cliente, String Lat,String Lng) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.CLIENTES_COLUMN_Lat, Lat);
        con.put(variables_publicas.CLIENTES_COLUMN_Lng, Lng);

        long rowsUpdated = database.update(variables_publicas.TABLE_CLIENTES, con, variables_publicas.CLIENTES_COLUMN_IdCliente + "= " + Cliente + "", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }

    public boolean ActualizarVisita(String Cliente, String Visita) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.CLIENTES_COLUMN_Visita, Visita);

        long rowsUpdated = database.update(variables_publicas.TABLE_CLIENTES, con, variables_publicas.CLIENTES_COLUMN_IdCliente + "= " + Cliente + "", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }

    public boolean EliminarMotivosNoVenta() {
        long deletedrows=  database.delete( variables_publicas.TABLE_MOTIVOS_NOVENTA,null,null);
        Log.d("motivos_noventa_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }
    public boolean GuardarMotivosNoVenta(String cod, String motivo){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.MOTIVOS_NOVENTA_COLUMN_codigo, cod);
        contentValues.put(variables_publicas.MOTIVOS_NOVENTA_COLUMN_motivo, motivo);
        long rowInserted = database.insert(variables_publicas.TABLE_MOTIVOS_NOVENTA, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    @SuppressLint("Range")
    public List<MotivosNoVenta> ObtenerListaMotivosNoVenta() {
        List<MotivosNoVenta> list = new ArrayList<MotivosNoVenta>();
        String Query = "SELECT DISTINCT " + variables_publicas.MOTIVOS_NOVENTA_COLUMN_codigo + " ," + variables_publicas.MOTIVOS_NOVENTA_COLUMN_motivo + " FROM " + variables_publicas.TABLE_MOTIVOS_NOVENTA + " ORDER BY "+ variables_publicas.MOTIVOS_NOVENTA_COLUMN_codigo+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new MotivosNoVenta(
                        c.getString(c.getColumnIndex("Codigo")),
                        c.getString(c.getColumnIndex("Motivo"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

}