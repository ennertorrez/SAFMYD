package com.saf.sistemas.safcafenorteno.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.HttpHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.net.URL;
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

    public boolean SincronizarFacturasSaldos(String vRuta, String vCliente)  {
        HttpHandler shC = new HttpHandler();
        String urlGetFacturasPendientes = variables_publicas.direccionIp + "/ServicioRecibos.svc/SpObtieneFacturasSaldoPendiente/";
        String urlStringC = urlGetFacturasPendientes + vRuta + "/" + vCliente + "/" + variables_publicas.usuario.getEmpresa_ID();

        String encodeUrl = "";
        try {
            URL Url = new URL(urlStringC);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStrC = shC.makeServiceCall(encodeUrl);

        if (jsonStrC != null) {

            try {
                //DbOpenHelper.database.beginTransaction();
                JSONObject jsonObj = new JSONObject(jsonStrC);
                // Getting JSON Array node
                JSONArray articulos = jsonObj.getJSONArray("SpObtieneFacturasSaldoPendienteResult");
                if (articulos.length() == 0) {
                    return false;
                }
                EliminaFacturasPendientes();
                // looping through All Contacts

                for (int i = 0; i < articulos.length(); i++) {
                    JSONObject c = articulos.getJSONObject(i);
                    String codvendedor = c.getString("codvendedor");
                    String No_Factura = c.getString("No_Factura");
                    String CodigoCliente = c.getString("CodigoCliente");
                    String Fecha = c.getString("Fecha");
                    String Total = c.getString("Total");
                    String Abono = c.getString("Abono");
                    String Saldo = c.getString("Saldo");
                    String Ruta = c.getString("Ruta");
                    String Guardada = c.getString("Guardada");
                    GuardarFacturasPendientes(codvendedor, Fecha, No_Factura, CodigoCliente,  Total, Abono, Saldo,Ruta, Guardada);
                }
                return true;
                // DbOpenHelper.database.setTransactionSuccessful();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el listado de facturas pendientes. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
                return false;
            }

          /*  finally {
                DbOpenHelper.database.endTransaction();
            }*/

        } else {
            new Funciones().SendMail("Ha ocurrido un error al actualizar listado de facturas pendientes. Ha ocurrido un error al sincronizar las Facturas Pendientes,Respuesta nula", variables_publicas.info + urlStringC, "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
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
