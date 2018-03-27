package com.suplidora.sistemas.sisago.Auxiliar;


import com.suplidora.sistemas.sisago.Entidades.Configuraciones;
import com.suplidora.sistemas.sisago.Entidades.Pedido;
import com.suplidora.sistemas.sisago.Entidades.Usuario;

public class variables_publicas {

    public static Usuario usuario = null;
    public static Configuraciones Configuracion = null;
    public static Pedido Pedidos = null;
    public static String ValorConfigServ = "";
    public static String AplicarPrecioMayoristaXCaja;
    public static String PermitirVentaDetAMayoristaXCaja;
    public static String[] lstDepartamentosForaneo1;
    public static String UsuarioLogin = "";
    public static String NombreVendedor = "";
    public static String CodigoVendedor = "";
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

    public static String noCedula = "";
    public static String nombreCed = "";
    public static String direccionCedula = "";

    public static final String direccionIp = "http://186.1.18.75:8080";
    public static final String correosErrores = "informatica@suplidora.com.ni,sistemas@suplidora.com.ni";
    public  static final String correoError= "sisago@suplidora.com.ni";
    //Variables BD
    public static final int DATABASE_VERSION = 74;
    public static final String DATABASE_NAME = "SysContabv3.db";
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
    public static final String TABLE_PRECIO_ESPECIAL = "ListaPrecioEspeciales";
    public static final String TABLE_CONFIGURACION_SISTEMA = "Configuraciones";
    public static final String TABLE_DPTOMUNIBARRIOS="DptoMuniBarrio";
    public static final String TABLE_INFORMES="Informes";
    public static final String TABLE_DETALLE_INFORMES="DetalleInformes";
    public static final String TABLE_FACTURAS_PENDIENTES="FacturasPendientes";
    public static final String TABLE_BANCOS="Bancos";

    //Variables CamposTbArticulos
    //public static final String ARTICULO_COLUMN_Id= "Id";
    public static final String ARTICULO_COLUMN_Codigo = "Codigo";
    public static final String ARTICULO_COLUMN_Nombre = "Nombre";
    public static final String ARTICULO_COLUMN_Costo = "Costo";
    public static final String ARTICULO_COLUMN_Unidad = "Unidad";
    public static final String ARTICULO_COLUMN_UnidadCaja = "UnidadCaja";
    public static final String ARTICULO_COLUMN_Isc = "Isc";
    public static final String ARTICULO_COLUMN_PorIva = "PorIva";
    public static final String ARTICULO_COLUMN_PrecioSuper = "PrecioSuper";
    public static final String ARTICULO_COLUMN_PrecioDetalle = "PrecioDetalle";
    public static final String ARTICULO_COLUMN_PrecioForaneo = "PrecioForaneo";
    public static final String ARTICULO_COLUMN_PrecioForaneo2 = "PrecioForaneo2";
    public static final String ARTICULO_COLUMN_PrecioMayorista = "PrecioMayorista";
    public static final String ARTICULO_COLUMN_Bonificable = "Bonificable";
    public static final String ARTICULO_COLUMN_AplicaPrecioDetalle = "AplicaPrecioDetalle";
    public static final String ARTICULO_COLUMN_DescuentoMaximo = "DescuentoMaximo";
    public static final String ARTICULO_COLUMN_Detallista = "Detallista";
    public static final String ARTICULO_COLUMN_Existencia="Existencia";
    public static final String ARTICULO_COLUMN_UnidadCajaVenta = "UnidadCajaVenta";
    public static final String ARTICULO_COLUMN_IdProveedor = "IdProveedor";

    //Variables CamposTbClientes
    public static final String CLIENTES_COLUMN_IdCliente = "IdCliente";
    public static final String CLIENTES_COLUMN_CodCv = "CodCv";
    public static final String CLIENTES_COLUMN_Nombre = "Nombre";
    public static final String CLIENTES_COLUMN_NombreCliente = "NombreCliente";
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
    public static final String CLIENTES_COLUMN_CodigoGalatea = "CodigoGalatea";
    public static final String CLIENTES_COLUMN_Descuento = "Descuento";
    public static final String CLIENTES_COLUMN_Empleado = "Empleado";
    public static final String CLIENTES_COLUMN_Detallista = "Detallista";
    public static final String CLIENTES_COLUMN_RutaForanea = "RutaForanea";
    public static final String CLIENTES_COLUMN_EsClienteVarios="EsClienteVarios";
    public static final String CLIENTES_COLUMN_IdBarrio="IdBarrio";
    public static final String CLIENTES_COLUMN_TipoNegocio="TipoNegocio";
    public static String VersionSistema = "";
    public static String info = "";
    //Variables CamposTbPedidos
    public static final String PEDIDOS_COLUMN_CodigoPedido = "CodigoPedido";
    public static final String PEDIDOS_COLUMN_IdVendedor = "IdVendedor";
    public static final String PEDIDOS_COLUMN_IdCliente = "IdCliente";
    public static final String PEDIDOS_COLUMN_Cod_cv = "Cod_cv";
    public static final String PEDIDOS_COLUMN_Tipo = "Tipo";
    public static final String PEDIDOS_COLUMN_Observacion = "Observacion";
    public static final String PEDIDOS_COLUMN_IdFormaPago = "IdFormaPago";
    public static final String PEDIDOS_COLUMN_IdSucursal = "IdSucursal";
    public static final String PEDIDOS_COLUMN_Fecha = "Fecha";
    public static final String PEDIDOS_COLUMN_Usuario = "Usuario";
    public static final String PEDIDOS_COLUMN_IMEI = "IMEI";
    public static final String PEDIDOS_COLUMN_Total = "Total";
    public static final String PEDIDOS_COLUMN_Subtotal = "subtotal";


    public static final String PEDIDOS_DETALLE_COLUMN_CodigoPedido = "CodigoPedido";
    public static final String PEDIDOS_DETALLE_COLUMN_CodigoArticulo = "CodigoArticulo";
    public static final String PEDIDOS_DETALLE_COLUMN_Descripcion = "Descripcion";
    public static final String PEDIDOS_DETALLE_COLUMN_Cantidad = "Cantidad";
    public static final String PEDIDOS_DETALLE_COLUMN_BonificaA = "BonificaA";
    public static final String PEDIDOS_DETALLE_COLUMN_TipoArt = "TipoArt";
    public static final String PEDIDOS_DETALLE_COLUMN_Descuento = "Descuento";
    public static final String PEDIDOS_DETALLE_COLUMN_PorDescuento = "PorDescuento";
    public static final String PEDIDOS_DETALLE_COLUMN_Isc = "Isc";
    public static final String PEDIDOS_DETALLE_COLUMN_Costo = "Costo";
    public static final String PEDIDOS_DETALLE_COLUMN_Precio = "Precio";
    public static final String PEDIDOS_DETALLE_COLUMN_TipoPrecio = "TipoPrecio";
    public static final String PEDIDOS_DETALLE_COLUMN_PorcentajeIva = "PorcentajeIva";
    public static final String PEDIDOS_DETALLE_COLUMN_Iva = "Iva";
    public static final String PEDIDOS_DETALLE_COLUMN_Um = "Um";
    public static final String PEDIDOS_DETALLE_COLUMN_Subtotal = "SubTotal";
    public static final String PEDIDOS_DETALLE_COLUMN_Total = "Total";

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

    public static final String VENDEDORES_COLUMN_CODIGO = "CODIGO";
    public static final String VENDEDORES_COLUMN_NOMBRE = "NOMBRE";
    public static final String VENDEDORES_COLUMN_COD_ZONA = "COD_ZONA";
    public static final String VENDEDORES_COLUMN_RUTA = "RUTA";
    public static final String VENDEDORES_COLUMN_codsuper = "codsuper";
    public static final String VENDEDORES_COLUMN_Supervisor = "Supervisor";
    public static final String VENDEDORES_COLUMN_Status = "Status";
    public static final String VENDEDORES_COLUMN_detalle = "detalle";
    public static final String VENDEDORES_COLUMN_horeca = "horeca";
    public static final String VENDEDORES_COLUMN_mayorista = "mayorista";
    public static final String VENDEDORES_COLUMN_Super = "Super";

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

    public static final String PRECIO_ESPECIAL_COLUMN_Id = "Id";
    public static final String PRECIO_ESPECIAL_COLUMN_CodigoArticulo = "CodigoArticulo";
    public static final String PRECIO_ESPECIAL_COLUMN_IdCliente = "IdCliente";
    public static final String PRECIO_ESPECIAL_COLUMN_Descuento = "Descuento";
    public static final String PRECIO_ESPECIAL_COLUMN_Precio = "Precio";
    public static final String PRECIO_ESPECIAL_COLUMN_Facturar = "Facturar";

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
    public static final String DPTOMUNIBARRIOS_COLUMN_Codigo_Barrio = "Codigo_Barrio";
    public static final String DPTOMUNIBARRIOS_COLUMN_Nombre_Barrio ="Nombre_Barrio";

    public static  final String INFORMES_COLUMN_CodInforme = "CodInforme";
    public static  final String INFORMES_COLUMN_Fecha = "Fecha";
    public static  final String INFORMES_COLUMN_IdVendedor = "IdVendedor";
    public static  final String INFORMES_COLUMN_Aprobada = "Aprobada";
    public static  final String INFORMES_COLUMN_Anulada = "Anulada";
    public static  final String INFORMES_COLUMN_FechaCreacion = "FechaCreacion";
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

    public static  final String BANCOS_COLUMN_codigo = "Codigo";
    public static final String  BANCOS_COLUMN_nombre ="Nombre";

}