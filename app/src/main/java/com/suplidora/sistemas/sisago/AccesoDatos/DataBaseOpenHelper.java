package com.suplidora.sistemas.sisago.AccesoDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;

public class DataBaseOpenHelper {


    OpenHelper openHelper;
    public SQLiteDatabase database;

    public DataBaseOpenHelper(Context context) {
        openHelper = new OpenHelper(context);
        database = openHelper.getWritableDatabase();
    }

    private class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context) {
            // TODO Auto-generated constructor stub
            super(context, variables_publicas.DATABASE_NAME, null, variables_publicas.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CLIENTES + "( "
                    + variables_publicas.CLIENTES_COLUMN_IdCliente + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_CodCv + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Nombre + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_NombreCliente + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_FechaCreacion + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Telefono + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Direccion + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdDepartamento + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdMunicipio + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Ciudad + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Ruc + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Cedula + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_LimiteCredito + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdFormaPago + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_IdVendedor + " INTEGER , "
                    + variables_publicas.CLIENTES_COLUMN_Excento + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_CodigoLetra + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Ruta + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Frecuencia + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_PrecioEspecial + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra + " TEXT,  "
                    + variables_publicas.CLIENTES_COLUMN_Tipo + " TEXT ,  "
                    + variables_publicas.CLIENTES_COLUMN_CodigoGalatea + " TEXT,  "
                    + variables_publicas.CLIENTES_COLUMN_Descuento + " TEXT ,  "
                    + variables_publicas.CLIENTES_COLUMN_Empleado + " TEXT ,"
                    + variables_publicas.CLIENTES_COLUMN_Detallista + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_RutaForanea + " TEXT ,"
                    + variables_publicas.CLIENTES_COLUMN_EsClienteVarios + " TEXT )");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_USUARIOS + "( "
                    + variables_publicas.USUARIOS_COLUMN_Codigo + " TEXT , "
                    + variables_publicas.USUARIOS_COLUMN_Nombre + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Usuario + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Contrasenia + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Tipo + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Ruta + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Canal + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_TasaCambio + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_RutaForanea + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_FechaActualiza + " TEXT )");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_ARTICULOS + "( "

                    + variables_publicas.ARTICULO_COLUMN_Codigo + " TEXT , "
                    + variables_publicas.ARTICULO_COLUMN_Nombre + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Costo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Unidad + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCaja + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Isc + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PorIva + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioSuper + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioDetalle + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioForaneo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioForaneo2 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PrecioMayorista + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Bonificable + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_AplicaPrecioDetalle + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_DescuentoMaximo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Detallista + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Existencia + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_IdProveedor + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_VENDEDORES + "( "
                    + variables_publicas.VENDEDORES_COLUMN_CODIGO + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_NOMBRE + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_COD_ZONA + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_RUTA + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_codsuper + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_Supervisor + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_Status + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_detalle + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_horeca + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_mayorista + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_Super + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CLIENTES_SUCURSALES + "( "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodSuc + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodCliente + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Sucursal + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Ciudad + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_DeptoID + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Direccion + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_FormaPagoID + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_FORMA_PAGO + "( "
                    + variables_publicas.FORMA_PAGO_COLUMN_CODIGO + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_NOMBRE + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_DIAS + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_EMPRESA + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CARTILLAS_BC + "( "
                    + variables_publicas.CARTILLAS_BC_COLUMN_id + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_COLUMN_codigo + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_COLUMN_fechaini + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_COLUMN_fechafinal + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_COLUMN_tipo + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_COLUMN_aprobado + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_DETALLE_CARTILLAS_BC + "( "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_id + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionV + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codigo + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_tipo + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_activo + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PRECIO_ESPECIAL + "( "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_Id + " TEXT , "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_CodigoArticulo + " TEXT , "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_IdCliente + " TEXT , "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_Descuento + " TEXT , "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_Precio + " TEXT , "
                    + variables_publicas.PRECIO_ESPECIAL_COLUMN_Facturar + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CONFIGURACION_SISTEMA + "( "
                    + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Id + " TEXT , "
                    + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Sistema + " TEXT , "
                    + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Configuracion + " TEXT , "
                    + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Valor + " TEXT , "
                    + variables_publicas.CONFIGURACION_SISTEMA_COLUMN_Activo + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PEDIDOS + "( "
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " TEXT, "
                    + variables_publicas.PEDIDOS_COLUMN_IdVendedor + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdCliente + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Cod_cv + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Tipo + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Observacion + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdFormaPago + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdSucursal + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Fecha + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Usuario + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IMEI + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Subtotal + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Total + " TEXT ) ");


            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PEDIDOS_DETALLE + " ("
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " TEXT, "
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Isc + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Um + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Total + " TEXT )");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CONSOLIDADO_CARGA + "( "
                    + variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_COLUMN_Vendedor + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_COLUMN_Direccion + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE + "( "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IdVehiculo + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Factura + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_PRECIO + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_TOTAL + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_IVA + " TEXT , "
                    + variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_DESCUENTO + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_DEVOLUCIONES + "( "
                    + variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_cliente + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_horagraba + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_usuario + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_subtotal + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_iva + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_total + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_estado + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_rango + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_motivo + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_factura + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_tipo + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_IMEI + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_COLUMN_IdVehiculo + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_DEVOLUCIONES_DETALLE + "( "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_ndevolucion + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_item + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_cantidad + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_precio + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_iva + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_subtotal + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_total + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_poriva + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_descuento + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_numero + " TEXT , "
                    + variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_factura + " TEXT ) ");



            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_MOTIVOS + "( "
                    + variables_publicas.MOTIVOS_COLUMN_id + " TEXT , "
                    + variables_publicas.MOTIVOS_COLUMN_motivo + " TEXT ) ");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CLIENTES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_USUARIOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_ARTICULOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_VENDEDORES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CLIENTES_SUCURSALES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_FORMA_PAGO);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CARTILLAS_BC);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DETALLE_CARTILLAS_BC);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PRECIO_ESPECIAL);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CONFIGURACION_SISTEMA);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PEDIDOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PEDIDOS_DETALLE);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CONSOLIDADO_CARGA);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CONSOLIDADO_CARGA_DETALLE);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DEVOLUCIONES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DEVOLUCIONES_DETALLE);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_MOTIVOS);
            onCreate(db);
        }
    }
}