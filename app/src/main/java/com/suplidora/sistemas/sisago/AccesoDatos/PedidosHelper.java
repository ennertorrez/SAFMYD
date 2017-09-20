package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Articulo;
import com.suplidora.sistemas.sisago.Entidades.Pedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidosHelper {


    private SQLiteDatabase database;

    public PedidosHelper(SQLiteDatabase db) {
        database = db;
    }

    public boolean GuardarPedido(String CodigoPedido, String IdVendedor,
                                 String IdCliente,
                                 String Cod_cv,
                                 String Tipo,
                                 String Observacion,
                                 String IdFormaPago,
                                 String IdSucursal,
                                 String Fecha,
                                 String Usuario,
                                 String IMEI,String Subtotal,String Total) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, CodigoPedido);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdVendedor, IdVendedor);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdCliente, IdCliente);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Cod_cv, Cod_cv);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Tipo   , Tipo);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Observacion, Observacion);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdFormaPago, IdFormaPago);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IdSucursal, IdSucursal);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Fecha, Fecha);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Usuario, Usuario);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_IMEI, IMEI);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal,Subtotal);
        contentValues.put(variables_publicas.PEDIDOS_COLUMN_Total, Total);
        long rowInserted = database.insert(variables_publicas.TABLE_PEDIDOS, null, contentValues);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public boolean ActualizarPedido(String CodigoPedido, String NoPedido) {
        ContentValues con = new ContentValues();
        con.put("CodigoPedido", NoPedido);
        long rowInserted = database.update(variables_publicas.TABLE_PEDIDOS, con, variables_publicas.PEDIDOS_COLUMN_CodigoPedido + "= '" + CodigoPedido+"'", null);
        if (rowInserted != -1)
            return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerListaPedidos() {
        HashMap<String, String> pedido = null;
        List<HashMap<String, String>> lst = new ArrayList<>();
        String Query = "SELECT * FROM " + variables_publicas.TABLE_PEDIDOS + ";";
        Cursor c = database.rawQuery(Query, null);
        if (c.moveToFirst()) {
            do {
                pedido = new HashMap<>();
                pedido.put(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdVendedor)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdCliente)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Cod_cv, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Cod_cv)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Tipo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Tipo)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Observacion, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Observacion)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdFormaPago, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdFormaPago)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdSucursal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdSucursal)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Fecha)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Usuario)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IMEI, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IMEI)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Subtotal,c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Subtotal)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Total,c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Total)));
                lst.add(pedido);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public void EliminaPedido(String IdPedido) {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_PEDIDOS + " WHERE" +
                " "+variables_publicas.PEDIDOS_COLUMN_CodigoPedido+" = '" + IdPedido + "' ;");
        Log.d("pedido_eliminado", "Datos eliminados");
    }

    public int ObtenerNuevoCodigoPedido() {

        String selectQuery = "SELECT COUNT(*) as Cantidad FROM " + variables_publicas.TABLE_PEDIDOS;
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

    public HashMap<String, String> ObtenerPedido(String CodigoPedido) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_PEDIDOS + " Where " + variables_publicas.PEDIDOS_COLUMN_CodigoPedido + " = ?", new String[]{CodigoPedido});
        HashMap<String, String> pedido = null;
        if (c.moveToFirst()) {
            do {
                pedido = new HashMap<>();
                pedido.put(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdVendedor, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdVendedor)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdCliente, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdCliente)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Cod_cv, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Cod_cv)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Tipo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Tipo)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Observacion, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Observacion)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdFormaPago, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdFormaPago)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IdSucursal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdSucursal)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Fecha, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Fecha)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Usuario, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Usuario)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_IMEI, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IMEI)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Subtotal,c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Subtotal)));
                pedido.put(variables_publicas.PEDIDOS_COLUMN_Total,c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Total)));
            } while (c.moveToNext());
        }
        c.close();
        return pedido;
    }

    public Pedido GetPedido(String CodigoPedido) {

        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_PEDIDOS + " Where " + variables_publicas.PEDIDOS_COLUMN_CodigoPedido + " = ?", new String[]{CodigoPedido});
        Pedido pedido = null;
        if (c.moveToFirst()) {
            do {
                pedido = new Pedido();
                pedido.setCodigoPedido(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
                pedido.setIdVendedor(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdVendedor)));
                pedido.setIdCliente(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdCliente)));
                pedido.setCod_cv(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Cod_cv)));
                pedido.setTipo(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Tipo)));
                pedido.setObservacion(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Observacion)));
                pedido.setIdFormaPago(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdFormaPago)));
                pedido.setIdSucursal(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdSucursal)));
                pedido.setFecha(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Fecha)));
                pedido.setUsuario(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Usuario)));
                pedido.setIMEI(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IMEI)));
                pedido.setSubtotal(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Subtotal)));
                pedido.setTotal(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Total)));
            } while (c.moveToNext());
        }
        c.close();
        return pedido;
    }

    public ArrayList<HashMap<String, String>> ObtenerPedidosLocales(String Fecha, String Nombre) {

        String selectQuery = "SELECT P.*,DATE(P.Fecha) as Fecha,'' as Factura,'NO ENVIADO' as Estado,Cl.Nombre as NombreCliente,Fp.NOMBRE as FormaPago FROM " + variables_publicas.TABLE_PEDIDOS +
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
    public Pedido BuscarPedidosSinconizar( ) {
        Pedido pedidos = null;
        String selectQuery="SELECT * FROM " + variables_publicas.TABLE_PEDIDOS+"";
        Cursor c= database.rawQuery(selectQuery , null);
        if (c.moveToFirst()) {
            do {
                pedidos = (new Pedido(c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdVendedor)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdCliente)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Cod_cv)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Tipo)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Observacion)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdFormaPago)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IdSucursal)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Fecha)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Usuario)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_IMEI)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Subtotal)),
                        c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_COLUMN_Total))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return pedidos;
    }

}