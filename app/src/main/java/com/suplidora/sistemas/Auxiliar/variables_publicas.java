package com.suplidora.sistemas.Auxiliar;


import android.app.AlertDialog;
import android.content.DialogInterface;

import com.suplidora.sistemas.R;

public class variables_publicas {

	public static String CodigoVendedor="";
	public static String UsuarioLogin="";
	public static String NombreVendedor="";
	public static String TipoUsuario="";
	public static boolean LoginOk=false;
	public static String MensajeLogin="";
	public static String IdCliente ="" ;
	public static String CodCv ="";
	public static String NombreCliente ="" ;
	public static String RutaCliente ="";
	public static String Canal ="";
	public static String PrecioActual ="";
	public static String direccionIp="http://186.1.18.75:8080";

	//Variables BD
	public static final int DATABASE_VERSION = 11;
	public static final String DATABASE_NAME = "SysContabv3.db";
	//Variables TB
	public static final String TABLE_ARTICULOS = "Articulos";
	public static final String TABLE_CLIENTES = "Cliente";
	public static final String TABLE_PEDIDOS = "Pedidos";
	public static final String TABLE_USUARIOS = "Usuarios";
	public static final String TABLE_VENDEDORES = "Vendedor";
	public static final String TABLE_CLIENTES_SUCURSALES = "ClientesSucursales";
	public static final String TABLE_FORMA_PAGO = "FormaPago";
	public static final String TABLE_CARTILLAS_BC = "CartillasBC";
	public static final String TABLE_DETALLE_CARTILLAS_BC = "DetalleCartillasBC";
	public static final String TABLE_PRECIO_ESPECIAL = "ListaPrecioEspeciales";
	public static final String TABLE_CONFIGURACION_SISTEMA = "Configuraciones";

	//Variables CamposTbArticulos
	//public static final String ARTICULO_COLUMN_Id= "Id";
	public static final String ARTICULO_COLUMN_Codigo= "Codigo";
	public static final String ARTICULO_COLUMN_Nombre = "Nombre";
	public static final String ARTICULO_COLUMN_COSTO = "COSTO";
	public static final String ARTICULO_COLUMN_UNIDAD = "UNIDAD";
	public static final String ARTICULO_COLUMN_UnidadCaja = "UnidadCaja";
	public static final String ARTICULO_COLUMN_ISC = "ISC";
	public static final String ARTICULO_COLUMN_PorIVA = "PorIVA";
	public static final String  ARTICULO_COLUMN_PrecioSuper= "PrecioSuper";
	public static final String ARTICULO_COLUMN_PrecioDetalle = "PrecioDetalle";
	public static final String ARTICULO_COLUMN_PrecioForaneo = "PrecioForaneo";
	public static final String ARTICULO_COLUMN_PrecioMayorista = "PrecioMayorista";
	public static final String ARTICULO_COLUMN_Bonificable = "Bonificable";
	public static final String ARTICULO_COLUMN_AplicaPrecioDetalle = "AplicaPrecioDetalle";
	public static final String ARTICULO_COLUMN_DESCUENTO_MAXIMO = "DESCUENTO_MAXIMO";
	public static final String ARTICULO_COLUMN_detallista = "detallista";

	//Variables CamposTbClientes
	public static final String CLIENTES_COLUMN_IdCliente = "IdCliente";
	public static final String CLIENTES_COLUMN_CodCv= "CodCv";
	public static final String CLIENTES_COLUMN_Nombre= "Nombre";
	public static final String CLIENTES_COLUMN_FechaCreacion = "FechaCreacion";
	public static final String CLIENTES_COLUMN_Telefono = "Telefono";
	public static final String CLIENTES_COLUMN_Direccion = "Direccion";
	public static final String CLIENTES_COLUMN_IdDepartamento= "IdDepartamento";
	public static final String CLIENTES_COLUMN_IdMunicipio = "IdMunicipio";
	public static final String CLIENTES_COLUMN_Ciudad = "Ciudad";
	public static final String CLIENTES_COLUMN_Ruc= "Ruc";
	public static final String CLIENTES_COLUMN_Cedula= "Cedula";
	public static final String CLIENTES_COLUMN_LimiteCredito= "LimiteCredito";
	public static final String CLIENTES_COLUMN_IdFormaPago= "IdFormaPago";
	public static final String CLIENTES_COLUMN_IdVendedor= "IdVendedor";
	public static final String CLIENTES_COLUMN_Excento= "Excento";
	public static final String CLIENTES_COLUMN_CodigoLetra= "CodigoLetra";
	public static final String CLIENTES_COLUMN_Ruta= "Ruta";
	public static final String CLIENTES_COLUMN_Frecuencia= "Frecuencia";
	public static final String CLIENTES_COLUMN_PrecioEspecial= "PrecioEspecial";
	public static final String CLIENTES_COLUMN_FechaUltimaCompra= "FechaUltimaCompra";


	//Variables CamposTbPedidos
	public static final String PEDIDOS_COLUMN_IdVendedor = "IdVendedor";
	public static final String PEDIDOS_COLUMN_IdCliente = "IdCliente";
	public static final String PEDIDOS_COLUMN_Cod_cv = "Cod_cv";
	public static final String PEDIDOS_COLUMN_Observacion = "Observacion";
	public static final String PEDIDOS_COLUMN_IdFormaPago = "IdFormaPago";
	public static final String PEDIDOS_COLUMN_IdSucursal = "IdSucursal";
	public static final String PEDIDOS_COLUMN_Fecha = "Fecha";
	public static final String PEDIDOS_COLUMN_Usuario = "Usuario";
	public static final String PEDIDOS_COLUMN_IMEI ="IMEI" ;

	//Variables CamposUsuario
	public static final String USUARIOS_COLUMN_Codigo = "Codigo" ;
	public static final String USUARIOS_COLUMN_nombre = "nombre" ;
	public static final String USUARIOS_COLUMN_Usuario = "Usuario" ;
	public static final String USUARIOS_COLUMN_Contrasenia = "Contrasenia" ;
	public static final String USUARIOS_COLUMN_Tipo = "Tipo" ;
	public static final String USUARIOS_COLUMN_Ruta = "Ruta" ;
	public static final String USUARIOS_COLUMN_Canal = "Canal" ;
	public static final String USUARIOS_COLUMN_TasaCambio = "TasaCambio";

	public static final String VENDEDORES_COLUMN_CODIGO = "CODIGO" ;
	public static final String VENDEDORES_COLUMN_NOMBRE = "NOMBRE" ;
	public static final String VENDEDORES_COLUMN_COD_ZONA = "COD_ZONA" ;
	public static final String VENDEDORES_COLUMN_RUTA = "RUTA" ;
	public static final String VENDEDORES_COLUMN_codsuper = "codsuper" ;
	public static final String VENDEDORES_COLUMN_Status = "Status" ;
	public static final String VENDEDORES_COLUMN_detalle = "detalle" ;
	public static final String VENDEDORES_COLUMN_horeca = "horeca" ;
	public static final String VENDEDORES_COLUMN_mayorista = "mayorista" ;
	public static final String VENDEDORES_COLUMN_Super = "Super" ;

	public static final String CLIENTES_SUCURSALES_COLUMN_CodSuc = "CodSuc" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_CodCliente = "CodCliente" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_Sucursal = "Sucursal" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_Ciudad = "Ciudad" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_DeptoID = "DeptoID" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_Direccion = "Direccion" ;
	public static final String CLIENTES_SUCURSALES_COLUMN_FormaPagoID = "FormaPagoID" ;

	public static final String FORMA_PAGO_COLUMN_CODIGO = "CODIGO" ;
	public static final String FORMA_PAGO_COLUMN_NOMBRE = "NOMBRE" ;
	public static final String FORMA_PAGO_COLUMN_DIAS = "DIAS" ;
	public static final String FORMA_PAGO_COLUMN_EMPRESA = "EMPRESA" ;

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

	public static final String CONFIGURACION_SISTEMA_COLUMN_Id = "Id";
	public static final String CONFIGURACION_SISTEMA_COLUMN_Sistema = "Sistema";
	public static final String CONFIGURACION_SISTEMA_COLUMN_Configuracion = "Configuracion";
	public static final String CONFIGURACION_SISTEMA_COLUMN_Valor = "Valor";
	public static final String CONFIGURACION_SISTEMA_COLUMN_Activo = "Activo";



}