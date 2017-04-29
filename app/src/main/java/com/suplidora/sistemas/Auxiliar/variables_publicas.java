package com.suplidora.sistemas.Auxiliar;


import android.app.AlertDialog;
import android.content.DialogInterface;

import com.suplidora.sistemas.R;

public class variables_publicas {

	public static String CodigoVendedor="";
	public static String UsuarioLogin="";
	public static String NombreVendedor="";
	public static boolean LoginOk=false;
	public static String IdCliente ="" ;
	public static String CodCv ="";
	public static String NombreCliente ="" ;
	public static String RutaCliente ="";
	public static String Canal ="";
	public static String PrecioActual ="";
	public static String direccionIp="http://186.1.18.75:8080";

	//Variables BD
	public static final int DATABASE_VERSION = 6;
	public static final String DATABASE_NAME = "SysContabv3.db";
	//Variables TB
	public static final String TABLE_ARTICULOS = "Articulos";
	public static final String TABLE_CLIENTES = "Clientes";
	public static final String TABLE_PEDIDOS = "Pedidos";
	public static final String TABLE_USUARIOS = "Usuarios";
	//Variables CamposTbArticulos
	//public static final String ARTICULO_COLUMN_Id= "Id";
	public static final String ARTICULO_COLUMN_Codigo= "Codigo";
	public static final String ARTICULO_COLUMN_Nombre = "Nombre";
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
	public static final String CLIENTES_COLUMN_Cliente = "Cliente";
	public static final String  CLIENTES_COLUMN_Nombre= "Nombre";
	public static final String CLIENTES_COLUMN_FechaIngreso = "FechaIngreso";
	public static final String CLIENTES_COLUMN_ClienteNuevo = "ClienteNuevo";
	public static final String CLIENTES_COLUMN_Ruta = "Ruta";
	public static final String CLIENTES_COLUMN_Direccion = "Direccion";
	public static final String CLIENTES_COLUMN_Cedula = "Cedula";
	public static final String CLIENTES_COLUMN_IdVendedor = "IdVendedor";
	public static final String CLIENTES_COLUMN_Vendedor = "Vendedor";
	public static final String CLIENTES_COLUMN_IdSupervisor = "IdSupervisor";
	public static final String CLIENTES_COLUMN_Supervisor = "Supervisor";
	public static final String CLIENTES_COLUMN_Subruta = "Subruta";
	public static final String CLIENTES_COLUMN_FechaUltimaCompra= "FechaUltimaCompra";
	public static final String CLIENTES_COLUMN_Frecuencia= "Frecuencia";

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

}
