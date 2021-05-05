package com.safi_d.sistemas.safiapp.Auxiliar;


import com.safi_d.sistemas.safiapp.Entidades.Configuraciones;
import com.safi_d.sistemas.safiapp.Entidades.Informe;
import com.safi_d.sistemas.safiapp.Entidades.Pedido;
import com.safi_d.sistemas.safiapp.Entidades.Usuario;

public class variables_publicas {

    public static Usuario usuario = null;
    public static Configuraciones Configuracion = null;
    public static Pedido Pedidos = null;
    public static Informe Informes = null;
    public static String rutacargada = "0";
    public static String rutacargadadescripcion = "";
    public static String ValorConfigServ = "";
    public static String AplicarPrecioMayoristaXCaja;
    public static String PermitirVentaDetAMayoristaXCaja;
    public static String AplicaIVAGral;
    public static String ValorIVAGral;
    public static String[] lstDepartamentosForaneo1;
    public static String UsuarioLogin = "";
    public static  String NombreVendedor = "";
    public static  String CodigoVendedor = "";
    public static final String KEY_IdVendedor = "vIdVendedor";
    public static final String KEY_NombreVendedor = "vnombreVendedor";
    public static final String KEY_idSerie = "vIdSerie";
    public static final String KEY_ultRecibo = "vUltNumero";
    public static String TipoUsuario = "";
    public static boolean LoginOk = false;
    public static String MensajeLogin = "";
    public static String IdCliente = "";
    public static String RutaCliente = "";
    public static String Canal = "";
    public static String FechaActual = "";
    public static String IMEI;
    public static String MensajeError;
    public static  int CantidadItemPromoGaga;
    public static final String diasventas="Dias";
    public static final String descMeses="DescMeses";
    public static boolean vEditando = false;
    public static String noInforme = "Informe";
    public static String estadoInforme = "Estado";
    public static String CodInforme = "Informe";
    public static String vVisualizar = "vVisualizar";


     public static final String direccionIp = "http://192.168.0.7:8088";
    //public static final String direccionIp = "http://192.168.1.244:8088";
    //public static final String direccionIp = "http://200.62.90.235:8088";
    public static final String correosErrores = "cysnicaragua@gmail.com";
    public  static final String correoError= "cysnicaragua@gmail.com";
    //Variables BD
    public static final int DATABASE_VERSION = 15;
    public static final String DATABASE_NAME = "Safi_D.db";
    //Variables TB
    public static final String TABLE_ARTICULOS = "Articulos";
    public static final String TABLE_CLIENTES = "Cliente";
    public static final String TABLE_PEDIDOS = "Pedidos";
    public static final String TABLE_PEDIDOS_DETALLE = "PedidosDetalle";
    public static final String TABLE_USUARIOS = "Usuarios";
    public static final String TABLE_VENDEDORES = "Vendedor";
    public static final String TABLE_CLIENTES_SUCURSALES = "ClientesSucursales";
    public static final String TABLE_FORMA_PAGO = "FormaPago";
    public static final String TABLE_CARTILLAS_BC = "CartillasBC";
    public static final String TABLE_DETALLE_CARTILLAS_BC = "DetalleCartillasBC";
    public static final String TABLE_CONFIGURACION_SISTEMA = "Configuraciones";
    public static final String TABLE_DPTOMUNIBARRIOS="DptoMuniBarrio";
    public static final String TABLE_INFORMES="Informes";
    public static final String TABLE_DETALLE_INFORMES="DetalleInformes";
    public static final String TABLE_FACTURAS_PENDIENTES="FacturasPendientes";
    public static final String TABLE_BANCOS="Bancos";
    public static final String TABLE_SERIE_RECIBOS="SerieRecibos";
    public static final String TABLE_ZONAS = "Zonas";
    public static final String TABLE_PRECIOS = "Precios";
    public static final String TABLE_TPRECIOS = "TipoPrecio";
    public static final String TABLE_CATEGORIAS = "Categorias";
    public static final String TABLE_RUTAS= "Rutas";
    //Variables CamposTbArticulos
    public static final String ARTICULO_COLUMN_Codigo = "Codigo";
    public static final String ARTICULO_COLUMN_Nombre = "Nombre";
    public static final String ARTICULO_COLUMN_Costo = "Costo";
    public static final String ARTICULO_COLUMN_Unidad = "Unidad";
    public static final String ARTICULO_COLUMN_UnidadCaja = "UnidadCaja";
    public static final String ARTICULO_COLUMN_Precio = "Precio";
    public static final String ARTICULO_COLUMN_Precio2 = "Precio2";
    public static final String ARTICULO_COLUMN_Precio3 = "Precio3";
    public static final String ARTICULO_COLUMN_Precio4 = "Precio4";
    public static final String ARTICULO_COLUMN_CodUM = "CodUM";
    public static final String ARTICULO_COLUMN_PorIva = "PorIva";
    public static final String ARTICULO_COLUMN_DescuentoMaximo = "DescuentoMaximo";
    public static final String ARTICULO_COLUMN_Existencia="Existencia";
    public static final String ARTICULO_COLUMN_UnidadCajaVenta = "UnidadCajaVenta";
    public static final String ARTICULO_COLUMN_UnidadCajaVenta2 = "UnidadCajaVenta2";
    public static final String ARTICULO_COLUMN_UnidadCajaVenta3 = "UnidadCajaVenta3";
    public static final String ARTICULO_COLUMN_IdProveedor = "IdProveedor";

    //Variables CamposTbClientes
    public static final String CLIENTES_COLUMN_IdCliente = "IdCliente";
    public static final String CLIENTES_COLUMN_NombreRuta = "NombreRuta";
    public static final String CLIENTES_COLUMN_Nombre = "Nombre";
    public static final String CLIENTES_COLUMN_TipoPrecio = "TipoPrecio";
    public static final String CLIENTES_COLUMN_FechaCreacion = "FechaCreacion";
    public static final String CLIENTES_COLUMN_Telefono = "Telefono";
    public static final String CLIENTES_COLUMN_Direccion = "Direccion";
    public static final String CLIENTES_COLUMN_IdDepartamento = "IdDepartamento";
    public static final String CLIENTES_COLUMN_IdMunicipio = "IdMunicipio";
    public static final String CLIENTES_COLUMN_Ciudad = "Ciudad";
    public static final String CLIENTES_COLUMN_Ruc = "Ruc";
    public static final String CLIENTES_COLUMN_Cedula = "Cedula";
    public static final String CLIENTES_COLUMN_LimiteCredito = "LimiteCredito";
    public static final String CLIENTES_COLUMN_IdFormaPago = "IdFormaPago";
    public static final String CLIENTES_COLUMN_IdVendedor = "IdVendedor";
    public static final String CLIENTES_COLUMN_Excento = "Excento";
    public static final String CLIENTES_COLUMN_CodigoLetra = "CodigoLetra";
    public static final String CLIENTES_COLUMN_Ruta = "Ruta";
    public static final String CLIENTES_COLUMN_Frecuencia = "Frecuencia";
    public static final String CLIENTES_COLUMN_PrecioEspecial = "PrecioEspecial";
    public static final String CLIENTES_COLUMN_FechaUltimaCompra = "FechaUltimaCompra";
    public static final String CLIENTES_COLUMN_Tipo = "Tipo";
    public static final String CLIENTES_COLUMN_IdSupervisor = "IdSupervisor";
    public static final String CLIENTES_COLUMN_Descuento = "Descuento";
    public static final String CLIENTES_COLUMN_Empleado = "Empleado";
    public static final String CLIENTES_COLUMN_Empresa = "Empresa";
    public static final String CLIENTES_COLUMN_Cod_Zona = "Cod_Zona";
    public static final String CLIENTES_COLUMN_Cod_SubZona="Cod_SubZona";
    public static final String CLIENTES_COLUMN_Pais_Id="Pais_Id";
    public static final String CLIENTES_COLUMN_Pais_Nombre="Pais_Nombre";
    public static final String CLIENTES_COLUMN_IdTipoNegocio="IdTipoNegocio";
    public static final String CLIENTES_COLUMN_TipoNegocio="TipoNegocio";
    public static String VersionSistema = "";
    public static String info = "";
    //Variables CamposTbPedidos
    public static final String PEDIDOS_COLUMN_CodigoPedido = "CodigoPedido";
    public static final String PEDIDOS_COLUMN_IdVendedor = "IdVendedor";
    public static final String PEDIDOS_COLUMN_IdCliente = "IdCliente";
    public static final String PEDIDOS_COLUMN_Tipo = "Tipo";
    public static final String PEDIDOS_COLUMN_Observacion = "Observacion";
    public static final String PEDIDOS_COLUMN_IdFormaPago = "IdFormaPago";
    public static final String PEDIDOS_COLUMN_IdSucursal = "IdSucursal";
    public static final String PEDIDOS_COLUMN_Fecha = "Fecha";
    public static final String PEDIDOS_COLUMN_Usuario = "Usuario";
    public static final String PEDIDOS_COLUMN_IMEI = "IMEI";
    public static final String PEDIDOS_COLUMN_Total = "Total";
    public static final String PEDIDOS_COLUMN_Subtotal = "subtotal";
    public static final String PEDIDOS_COLUMN_TCambio = "TCambio";
    public static final String PEDIDOS_COLUMN_Empresa = "Empresa";

    public static final String PEDIDOS_DETALLE_COLUMN_CodigoPedido = "CodigoPedido";
    public static final String PEDIDOS_DETALLE_COLUMN_CodigoArticulo = "CodigoArticulo";
    public static final String PEDIDOS_DETALLE_COLUMN_Descripcion = "Descripcion";
    public static final String PEDIDOS_DETALLE_COLUMN_Cantidad = "Cantidad";
    public static final String PEDIDOS_DETALLE_COLUMN_BonificaA = "BonificaA";
    public static final String PEDIDOS_DETALLE_COLUMN_TipoArt = "TipoArt";
    public static final String PEDIDOS_DETALLE_COLUMN_Descuento = "Descuento";
    public static final String PEDIDOS_DETALLE_COLUMN_PorDescuento = "PorDescuento";
    public static final String PEDIDOS_DETALLE_COLUMN_CodUM = "CodUM";
    public static final String PEDIDOS_DETALLE_COLUMN_Unidades = "Unidades";
    public static final String PEDIDOS_DETALLE_COLUMN_Costo = "Costo";
    public static final String PEDIDOS_DETALLE_COLUMN_Precio = "Precio";
    public static final String PEDIDOS_DETALLE_COLUMN_TipoPrecio = "TipoPrecio";
    public static final String PEDIDOS_DETALLE_COLUMN_PorcentajeIva = "PorcentajeIva";
    public static final String PEDIDOS_DETALLE_COLUMN_Iva = "Iva";
    public static final String PEDIDOS_DETALLE_COLUMN_Um = "Um";
    public static final String PEDIDOS_DETALLE_COLUMN_Subtotal = "SubTotal";
    public static final String PEDIDOS_DETALLE_COLUMN_Total = "Total";
    public static final String PEDIDOS_DETALLE_COLUMN_Bodega = "Bodega";

    //Variables CamposUsuario
    public static final String USUARIOS_COLUMN_Codigo = "Codigo";
    public static final String USUARIOS_COLUMN_Nombre = "Nombre";
    public static final String USUARIOS_COLUMN_Usuario = "Usuario";
    public static final String USUARIOS_COLUMN_Contrasenia = "Contrasenia";
    public static final String USUARIOS_COLUMN_Tipo = "Tipo";
    public static final String USUARIOS_COLUMN_Ruta = "Ruta";
    public static final String USUARIOS_COLUMN_Canal = "Canal";
    public static final String USUARIOS_COLUMN_TasaCambio = "TasaCambio";
    public static final String USUARIOS_COLUMN_RutaForanea = "RutaForanea";
    public static final String USUARIOS_COLUMN_FechaActualiza = "FechaActualiza";
    public static final String USUARIOS_COLUMN_EsVendedor = "EsVendedor";
    public static final String USUARIOS_COLUMN_Empresa_ID = "Empresa_ID";

    public static final String VENDEDORES_COLUMN_CODIGO = "CODIGO";
    public static final String VENDEDORES_COLUMN_NOMBRE = "NOMBRE";
    public static final String VENDEDORES_COLUMN_IDRUTA = "IDRUTA";
    public static final String VENDEDORES_COLUMN_RUTA = "RUTA";
    public static final String VENDEDORES_COLUMN_EMPRESA = "EMPRESA";
    public static final String VENDEDORES_COLUMN_codsuper = "codsuper";
    public static final String VENDEDORES_COLUMN_Supervisor = "Supervisor";
    public static final String VENDEDORES_COLUMN_Status = "Status";

    public static final String CLIENTES_SUCURSALES_COLUMN_CodSuc = "CodSuc";
    public static final String CLIENTES_SUCURSALES_COLUMN_CodCliente = "CodCliente";
    public static final String CLIENTES_SUCURSALES_COLUMN_Sucursal = "Sucursal";
    public static final String CLIENTES_SUCURSALES_COLUMN_Ciudad = "Ciudad";
    public static final String CLIENTES_SUCURSALES_COLUMN_DeptoID = "DeptoID";
    public static final String CLIENTES_SUCURSALES_COLUMN_Direccion = "Direccion";
    public static final String CLIENTES_SUCURSALES_COLUMN_FormaPagoID = "FormaPagoID";
    public static final String CLIENTES_SUCURSALES_COLUMN_Descuento = "Descuento";

    public static final String FORMA_PAGO_COLUMN_CODIGO = "CODIGO";
    public static final String FORMA_PAGO_COLUMN_NOMBRE = "NOMBRE";
    public static final String FORMA_PAGO_COLUMN_DIAS = "DIAS";
    public static final String FORMA_PAGO_COLUMN_EMPRESA = "EMPRESA";

    public static final String ZONAS_COLUMN_EMPRESA = "Empresa";
    public static final String ZONAS_COLUMN_CODZONA = "Cod_Zona";
    public static final String ZONAS_COLUMN_ZONA= "Zona";
    public static final String ZONAS_COLUMN_CODSUBZONA = "Cod_SubZona";
    public static final String ZONAS_COLUMN_SUBZONA = "SubZona";

    public static final String CARTILLAS_BC_COLUMN_id = "id";
    public static final String CARTILLAS_BC_COLUMN_codigo = "codigo";
    public static final String CARTILLAS_BC_COLUMN_fechaini = "fechaini";
    public static final String CARTILLAS_BC_COLUMN_fechafinal = "fechafinal";
    public static final String CARTILLAS_BC_COLUMN_tipo = "tipo";
    public static final String CARTILLAS_BC_COLUMN_aprobado = "aprobado";

    public static final String CARTILLAS_BC_DETALLE_COLUMN_id = "id";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_itemV = "itemV";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_descripcionV = "descripcionV";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_cantidad = "cantidad";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_itemB = "itemB";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_descripcionB = "descripcionB";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_cantidadB = "cantidadB";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_codigo = "codigo";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_tipo = "tipo";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_activo = "activo";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_codUMV = "codUMV";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_codUMB = "codUMB";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_unidadesV = "unidadesV";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_unidadesB = "unidadesB";
    public static final String CARTILLAS_BC_DETALLE_COLUMN_umB = "umB";

    public static final String CONFIGURACION_SISTEMA_COLUMN_Id = "Id";
    public static final String CONFIGURACION_SISTEMA_COLUMN_Sistema = "Sistema";
    public static final String CONFIGURACION_SISTEMA_COLUMN_Configuracion = "Configuracion";
    public static final String CONFIGURACION_SISTEMA_COLUMN_Valor = "Valor";
    public static final String CONFIGURACION_SISTEMA_COLUMN_Activo = "Activo";

    //Variables CamposTbDptoMuniBarrios
    public static final String DPTOMUNIBARRIOS_COLUMN_Codigo_Departamento = "Codigo_Departamento";
    public static final String DPTOMUNIBARRIOS_COLUMN_Nombre_Departamento ="Nombre_Departamento";
    public static final String DPTOMUNIBARRIOS_COLUMN_Codigo_Municipio = "Codigo_Municipio";
    public static final String DPTOMUNIBARRIOS_COLUMN_Nombre_Municipio ="Nombre_Municipio";

    public static  final String INFORMES_COLUMN_CodInforme = "CodInforme";
    public static  final String INFORMES_COLUMN_Fecha = "Fecha";
    public static  final String INFORMES_COLUMN_IdVendedor = "IdVendedor";
    public static  final String INFORMES_COLUMN_Aprobada = "Aprobada";
    public static  final String INFORMES_COLUMN_Anulada = "Anulada";
    public static  final String INFORMES_COLUMN_Imei = "Imei";
    public static  final String INFORMES_COLUMN_Usuario = "Usuario";

    public static  final String DETALLEINFORMES_COLUMN_CodInforme = "CodInforme";
    public static  final String DETALLEINFORMES_COLUMN_Recibo = "Recibo";
    public static  final String DETALLEINFORMES_COLUMN_Idvendedor = "Idvendedor";
    public static  final String DETALLEINFORMES_COLUMN_IdCliente = "IdCliente";
    public static  final String DETALLEINFORMES_COLUMN_Factura = "Factura";
    public static  final String DETALLEINFORMES_COLUMN_Saldo = "Saldo";
    public static  final String DETALLEINFORMES_COLUMN_Monto = "Monto";
    public static  final String DETALLEINFORMES_COLUMN_Abono = "Abono";
    public static  final String DETALLEINFORMES_COLUMN_NoCheque = "NoCheque";
    public static  final String DETALLEINFORMES_COLUMN_BancoE = "BancoE";
    public static  final String DETALLEINFORMES_COLUMN_BancoR = "BancoR";
    public static  final String DETALLEINFORMES_COLUMN_FechaCK = "FechaCK";
    public static  final String DETALLEINFORMES_COLUMN_FechaDep = "FechaDep";
    public static  final String DETALLEINFORMES_COLUMN_Efectivo = "Efectivo";
    public static  final String DETALLEINFORMES_COLUMN_Moneda = "Moneda";
    public static  final String DETALLEINFORMES_COLUMN_Aprobado = "Aprobado";
    public static  final String DETALLEINFORMES_COLUMN_Posfechado = "Posfechado";
    public static  final String DETALLEINFORMES_COLUMN_Procesado = "Procesado";
    public static  final String DETALLEINFORMES_COLUMN_Usuario = "Usuario";
    public static  final String DETALLEINFORMES_COLUMN_Vendedor = "Vendedor";
    public static  final String DETALLEINFORMES_COLUMN_Cliente = "Cliente";
    public static  final String DETALLEINFORMES_COLUMN_CodigoLetra = "CodigoLetra";
    public static  final String DETALLEINFORMES_COLUMN_CantLetra = "CantLetra";
    public static  final String DETALLEINFORMES_COLUMN_Observacion = "Observacion";
    public static  final String DETALLEINFORMES_COLUMN_Concepto = "Concepto";
    public static  final String DETALLEINFORMES_COLUMN_DepPendiente = "DepPendiente";

    public static  final String FACTURAS_PENDIENTES_COLUMN_codvendedor = "codvendedor";
    public static  final String FACTURAS_PENDIENTES_COLUMN_No_Factura = "No_Factura";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Cliente = "Cliente";
    public static  final String FACTURAS_PENDIENTES_COLUMN_CodigoCliente = "CodigoCliente";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Fecha = "Fecha";
    public static  final String FACTURAS_PENDIENTES_COLUMN_IVA = "IVA";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Tipo = "Tipo";
    public static  final String FACTURAS_PENDIENTES_COLUMN_SubTotal = "SubTotal";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Descuento = "Descuento";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Total = "Total";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Abono = "Abono";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Saldo = "Saldo";
    public static  final String FACTURAS_PENDIENTES_COLUMN_Guardada = "Guardada";

    public static  final String BANCOS_COLUMN_codigo = "Codigo";
    public static final String  BANCOS_COLUMN_nombre ="Nombre";

    public static  final String RUTA_COLUMN_idRuta = "IDRUTA";
    public static final String  RUTA_COLUMN_ruta ="RUTA";
    public static final String  RUTA_COLUMN_vendedor ="VENDEDOR";

    public static  final String CATEGORIAS_COLUMN_Cod_Cat = "Cod_Cat";
    public static final String  CATEGORIAS_COLUMN_Categoria ="Categoria";

    public static final String  SERIERECIBOS_COLUMN_IdSerie ="IdSerie";
    public static final String  SERIERECIBOS_COLUMN_CodVendedor ="CodVendedor";
    public static final String  SERIERECIBOS_COLUMN_nInicial ="nInicial";
    public static final String  SERIERECIBOS_COLUMN_nFinal ="nFinal";
    public static final String  SERIERECIBOS_COLUMN_Numero ="Numero";

    public static final String PRECIOS_COLUMN_EMPRESA = "EMPRESA";
    public static final String PRECIOS_COLUMN_CODIGO = "CODIGO";
    public static final String PRECIOS_COLUMN_COD_TIPO_PRECIO = "COD_TIPO_PRECIO";
    public static final String PRECIOS_COLUMN_TIPO_PRECIO = "TIPO_PRECIO";
    public static final String PRECIOS_COLUMN_COD_UM = "COD_UM";
    public static final String PRECIOS_COLUMN_UM = "UM";
    public static final String PRECIOS_COLUMN_UNIDADES = "UNIDADES";
    public static final String PRECIOS_COLUMN_MONTO = "MONTO";

    public static final String TPRECIOS_COLUMN_COD_TIPO_PRECIO = "COD_TIPO_PRECIO";
    public static final String TPRECIOS_COLUMN_TIPO_PRECIO = "TIPO_PRECIO";

    public final static int COUNTDOWN_FREQUENCY = 1000;
    public final static int VIBRATE_THRESHOLD = 30 * 1000;
    public final static int PLAY_SOUND_THRESHOLD = 15 * 1000;
    public final static int MIN_VIBRATE = 200;
    public final static int MAX_VIBRATE = 600;
    public final static long LOCATION_ACQUIRE_TIMEOUT = 10000;
}