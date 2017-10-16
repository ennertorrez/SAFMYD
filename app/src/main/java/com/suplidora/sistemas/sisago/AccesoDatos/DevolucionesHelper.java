package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Devoluciones;
import com.suplidora.sistemas.sisago.Entidades.Pedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DevolucionesHelper {


    private SQLiteDatabase database;

    public DevolucionesHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarDevolucion(String ndevolucion, String cliente,
                                 String horagraba,
                                 String usuario,
                                 String subtotal,
                                 String iva,
                                 String total,
                                 String estado,
                                 String rango,
                                 String motivo,
                                 String factura,String procesado,String useranula,
                                     String horaanula,String tipo,String ejecutada,String IMEI) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, ndevolucion);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, cliente);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, horagraba);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, usuario);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal   , subtotal);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, iva);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_total, total);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, estado);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, rango);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, motivo);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, factura);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado,procesado);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_useranula, useranula);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_horaanula, horaanula);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo, tipo);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada, ejecutada);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI, IMEI);
        long rowInserted = database.insert(variables_publicas.TABLE_DEVOLUCIONES, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean GuardarMotivos(String id, String motivo){
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, id);
        contentValues.put(variables_publicas.DEVOLUCIONES_COLUMN_cliente, motivo);
        long rowInserted = database.insert(variables_publicas.TABLE_MOTIVOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
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
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_useranula,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_useranula)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_horaanula,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horaanula)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devoluciones.put(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
                lst.add(devoluciones);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public void EliminaDevolucion(String ndevolucion) {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_DEVOLUCIONES + " WHERE" +
                " "+variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion+" = '" + ndevolucion + "' ;");
        Log.d("devolucion_deleted", "Datos eliminados");
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
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_horagraba, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_usuario, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_iva, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_total, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_estado, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_rango, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_motivo, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_factura, c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_procesado,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_useranula,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_useranula)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_horaanula,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horaanula)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_tipo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_IMEI,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devolucion.put(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo,c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
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
                devolucion.setProcesado(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)));
                devolucion.setUseranula(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_useranula)));
                devolucion.setHoraanula(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horaanula)));
                devolucion.setTipo(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)));
                devolucion.setEjecutada(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)));
                devolucion.setEjecutada(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)));
                devolucion.setEjecutada(c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo)));
            } while (c.moveToNext());
        }
        c.close();
        return devolucion;
    }

    public ArrayList<HashMap<String, String>> ObtenerDevolucionesLocales(String Fecha, String Nombre) {

        String selectQuery = "SELECT P.*,DATE(P.Fecha) as Fecha,'' as Factura,'NO ENVIADO' as Estado,Cl.Nombre as NombreCliente,Fp.NOMBRE as FormaPago, CL.Detallista FROM " + variables_publicas.TABLE_PEDIDOS +
                " P INNER JOIN " + variables_publicas.TABLE_CLIENTES + " Cl ON CAST( Cl." + variables_publicas.CLIENTES_COLUMN_IdCliente + " AS INT) = cast(P." + variables_publicas.PEDIDOS_COLUMN_IdCliente + " AS INT) AND Cl."+variables_publicas.CLIENTES_COLUMN_CodCv+" = P."+variables_publicas.PEDIDOS_COLUMN_Cod_cv+
                " INNER JOIN "+variables_publicas.TABLE_FORMA_PAGO+" " +
                "Fp ON Fp."+variables_publicas.FORMA_PAGO_COLUMN_CODIGO+" = P."+variables_publicas.PEDIDOS_COLUMN_IdFormaPago+""+
                " WHERE P."+variables_publicas.PEDIDOS_COLUMN_CodigoPedido+" LIKE '-%' AND Cl." + variables_publicas.CLIENTES_COLUMN_NombreCliente + " LIKE '%" + Nombre + "%' AND DATE(P." + variables_publicas.PEDIDOS_COLUMN_Fecha + ") = DATE('" + Fecha + "')"+
                " AND P."+ variables_publicas.PEDIDOS_COLUMN_IdVendedor+ " = "+variables_publicas.usuario.getCodigo();

        Cursor c = database.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> lst = new ArrayList<HashMap<String, String>>();
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> pedido = new HashMap<>();
                pedido.put(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdVendedor)));
                pedido.put("NombreCliente", c.getString(c.getColumnIndex("NombreCliente")));
                pedido.put("FormaPago", c.getString(c.getColumnIndex("FormaPago")));
                pedido.put("Factura", c.getString(c.getColumnIndex("Factura")));
                pedido.put("Estado", c.getString(c.getColumnIndex("Estado")));
                pedido.put("Detallista" , c.getString(c.getColumnIndex(variables_publicas.CLIENTES_COLUMN_Detallista)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdCliente)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Cod_cv, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Cod_cv)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Tipo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Tipo)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Observacion, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Observacion)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdFormaPago, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdFormaPago)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdSucursal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdSucursal)));
                pedido.put("Fecha", c.getString(c.getColumnIndex("Fecha")));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Usuario)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IMEI, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IMEI)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Subtotal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Subtotal)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Total)));
                lst.add(pedido);
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
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horagraba)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_usuario)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_subtotal)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_iva)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_total)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_estado)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_rango)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_motivo)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_factura)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_procesado)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_useranula)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_horaanula)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_ejecutada)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IMEI)),
                        c.getString(c.getColumnIndex(variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return devoluciones;
    }

}