package com.safi_d.sistemas.safiapp.AccesoDatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;

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
                    + variables_publicas.CLIENTES_COLUMN_Nombre + " TEXT , "
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
                    + variables_publicas.CLIENTES_COLUMN_NombreRuta + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Frecuencia + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_PrecioEspecial + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra + " TEXT,  "
                    + variables_publicas.CLIENTES_COLUMN_Tipo + " TEXT ,  "
                    + variables_publicas.CLIENTES_COLUMN_TipoPrecio + " TEXT,  "
                    + variables_publicas.CLIENTES_COLUMN_Descuento + " TEXT ,  "
                    + variables_publicas.CLIENTES_COLUMN_Empleado + " TEXT ,"
                    + variables_publicas.CLIENTES_COLUMN_IdSupervisor + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Empresa + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Cod_Zona + " TEXT , "
                    + variables_publicas.CLIENTES_COLUMN_Cod_SubZona + " TEXT ,"
                    + variables_publicas.CLIENTES_COLUMN_Pais_Id + " TEXT,"
                    + variables_publicas.CLIENTES_COLUMN_Pais_Nombre + " TEXT, "
                    + variables_publicas.CLIENTES_COLUMN_IdTipoNegocio + " TEXT, "
                    + variables_publicas.CLIENTES_COLUMN_TipoNegocio + " TEXT)");

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
                    + variables_publicas.USUARIOS_COLUMN_FechaActualiza + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_EsVendedor + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_Empresa_ID + " TEXT, "
                    + variables_publicas.USUARIOS_COLUMN_AddCliente + " TEXT )");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_ARTICULOS + "( "

                    + variables_publicas.ARTICULO_COLUMN_Codigo + " TEXT , "
                    + variables_publicas.ARTICULO_COLUMN_Nombre + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Costo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Unidad + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCaja + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Precio + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Precio2 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Precio3 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Precio4 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_CodUM+ " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_PorIva + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_DescuentoMaximo + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Existencia + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta2 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_UnidadCajaVenta3 + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_IdProveedor + " TEXT, "
                    + variables_publicas.ARTICULO_COLUMN_Escala + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_VENDEDORES + "( "
                    + variables_publicas.VENDEDORES_COLUMN_CODIGO + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_NOMBRE + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_IDRUTA + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_RUTA + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_EMPRESA + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_codsuper + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_Supervisor + " TEXT , "
                    + variables_publicas.VENDEDORES_COLUMN_Status + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CLIENTES_SUCURSALES + "( "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodSuc + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_CodCliente + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Sucursal + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Ciudad + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_DeptoID + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Direccion + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_Descuento + " TEXT , "
                    + variables_publicas.CLIENTES_SUCURSALES_COLUMN_FormaPagoID + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_FORMA_PAGO + "( "
                    + variables_publicas.FORMA_PAGO_COLUMN_CODIGO + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_NOMBRE + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_DIAS + " TEXT , "
                    + variables_publicas.FORMA_PAGO_COLUMN_EMPRESA + " TEXT ) ");


            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_ZONAS + "( "
                    + variables_publicas.ZONAS_COLUMN_EMPRESA + " TEXT , "
                    + variables_publicas.ZONAS_COLUMN_CODZONA + " TEXT , "
                    + variables_publicas.ZONAS_COLUMN_ZONA + " TEXT , "
                    + variables_publicas.ZONAS_COLUMN_CODSUBZONA + " TEXT , "
                    + variables_publicas.ZONAS_COLUMN_SUBZONA + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PRECIOS + "( "
                    + variables_publicas.PRECIOS_COLUMN_EMPRESA + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_CODIGO + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_COD_TIPO_PRECIO + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_TIPO_PRECIO + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_COD_UM + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_UM + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_UNIDADES + " TEXT , "
                    + variables_publicas.PRECIOS_COLUMN_MONTO + " TEXT ) ");

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
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_activo + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMV + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_codUMB + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesV + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_unidadesB + " TEXT , "
                    + variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_umB + " TEXT) ");

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
                    + variables_publicas.PEDIDOS_COLUMN_Tipo + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Observacion + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdFormaPago + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IdSucursal + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Fecha + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Usuario + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_IMEI + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Subtotal + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_Total + " TEXT , "
                    + variables_publicas.PEDIDOS_COLUMN_TCambio + " TEXT, "
                    + variables_publicas.PEDIDOS_COLUMN_Empresa + " TEXT ) ");


            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PEDIDOS_DETALLE + " ("
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido + " TEXT, "
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_CodUM + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Unidades + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Costo + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_PorcentajeIva + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Um + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Total + " TEXT ,"
                    + variables_publicas.PEDIDOS_DETALLE_COLUMN_Bodega + " TEXT )");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_DPTOMUNIBARRIOS + "( "
                    + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento + " TEXT , "
                    + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento + " TEXT , "
                    + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio + " TEXT , "
                    + variables_publicas.DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_INFORMES + "( "
                    + variables_publicas.INFORMES_COLUMN_CodInforme + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_Fecha + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_IdVendedor + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_Aprobada + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_Anulada + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_Imei + " TEXT , "
                    + variables_publicas.INFORMES_COLUMN_Usuario + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_DETALLE_INFORMES + "( "
                    + variables_publicas.DETALLEINFORMES_COLUMN_CodInforme + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Recibo + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Idvendedor + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_IdCliente + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Factura + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Saldo + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Monto + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Abono + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_NoCheque + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_BancoE + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_BancoR + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_FechaCK + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_FechaDep + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Efectivo + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Moneda + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Aprobado + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Posfechado + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Procesado + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Usuario + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Vendedor + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Cliente + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_CodigoLetra + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_CantLetra + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Observacion + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_Concepto + " TEXT , "
                    + variables_publicas.DETALLEINFORMES_COLUMN_DepPendiente + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_FACTURAS_PENDIENTES + "( "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_codvendedor + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Cliente + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_CodigoCliente + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Fecha + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_IVA + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Tipo + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_SubTotal + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Descuento + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Total + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Abono + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Saldo + " TEXT , "
                    + variables_publicas.FACTURAS_PENDIENTES_COLUMN_Guardada + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_BANCOS + "( "
                    + variables_publicas.BANCOS_COLUMN_codigo + " TEXT , "
                    + variables_publicas.BANCOS_COLUMN_nombre + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_TPRECIOS + "( "
                    + variables_publicas.TPRECIOS_COLUMN_COD_TIPO_PRECIO + " TEXT , "
                    + variables_publicas.TPRECIOS_COLUMN_TIPO_PRECIO + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_RUTAS + "( "
                    + variables_publicas.RUTA_COLUMN_idRuta + " TEXT , "
                    + variables_publicas.RUTA_COLUMN_ruta + " TEXT , "
                    + variables_publicas.RUTA_COLUMN_vendedor + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_PROMOCIONES + "( "
                    + variables_publicas.PROMOCIONES_COLUMN_codPromo + " TEXT , "
                    + variables_publicas.PROMOCIONES_COLUMN_itemV + " TEXT , "
                    + variables_publicas.PROMOCIONES_COLUMN_cantV + " TEXT , "
                    + variables_publicas.PROMOCIONES_COLUMN_itemB + " TEXT , "
                    + variables_publicas.PROMOCIONES_COLUMN_cantB + " TEXT ) ");


            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_CATEGORIAS + "( "
                    + variables_publicas.CATEGORIAS_COLUMN_Cod_Cat + " TEXT , "
                    + variables_publicas.CATEGORIAS_COLUMN_Categoria + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_SERIE_RECIBOS + "( "
                    + variables_publicas.SERIERECIBOS_COLUMN_IdSerie + " TEXT , "
                    + variables_publicas.SERIERECIBOS_COLUMN_CodVendedor + " TEXT , "
                    + variables_publicas.SERIERECIBOS_COLUMN_nInicial + " TEXT , "
                    + variables_publicas.SERIERECIBOS_COLUMN_nFinal + " TEXT , "
                    + variables_publicas.SERIERECIBOS_COLUMN_Numero + " TEXT ) ");

            db.execSQL("CREATE TABLE " + variables_publicas.TABLE_ESCALAPRECIOS + "( "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_CODESCALA + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_LISTAARTICULOS + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA1 + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA2 + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_ESCALA3 + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO1 + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO2 + " TEXT , "
                    + variables_publicas.ESCALAPRECIOS_COLUMN_PRECIO3 + " TEXT ) ");
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
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_ZONAS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PRECIOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CARTILLAS_BC);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DETALLE_CARTILLAS_BC);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CONFIGURACION_SISTEMA);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PEDIDOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PEDIDOS_DETALLE);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DPTOMUNIBARRIOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_INFORMES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_DETALLE_INFORMES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_FACTURAS_PENDIENTES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_BANCOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_TPRECIOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_RUTAS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_PROMOCIONES);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_CATEGORIAS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_SERIE_RECIBOS);
            db.execSQL("DROP TABLE IF EXISTS " + variables_publicas.TABLE_ESCALAPRECIOS);
            onCreate(db);
        }
    }
}