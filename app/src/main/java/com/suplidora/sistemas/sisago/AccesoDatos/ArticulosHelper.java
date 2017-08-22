package com.suplidora.sistemas.sisago.AccesoDatos;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Articulo;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;

import java.util.ArrayList;
import java.util.HashMap;

public class ArticulosHelper {

    private SQLiteDatabase database;

    public ArticulosHelper(SQLiteDatabase db){
        database = db;
    }
    public void GuardarTotalArticulos(String Codigo, String Nombre,
                                      String COSTO,String UNIDAD,String UnidadCaja,String ISC,String PorIVA,String PrecioSuper,
                                   String PrecioDetalle, String PrecioForaneo,String PrecioForaneo2, String PrecioMayorista,
                                      String Bonificable,String AplicaPrecioDetalle,String DESCUENTO_MAXIMO,String detallista
                                      ) {
        long rows =0;
        ContentValues contentValues = new ContentValues();
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Costo , COSTO);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Unidad , UNIDAD);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja , UnidadCaja);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_Isc , ISC);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_PorIva , PorIVA);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, PrecioSuper);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, PrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, PrecioForaneo);
        contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo2, PrecioForaneo2);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, PrecioMayorista);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Bonificable, Bonificable);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, AplicaPrecioDetalle);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, DESCUENTO_MAXIMO);
         contentValues.put(variables_publicas.ARTICULO_COLUMN_Detallista, detallista);

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
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Isc)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PorIva)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioSuper)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioDetalle)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioForaneo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioForaneo2)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_PrecioMayorista)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Bonificable)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo)),
                        c.getString(c.getColumnIndex(variables_publicas.ARTICULO_COLUMN_Detallista))
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
                articulo.put(variables_publicas.ARTICULO_COLUMN_Isc, c.getString(c.getColumnIndex("Isc")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, c.getString(c.getColumnIndex("PrecioSuper")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, c.getString(c.getColumnIndex("PrecioDetalle")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, c.getString(c.getColumnIndex("PrecioForaneo")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo2, c.getString(c.getColumnIndex("PrecioForaneo2")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, c.getString(c.getColumnIndex("PrecioMayorista")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Bonificable, c.getString(c.getColumnIndex("Bonificable")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, c.getString(c.getColumnIndex("AplicaPrecioDetalle")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulo.put(variables_publicas.ARTICULO_COLUMN_Detallista, c.getString(c.getColumnIndex("Detallista")));

            } while (c.moveToNext());
        }
        c.close();
        return articulo;
    }
    public ArrayList<HashMap<String, String>> BuscarArticuloCodigo(String Busqueda) {
        Cursor c= database.rawQuery("select * from " + variables_publicas.TABLE_ARTICULOS+" where "+variables_publicas.ARTICULO_COLUMN_Codigo+" like '%"+Busqueda+"'", null);
        ArrayList<HashMap<String, String>> lst= new ArrayList<HashMap<String, String>> () ;

        if(c.moveToFirst()){
            do{
                HashMap<String,String> articulos= new HashMap<>();
                articulos.put(variables_publicas.ARTICULO_COLUMN_Codigo, c.getString(c.getColumnIndex("Codigo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Nombre, c.getString(c.getColumnIndex("Nombre")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Costo, c.getString(c.getColumnIndex("Costo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Unidad, c.getString(c.getColumnIndex("Unidad")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_UnidadCaja, c.getString(c.getColumnIndex("UnidadCaja")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Isc, c.getString(c.getColumnIndex("Isc")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, c.getString(c.getColumnIndex("PrecioSuper")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, c.getString(c.getColumnIndex("PrecioDetalle")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, c.getString(c.getColumnIndex("PrecioForaneo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo2, c.getString(c.getColumnIndex("PrecioForaneo2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, c.getString(c.getColumnIndex("PrecioMayorista")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Bonificable, c.getString(c.getColumnIndex("Bonificable")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, c.getString(c.getColumnIndex("AplicaPrecioDetalle")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Detallista, c.getString(c.getColumnIndex("Detallista")));
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
                articulos.put(variables_publicas.ARTICULO_COLUMN_Isc, c.getString(c.getColumnIndex("Isc")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PorIva, c.getString(c.getColumnIndex("PorIva")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioSuper, c.getString(c.getColumnIndex("PrecioSuper")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, c.getString(c.getColumnIndex("PrecioDetalle")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, c.getString(c.getColumnIndex("PrecioForaneo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioForaneo2, c.getString(c.getColumnIndex("PrecioForaneo2")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, c.getString(c.getColumnIndex("PrecioMayorista")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Bonificable, c.getString(c.getColumnIndex("Bonificable")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle, c.getString(c.getColumnIndex("AplicaPrecioDetalle")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_DescuentoMaximo, c.getString(c.getColumnIndex("DescuentoMaximo")));
                articulos.put(variables_publicas.ARTICULO_COLUMN_Detallista, c.getString(c.getColumnIndex("Detallista")));
                lst.add(articulos);

            }while (c.moveToNext());
        }
        return  lst;
    }
    public  void EliminaArticulos() {
        database.execSQL("DELETE FROM "+variables_publicas.TABLE_ARTICULOS+";");
        Log.d("Articulo_elimina", "Datos eliminados");
    }
}