package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FacturasPendientesHelper {
    private DataBaseOpenHelper DbOpenHelper;

    private SQLiteDatabase database;

    public FacturasPendientesHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarFacturasPendientes(String vendedor,
                                          String fecha,
                                          String factura,
                                          String idcliente,
                                          String total,
                                          String abono,
                                          String saldo,
                                          String ruta,
                                          String guardada
    ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, vendedor);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, factura);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, idcliente);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, fecha);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, total);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, abono);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, saldo);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta, ruta);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, guardada);
        database.insert(variables_publicas.TABLE_FACTURAS_PENDIENTES, null, contentValues);
    }

    public boolean GuardarFacturasPendientes2(String vendedor,
                                          String fecha,
                                          String factura,
                                          String idcliente,
                                          String total,
                                          String abono,
                                          String saldo,
                                          String ruta,
                                          String guardada
    ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, vendedor);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, factura);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, idcliente);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, fecha);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, total);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, abono);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, saldo);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta, ruta);
        contentValues.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, guardada);

        long rowInserted =database.insert(variables_publicas.TABLE_FACTURAS_PENDIENTES, null, contentValues);

        if (rowInserted != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public List<HashMap<String, String>> ObtenerFacturasPendientes(String vRuta, String vCliente) {
        List<HashMap<String, String>> lst = new ArrayList<>();
        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta + " = " + vRuta + " AND " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "= CASE WHEN " + vCliente + "=0 THEN " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "ELSE " + vCliente + " END;", null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerDatosFacturaPendiente(String vFactura) {
        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();

        Cursor c = database.rawQuery("SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + " = '" + vFactura + "';", null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Ruta)));
                detalle.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    @SuppressLint("Range")
    public java.util.ArrayList<String> ObtenerFacturasPendientesArrayList(String vCliente) {
        java.util.ArrayList<String> lst = new java.util.ArrayList<String>();

        String sql = "SELECT  * FROM " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + "= CASE WHEN " + vCliente + "=0 THEN " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + " ELSE " + vCliente + " END AND " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada + "= 'false';";
        Cursor c = database.rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                lst.add(new String(
                        c.getString(c.getColumnIndex(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura))
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

    @SuppressLint("Range")
    public Double BuscarSaldoFactura(String Factura) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + ")) as vSaldo from " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE  " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + " = '" + Factura + "'";
        double salFacturaoriginal = 0;
        Cursor c = database.rawQuery(Query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                salFacturaoriginal = c.getDouble(c.getColumnIndex("vSaldo"));
            } while (c.moveToNext());
        }
        c.close();
        return salFacturaoriginal;
    }

    @SuppressLint("Range")
    public Double BuscarAbonoTotalFactura(String Factura) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono+ ")) as vAbono from " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE  " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + " = '" + Factura + "'";
        double montoFacturaoriginal = 0;
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                montoFacturaoriginal = c.getDouble(c.getColumnIndex("vAbono"));
            } while (c.moveToNext());
        }
        c.close();
        return montoFacturaoriginal;
    }
    public boolean ActualizarFacturasPendientes(String factura, String estado,double vSaldo,double vMonto) {
        ContentValues con = new ContentValues();
        double AbonoTotal= 0;
        double nuevoAbono=0;
        AbonoTotal=BuscarAbonoTotalFactura(factura);
        nuevoAbono=AbonoTotal + vMonto;
        if (vSaldo>0){
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, nuevoAbono);
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, vSaldo);
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, "false");
        }else{
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono, nuevoAbono);
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo, vSaldo);
            con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, "true");
        }
//        con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, estado);
        long rowsUpdated = database.update(variables_publicas.TABLE_FACTURAS_PENDIENTES, con, variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + "= '" + factura + "'", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }
    public boolean ActualizarFacturasPendientes2(String factura, String estado) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada, estado);
        long rowsUpdated = database.update(variables_publicas.TABLE_FACTURAS_PENDIENTES, con, variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + "= '" + factura + "'", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }

    @SuppressLint("Range")
    public boolean ActualizarTodasFacturasPendientes(String recibo, String serie) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.RECIBOS_COLUMN_Abono + ")) as vAbono, "+ variables_publicas.RECIBOS_COLUMN_Factura +" as fact from " + variables_publicas.TABLE_RECIBOS + " WHERE  "+ variables_publicas.RECIBOS_COLUMN_Recibo +"=" + recibo + " AND "+ variables_publicas.RECIBOS_COLUMN_Serie +"='" + serie + "' GROUP BY "+ variables_publicas.RECIBOS_COLUMN_Factura +"";
        double valorAbonado = 0;
        String vFactura="";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                valorAbonado = c.getDouble(c.getColumnIndex("vAbono"));
                vFactura = c.getString(c.getColumnIndex("fact"));
                database.execSQL("Update "+ variables_publicas.TABLE_FACTURAS_PENDIENTES +" SET " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono + " = " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono + " - "+ valorAbonado +", " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + "= " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + " + "+ valorAbonado +"," + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada + "='false' WHERE "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura +" ='"+ vFactura +"'");
            } while (c.moveToNext());
        }
        c.close();
        return true;
    }

    @SuppressLint("Range")
    public boolean ActualizarTodasFacturasPendientesRecibo(String vRecibo, String vSerie) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.RECIBOS_COLUMN_Abono + ")) as vAbono, "+ variables_publicas.RECIBOS_COLUMN_Factura +" as fact from " + variables_publicas.TABLE_RECIBOS + " WHERE  "+ variables_publicas.RECIBOS_COLUMN_Recibo +"=" + vRecibo + " AND "+ variables_publicas.RECIBOS_COLUMN_Serie +"='" + vSerie + "' GROUP BY "+ variables_publicas.RECIBOS_COLUMN_Factura +"";
        double valorAbonado = 0;
        String vFactura="";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                valorAbonado = c.getDouble(c.getColumnIndex("vAbono"));
                vFactura = c.getString(c.getColumnIndex("fact"));
                database.execSQL("Update "+ variables_publicas.TABLE_FACTURAS_PENDIENTES +" SET " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono + " = " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono + " - "+ valorAbonado +", " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + "= " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + " + "+ valorAbonado +"," + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada + "='false' WHERE "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura +" ='"+ vFactura +"'");
            } while (c.moveToNext());
        }
        c.close();
        return true;
    }

    @SuppressLint("Range")
    public boolean ActualizarSaldoCliente(String cliente) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + ")) as vSaldo, "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente +" AS Cliente from " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE  "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente +"= CASE WHEN '" + cliente + "'='0' THEN "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente +" ELSE '" + cliente + "' END group by "+ variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente +" ";
        double valorSaldo = 0;
        String vCliente="0";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                valorSaldo = c.getDouble(c.getColumnIndex("vSaldo"));
                vCliente= c.getString(c.getColumnIndex("Cliente"));
                database.execSQL("Update "+ variables_publicas.TABLE_CLIENTES +" SET " + variables_publicas.CLIENTES_COLUMN_Saldo + " = " + valorSaldo + " WHERE "+ variables_publicas.CLIENTES_COLUMN_IdCliente +" ='"+ vCliente +"'");
            } while (c.moveToNext());
        }
        c.close();
        return true;
    }


    @SuppressLint("Range")
    public Double BuscarSaldoCliente(String cliente) {
        String Query = "select printf(\"%.2f\",SUM(" + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + ")) as vSaldo from " + variables_publicas.TABLE_FACTURAS_PENDIENTES + " WHERE  " + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + " = " + cliente + "";
        double saldotodo = 0;
        Cursor c = database.rawQuery(Query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                saldotodo = c.getDouble(c.getColumnIndex("vSaldo"));
            } while (c.moveToNext());
        }
        c.close();
        return saldotodo;
    }
}
