package com.safi_d.sistemas.safiapp.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Articulo;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticulosHelper {

    private SQLiteDatabase database;

    public ArticulosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre,
                                      String COSTO,String UNIDAD,String UnidadCaja,String Precio,String Precio2
                                      ,String Precio3,String Precio4,String CodUM,String PorIVA,String DESCUENTO_MAXIMO,
                                      String existencia,String UnidadCajaVenta,String IdProveedor
                                      ) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Costo , COSTO);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Unidad , UNIDAD);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja , UnidadCaja);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Precio , Precio);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Precio2 , Precio2);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Precio3 , Precio3);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Precio4 , Precio4);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_CodUM , CodUM);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_PorIva , PorIVA);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, DESCUENTO_MAXIMO);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Existencia, existencia);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta , UnidadCajaVenta);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_IdProveedor , IdProveedor);

        database.insert(variables_publicas.TABLE_ARTICULOS, null, contentValues);
    }


    public Articulo BuscarArticulo(String Codigo) {
        String selectQuery = "select * from " + variables_publicas.TABLE_ARTICULOS + " where " + variables_publicas.ARTICULO_COLUMN_Codigo + " like '%" + Codigo + "' LIMIT 1";
        Cursor c = database.rawQuery(selectQuery, null);
        Articulo articulo=null;
        if (c.moveToFirst()) {
            do {
                articulo = (new Articulo(c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Codigo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Nombre)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Costo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Unidad)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCaja)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Precio)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Precio2)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Precio3)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Precio4)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_CodUM)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PorIva)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Existencia)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_IdProveedor))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }

    public HashMap<String,String> BuscarArticuloHashMap(String Codigo) {
        String selectQuery = "select * from " + variables_publicas.TABLE_ARTICULOS + " where " + variables_publicas.ARTICULO_COLUMN_Codigo + " like '%" + Codigo + "' LIMIT 1";
        Cursor c = database.rawQuery(selectQuery, null);
        HashMap<String,String> articulo=null;
        if (c.moveToFirst()) {
            do {
                articulo= new HashMap<>();
                articulo.put(variables_publicas.ARTICULO_COLUMN_Codigo, c.getString(c.getColumnIndex("Codigo")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Costo, c.getString(c.getColumnIndex("Costo")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Unidad, c.getString(c.getColumnIndex("Unidad")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja, c.getString(c.getColumnIndex("UnidadCaja")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Precio, c.getString(c.getColumnIndex("Precio")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Precio2, c.getString(c.getColumnIndex("Precio2")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Precio3, c.getString(c.getColumnIndex("Precio3")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Precio4, c.getString(c.getColumnIndex("Precio4")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_CodUM, c.getString(c.getColumnIndex("CodUM")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Existencia, c.getString(c.getColumnIndex("Existencia")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta,c.getString(c.getColumnIndex("UnidadCajaVenta")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_IdProveedor,c.getString(c.getColumnIndex("IdProveedor")));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }
    public ArrayList<HashMap<String, String>> BuscarArticuloCodigo(String Busqueda) {
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS+" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Busqueda+"%'", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> articulos= new HashMap<>();
                articulos.put(variables_publicas.ARTICULO_COLUMN_Codigo, c.getString(c.getColumnIndex("Codigo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Costo, c.getString(c.getColumnIndex("Costo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Unidad, c.getString(c.getColumnIndex("Unidad")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja, c.getString(c.getColumnIndex("UnidadCaja")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio, c.getString(c.getColumnIndex("Precio")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio2, c.getString(c.getColumnIndex("Precio2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio3, c.getString(c.getColumnIndex("Precio3")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio4, c.getString(c.getColumnIndex("Precio4")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_CodUM, c.getString(c.getColumnIndex("CodUM")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Existencia, String.valueOf((int) Double.parseDouble( c.getString(c.getColumnIndex("Existencia")) ) ) );
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta, c.getString(c.getColumnIndex("UnidadCajaVenta")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_IdProveedor, c.getString(c.getColumnIndex("IdProveedor")));
                lst.add(articulos);

            }while (c.moveToNext());
        }
        return  lst;
    }

    public ArrayList<HashMap<String, String>> BuscarArticuloNombre(String Busqueda) {
        Busqueda= Busqueda.replace(" ","%");
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS+" where "+variables_publicas.ARTICULO_COLUMN_Nombre+" like '%"+Busqueda+"%'", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> articulos= new HashMap<>();
                articulos.put(variables_publicas.ARTICULO_COLUMN_Codigo, c.getString(c.getColumnIndex("Codigo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Costo, c.getString(c.getColumnIndex("Costo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Unidad, c.getString(c.getColumnIndex("Unidad")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja, c.getString(c.getColumnIndex("UnidadCaja")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio, c.getString(c.getColumnIndex("Precio")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio2, c.getString(c.getColumnIndex("Precio2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio3, c.getString(c.getColumnIndex("Precio3")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Precio4, c.getString(c.getColumnIndex("Precio4")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_CodUM, c.getString(c.getColumnIndex("CodUM")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Existencia, String.valueOf((int) Double.parseDouble( c.getString(c.getColumnIndex("Existencia")) ) ) );
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta, c.getString(c.getColumnIndex("UnidadCajaVenta")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_IdProveedor, c.getString(c.getColumnIndex("IdProveedor")));
                lst.add(articulos);

            }while (c.moveToNext());
        }
        return  lst;
    }
    public  void EliminaArticulos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ARTICULOS+";");
        Log.d("Articulo_elimina", "Datos eliminados");
    }

    public  boolean ActualizarExistencias(String CodigoArticulo,String existencia) {
        ContentValues con = new ContentValues();
        con.put(variables_publicas.ARTICULO_COLUMN_Existencia, existencia);
        long rowUpdated = database.update(variables_publicas.TABLE_ARTICULOS, con, variables_publicas.ARTICULO_COLUMN_Codigo + "= '" + CodigoArticulo+"'", null);
        if (rowUpdated != -1)
            return true;
        else return false;
    }
}