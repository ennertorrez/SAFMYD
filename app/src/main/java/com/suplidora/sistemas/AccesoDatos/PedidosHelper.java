package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Entidades.Articulo;
import com.suplidora.sistemas.Entidades.Pedido;

public class PedidosHelper {


    private SQLiteDatabase database;

    public PedidosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalPedidos(String IdVendedor ,
                                    String IdCliente ,
                                    String Cod_cv ,
                                    String Observacion ,
                                    String IdFormaPago ,
                                    String IdSucursal ,
                                    String Fecha ,
                                    String Usuario ,
                                    String IMEI ) {
        long rows =0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdVendedor  , IdVendedor );
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdCliente  , IdCliente );
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Cod_cv  , Cod_cv );
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Observacion  ,  Observacion);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdFormaPago  , IdFormaPago );
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdSucursal  ,  IdSucursal);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Fecha  ,  Fecha);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Usuario  , Usuario );
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IMEI , IMEI);
        //rows = database.insertWithOnConflict(TABLE_NAME,null,contentValues,SQLiteDatabase.CONFLICT_REPLACE);
        database.insert(variables_publicas.TABLE_PEDIDOS, null, contentValues);
    }
    public Cursor ObtenerListaPedidos() {
        return database.rawQuery("select * from " + variables_publicas.TABLE_PEDIDOS, null);
    }
    public  void EliminaPedidos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_PEDIDOS+";");
        Log.d("pedidos_elimina", "Datos eliminados");
    }

    public int ObtenerNuevoCodigoPedido(){

        String selectQuery = "SELECT COUNT(*) as Cantidad FROM " + variables_publicas.TABLE_PEDIDOS  ;
        Cursor c = database.rawQuery(selectQuery, null);
        int numero=0;
        if (c.moveToFirst()) {
            do {
               numero=c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        return numero+1;
    }

}