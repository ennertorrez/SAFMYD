package com.suplidora.sistemas.AccesoDatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.Auxiliar.variables_publicas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PedidosDetalleHelper {


    private SQLiteDatabase database;

    public PedidosDetalleHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarDetallePedido(String CodigoPedido, String CodigoArticulo,
                                     String Descripcion,
                                     String Cantidad,
                                     String BonificaA,
                                     String TipoArt,
                                     String PorDescuento,
                                     String Descuento,
                                     String Isc,
                                     String Costo,
                                     String Precio,
                                     String PorcentajeIva,
                                     String Iva,
                                     String Um,
                                     String Subtotal,
                                     String Total
    ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido, CodigoPedido);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo, CodigoArticulo);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion, Descripcion);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, Cantidad);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, BonificaA);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt, TipoArt);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento, PorDescuento);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, Descuento);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc, Isc);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, Costo);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, Precio);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, PorcentajeIva);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, Iva);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, Um);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, Subtotal);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, Total);
        database.insert(variables_publicas.TABLE_PEDIDOS_DETALLE, null, contentValues);
    }


    public boolean GuardarDetallePedido(HashMap<String,String> lstArticulos) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total));
        long rowInserted=database.insert(variables_publicas.TABLE_PEDIDOS_DETALLE, null, contentValues);
        if(rowInserted != -1)
           return true;
        else return false;
    }

    public List<HashMap<String, String>> ObtenerDetallePedido(String CodigoPedido) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        Cursor c = database.rawQuery("select * from " + variables_publicas.TABLE_PEDIDOS_DETALLE + " Where " + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " = ? COLLATE NOCASE", new String[]{CodigoPedido});
        if (c.moveToFirst()) {
            do {
                HashMap<String, String> detalle = new HashMap<>();
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public void LimpiarPedidosDetalle() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_PEDIDOS_DETALLE + ";");
        Log.d("pedidos detalle_elimina", "Datos eliminados");
    }
    public void EliminarDetallePedido(String CodigoPedido) {
        database.rawQuery("DELETE FROM " + variables_publicas.TABLE_PEDIDOS_DETALLE + " where "+variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido+ " = ? ",new String[]{CodigoPedido});
        Log.d("Det. pedido eliminado: "+CodigoPedido, "Datos eliminados");
    }



}