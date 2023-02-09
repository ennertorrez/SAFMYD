package com.saf.sistemas.safcafenorteno.AccesoDatos;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Entidades.Articulo;
import com.saf.sistemas.safcafenorteno.Entidades.Existencias;
import com.saf.sistemas.safcafenorteno.Entidades.Model;
import com.saf.sistemas.safcafenorteno.Entidades.Model2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArticulosHelper {

    private SQLiteDatabase database;

    public ArticulosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre,
                                      String COSTO,String UNIDAD,String UnidadCaja,String Precio,String Precio2
            ,String Precio3,String Precio4,String CodUM,String PorIVA,String DESCUENTO_MAXIMO,
                                      String existencia,String UnidadCajaVenta,String UnidadCajaVenta2,String UnidadCajaVenta3,String IdProveedor,String Escala
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
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2 , UnidadCajaVenta2);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3 , UnidadCajaVenta3);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_IdProveedor , IdProveedor);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Escala, Escala);
        database.insert(variables_publicas.TABLE_ARTICULOS, null, contentValues);
    }


    @SuppressLint("Range")
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
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_IdProveedor)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Escala))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }

    @SuppressLint("Range")
    public Articulo BuscarArticuloE(String Codigo) {
        String selectQuery = "select A.Codigo,A.Nombre,A.Costo,A.Unidad,A.UnidadCaja,A.Precio,A.Precio2,A.Precio3,A.Precio4,A.CodUM,A.PorIva,A.DescuentoMaximo,E."+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +" as Existencia,A.UnidadCajaVenta,A.UnidadCajaVenta2,A.UnidadCajaVenta3,A.IdProveedor,A.Escala from " + variables_publicas.TABLE_ARTICULOS + " A INNER JOIN  "+ variables_publicas.TABLE_EXISTENCIAS +" E ON A."+ variables_publicas.ARTICULO_COLUMN_Codigo +" = E."+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +" where A." + variables_publicas.ARTICULO_COLUMN_Codigo + " like '%" + Codigo + "' LIMIT 1";
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
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_IdProveedor)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Escala))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }

    @SuppressLint("Range")
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
                articulo.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2,c.getString(c.getColumnIndex("UnidadCajaVenta2")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3,c.getString(c.getColumnIndex("UnidadCajaVenta3")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_IdProveedor,c.getString(c.getColumnIndex("IdProveedor")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Escala,c.getString(c.getColumnIndex("Escala")));
            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }
    @SuppressLint("Range")
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
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2, c.getString(c.getColumnIndex("UnidadCajaVenta2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3, c.getString(c.getColumnIndex("UnidadCajaVenta3")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_IdProveedor, c.getString(c.getColumnIndex("IdProveedor")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Escala, c.getString(c.getColumnIndex("Escala")));
                lst.add(articulos);

            }while (c.moveToNext());
        }
        return  lst;
    }

    @SuppressLint("Range")
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
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2, c.getString(c.getColumnIndex("UnidadCajaVenta2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3, c.getString(c.getColumnIndex("UnidadCajaVenta3")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_IdProveedor, c.getString(c.getColumnIndex("IdProveedor")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Escala, c.getString(c.getColumnIndex("Escala")));
                lst.add(articulos);

            }while (c.moveToNext());
        }
        return  lst;
    }

    @SuppressLint("Range")
    public List<Model> BuscarArticuloCodigoNew(String Busqueda) {
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS+" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Busqueda+"%'", null);
        List<Model> lst= new ArrayList<Model>();

        if(c.moveToFirst()){
            do{
                lst.add(new Model(c.getString(c.getColumnIndex("Codigo")),c.getString(c.getColumnIndex("Precio")),c.getString(c.getColumnIndex("Nombre"))));
            }while (c.moveToNext());
        }
        return  lst;
    }
    @SuppressLint("Range")
    public List<Model2> BuscarArticuloCodigoNew2(String Busqueda) {
        Cursor c= database.rawQuery("select A.*,E."+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +" AS Existencia2 from " + variables_publicas.TABLE_ARTICULOS+" A INNER JOIN "+ variables_publicas.TABLE_EXISTENCIAS +" E ON E."+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +" = A."+ variables_publicas.ARTICULO_COLUMN_Codigo +" where A."+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Busqueda+"%'", null);
        List<Model2> lst= new ArrayList<Model2>();

        if(c.moveToFirst()){
            do{
                lst.add(new Model2(c.getString(c.getColumnIndex("Codigo")),c.getString(c.getColumnIndex("Precio")),c.getString(c.getColumnIndex("Nombre")),c.getString(c.getColumnIndex("Existencia2"))));
            }while (c.moveToNext());
        }
        return  lst;
    }
    @SuppressLint("Range")
    public List<Model> BuscarArticuloNombreNew(String Busqueda) {
        Busqueda= Busqueda.replace(" ","%");
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS+" where "+variables_publicas.ARTICULO_COLUMN_Nombre+" like '%"+Busqueda+"%'", null);
        List<Model> lst= new ArrayList<Model>();

        if(c.moveToFirst()){
            do{
                lst.add(new Model(c.getString(c.getColumnIndex("Codigo")),c.getString(c.getColumnIndex("Precio")),c.getString(c.getColumnIndex("Nombre"))));
            }while (c.moveToNext());
        }
        return  lst;
    }
    @SuppressLint("Range")
    public List<Model2> BuscarArticuloNombreNew2(String Busqueda) {
        Cursor c= database.rawQuery("select A.*,E."+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +" AS Existencia2 from " + variables_publicas.TABLE_ARTICULOS+" A INNER JOIN "+ variables_publicas.TABLE_EXISTENCIAS +" E ON E."+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +" = A."+ variables_publicas.ARTICULO_COLUMN_Codigo +" where A."+variables_publicas.ARTICULO_COLUMN_Nombre+" like '%"+Busqueda+"%'", null);
        List<Model2> lst= new ArrayList<Model2>();

        if(c.moveToFirst()){
            do{
                lst.add(new Model2(c.getString(c.getColumnIndex("Codigo")),c.getString(c.getColumnIndex("Precio")),c.getString(c.getColumnIndex("Nombre")),c.getString(c.getColumnIndex("Existencia2"))));
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

    public void GuardarTotalExistencias(String Codigo, String Bodega,
                                      String Existencia    ) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
        contentValues.put(variables_publicas.EXISTENCIAS_COLUMN_Codigo, Codigo);
        contentValues.put(variables_publicas.EXISTENCIAS_COLUMN_Bodega, Bodega);
        contentValues.put(variables_publicas.EXISTENCIAS_COLUMN_Existencia , Existencia );

        database.insert(variables_publicas.TABLE_EXISTENCIAS, null, contentValues);
    }

    public  void EliminaExistencias() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_EXISTENCIAS+";");
        Log.d("Existencias_elimina", "Datos eliminados");
    }

    @SuppressLint("Range")
    public  boolean ActualizarExistencias2(String Factura, String estado) {

        Cursor c= database.rawQuery("select "+ variables_publicas.FACTURAS_COLUMN_noFactura +","+ variables_publicas.FACTURAS_LINEAS_COLUMN_item +", printf(\"%.2f\",SUM("+ variables_publicas.FACTURAS_LINEAS_COLUMN_cantidad +")) as Cantidad from " + variables_publicas.TABLE_FACTURAS_LINEAS +" where "+variables_publicas.FACTURAS_COLUMN_noFactura+" like '%"+Factura+"%' GROUP BY "+ variables_publicas.FACTURAS_COLUMN_noFactura +","+ variables_publicas.FACTURAS_LINEAS_COLUMN_item +" ", null);
        ContentValues con = new ContentValues();
        long rowUpdated=-1;
        if(c.moveToFirst()){
            do{
                Cursor c2= database.rawQuery("select "+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +", "+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +" from " + variables_publicas.TABLE_EXISTENCIAS +" where "+variables_publicas.EXISTENCIAS_COLUMN_Codigo+" = '"+  c.getString(c.getColumnIndex(variables_publicas.FACTURAS_LINEAS_COLUMN_item)) +"' ", null);
                if(c2.moveToFirst()){
                    do{
                        double existencia=c2.getDouble(c2.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Existencia));
                        double existenciaactualizada=existencia-c.getDouble(c.getColumnIndex("Cantidad"));
                        con.put(variables_publicas.EXISTENCIAS_COLUMN_Existencia, String.valueOf(existenciaactualizada));
                        rowUpdated = database.update(variables_publicas.TABLE_EXISTENCIAS, con, variables_publicas.EXISTENCIAS_COLUMN_Codigo + "= '" + c2.getString(c2.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Codigo)) +"'", null);
                    }while (c2.moveToNext());
                }


            }while (c.moveToNext());
        }
        if (rowUpdated != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public  boolean ActualizarExistenciasItem(String Item, String CantidadAnterior, String CantidadNueva) {

        ContentValues con = new ContentValues();
        long rowUpdated=-1;

                Cursor c2= database.rawQuery("select "+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +" from " + variables_publicas.TABLE_EXISTENCIAS +" where "+variables_publicas.EXISTENCIAS_COLUMN_Codigo+" = '"+  Item +"' ", null);
                if(c2.moveToFirst()){
                    do{
                        double existencia=c2.getDouble(c2.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Existencia));
                        double existenciaactualizada=existencia + Double.parseDouble(CantidadAnterior) - Double.parseDouble(CantidadNueva);
                        con.put(variables_publicas.EXISTENCIAS_COLUMN_Existencia, String.valueOf(existenciaactualizada));
                        rowUpdated = database.update(variables_publicas.TABLE_EXISTENCIAS, con, variables_publicas.EXISTENCIAS_COLUMN_Codigo + "= '" + Item +"'", null);
                    }while (c2.moveToNext());
                }

        if (rowUpdated != -1)
            return true;
        else return false;
    }

    @SuppressLint("Range")
    public Existencias BuscarExistencia(String Codigo) {
        String selectQuery = "select * from " + variables_publicas.TABLE_EXISTENCIAS + " where " + variables_publicas.EXISTENCIAS_COLUMN_Codigo + " like '%" + Codigo + "' LIMIT 1";
        Cursor c = database.rawQuery(selectQuery, null);
        Existencias existencias=null;
        if (c.moveToFirst()) {
            do {
                existencias = (new Existencias(c.getString(c.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Codigo)),
                        c.getString(c.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Bodega)),
                        c.getString(c.getColumnIndex(variables_publicas.EXISTENCIAS_COLUMN_Existencia))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return existencias;
    }
   // ActualizarExistenciasTotales
   public  void ActualizarExistenciasTotales() {
       database.execSQL("UPDATE "+ variables_publicas.TABLE_ARTICULOS +" SET "+ variables_publicas.ARTICULO_COLUMN_Existencia +"= (SELECT "+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +"  FROM "+ variables_publicas.TABLE_EXISTENCIAS +" WHERE "+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +"="+ variables_publicas.TABLE_ARTICULOS +"."+ variables_publicas.ARTICULO_COLUMN_Codigo +")  WHERE EXISTS (SELECT "+ variables_publicas.EXISTENCIAS_COLUMN_Existencia +"  FROM "+ variables_publicas.TABLE_EXISTENCIAS +" WHERE "+ variables_publicas.EXISTENCIAS_COLUMN_Codigo +"="+ variables_publicas.TABLE_ARTICULOS +"."+ variables_publicas.ARTICULO_COLUMN_Codigo +") ;");
       Log.d("Existencias_actualiza", "Datos actualizados");
   }
}