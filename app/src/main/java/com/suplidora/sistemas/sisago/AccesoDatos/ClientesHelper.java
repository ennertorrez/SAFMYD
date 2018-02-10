package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.DptpMuniBarrio;

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
                                         String NombreCliente ,
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
                                      String FechaUltimaCompra,String Tipo,String CodigoGalatea,String Descuento,String Empleado,String Detallista, String RutaForanea,String EsClienteVarios, String idBarrio, String tipoNegocio) {
        long rows =0;

        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdCliente ,IdCliente );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_CodCv ,CodCv );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Nombre , Nombre );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NombreCliente , NombreCliente );
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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_Detallista, Detallista) ;
        contentValues.put(variables_publicas.CLIENTES_COLUMN_RutaForanea, RutaForanea );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, EsClienteVarios );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdBarrio, idBarrio );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, tipoNegocio );

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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_NombreCliente , cliente.get(variables_publicas.CLIENTES_COLUMN_NombreCliente)  );
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
        contentValues.put(variables_publicas.CLIENTES_COLUMN_RutaForanea, cliente.get(variables_publicas.CLIENTES_COLUMN_RutaForanea) );
        contentValues.put(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, cliente.get(variables_publicas.CLIENTES_COLUMN_EsClienteVarios));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_IdBarrio, cliente.get(variables_publicas.CLIENTES_COLUMN_IdBarrio));
        contentValues.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, cliente.get(variables_publicas.CLIENTES_COLUMN_TipoNegocio));
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
        Cursor c= database.rawQuery("SELECT *,CASE WHEN CodCv='' THEN CodCv ELSE ('Cod_Cv: ' || CodCv) END AS CodCv2, CASE WHEN Nombre = NombreCliente THEN Nombre ELSE  (Nombre || ' / ' || ifnull(NombreCliente,'') ) END AS NombreCompleto FROM "
                + variables_publicas.TABLE_CLIENTES+" where "+variables_publicas.CLIENTES_COLUMN_NombreCliente+" like '%"+Busqueda+"%' AND NOT("+variables_publicas.CLIENTES_COLUMN_Nombre+"  LIKE 'CLIENTES VARIOS%' AND CodCv= '' )", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodCv, c.getString(c.getColumnIndex("CodCv")));
                cliente.put("CodCv2", c.getString(c.getColumnIndex("CodCv2")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreCliente, c.getString(c.getColumnIndex("NombreCliente")));
                cliente.put("NombreCompleto", c.getString(c.getColumnIndex("NombreCompleto")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_RutaForanea, c.getString(c.getColumnIndex("RutaForanea")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, c.getString(c.getColumnIndex("EsClienteVarios")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdBarrio, c.getString(c.getColumnIndex("IdBarrio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
                        lst.add(cliente);

            }while (c.moveToNext());
        }
        return  lst;
    }

    public ArrayList<HashMap<String, String>>  BuscarClientesCodigo(String Busqueda) {
        Cursor c= database.rawQuery("SELECT *,CASE WHEN CodCv='' THEN CodCv ELSE ('Cod_Cv: ' || CodCv) END AS CodCv2, CASE WHEN Nombre = NombreCliente THEN Nombre ELSE  (Nombre || ' / ' || ifnull(NombreCliente,'') ) END AS NombreCompleto FROM "
                + variables_publicas.TABLE_CLIENTES+" WHERE (("+variables_publicas.CLIENTES_COLUMN_IdCliente+" = CASE WHEN '' = '"+Busqueda+"' THEN "+variables_publicas.CLIENTES_COLUMN_IdCliente+" ELSE '"+Busqueda+"' END) "+
                "OR (CodCv = CASE WHEN '"+Busqueda+"' = '' THEN CodCv ELSE '"+ Busqueda +"' END ) )"+
                " AND NOT("+variables_publicas.CLIENTES_COLUMN_Nombre+"  LIKE 'CLIENTES VARIOS%' AND CodCv= '' )", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> cliente= new HashMap<>();
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString(c.getColumnIndex("IdCliente")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_CodCv, c.getString(c.getColumnIndex("CodCv")));
                cliente.put("CodCv2", c.getString(c.getColumnIndex("CodCv2")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_NombreCliente, c.getString(c.getColumnIndex("NombreCliente")));
                cliente.put("NombreCompleto", c.getString(c.getColumnIndex("NombreCompleto")));
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
                cliente.put(variables_publicas.CLIENTES_COLUMN_RutaForanea, c.getString(c.getColumnIndex("RutaForanea")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, c.getString(c.getColumnIndex("EsClienteVarios")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_IdBarrio, c.getString(c.getColumnIndex("IdBarrio")));
                cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString(c.getColumnIndex("TipoNegocio")));
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

    public Cliente BuscarCliente(String Codigo,String CodCv){
        Cliente cli= new Cliente();
        String sql="select * from " + variables_publicas.TABLE_CLIENTES + " Where IdCliente = "+Codigo +" AND CodCv = '"+CodCv.replace("Cod_Cv: ","")+"' ";
        Cursor c= database.rawQuery(sql, null);
        if(c.moveToFirst()){
            do {
                cli = new Cliente(c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_CodCv)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_NombreCliente)),
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
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Detallista)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_RutaForanea)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_EsClienteVarios)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_IdBarrio)),
                        c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_TipoNegocio))
                );
            }while (c.moveToNext());
        }

        return cli;
    }

    public List<DptpMuniBarrio> ObtenerListaDepartamentos() {
        List<DptpMuniBarrio> list = new ArrayList<DptpMuniBarrio>();
        String Query = "SELECT DISTINCT " + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + " FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new DptpMuniBarrio(
                        c.getString(c.getColumnIndex("Nombre_Departamento"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<DptpMuniBarrio> ObtenerListaMunicipios( String DesDepto) {
        List<DptpMuniBarrio> list = new ArrayList<DptpMuniBarrio>();
        String Query = "SELECT DISTINCT " + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio + " FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " WHERE "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + "= '"+ DesDepto + "' ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new DptpMuniBarrio(
                        c.getString(c.getColumnIndex("Nombre_Municipio"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<DptpMuniBarrio> ObtenerListaBarrios( String DesDepto) {
        List<DptpMuniBarrio> list = new ArrayList<DptpMuniBarrio>();
        String Query = "SELECT DISTINCT " + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Barrio + " FROM " + variables_publicas.TABLE_DPTOMUNIBARRIOS + " WHERE "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + "= '"+ DesDepto + "' ORDER BY "+ variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Barrio+";";
        Cursor c = database.rawQuery(Query, null);
        c.getCount();
        if (c.moveToFirst()) {
            do {
                list.add(new DptpMuniBarrio(
                        c.getString(c.getColumnIndex("Nombre_Barrio"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<String>  ObtenerListaRutas(String Idvendedor) {
        Cursor c= database.rawQuery("SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_Ruta + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdVendedor+ "= '"+ Idvendedor + "' ORDER BY "+ variables_publicas.CLIENTES_COLUMN_Ruta +";", null);
        List<String> lst= new ArrayList<String>();

        if(c.moveToFirst()){
            do{
                lst.add(c.getString(c.getColumnIndex("Ruta")));
            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }

    public List<String> ObtenerRutas( ) {
        Cursor c= database.rawQuery("SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_Ruta + " FROM " + variables_publicas.TABLE_CLIENTES + " ORDER BY "+ variables_publicas.CLIENTES_COLUMN_Ruta +";",null);
        List<String> lst= new ArrayList<String>();
        if(c.moveToFirst()){
            do{
                lst.add(c.getString(c.getColumnIndex("Ruta")));
            }while (c.moveToNext());
        }
        c.close();
        return  lst;
    }
    public ArrayList<HashMap<String, String>> BuscarClientesVarios( String Idvendedor) {
        String Query = "SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_IdCliente + ", " + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdVendedor + "= '"+ Idvendedor + "' AND "+ variables_publicas.CLIENTES_COLUMN_Nombre +" LIKE 'CLIENTES VARIOS%' LIMIT 1;";
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
    public String ObtenerClientesVariosId(String Idvendedor) {

        String Query = "SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_IdCliente + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdVendedor + "= '"+ Idvendedor + "' AND "+ variables_publicas.CLIENTES_COLUMN_Nombre +" LIKE 'CLIENTES VARIOS%' LIMIT 1;";
        Cursor c = database.rawQuery(Query, null);
        String numero = "0";
        if (c.moveToFirst()) {
            do {
                numero = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        return numero;
    }

    public boolean GuardarDptosMuniBarrios(String codDpto, String DesDepto,String codMun, String DesMun,String codBarr, String DesBarr){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento, codDpto);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento, DesDepto);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio, codMun);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio, DesMun);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Barrio, codBarr);
        contentValues.put(variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Barrio, DesBarr);
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
    public ArrayList<HashMap<String, String>> ObtenerClienteCedula( String vCedula) {
        String Query = "SELECT DISTINCT " + variables_publicas.CLIENTES_COLUMN_IdCliente + ", " + variables_publicas.CLIENTES_COLUMN_Nombre + " FROM " + variables_publicas.TABLE_CLIENTES + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdVendedor + "= '"+ vCedula + "' AND "+ variables_publicas.CLIENTES_COLUMN_Nombre +" LIKE 'CLIENTES VARIOS%' LIMIT 1;";
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
}