package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Devoluciones;
import com.suplidora.sistemas.sisago.Entidades.Motivos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevolucionesHelper {


    private SQLiteDatabase database;

    public DevolucionesHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarDevolucion(String ndevolucion, String cliente,String nombrecliente,
                                 String horagraba,
                                 String usuario,
                                 String subtotal,
                                 String iva,
                                 String total,
                                 String estado,
                                 String rango,
                                 String motivo,
                                 String factura, String tipo,String IMEI,String IdVehiculo,String Observaciones,String ejecutada,String procesado) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, ndevolucion);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, cliente);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente, nombrecliente);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, horagraba);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, usuario);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal   , subtotal);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, iva);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_total, total);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, estado);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, rango);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, motivo);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, factura);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo, tipo);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI, IMEI);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo, IdVehiculo);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones, Observaciones);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada, ejecutada);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado, procesado);

        long rowInserted = database.insert(variables_publicas.TABLE_DEVOLUCIONES, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean GuardarMotivos(String id, String motivo){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.MOTIVOS_COLUMN_id, id);
        contentValues.put(variables_publicas.MOTIVOS_COLUMN_motivo, motivo);
        long rowInserted = database.insert(variables_publicas.TABLE_MOTIVOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerListaMotivosHashMap() {
        HashMap<String, String> motivos = null;
        List<HashMap<String, String>> lst = new ArrayList<>();
        String Query = "SELECT * FROM " + variables_publicas.TABLE_MOTIVOS + ";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                motivos = new HashMap<>();
                motivos.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.MOTIVOS_COLUMN_id)));
                motivos.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.MOTIVOS_COLUMN_motivo)));
                lst.add(motivos);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public ArrayList<Motivos> ObtenerListaMotivos() {
        Motivos motivo = null;
        ArrayList<Motivos> lst = new ArrayList<>();
        String Query = "SELECT * FROM " + variables_publicas.TABLE_MOTIVOS + " ORDER BY "+variables_publicas.MOTIVOS_COLUMN_motivo+";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                motivo = new Motivos(
                 c.getString(c.getColumnIndex(variables_publicas.MOTIVOS_COLUMN_id)),
               c.getString(c.getColumnIndex(variables_publicas.MOTIVOS_COLUMN_motivo)));
                lst.add(motivo);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean ActualizarDevoluciones(String ndevolucion , String CodigoDevolucion) {
        ContentValues con = new ContentValues();
        con.put("ndevolucion", CodigoDevolucion);
        long rowInserted = database.update(variables_publicas.TABLE_DEVOLUCIONES, con, variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion + "= '" + ndevolucion+"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerListaDevoluciones() {
        HashMap<String, String> devoluciones = null;
        List<HashMap<String, String>> lst = new ArrayList<>();
        String Query = "SELECT * FROM " + variables_publicas.TABLE_DEVOLUCIONES + ";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                devoluciones = new HashMap<>();
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_cliente)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
                lst.add(devoluciones);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean EliminaDevolucion(String ndevolucion) {
      /*  database.execSQL("DELETE FROM " + variables_publicas.TABLE_DEVOLUCIONES + " WHERE" +
                " "+variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion+" = '" + ndevolucion + "' ;");*/

       long deletedRows= database.delete(variables_publicas.TABLE_DEVOLUCIONES,variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion+" = '" + ndevolucion + "'",null);
        Log.d("devolucion_deleted", "Datos eliminados");
        return deletedRows>0;
    }

    public boolean EliminarMotivos() {
      long deletedrows=  database.delete( variables_publicas.TABLE_MOTIVOS,null,null);
        Log.d("devolucion_deleted", "Datos eliminados");
        return deletedrows!=-1;
    }

    public int ObtenerNuevoCodigoDevolucion() {

        String selectQuery = "SELECT COUNT(*) as Cantidad FROM " + variables_publicas.TABLE_DEVOLUCIONES;
        Cursor c = database.rawQuery(selectQuery, null);
        int numero = 0;
        if (c.moveToFirst()) {
            do {
                numero = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        return numero + 1;
    }

    public HashMap<String, String> ObtenerDevolucion(String ndevolucion) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_DEVOLUCIONES + " Where " + variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion + " = ?", new String[]{ndevolucion});
        HashMap<String, String> devolucion = null;
        if (c.moveToFirst()) {
            do {
                devolucion = new HashMap<>();
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_cliente)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente, Funciones.Codificar( c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente))));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones,Funciones.Codificar( c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones))));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
            } while (c.moveToNext());
        }
        c.close();
        return devolucion;
    }

    public Devoluciones GetDevolucion(String ndevolucion) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_DEVOLUCIONES + " Where " + variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion + " = ?", new String[]{ndevolucion});
        Devoluciones devolucion = null;
        if (c.moveToFirst()) {
            do {
                devolucion = new Devoluciones();
                devolucion.setNdevolucion(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion)));
                devolucion.setCliente(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_cliente)));
                devolucion.setHoragraba(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devolucion.setUsuario(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devolucion.setSubtotal(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devolucion.setIva(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devolucion.setTotal(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devolucion.setEstado(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devolucion.setRango(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devolucion.setMotivo(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devolucion.setFactura(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devolucion.setTipo(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devolucion.setIMEI(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devolucion.setIdVehiculo(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
                devolucion.setIdVehiculo(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones)));
                devolucion.setEjecutada(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devolucion.setProcesado(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
            } while (c.moveToNext());
        }
        c.close();
        return devolucion;
    }

    public ArrayList<HashMap<String, String>> ObtenerDevolucionesLocales(String Fecha, String columnaFiltro, String filtro) {

        String selectQuery = "SELECT * FROM " + variables_publicas.TABLE_DEVOLUCIONES+ " WHERE "+variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion+" LIKE '-%' AND  "+" DATE( " + variables_publicas.DEVOLUCIONES_COLUMN_horagraba + ") = DATE('" + Fecha + "') AND " + columnaFiltro + " LIKE '%" + filtro + "%'";

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> devolucion = new HashMap<>();
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_cliente)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
                lst.add(devolucion);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }
    public Devoluciones BuscarDevolucionesSinconizar( ) {
        Devoluciones devoluciones = null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_DEVOLUCIONES+"";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                devoluciones = (new Devoluciones(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_cliente)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_nombrecliente)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado))

                ));
            } while (c.moveToNext());
        }
        c.close();
        return devoluciones;
    }

}