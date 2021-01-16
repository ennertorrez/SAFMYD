package com.safi_d.sistemas.safiapp.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.ClienteSucursal;

import java.util.ArrayList;
import java.util.List;

import static com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodCliente;

public class ClientesSucursalHelper {

//    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public ClientesSucursalHelper(SQLiteDatabase db){
        database = db;
    }

    public void GuardarTotalClientesSucursal(
                                     String CodSuc ,
                                     String CodCliente ,
                                     String Sucursal ,
                                     String Ciudad ,
                                     String DeptoID ,
                                     String Direccion ,
                                     String FormaPagoID,String Descuento
                                     )
    {
        long rows =0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodSuc, CodSuc);
        contentValues.put(CLIENTES_SUCURSALES_COLUMN_CodCliente, CodCliente);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Sucursal, Sucursal);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Ciudad, Ciudad);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_DeptoID, DeptoID);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Direccion, Direccion);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_FormaPagoID, FormaPagoID);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Descuento, Descuento);

        database.insert(variables_publicas.TABLE_CLIENTES_SUCURSALES, null, contentValues);
    }
//    public Cursor ObtenerListaVendedores() {
//        return database.rawQuery("select * from " + variables_publicas.TABLE_VENDEDORES, null);
//    }
    public  void EliminaClientesSucursales() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES_SUCURSALES+";");
        Log.d("ClientesSuc_elimina", "Datos eliminados");
    }
    public List<ClienteSucursal> ObtenerListaClientesSucursales(String IdCliente){
        List<ClienteSucursal> list = new ArrayList<ClienteSucursal>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_CLIENTES_SUCURSALES + " Where "+CLIENTES_SUCURSALES_COLUMN_CodCliente+" = "+IdCliente;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(new ClienteSucursal(cursor.getString(cursor.getColumnIndex("CodSuc")),
                        cursor.getString(cursor.getColumnIndex("CodCliente")),
                        cursor.getString(cursor.getColumnIndex("Sucursal")),
                        cursor.getString(cursor.getColumnIndex("Ciudad")),
                        cursor.getString(cursor.getColumnIndex("DeptoID")),
                        cursor.getString(cursor.getColumnIndex("Direccion")),
                        cursor.getString(cursor.getColumnIndex("FormaPagoID")),
                        cursor.getString(cursor.getColumnIndex("Descuento"))
                        ));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
     //   database.close();

        return list;
    }

    public List<ClienteSucursal> ObtenerClienteSucursales(String IdCliente){
        List<ClienteSucursal> list = new ArrayList<ClienteSucursal>();
        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_CLIENTES_SUCURSALES+" where "+variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodCliente+" = "+IdCliente+ " ORDER BY "+variables_publicas.CLIENTES_SUCURSALES_COLUMN_Sucursal;
        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        list.add(new ClienteSucursal("0",IdCliente,"[SIN SUCURSAL]","","","","","0"));
        if (cursor.moveToFirst()) {
            do {
                list.add(new ClienteSucursal(cursor.getString(cursor.getColumnIndex("CodSuc")),
                        cursor.getString(cursor.getColumnIndex("CodCliente")),
                        cursor.getString(cursor.getColumnIndex("Sucursal")),
                        cursor.getString(cursor.getColumnIndex("Ciudad")),
                        cursor.getString(cursor.getColumnIndex("DeptoID")),
                        cursor.getString(cursor.getColumnIndex("Direccion")),
                        cursor.getString(cursor.getColumnIndex("FormaPagoID")),
                        cursor.getString(cursor.getColumnIndex("Descuento"))
                ));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        //database.close();
        return list;
    }

}