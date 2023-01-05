package com.saf.sistemas.safasuncion.AccesoDatos;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;

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
                                     String CodUM,
                                     String Unidades,
                                     String Costo,
                                     String Precio,
                                     String TipoPrecio,
                                     String PorcentajeIva,
                                     String Iva,
                                     String Um,
                                     String Subtotal,
                                     String Total,
                                     String Bodega
    ) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido, CodigoPedido);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo, CodigoArticulo);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion, Descripcion);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, Cantidad);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, BonificaA);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt, TipoArt);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento, PorDescuento);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, Descuento);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM, CodUM);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades, Unidades);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, Costo);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, Precio);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio, TipoPrecio);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, PorcentajeIva);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, Iva);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, Um);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, Subtotal);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, Total);
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega, Bodega);
        database.insert(variables_publicas.TABLE_PEDIDOS_DETALLE, null, contentValues);
    }


    public boolean GuardarDetallePedido(HashMap<String,String> lstArticulos) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total));
        contentValues.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega, lstArticulos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega));
        long rowInserted=database.insert(variables_publicas.TABLE_PEDIDOS_DETALLE, null, contentValues);
        if(rowInserted != -1)
           return true;
        else return false;
    }

    @SuppressLint("Range")
    public List<HashMap<String, String>> ObtenerPedidoDetalle(String CodigoPedido) {
        List<HashMap<String,String>> lst= new ArrayList<>();
        String sqlQuery ="SELECT  * FROM " + variables_publicas.TABLE_PEDIDOS_DETALLE + " WHERE " + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " = '" + CodigoPedido + "'";
        Cursor c = database.rawQuery(sqlQuery,null);
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
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> ObtenerPedidoDetalleArrayList(String CodigoPedido) {
        ArrayList<HashMap<String,String>> lst= new ArrayList<>();
        String sqlQuery="SELECT  * FROM " + variables_publicas.TABLE_PEDIDOS_DETALLE + " WHERE " + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " = '" + CodigoPedido + "'";
        Cursor c = database.rawQuery(sqlQuery,null);
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
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Um)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total)));
                detalle.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega, c.getString(c.getColumnIndex(variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega)));
                lst.add(detalle);
            } while (c.moveToNext());
        }
        c.close();
        return lst;
    }

    public boolean EliminarDetallePedido(String CodigoPedido) {
        int rowsAffected =database.delete( variables_publicas.TABLE_PEDIDOS_DETALLE, variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido+ "='" +CodigoPedido+"'",null) ;
        if(rowsAffected != -1)
            return true;
        else return false;
    }

    public boolean ActualizarCodigoPedido(String CodigoPedido, String NoPedido){
        ContentValues con = new ContentValues();
        con.put("CodigoPedido", NoPedido);
        long rowInserted= database.update(variables_publicas.TABLE_PEDIDOS_DETALLE, con, variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido +"='"+CodigoPedido+"'", null );
        if(rowInserted != -1)
            return true;
        else return false;
    }

}