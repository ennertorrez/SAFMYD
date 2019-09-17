package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

public class PromoUnicaVezHelper {

    //    ClientesOpenHelper openHelper;
    private SQLiteDatabase database;

    public PromoUnicaVezHelper(SQLiteDatabase db) {
        database = db;
    }

    public void GuardarPromoUnicaVez(String Cliente,
                                      String Item,
                                      String CodCV
    ) {
        long rows = 0;
        ContentValues contentValues = new ContentValues();

        contentValues.put(variables_publicas.PROMO_PROMO_UNICA_VEZ_COLUMN_Cliente, Cliente);
        contentValues.put(variables_publicas.PROMO_PROMO_UNICA_VEZ_COLUMN_Item, Item);
        contentValues.put(variables_publicas.PROMO_PROMO_UNICA_VEZ_COLUMN_CodCV, CodCV);

        database.insert(variables_publicas.TABLE_PROMO_UNICA_VEZ, null, contentValues);
    }

    public void EliminaPromoUnicaVez() {
        database.execSQL("DELETE FROM " + variables_publicas.TABLE_PROMO_UNICA_VEZ + ";");
        Log.d("promounicavez_elimina", "Datos eliminados");
    }
}
