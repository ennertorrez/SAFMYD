package com.suplidora.sistemas.sisago.Auxiliar;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConfiguracionSistemaHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static com.suplidora.sistemas.sisago.Auxiliar.Funciones.Codificar;

/**
 * Created by usuario on 5/5/2017.
 */

public class SincronizarDatos {



    private String urlClientes = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes";
    private String urlDptoMuniBarrios = variables_publicas.direccionIp + "/ServicioClientes.svc/GetDptoMuniBarrios";
    private String urlArticulos = variables_publicas.direccionIp + "/ServicioTotalArticulos.svc/BuscarTotalArticulo";
    final String urlVendedores = variables_publicas.direccionIp + "/ServicioPedidos.svc/ListaVendedores/";
    final String urlCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetCartillasBC/";
    final String urlDetalleCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetDetalleCartillasBC/";
    final String urlFormasPago = variables_publicas.direccionIp + "/ServicioPedidos.svc/FormasPago/";
    final String urlListPrecioEspecial = variables_publicas.direccionIp + "/ServicioPedidos.svc/ListPrecioEspecial/";
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones/";
    final String urlGetClienteSucursales = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetClienteSucursales/";
    final String url = variables_publicas.direccionIp + "/ServicioLogin.svc/BuscarUsuario/";
    static final String urlConsultarExistencias = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerInventarioArticulo/";
    private String TAG = SincronizarDatos.class.getSimpleName();
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private PedidosHelper PedidosH;
    private PedidosDetalleHelper PedidosDetalleH;
    private CartillasBcHelper CartillasBcH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private FormaPagoHelper FormaPagoH;
    private PrecioEspecialHelper PrecioEspecialH;
    private ConfiguracionSistemaHelper ConfigSistemasH;
    private ClientesSucursalHelper ClientesSucH;


    public SincronizarDatos(DataBaseOpenHelper dbh, ClientesHelper Clientesh,
                            VendedoresHelper Vendedoresh, CartillasBcHelper CatillasBch,
                            CartillasBcDetalleHelper CartillasBcDetalleh, FormaPagoHelper FormaPagoh,
                            PrecioEspecialHelper PrecioEspecialh, ConfiguracionSistemaHelper ConfigSistemah,
                            ClientesSucursalHelper ClientesSuch, ArticulosHelper Articulosh, UsuariosHelper usuariosH,
                            PedidosHelper pedidoH, PedidosDetalleHelper pedidosDetalleH ) {
        DbOpenHelper = dbh;
        ClientesH = Clientesh;
        VendedoresH = Vendedoresh;
        CartillasBcH = CatillasBch;
        CartillasBcDetalleH = CartillasBcDetalleh;
        FormaPagoH = FormaPagoh;
        PrecioEspecialH = PrecioEspecialh;
        ConfigSistemasH = ConfigSistemah;
        ClientesSucH = ClientesSuch;
        ArticulosH = Articulosh;
        UsuariosH = usuariosH;
        PedidosH = pedidoH;
        PedidosDetalleH = pedidosDetalleH;
    }

  /*   public SincronizarDatos(DataBaseOpenHelper dbh,  ConfiguracionSistemaHelper ConfigSistemah,
                             ArticulosHelper Articulosh, UsuariosHelper usuariosH) {
        DbOpenHelper = dbh;
        ConfigSistemasH = ConfigSistemah;
        ArticulosH = Articulosh;
        UsuariosH = usuariosH;
       ConsolidadoCargaH = ConsolidadoCargah;
        ConsolidadoCargaDetalleH = ConsolidadoCargaDetalleh;
        DevolucionesH =devolucionesH;
        DevolucionesDetalleH =devolucionesDetalleH;
    }*/

    public SincronizarDatos(DataBaseOpenHelper dbh, ClientesHelper Clientesh ) {
        DbOpenHelper = dbh;
        ClientesH = Clientesh;
    }

    private boolean SincronizarArticulos() throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlArticulos;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Articulos, Respuesta nula GET", variables_publicas.info + urlStringC, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();

        ArticulosH.EliminaArticulos();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray articulos = jsonObjC.getJSONArray("BuscarTotalArticuloResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < articulos.length(); i++) {
                JSONObject c = articulos.getJSONObject(i);

                String Codigo = c.getString("CODIGO_ARTICULO");
                String Nombre = c.getString("NOMBRE");
                String COSTO = c.getString("COSTO");
                String UNIDAD = c.getString("UNIDAD");
                String UnidadCaja = c.getString("UnidadCaja");
                String ISC = c.getString("ISC");
                String PorIVA = c.getString("PorIVA");
                String PrecioSuper = c.getString("PrecioSuper");
                String PrecioDetalle = c.getString("PrecioDetalle");
                String PrecioForaneo = c.getString("PrecioForaneo");
                String PrecioForaneo2 = c.getString("PrecioForaneo2");
                String PrecioMayorista = c.getString("PrecioMayorista");
                String Bonificable = c.getString("Bonificable");
                String AplicaPrecioDetalle = c.getString("AplicaPrecioDetalle");
                String DESCUENTO_MAXIMO = c.getString("DESCUENTO_MAXIMO");
                String detallista = c.getString("detallista");
                String existencia = c.getString("Existencia");
                String UnidadCajaVenta = c.getString("UnidadCajaVenta");
                String IdProveedor = c.getString("IdProveedor");
                ArticulosH.GuardarTotalArticulos(Codigo, Nombre, COSTO, UNIDAD, UnidadCaja, ISC, PorIVA, PrecioSuper, PrecioDetalle, PrecioForaneo, PrecioForaneo2,
                        PrecioMayorista, Bonificable, AplicaPrecioDetalle, DESCUENTO_MAXIMO, detallista, existencia, UnidadCajaVenta, IdProveedor);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Articulos, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //Cliente
    public boolean SincronizarClientes() throws JSONException {
        /*******************************CLIENTES******************************/
        //************CLIENTES
        DbOpenHelper.database.beginTransaction();

        ObtenerDptosMuniBarrios();

        HttpHandler shC = new HttpHandler();
        String urlStringC = urlClientes + "/" + variables_publicas.usuario.getCodigo() + "/" + 3;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar clientes, Respuesta nula GET", variables_publicas.info + urlStringC, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);

        ClientesH.EliminaClientes();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray clientes = jsonObjC.getJSONArray("BuscarClientesResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < clientes.length(); i++) {
                JSONObject c = clientes.getJSONObject(i);

                String IdCliente = c.getString("IdCliente");
                String CodCv = c.getString("CodCv");
                String Nombre = c.getString("Nombre");
                String NombreCliente = c.getString("NombreCliente");
                String FechaCreacion = c.getString("FechaCreacion");
                String Telefono = c.getString("Telefono");
                String Direccion = c.getString("Direccion");
                String IdDepartamento = c.getString("IdDepartamento");
                String IdMunicipio = c.getString("IdMunicipio");
                String Ciudad = c.getString("Ciudad");
                String Ruc = c.getString("Ruc");
                String Cedula = c.getString("Cedula");
                String LimiteCredito = c.getString("LimiteCredito");
                String IdFormaPago = c.getString("IdFormaPago");
                String IdVendedor = c.getString("IdVendedor");
                String Excento = c.getString("Excento");
                String CodigoLetra = c.getString("CodigoLetra");
                String Ruta = c.getString("Ruta");
                String Frecuencia = c.getString("Frecuencia");
                String PrecioEspecial = c.getString("PrecioEspecial");
                String FechaUltimaCompra = c.getString("FechaUltimaCompra");
                String Tipo = c.getString("Tipo");
                String CodigoGalatea = c.getString("CodigoGalatea");
                String Descuento = c.getString("Descuento");
                String Empleado = c.getString("Empleado");
                String Detallista = c.getString("Detallista");
                String RutaForanea = c.getString("RutaForanea");
                String EsClienteVarios = c.getString("EsClienteVarios");
                String IdBarrio = c.getString("IdBarrio");
                String TipoNegocio = c.getString("TipoNegocio");
                ClientesH.GuardarTotalClientes(IdCliente, CodCv, Nombre, NombreCliente, FechaCreacion, Telefono, Direccion, IdDepartamento, IdMunicipio, Ciudad, Ruc, Cedula, LimiteCredito, IdFormaPago, IdVendedor, Excento, CodigoLetra, Ruta, Frecuencia, PrecioEspecial, FechaUltimaCompra, Tipo, CodigoGalatea, Descuento, Empleado, Detallista, RutaForanea, EsClienteVarios,IdBarrio,TipoNegocio);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar clientes, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //Vendedor
    public boolean SincronizarVendedores() throws JSONException {
        //************VENDEDORES
        HttpHandler shV = new HttpHandler();
        String urlStringV = urlVendedores;
        String jsonStrV = shV.makeServiceCall(urlStringV);

        if (jsonStrV == null) {
            new Funciones().SendMail("Ha ocurrido un error: al sincronizar vendedores, Respuesta Nula GET", variables_publicas.info + urlStringV, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        //Log.e(TAG, "Response from url: " + jsonStrC);
        DbOpenHelper.database.beginTransaction();
        VendedoresH.EliminaVendedores();
        JSONObject jsonObjV = new JSONObject(jsonStrV);
        // Getting JSON Array node
        JSONArray vendedores = jsonObjV.getJSONArray("ListaVendedoresResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < vendedores.length(); i++) {
                JSONObject c = vendedores.getJSONObject(i);

                String CODIGO = c.getString("CODIGO");
                String NOMBRE = c.getString("NOMBRE");
                String DEPARTAMENTO = c.getString("DEPARTAMENTO");
                String MUNICIPIO = c.getString("MUNICIPIO");
                String CIUDAD = c.getString("CIUDAD");
                String TELEFONO = c.getString("TELEFONO");
                String CELULAR = c.getString("CELULAR");
                String CORREO = c.getString("CORREO");
                String COD_ZONA = c.getString("COD_ZONA");
                String RUTA = c.getString("RUTA");
                String codsuper = c.getString("codsuper");
                String Status = c.getString("Status");
                String detalle = c.getString("detalle");
                String horeca = c.getString("horeca");
                String mayorista = c.getString("mayorista");
                String Super = c.getString("super");
                String Supervisor = c.getString("Supervisor");

                VendedoresH.GuardarTotalVendedores(CODIGO, NOMBRE, DEPARTAMENTO, MUNICIPIO, CIUDAD, TELEFONO, CELULAR, CORREO, COD_ZONA, RUTA, codsuper,Supervisor, Status, detalle, horeca, mayorista, Super);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar vendedores, Excepcion controlada", variables_publicas.info + urlStringV, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //CartillasBc
    public boolean SincronizarCartillasBc() throws JSONException {
        HttpHandler shCartillas = new HttpHandler();
        String urlStringCartillas = urlCartillasBc;
        String jsonStrCartillas = shCartillas.makeServiceCall(urlStringCartillas);

        if (jsonStrCartillas == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronicar CartillasBC, Respuesta nula GET", variables_publicas.info + urlStringCartillas, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        CartillasBcH.EliminaCartillasBc();
        JSONObject jsonObjCartillas = new JSONObject(jsonStrCartillas);
        // Getting JSON Array node
        JSONArray cartillas = jsonObjCartillas.getJSONArray("GetCartillasBCResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < cartillas.length(); i++) {
                JSONObject c = cartillas.getJSONObject(i);

                String id = c.getString("id");
                String codigo = c.getString("codigo");
                String fechaini = c.getString("fechaini");
                String fechafinal = c.getString("fechafinal");
                String tipo = c.getString("tipo");
                String aprobado = c.getString("aprobado");

                CartillasBcH.GuardarCartillasBc(id, codigo, fechaini, fechafinal, tipo, aprobado);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronicar CartillasBC, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }


    //CartillasBcDetalle
    public boolean SincronizarCartillasBcDetalle() throws JSONException {
        HttpHandler shCartillasD = new HttpHandler();
        String urlStringCartillasD = urlDetalleCartillasBc;
        String jsonStrCartillasD = shCartillasD.makeServiceCall(urlStringCartillasD);

        if (jsonStrCartillasD == null) {
            new Funciones().SendMail("Ha ocurrido un error DetallaCartillaBC", variables_publicas.info + urlStringCartillasD, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        DbOpenHelper.database.beginTransaction();
        CartillasBcDetalleH.EliminaCartillasBcDetalle();
        JSONObject jsonObjCartillasD = new JSONObject(jsonStrCartillasD);
        // Getting JSON Array node
        JSONArray cartillasD = jsonObjCartillasD.getJSONArray("GetDetalleCartillasBCResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < cartillasD.length(); i++) {
                JSONObject c = cartillasD.getJSONObject(i);
                String id = c.getString("id");
                String itemV = c.getString("itemV");
                String descripcionV = c.getString("descripcionV");
                String cantidad = c.getString("cantidad");
                String itemB = c.getString("itemB");
                String descripcionB = c.getString("descripcionB");
                String cantidadB = c.getString("cantidadB");
                String codigo = c.getString("codigo");
                String tipo = c.getString("tipo");
                String activo = c.getString("activo");
                CartillasBcDetalleH.GuardarCartillasBcDetalle(id, itemV, descripcionV, cantidad, itemB, descripcionB, cantidadB, codigo, tipo, activo);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar DetallaCartillaBC, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    //FormaPago
    public boolean SincronizarFormaPago() throws JSONException {
        HttpHandler shFormaPago = new HttpHandler();
        String urlStringFormaPago = urlFormasPago;
        String jsonStrFormaPago = shFormaPago.makeServiceCall(urlStringFormaPago);

        if (jsonStrFormaPago == null) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Forma de pago, Respuesta nula GET", variables_publicas.info + urlStringFormaPago, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        DbOpenHelper.database.beginTransaction();
        FormaPagoH.EliminaFormaPago();
        JSONObject jsonObjFormaPago = new JSONObject(jsonStrFormaPago);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjFormaPago.getJSONArray("FormasPagoResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String CODIGO = c.getString("CODIGO");
                String NOMBRE = c.getString("NOMBRE");
                String DIAS = c.getString("DIAS");
                String EMPRESA = c.getString("EMPRESA");

                FormaPagoH.GuardarTotalFormaPago(CODIGO, NOMBRE, DIAS, EMPRESA);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar Forma de pago, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //PrecioEspecial
    public boolean SincronizarPrecioEspecial() throws JSONException {
        HttpHandler shPrecioEspecial = new HttpHandler();
        String urlStringPrecioEspecial = urlListPrecioEspecial;
        String jsonStrPrecioEspecial = shPrecioEspecial.makeServiceCall(urlStringPrecioEspecial);

        if (jsonStrPrecioEspecial == null) {
            new Funciones().SendMail("Ha ocurrido un error al Sincronizar PrecioEspecial, Respuesta nula", variables_publicas.info + urlStringPrecioEspecial, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        PrecioEspecialH.EliminaPrecioEspecial();
        JSONObject jsonObjPrecioEspecial = new JSONObject(jsonStrPrecioEspecial);
        // Getting JSON Array node
        JSONArray PrecioEspecial = jsonObjPrecioEspecial.getJSONArray("ListPrecioEspecialResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < PrecioEspecial.length(); i++) {
                JSONObject c = PrecioEspecial.getJSONObject(i);

                String Id = c.getString("Id");
                String CodigoArticulo = c.getString("CodigoArticulo");
                String IdCliente = c.getString("IdCliente");
                String Descuento = c.getString("Descuento");
                String Precio = c.getString("Precio");
                String Facturar = c.getString("Facturar");

                PrecioEspecialH.GuardarPrecioEspecial(Id, CodigoArticulo, IdCliente, Descuento, Precio, Facturar);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al Sincronizar PrecioEspecial, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }

    }

    //ConfiguracionSistema
    public boolean SincronizarConfiguracionSistema() throws JSONException {
        HttpHandler shConfiguracionSistema = new HttpHandler();
        String urlStringConfiguracionSistema = urlGetConfiguraciones;
        String jsonStrConfiguracionSistema = shConfiguracionSistema.makeServiceCall(urlStringConfiguracionSistema);

        if (jsonStrConfiguracionSistema == null) {
            new Funciones().SendMail("Ha ocurrido un error al Sincronizar ConfiguracionSistema, Respuesta nula GET", variables_publicas.info + urlStringConfiguracionSistema, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        ConfigSistemasH.EliminaConfigSistema();
        JSONObject jsonObjConfiguracionSistema = new JSONObject(jsonStrConfiguracionSistema);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjConfiguracionSistema.getJSONArray("GetConfiguracionesResult");


        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String Id = c.getString("Id");
                String Sistema = c.getString("Sistema");
                String Configuracion = c.getString("Configuracion");
                String Valor = c.getString("Valor");
                String Activo = c.getString("Activo");

                if (Configuracion.equalsIgnoreCase("VersionDatos")) {
                    variables_publicas.ValorConfigServ = Valor;
                }
                if (Configuracion.equalsIgnoreCase("AplicarPrecioMayoristaXCaja")) {
                    variables_publicas.AplicarPrecioMayoristaXCaja = Valor;
                }
                if (Configuracion.equalsIgnoreCase("PermitirVentaDetAMayoristaXCaja")) {
                    variables_publicas.PermitirVentaDetAMayoristaXCaja = Valor;
                }
                if (Configuracion.equalsIgnoreCase("lstDepartamentosForaneo1")) {
                    variables_publicas.lstDepartamentosForaneo1 = Valor.split(",");
                }


                ConfigSistemasH.GuardarConfiguracionSistema(Id, Sistema, Configuracion, Valor, Activo);
                variables_publicas.Configuracion = ConfigSistemasH.BuscarValorConfig("VersionDatos");
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al Sincronizar ConfiguracionSistema, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    public String ObtenerValorConfigDatos() throws JSONException {
        HttpHandler shConfigSistema = new HttpHandler();
        String urlStringConfigSistema = urlGetConfiguraciones;
        String jsonStrConfiguracionSistema = shConfigSistema.makeServiceCall(urlStringConfigSistema);

        if (jsonStrConfiguracionSistema == null)
            return null;

        JSONObject jsonObjConfiguracionSistema = new JSONObject(jsonStrConfiguracionSistema);
        JSONArray ValorConfig = jsonObjConfiguracionSistema.getJSONArray("GetConfiguracionesResult");

        for (int i = 0; i < ValorConfig.length(); i++) {
            JSONObject c = ValorConfig.getJSONObject(i);
            String Valor = c.getString("Valor");
            String Configuracion = c.getString("Configuracion");
            if (Configuracion == "VersionDatos") {
                variables_publicas.ValorConfigServ = Valor;
            }
            if (Configuracion == "AplicarPrecioMayoristaXCaja") {
                variables_publicas.AplicarPrecioMayoristaXCaja = Valor;
            }
            if (Configuracion == "PermitirVentaDetAMayoristaXCaja") {
                variables_publicas.PermitirVentaDetAMayoristaXCaja = Valor;
            }
            if (Configuracion == "lstDepartamentosForaneo1") {
                variables_publicas.lstDepartamentosForaneo1 = Valor.split(",");
            }
        }
        return jsonStrConfiguracionSistema;
    }

    //ClientesSucursal
    public boolean SincronizarClientesSucursal() throws JSONException {
        HttpHandler shClientesSucursal = new HttpHandler();
        String urlStringClientesSucursal = urlGetClienteSucursales;
        String jsonStrClientesSucursal = shClientesSucursal.makeServiceCall(urlStringClientesSucursal);

        if (jsonStrClientesSucursal == null) {
            new Funciones().SendMail("Ha ocurrido un error al SincronizarClientesSucursal, Respuesta nula", variables_publicas.info + urlStringClientesSucursal, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }

        DbOpenHelper.database.beginTransaction();
        ClientesSucH.EliminaClientesSucursales();
        JSONObject jsonObjClientesSucursal = new JSONObject(jsonStrClientesSucursal);
        // Getting JSON Array node
        JSONArray PrecioEspecial = jsonObjClientesSucursal.getJSONArray("GetClienteSucursalesResult");



        try {
            // looping through All Contacts
            for (int i = 0; i < PrecioEspecial.length(); i++) {
                JSONObject c = PrecioEspecial.getJSONObject(i);

                String CodSuc = c.getString("CodSuc");
                String CodCliente = c.getString("CodCliente");
                String Sucursal = c.getString("Sucursal");
                String Ciudad = c.getString("Ciudad");
                String DeptoID = c.getString("DeptoID");
                String Direccion = c.getString("Direccion");
                String FormaPagoID = c.getString("FormaPagoID");
                String Descuento = c.getString("Descuento");

                ClientesSucH.GuardarTotalClientesSucursal(CodSuc, CodCliente, Sucursal, Ciudad, DeptoID, Direccion, FormaPagoID,Descuento);
            }
            DbOpenHelper.database.setTransactionSuccessful();
            return true;
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al SincronizarClientesSucursal,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        } finally {
            DbOpenHelper.database.endTransaction();
        }
    }

    public boolean SincronizarTodo() throws JSONException {

            if (SincronizarArticulos()) {
                if (SincronizarClientes()) {
                    if (SincronizarVendedores()) {
                        if (SincronizarCartillasBc()) {
                            if (SincronizarCartillasBcDetalle()) {
                                if (SincronizarFormaPago()) {
                                    if (SincronizarPrecioEspecial()) {
                                        if (SincronizarClientesSucursal()) {
                                            if ( SincronizarConfiguracionSistema()) {
                                                if(ActualizarUsuario()){
                                                    SincronizarPedidosLocales();
                                                    return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        return false;
    }

    public void SincronizarTablas() throws JSONException {
        SincronizarArticulos();
        SincronizarClientes();
        SincronizarCartillasBc();
        SincronizarCartillasBcDetalle();
        SincronizarPrecioEspecial();
        SincronizarClientesSucursal();
        SincronizarConfiguracionSistema();
    }

    public boolean SincronizarPedidosLocales() {

        boolean guardadoOK = true;
        List<HashMap<String, String>> PedidosLocal = PedidosH.ObtenerPedidosLocales(Funciones.GetLocalDateTime(), "");
        for (HashMap<String, String> item : PedidosLocal) {
            if (guardadoOK == false) {
                break;
            }
            Gson gson = new Gson();
            Vendedor vendedor = VendedoresH.ObtenerVendedor(item.get(variables_publicas.PEDIDOS_COLUMN_IdVendedor));
            Cliente cliente = ClientesH.BuscarCliente(item.get(variables_publicas.PEDIDOS_COLUMN_IdCliente), item.get(variables_publicas.PEDIDOS_COLUMN_Cod_cv));
            String jsonPedido = gson.toJson(PedidosH.ObtenerPedido(item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
            guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarPedido(PedidosH, PedidosDetalleH, vendedor, cliente, item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido), jsonPedido, false).split(",")[0]);
        }
        return guardadoOK;
    }

    private boolean ActualizarUsuario() {

        HttpHandler sh = new HttpHandler();
        String urlString = url + variables_publicas.usuario.getUsuario() + "/" + Funciones.Codificar(variables_publicas.usuario.getContrasenia());
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************USUARIOS**************************************/
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Usuarios = jsonObj.getJSONArray("BuscarUsuarioResult");
                if (Usuarios.length() == 0) {
                    return false;
                }
                UsuariosH.EliminaUsuarios();
                // looping through All Contacts

                for (int i = 0; i < Usuarios.length(); i++) {
                    JSONObject c = Usuarios.getJSONObject(i);
                    variables_publicas.CodigoVendedor = c.getString("Codigo");
                    variables_publicas.NombreVendedor = c.getString("Nombre");
                    variables_publicas.UsuarioLogin = c.getString("Usuario");
                    variables_publicas.TipoUsuario = c.getString("Tipo");
                    String Contrasenia = c.getString("Contrasenia");
                    String Tipo = c.getString("Tipo");
                    variables_publicas.RutaCliente = c.getString("Ruta");
                    variables_publicas.Canal = c.getString("Canal");
                    String TasaCambio = c.getString("TasaCambio");
                    String RutaForanea = c.getString("RutaForanea");
                    String FechaActualiza = Funciones.getDatePhone();
                    UsuariosH.GuardarUsuario(variables_publicas.CodigoVendedor, variables_publicas.NombreVendedor,
                            variables_publicas.UsuarioLogin, Contrasenia, Tipo, variables_publicas.RutaCliente, variables_publicas.Canal, TasaCambio, RutaForanea, FechaActualiza);

                    variables_publicas.usuario = UsuariosH.BuscarUsuarios(variables_publicas.usuario.getUsuario(), Contrasenia);
                    return true;
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos del usuario,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                return false;
            }

        } else {
            new Funciones().SendMail("Ha ocurrido un error al obtener los datos del usuario,Respuesta nula", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
       return false;
    }

    private boolean ObtenerDptosMuniBarrios() {

        HttpHandler sh = new HttpHandler();
        String urlString = urlDptoMuniBarrios;
        String encodeUrl = "";
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStr = sh.makeServiceCall(encodeUrl);

        /**********************************DEPARTAMENTOS**************************************/
        if (jsonStr != null) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray dptosMunBarr = jsonObj.getJSONArray("GetDptoMuniBarriosResult");
                if (dptosMunBarr.length() == 0) {
                    return false;
                }
                ClientesH.EliminarDptosMuniBarrios();
                // looping through All Contacts

                for (int i = 0; i < dptosMunBarr.length(); i++) {
                    JSONObject c = dptosMunBarr.getJSONObject(i);
                    ClientesH.GuardarDptosMuniBarrios(c.get("Codigo_Departamento").toString(),c.get("Nombre_Departamento").toString(),c.get("Codigo_Municipio").toString(),c.get("Nombre_Municipio").toString(),c.get("Codigo_Barrio").toString(),c.get("Nombre_Barrio").toString());
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener los Departamentos, Municipios y Barrios,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                return false;
            }

        } else {
            new Funciones().SendMail("Ha ocurrido un error al obtener los Departamentos, Municipios y Barrios,Respuesta nula", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
        return false;
    }

    public static String SincronizarPedido(PedidosHelper PedidoH, PedidosDetalleHelper PedidoDetalleH, Vendedor vendedor, Cliente cliente, String IdPedido, String jsonPedido, boolean Editar) {

        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();
        List<HashMap<String, String>> pedidoDetalle = PedidoDetalleH.ObtenerPedidoDetalle(IdPedido);
        for (HashMap<String, String> item : pedidoDetalle) {
            item.put("SubTotal", item.get("SubTotal").replace(",", ""));
            item.put("Costo", item.get("Costo").replace(",", ""));
            item.put("Total", item.get("Total").replace(",", ""));
            item.put("Iva", item.get("Iva").replace(",", ""));
            item.put("Precio", item.get("Precio").replace(",", ""));
            item.put("Descuento", item.get("Descuento").replace(",", ""));
            item.put("Descripcion", Codificar(item.get("Descripcion")));
        }
        String jsonPedidoDetalle = gson.toJson(pedidoDetalle);
        final String urlDetalle = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedidoTotal/";
        final String urlStringDetalle = urlDetalle + cliente.getCodigoLetra() + "/" + String.valueOf(Editar) + "/" + vendedor.getCODIGO() + "/" + jsonPedido + "/" + jsonPedidoDetalle;
/*
        try {
            URL Url = new URL(urlStringDetalle);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            new Funciones().SendMail("Ha ocurrido un error al sincronizar pedido, Codificar URL", variables_publicas.info + e.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            e.printStackTrace();
            return "false," + e.getMessage();
        }*/

        HashMap<String,String> postData = new HashMap<>();
        postData.put("CodigoLetra",cliente.getCodigoLetra());
        postData.put("Editar",String.valueOf(Editar));
        postData.put("IdVendedor",vendedor.getCODIGO());
        postData.put("pedido",jsonPedido);
        postData.put("Detalle",jsonPedidoDetalle)   ;

        String jsonStrPedido = sh.performPostCall(urlDetalle,postData);

      //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrPedido == null || jsonPedido.isEmpty()) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido,Respuesta nula POST", variables_publicas.info + urlStringDetalle, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return "false,Ha ocurrido un error al sincronizar el detalle del pedido,Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrPedido);
                String resultState = (String) ((String) result.get("SincronizarPedidoTotalResult")).split(",")[0];
                String NoPedido = (String) ((String) result.get("SincronizarPedidoTotalResult")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoPedido.equalsIgnoreCase("Pedido ya existe en base de datos")) {
                        NoPedido =  ((String) result.get("SincronizarPedidoTotalResult")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido ,Respuesta false", variables_publicas.info + NoPedido +" *** "+urlStringDetalle, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                        return "false," + NoPedido;
                    }
                }
                PedidoH.ActualizarPedido(IdPedido, NoPedido);
                PedidoDetalleH.ActualizarCodigoPedido(IdPedido, NoPedido);
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el pedido, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

    public static String ConsultarExistencias(final Activity activity, PedidosHelper PedidoH, ArticulosHelper ArticulosH, String CodigoArticulo) {
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";

        final String urlConsulta = urlConsultarExistencias + CodigoArticulo;

        try {
            URL Url = new URL(urlConsulta);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Codificar URL", variables_publicas.info + e.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            e.printStackTrace();
            return "N/A";
        }

        String jsonExistencia = sh.makeServiceCall(encodeUrl);
        if (jsonExistencia == null) {
      /*      new Funciones().SendMail("Ha ocurrido un error al obtener las existencias,Respuesta nula GET", variables_publicas.info + " --- " + urlConsulta, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    variables_publicas.MensajeError = "Ha ocurrido un error al obtener las existencias,Respuesta nula";
                    Toast.makeText(activity.getApplicationContext(),
                            "Ha ocurrido un error al obtener las existencias,Respuesta nula",
                            Toast.LENGTH_LONG).show();
                }
            });*/
            return "N/A";
        } else {
            try {
                JSONObject result = new JSONObject(jsonExistencia);
                String resultState = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[0];
                final String existencia = (String) ((String) result.get("ObtenerInventarioArticuloResult")).split(",")[1];
                if (resultState.equals("false")) {

                    new Funciones().SendMail("Ha ocurrido un error al obtener las existencias ,Respuesta false", variables_publicas.info + " --- " + existencia, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                  /*  activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            variables_publicas.MensajeError = existencia;
                            Toast.makeText(activity.getApplicationContext(),
                                    existencia,
                                    Toast.LENGTH_LONG).show();
                        }
                    });*/
                    return "N/A";
                }
                /*Si no hubo ningun problema procedemos a actualizar las existencias locales*/
                ArticulosH.ActualizarExistencias(CodigoArticulo, existencia);
                return existencia;
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al obtener las existencias, Excepcion controlada ", variables_publicas.info + ex.getMessage() + " ---json: " + urlConsulta + " ---Response: " + jsonExistencia, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "N/A";
            }

        }

    }

    public static String SincronizarClientesTotal(Cliente cliente, String jsonCliente) {
        boolean Editar=false;
        if (variables_publicas.vEditando){
            Editar=true;
        }else {
            Editar=false;
        }
        HttpHandler sh = new HttpHandler();
        String encodeUrl = "";
        Gson gson = new Gson();

        final String urlCliente = variables_publicas.direccionIp + "/ServicioClientes.svc/SincronizarClienteTotal/";
        final String urlStringDetalle = urlCliente + cliente.getIdCliente() + "/" + String.valueOf(Editar) + "/" + cliente.getCodCv() + "/" + jsonCliente;


        HashMap<String,String> postData = new HashMap<>();
        postData.put("Editar",String.valueOf(Editar));
        postData.put("IdCliente",cliente.getIdCliente());
        postData.put("IdClienteVario",cliente.getCodCv());
        postData.put("cliente",jsonCliente);


        String jsonStrCliente = sh.performPostCall(urlCliente,postData);

        //  String jsonStrPedido = sh.makeServiceCallPost(encodeUrl);
        if (jsonStrCliente == null || jsonCliente.isEmpty()) {
            new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente,Respuesta nula POST", variables_publicas.info + urlStringDetalle, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            return "false,Ha ocurrido un error al sincronizar el cliente,Respuesta nula";
        } else {
            try {
                JSONObject result = new JSONObject(jsonStrCliente);
                String resultState = (String) ((String) result.get("SincronizarClienteTotalResult")).split(",")[0];
                String NoCliente = (String) ((String) result.get("SincronizarClienteTotalResult")).split(",")[1];
                if (resultState.equals("false")) {

                    if (NoCliente.equalsIgnoreCase("Cliente ya existe en base de datos")) {
                        NoCliente =  ((String) result.get("SincronizarClienteTotalResult")).split(",")[1];
                    } else {
                        new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente ,Respuesta false", variables_publicas.info + NoCliente +" *** "+urlStringDetalle, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                        return "false," + NoCliente;
                    }
                }
               // PedidoH.ActualizarPedido(IdPedido, NoPedido);
//
                return "true";
            } catch (Exception ex) {
                new Funciones().SendMail("Ha ocurrido un error al sincronizar el Cliente, Excepcion controlada ", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                Log.e("Error", ex.getMessage());
                return "false," + ex.getMessage() + "";
            }

        }

    }

}