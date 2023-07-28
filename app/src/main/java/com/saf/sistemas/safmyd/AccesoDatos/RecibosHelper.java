package com.saf.sistemas.safmyd.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Bancos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecibosHelper

{
    private SQLiteDatabase database;

    public RecibosHelper(SQLiteDatabase db) {
        database = db;
    }
    public boolean GuardarRecibo(String Serie,
                                 String Recibo,
                                 String Factura,
                                 String Fecha,
                                 String Monto,
                                 String NoCheque,
                                 String BancoR,
                                 String Abono,
                                 String Moneda,
                                 String TipoPago,
                                 String Concepto,
                                 String IdVendedor,
                                 String IdCliente,
                                 String Saldo,
                                 String Usuario,
                                 String Impresion,
                                 String Guardado,
                                 String Latitud,
                                 String Longitud,
                                 String DireccionGeo
                                 ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Serie, Serie);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Recibo, Recibo);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Factura, Factura);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Fecha, Fecha);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Monto, Monto);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_NoCheque, NoCheque);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_BancoR, BancoR);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Abono, Abono);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Moneda, Moneda);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_TipoPago, TipoPago);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Concepto, Concepto);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_IdCliente, IdCliente);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Saldo, Saldo);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Usuario, Usuario);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Impresion, Impresion);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_Guardado, Guardado);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_latitud, Latitud);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_longitud, Longitud);
        contentValues.put(variables_publicas.RECIBOS_COLUMN_direccionGeo, DireccionGeo);

        long rowInserted=database.insert(variables_publicas.TABLE_RECIBOS, null, contentValues);
        if(rowInserted != -1)
            return true;
        else return false;
    }
    public boolean EliminarRecibo(String vRecibo, String vSerie) {
        int rowsAffected =database.delete( variables_publicas.TABLE_RECIBOS, variables_publicas.RECIBOS_COLUMN_Recibo + "=" +vRecibo + " AND "+ variables_publicas.RECIBOS_COLUMN_Serie +"= '"+ vSerie +"'",null) ;
        if(rowsAffected != -1)
            return true;
        else return false;
    }
    @SuppressLint("Range")
    public List<HashMap<String, String>> ObtenerRecibos(String Recibo, String Serie) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        String selectQuery="SELECT  * FROM " + variables_publicas.TABLE_RECIBOS + " WHERE " + variables_publicas.RECIBOS_COLUMN_Recibo + " = "+ Recibo +" AND " + variables_publicas.RECIBOS_COLUMN_Serie + " = '"+ Serie +"' ";
        Cursor c = database.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.RECIBOS_COLUMN_Serie, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Serie)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Recibo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Recibo)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Factura)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Fecha)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Monto, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Monto)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_NoCheque, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_NoCheque)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_BancoR, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_BancoR)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Abono)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Moneda, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Moneda)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_TipoPago, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_TipoPago)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Concepto, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Concepto)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_IdVendedor)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_IdCliente)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Saldo)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Usuario)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Impresion, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Impresion)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Guardado, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Guardado)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_latitud, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_latitud)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_longitud, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_longitud)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_direccionGeo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_direccionGeo)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    @SuppressLint("Range")
    public List<HashMap<String, String>> ObtenerRecibosSincronizar(String Recibo, String Serie) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        String selectQuery="SELECT  * FROM " + variables_publicas.TABLE_RECIBOS + " WHERE " + variables_publicas.RECIBOS_COLUMN_Recibo + " = "+ Recibo +" AND " + variables_publicas.RECIBOS_COLUMN_Serie + " = '"+ Serie +"' ";
        Cursor c = database.rawQuery(selectQuery,null);
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.RECIBOS_COLUMN_Serie, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Serie)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Recibo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Recibo)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_IdVendedor)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_IdCliente)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Factura, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Factura)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Saldo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Saldo)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Monto, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Monto)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Abono, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Abono)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_NoCheque, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_NoCheque)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_BancoR, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_BancoR)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Fecha)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Moneda, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Moneda)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Usuario)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_TipoPago, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_TipoPago)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_Concepto, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_Concepto)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_latitud, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_latitud)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_longitud, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_longitud)));
                detalle.put(variables_publicas.RECIBOS_COLUMN_direccionGeo, c.getString(c.getColumnIndex(variables_publicas.RECIBOS_COLUMN_direccionGeo)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public boolean ActualizarConceptoRecibo(String Serie, String Recibo,String concepto ) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.RECIBOS_COLUMN_Concepto, concepto);
        long rowsUpdated = database.update(variables_publicas.TABLE_RECIBOS, con,  variables_publicas.RECIBOS_COLUMN_Recibo + "= '" + Recibo + "'AND "+ variables_publicas.RECIBOS_COLUMN_Serie + "= '" + Serie + "' ", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }

    public boolean ActualizarReciboGuardado(String Serie, String Recibo,String estado ) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.RECIBOS_COLUMN_Guardado, estado);
        long rowsUpdated = database.update(variables_publicas.TABLE_RECIBOS, con,  variables_publicas.RECIBOS_COLUMN_Recibo + "= '" + Recibo + "'AND "+ variables_publicas.RECIBOS_COLUMN_Serie + "= '" + Serie + "' ", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerRecibo(String CodigoRecibo, String Serie) {

        String selectQuery="SELECT R."+ variables_publicas.RECIBOS_COLUMN_Serie +" as Serie,R."+ variables_publicas.RECIBOS_COLUMN_Serie + "||substr('00000'||R." + variables_publicas.RECIBOS_COLUMN_Recibo +", -5, 5) as ReciboI , R."+ variables_publicas.RECIBOS_COLUMN_Recibo +" as Recibo, R."+ variables_publicas.RECIBOS_COLUMN_IdCliente +" as Id,C."+ variables_publicas.CLIENTES_COLUMN_Nombre +" as Cliente, " +
                " R."+ variables_publicas.RECIBOS_COLUMN_TipoPago +" as TipoPago,R."+ variables_publicas.RECIBOS_COLUMN_Fecha +" as Fecha,R."+ variables_publicas.RECIBOS_COLUMN_Concepto +" as Concepto, R."+ variables_publicas.RECIBOS_COLUMN_IdVendedor +" as Vendedor, "+
                " R."+ variables_publicas.RECIBOS_COLUMN_NoCheque +" as Documento,R."+ variables_publicas.RECIBOS_COLUMN_BancoR +" as Banco FROM "+ variables_publicas.TABLE_RECIBOS +" R INNER JOIN "+ variables_publicas.TABLE_CLIENTES +" C ON " +
                " R."+ variables_publicas.RECIBOS_COLUMN_IdCliente +"= C."+ variables_publicas.CLIENTES_COLUMN_IdCliente+" WHERE R."+ variables_publicas.RECIBOS_COLUMN_Recibo+"= "+ CodigoRecibo +" AND R."+ variables_publicas.RECIBOS_COLUMN_Serie +"= '"+ Serie +"' LIMIT 1;";

        Cursor c = database.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> recibo= new ArrayList<HashMap<String, String>> () ;
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtRpc= new HashMap<>();
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_Serie, c.getString(c.getColumnIndex("Serie")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_Recibo, c.getString(c.getColumnIndex("Recibo")));
                dtRpc.put("ReciboI", c.getString(c.getColumnIndex("ReciboI")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_IdCliente, c.getString(c.getColumnIndex("Id")));
                dtRpc.put("Cliente", c.getString(c.getColumnIndex("Cliente")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_TipoPago,c.getString(c.getColumnIndex("TipoPago")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_Fecha, c.getString(c.getColumnIndex("Fecha")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_NoCheque, c.getString(c.getColumnIndex("Documento")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_BancoR, c.getString(c.getColumnIndex("Banco")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex("Vendedor")));
                dtRpc.put(variables_publicas.RECIBOS_COLUMN_Concepto, c.getString(c.getColumnIndex("Concepto")));
                recibo.add(dtRpc);
            } while (c.moveToNext());
        }
        c.close();
        return recibo;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerReciboMontosRecibo(String CodigoRecibo, String Serie) {

        String selectQuery="SELECT DISTINCT "+ variables_publicas.RECIBOS_COLUMN_Monto +" as Monto, group_concat("+ variables_publicas.RECIBOS_COLUMN_Factura +") as Facturas "+
                " FROM "+ variables_publicas.TABLE_RECIBOS +" WHERE "+ variables_publicas.RECIBOS_COLUMN_Recibo+"= "+ CodigoRecibo +" AND "+ variables_publicas.RECIBOS_COLUMN_Serie +"= '"+ Serie +"';";

        Cursor c = database.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> recibo= new ArrayList<HashMap<String, String>> () ;
        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtRpc= new HashMap<>();
                dtRpc.put("Monto", c.getString(c.getColumnIndex("Monto")));
                dtRpc.put("Facturas", c.getString(c.getColumnIndex("Facturas")));
                recibo.add(dtRpc);
            } while (c.moveToNext());
        }
        c.close();
        return recibo;
    }
    public boolean ActualizarImpresionRecibo(String Serie, String Recibo,String valor) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.RECIBOS_COLUMN_Impresion, valor);

        long rowsUpdated = database.update(variables_publicas.TABLE_RECIBOS, con,  variables_publicas.RECIBOS_COLUMN_Recibo + "= '" + Recibo + "'AND "+ variables_publicas.RECIBOS_COLUMN_Serie + "= '" + Serie + "' ", null);
        if (rowsUpdated != -1)
            return true;
        else
            return false;
    }
    public String ObtenerTipoImpresion(String CodigoRecibo, String Serie) {

        String selectQuery="SELECT DISTINCT "+ variables_publicas.RECIBOS_COLUMN_Impresion +" as Tipo "+
                " FROM "+ variables_publicas.TABLE_RECIBOS +" WHERE "+ variables_publicas.RECIBOS_COLUMN_Recibo+"= "+ CodigoRecibo +" AND "+ variables_publicas.RECIBOS_COLUMN_Serie +"= '"+ Serie +"' ;";
        Cursor c = database.rawQuery(selectQuery, null);
        String resultado="";

        if (c.moveToFirst()) {
            do {
                resultado = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }
    public boolean EliminarBancos() {
        long deletedrows=  database.delete( variables_publicas.TABLE_BANCOS,null,null);
        Log.d("bancos_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }
    public boolean GuardarBancos(String cod, String banco){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.BANCOS_COLUMN_codigo, cod);
        contentValues.put(variables_publicas.BANCOS_COLUMN_nombre, banco);
        long rowInserted = database.insert(variables_publicas.TABLE_BANCOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    @SuppressLint("Range")
    public List<Bancos> ObtenerListaBancos() {
        List<Bancos> list = new ArrayList<Bancos>();
        String Query = "SELECT DISTINCT " + variables_publicas.BANCOS_COLUMN_codigo + " ," + variables_publicas.BANCOS_COLUMN_nombre + " FROM " + variables_publicas.TABLE_BANCOS + " ORDER BY "+ variables_publicas.BANCOS_COLUMN_codigo+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Bancos(
                        c.getString(c.getColumnIndex("Codigo")),
                        c.getString(c.getColumnIndex("Nombre"))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean GuardarImpresora(String dirMAC,
                                    String nombre) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.IMPRESORA_COLUMN_dirMAC, dirMAC);
        contentValues.put(variables_publicas.IMPRESORA_COLUMN_nombre, nombre);
        long rowInserted = database.insert(variables_publicas.TABLE_IMPRESORA, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    public boolean EliminaImpresora() {
        long rowInserted = database.delete(variables_publicas.TABLE_IMPRESORA, null, null);
        if (rowInserted != -1)
            return true;
        else return false;
    }
    @SuppressLint("Range")
    public String  BuscarNombreImpresora() {
        String Query = "SELECT "+ variables_publicas.IMPRESORA_COLUMN_nombre +" as nombre FROM "+ variables_publicas.TABLE_IMPRESORA +" LIMIT 1;";
        String vnombre="0";
        Cursor c = database.rawQuery(Query, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                vnombre=c.getString(c.getColumnIndex("nombre"));
            } while (c.moveToNext());
        }
        c.close();
        return vnombre;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerRecibosLocales(String Fecha, String Fecha2, String recibo,String serie) {

        String selectQuery = "SELECT D.codigo Recibo,D.Serie,D.ReciboI as ReciboI,D.cliente Cliente,D.fech Fecha,group_concat(D.fact) as Facturas,printf(\"%.2f\",SUM(D.total)) Monto,D.Est Status FROM "+
                "(SELECT I."+ variables_publicas.RECIBOS_COLUMN_Serie +" as Serie, I."+ variables_publicas.RECIBOS_COLUMN_Recibo +" " +
                "codigo, "+ variables_publicas.RECIBOS_COLUMN_Serie +"||substr('00000'||"+ variables_publicas.RECIBOS_COLUMN_Recibo +", -5, 5) as ReciboI,DI."+ variables_publicas.CLIENTES_COLUMN_Nombre +" cliente,DATE(I."+ variables_publicas.RECIBOS_COLUMN_Fecha +") as fech," +
                "I."+ variables_publicas.RECIBOS_COLUMN_Factura +" fact,printf(\"%.2f\",SUM(I." + variables_publicas.RECIBOS_COLUMN_Abono + ")) total,case when I." + variables_publicas.RECIBOS_COLUMN_Guardado + " = 'true' then 'APLICADO' when I." + variables_publicas.RECIBOS_COLUMN_Guardado + " = 'anulado' then 'ANULADO' else 'NO ENVIADO' end as Est  " +
                "FROM "+ variables_publicas.TABLE_RECIBOS +" I INNER JOIN "+ variables_publicas.TABLE_CLIENTES +" DI ON I."+ variables_publicas.RECIBOS_COLUMN_IdCliente +"=DI."+ variables_publicas.CLIENTES_COLUMN_IdCliente +" " +
                "WHERE I."+ variables_publicas.RECIBOS_COLUMN_Guardado +" in('false','anulado') and (DATE(I." + variables_publicas.RECIBOS_COLUMN_Fecha + ") BETWEEN DATE('" + Fecha + "')  AND DATE('" + Fecha2 + "')) AND I." + variables_publicas.RECIBOS_COLUMN_Recibo + " LIKE '%" + recibo + "%' " +
                "AND I." + variables_publicas.RECIBOS_COLUMN_Serie + " = '" + serie  + "' GROUP BY I." + variables_publicas.RECIBOS_COLUMN_Recibo + ",DI."+ variables_publicas.CLIENTES_COLUMN_Nombre +",DATE(I."+ variables_publicas.RECIBOS_COLUMN_Fecha +"),I."+ variables_publicas.RECIBOS_COLUMN_Factura +") D "+
                "GROUP BY  D.codigo,D.Serie,D.ReciboI,D.cliente,D.fech,D.Est;";

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> informes = new HashMap<>();
                informes.put("Recibo", c.getString(c.getColumnIndex("Recibo")));
                informes.put("ReciboI", c.getString(c.getColumnIndex("ReciboI")));
                informes.put("Serie", c.getString(c.getColumnIndex("Serie")));
                informes.put("Cliente", c.getString(c.getColumnIndex("Cliente")));
                informes.put("Fecha", c.getString(c.getColumnIndex("Fecha")));
                informes.put("Facturas", c.getString(c.getColumnIndex("Facturas")));
                informes.put("Monto", c.getString(c.getColumnIndex("Monto")));
                informes.put("Estado", c.getString(c.getColumnIndex("Status")));
                lst.add(informes);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerRecibosLocalesTodos(String Fecha, String Fecha2, String serie) {

        String selectQuery = "SELECT D.codigo Recibo,D.Serie,D.ReciboI as ReciboI,D.cliente Cliente,D.fech Fecha,group_concat(D.fact) as Facturas,printf(\"%.2f\",SUM(D.total)) Monto,D.Est Status FROM "+
                "(SELECT I."+ variables_publicas.RECIBOS_COLUMN_Serie +" as Serie, I."+ variables_publicas.RECIBOS_COLUMN_Recibo +" " +
                "codigo, "+ variables_publicas.RECIBOS_COLUMN_Serie +"||substr('00000'||"+ variables_publicas.RECIBOS_COLUMN_Recibo +", -5, 5) as ReciboI,DI."+ variables_publicas.CLIENTES_COLUMN_Nombre +" cliente,DATE(I."+ variables_publicas.RECIBOS_COLUMN_Fecha +") as fech," +
                "I."+ variables_publicas.RECIBOS_COLUMN_Factura +" fact,printf(\"%.2f\",SUM(I." + variables_publicas.RECIBOS_COLUMN_Abono + ")) total,case when I." + variables_publicas.RECIBOS_COLUMN_Guardado + " = 'true' then 'APLICADO' when I." + variables_publicas.RECIBOS_COLUMN_Guardado + " = 'anulado' then 'ANULADO' else 'NO ENVIADO' end as Est " +
                "FROM "+ variables_publicas.TABLE_RECIBOS +" I INNER JOIN "+ variables_publicas.TABLE_CLIENTES +" DI ON I."+ variables_publicas.RECIBOS_COLUMN_IdCliente +"=DI."+ variables_publicas.CLIENTES_COLUMN_IdCliente +" " +
                "WHERE  (DATE(I." + variables_publicas.RECIBOS_COLUMN_Fecha + ") BETWEEN DATE('" + Fecha + "')  AND DATE('" + Fecha2 + "'))  " +
                "AND I." + variables_publicas.RECIBOS_COLUMN_Serie + " = '" + serie  + "' GROUP BY I." + variables_publicas.RECIBOS_COLUMN_Recibo + ",DI."+ variables_publicas.CLIENTES_COLUMN_Nombre +",DATE(I."+ variables_publicas.RECIBOS_COLUMN_Fecha +"),I."+ variables_publicas.RECIBOS_COLUMN_Factura +") D "+
                "GROUP BY  D.codigo,D.Serie,D.ReciboI,D.cliente,D.fech,D.Est;";

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> informes = new HashMap<>();
                informes.put("Recibo", c.getString(c.getColumnIndex("Recibo")));
                informes.put("ReciboI", c.getString(c.getColumnIndex("ReciboI")));
                informes.put("Serie", c.getString(c.getColumnIndex("Serie")));
                informes.put("Cliente", c.getString(c.getColumnIndex("Cliente")));
                informes.put("Fecha", c.getString(c.getColumnIndex("Fecha")));
                informes.put("Facturas", c.getString(c.getColumnIndex("Facturas")));
                informes.put("Monto", c.getString(c.getColumnIndex("Monto")));
                informes.put("Estado", c.getString(c.getColumnIndex("Status")));
                lst.add(informes);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public String ObtenerSerieRuta(String idRuta) {

        String selectQuery="SELECT "+ variables_publicas.RUTA_COLUMN_recibo +" as Serie "+
                " FROM "+ variables_publicas.TABLE_RUTAS +" WHERE "+ variables_publicas.RUTA_COLUMN_idRuta+"= "+ idRuta +" ;";
        Cursor c = database.rawQuery(selectQuery, null);
        String resultado="";

        if (c.moveToFirst()) {
            do {
                resultado = c.getString(0);

            } while (c.moveToNext());
        }
        c.close();
        return resultado;
    }
    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerUltimoCodigoRecibo(String idRuta, String vSerie) {

        String selectQuery = "SELECT "+ variables_publicas.RUTA_COLUMN_recibo +" AS RECIBO,"+ variables_publicas.RUTA_COLUMN_ultrecibo +"  AS ULTRECIBIO FROM " + variables_publicas.TABLE_RUTAS + " where  "+ variables_publicas.RUTA_COLUMN_idRuta +"="+ idRuta +" AND  "+ variables_publicas.RUTA_COLUMN_recibo +"='"+ vSerie +"';";
        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if (c.moveToFirst()) {
            do {
                HashMap<String,String> dtSerie= new HashMap<>();
                dtSerie.put(variables_publicas.RUTA_COLUMN_recibo, c.getString(c.getColumnIndex("RECIBO")));
                dtSerie.put(variables_publicas.RUTA_COLUMN_ultrecibo, c.getString(c.getColumnIndex("ULTRECIBIO")));
                lst.add(dtSerie);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public boolean ActualizarCodigoRecibo(String idserie, String vnumero, String ruta){
        ContentValues con = new ContentValues();
        con.put("ULTRECIBO", vnumero);
        long rowInserted= database.update(variables_publicas.TABLE_RUTAS, con, variables_publicas.RUTA_COLUMN_recibo +"='"+idserie+"' and "+ variables_publicas.RUTA_COLUMN_idRuta +"= "+ ruta +";", null );
        if(rowInserted != -1)
            return true;
        else return false;
    }



}
