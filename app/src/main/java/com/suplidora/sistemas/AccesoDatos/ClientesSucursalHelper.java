package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.List;

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
                                     String FormaPagoID ,
                                     String Contacto ,
                                     String ContactoTel ,
                                     String VendedorID ,
                                     String codigo2 ,
                                     String Status ,
                                     String detalle ,
                                     String horeca ,
                                     String mayorista )
    {
        long rows =0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodSuc, CodSuc);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodCliente, CodCliente);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Sucursal, Sucursal);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Ciudad, Ciudad);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_DeptoID, DeptoID);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_Direccion, Direccion);
        contentValues.put(variables_publicas.CLIENTES_SUCURSALES_COLUMN_FormaPagoID, FormaPagoID);


        database.insert(variables_publicas.TABLE_CLIENTES_SUCURSALES, null, contentValues);
    }
//    public Cursor ObtenerListaVendedores() {
//        return database.rawQuery("select * from " + variables_publicas.TABLE_VENDEDORES, null);
//    }
    public  void EliminaClientesSucursales() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_CLIENTES_SUCURSALES+";");
        Log.d("ClientesSuc_elimina", "Datos eliminados");
    }
    public List<String> ObtenerListaClientesSucursales(){
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + variables_publicas.TABLE_CLIENTES_SUCURSALES;

        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        database.close();

        return list;
    }

}