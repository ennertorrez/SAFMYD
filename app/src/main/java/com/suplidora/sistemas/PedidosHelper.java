package com.suplidora.sistemas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PedidosHelper {


    PedidosOpenHelper openHelper;
    private SQLiteDatabase database;

    public PedidosHelper(Context context){
        openHelper = new PedidosOpenHelper(context);
        database = openHelper.getWritableDatabase();
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
    private class PedidosOpenHelper extends SQLiteOpenHelper {
        public PedidosOpenHelper(Context context) {
            // TODO Auto-generated constructor stub
            super(context, variables_publicas.DATABASE_NAME, null, variables_publicas.DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PEDIDOS + "( "
                    + variables_publicas.PEDIDOS_COLUMN_IdVendedor + " INTEGER , "
                    + variables_publicas.PEDIDOS_COLUMN_IdCliente + " INTEGER , "
                    + variables_publicas.PEDIDOS_COLUMN_Cod_cv + " INTEGER , "
                    + variables_publicas.PEDIDOS_COLUMN_Observacion + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdFormaPago + " INTEGER , "
                    + variables_publicas.PEDIDOS_COLUMN_IdSucursal + " INTEGER , "
                    + variables_publicas.PEDIDOS_COLUMN_Fecha + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Usuario + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IMEI + " TEXT ) " );
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS "+ variables_publicas.TABLE_PEDIDOS);
            onCreate(db);
        }
    }
}