package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacturasPendientesHelper {


    private SQLiteDatabase database;

    public FacturasPendientesHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarFacturasPendientes(String vendedor,
                                      String fecha,
                                      String factura,
                                      String nombrecliente,
                                      String idcliente,
                                      String iva,
                                      String tipo,
                                      String subtotal,
                                      String descuento,
                                      String total,
                                      String abono,
                                      String saldo,
                                      String guardada
    ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, vendedor);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, factura);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente, nombrecliente);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, idcliente);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, fecha);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA, iva);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo, tipo);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal, subtotal);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento, descuento);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, total);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, abono);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, saldo);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, guardada);
        database.insert(variables_publicas.TABLE_FACTURAS_PENDIENTES, null, contentValues);
    }


    public boolean GuardarFacturasPendientes(HashMap<String,String> lstFacturas) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo));
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, lstFacturas.get(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada));
        long rowInserted=database.insert(variables_publicas.TABLE_FACTURAS_PENDIENTES, null, contentValues);
        if(rowInserted != -1)
           return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerFacturasPendientes(String vVendedor, String vCliente) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor + " = "+ vVendedor + " AND " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "= CASE WHEN "+ vCliente + "=0 THEN " +  variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "ELSE " + vCliente + " END;" ,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public ArrayList<HashMap<String, String>> ObtenerDatosFacturaPendiente(String vFactura) {
        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();

        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + " = '"+ vFactura + "';" ,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public java.util.ArrayList<String> ObtenerFacturasPendientesArrayList(String vVendedor, String vCliente) {
        java.util.ArrayList<String> lst = new java.util.ArrayList<String>();
        String sql = "SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor + " = "+ vVendedor + " AND " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "= CASE WHEN "+ vCliente + "=0 THEN " +  variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + " ELSE " + vCliente + " END AND "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada +"= 'false';";
        Cursor c = database.rawQuery(sql,null);
        if (c.moveToFirst()) {
            do {
                lst.add(new String(
                        //c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura))
                       /* c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono)),
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo))*/
                ));
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public void EliminaFacturasPendientes() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + ";");
        Log.d("fact_pend_eliminadas", "Datos eliminados");
    }
    public Double  BuscarSaldoFactura(String Factura) {
        String Query = "select printf(\"%.2f\",SUM("+variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo+")) as vSaldo from " + variables_publicas.TABLE_FACTURAS_PENDIENTES +" WHERE  "+variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura+" = '"+ Factura +"'";
        double salFacturaoriginal=0;
        Cursor c = database.rawQuery(Query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                salFacturaoriginal=c.getDouble(c.getColumnIndex("vSaldo"));
            } while (c.moveToNext());
        }
        c.close();
        return salFacturaoriginal;
    }
    public boolean ActualizarFacturasPendientes(String factura,String estado ) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada,estado);
        long rowsUpdated = database.update(variables_publicas.TABLE_FACTURAS_PENDIENTES, con, variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + "= '" + factura+"'", null);
        if (rowsUpdated != -1)
            return true;
        else return false;
    }
}