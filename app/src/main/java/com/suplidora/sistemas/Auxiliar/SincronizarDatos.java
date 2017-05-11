package com.suplidora.sistemas.Auxiliar;

import android.content.Context;
import android.support.v4.content.res.ConfigurationHelper;
import android.util.Log;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.AccesoDatos.CartillasBcHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.AccesoDatos.ConfiguracionSistemaHelper;
import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by usuario on 5/5/2017.
 */

public class SincronizarDatos {


    private String urlClientes = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes";
    private String urlArticulos= variables_publicas.direccionIp+"/ServicioTotalArticulos.svc/BuscarTotalArticulo";
    final String urlVendedores = variables_publicas.direccionIp + "/ServicioPedidos.svc/ListaVendedores/";
    final String urlCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetCartillasBC/";
    final String urlDetalleCartillasBc = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetDetalleCartillasBC/";
    final String urlFormasPago= variables_publicas.direccionIp + "/ServicioPedidos.svc/FormasPago/";
    final String urlListPrecioEspecial= variables_publicas.direccionIp + "/ServicioPedidos.svc/ListPrecioEspecial/";
    final String urlGetConfiguraciones= variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones/";
    final String urlGetClienteSucursales= variables_publicas.direccionIp + "/ServicioPedidos.svc/GetClienteSucursales/";


    private String TAG = SincronizarDatos.class.getSimpleName();
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private ArticulosHelper ArticulosH;

    private CartillasBcHelper CartillasBcH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private FormaPagoHelper FormaPagoH;
    private PrecioEspecialHelper PrecioEspecialH;
    private ConfiguracionSistemaHelper ConfigSistemasH;
    private ClientesSucursalHelper ClientesSucH;

    public SincronizarDatos(DataBaseOpenHelper dbh , ClientesHelper Clientesh,
                            VendedoresHelper Vendedoresh,CartillasBcHelper CatillasBch,
                            CartillasBcDetalleHelper CartillasBcDetalleh,FormaPagoHelper FormaPagoh,
                            PrecioEspecialHelper PrecioEspecialh,ConfiguracionSistemaHelper ConfigSistemah,
                            ClientesSucursalHelper ClientesSuch,ArticulosHelper Articulosh) {
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
    }

    private String SincronizarArticulos()throws JSONException {
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlArticulos;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null)
            return null;
        //Log.e(TAG, "Response from url: " + jsonStrC);

        ArticulosH.EliminaArticulos();
        JSONObject jsonObjC = new JSONObject(jsonStrC);
        // Getting JSON Array node
        JSONArray articulos = jsonObjC.getJSONArray("BuscarTotalArticuloResult");

        DbOpenHelper.database.beginTransaction();
        try {
            // looping through All Contacts
            for (int i = 0; i < articulos.length(); i++) {
                JSONObject c = articulos.getJSONObject(i);

                String Codigo= c.getString("CODIGO_ARTICULO");
                String Nombre = c.getString("NOMBRE");
                String COSTO = c.getString("COSTO");
                String UNIDAD = c.getString("UNIDAD");
                String UnidadCaja = c.getString("UnidadCaja");
                String ISC = c.getString("ISC");
                String PorIVA = c.getString("PorIVA");
                String PrecioSuper=  c.getString("PrecioSuper");
                String PrecioDetalle =  c.getString("PrecioDetalle");
                String PrecioForaneo =  c.getString("PrecioForaneo");
                String PrecioMayorista =  c.getString("PrecioMayorista");
                String Bonificable =  c.getString("Bonificable");
                String AplicaPrecioDetalle =  c.getString("AplicaPrecioDetalle");
                String DESCUENTO_MAXIMO =  c.getString("DESCUENTO_MAXIMO");
                String detallista =  c.getString("detallista");

                ArticulosH.GuardarTotalArticulos(Codigo,Nombre,COSTO,UNIDAD,UnidadCaja,ISC,PorIVA, PrecioSuper,PrecioDetalle,PrecioForaneo,PrecioMayorista,Bonificable,AplicaPrecioDetalle,DESCUENTO_MAXIMO,detallista);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrC;
    }

    //Cliente
    public String SincronizarClientes()throws JSONException {
        /*******************************CLIENTES******************************/
        //************CLIENTES
        HttpHandler shC = new HttpHandler();
        String urlStringC = urlClientes + "/" + variables_publicas.CodigoVendedor + "/" + 3;
        String jsonStrC = shC.makeServiceCall(urlStringC);

        if (jsonStrC == null)
            return null;
        //Log.e(TAG, "Response from url: " + jsonStrC);

            ClientesH.EliminaClientes();
            JSONObject jsonObjC = new JSONObject(jsonStrC);
            // Getting JSON Array node
            JSONArray clientes = jsonObjC.getJSONArray("BuscarClientesResult");

        DbOpenHelper.database.beginTransaction();
        try {
            // looping through All Contacts
            for (int i = 0; i < clientes.length(); i++) {
                JSONObject c = clientes.getJSONObject(i);

                String IdCliente = c.getString("IdCliente");
                String CodCv = c.getString("CodCv");
                String Nombre = c.getString("Nombre");
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

                ClientesH.GuardarTotalClientes(IdCliente, CodCv, Nombre, FechaCreacion, Telefono, Direccion, IdDepartamento, IdMunicipio, Ciudad, Ruc, Cedula, LimiteCredito, IdFormaPago, IdVendedor, Excento, CodigoLetra, Ruta, Frecuencia, PrecioEspecial, FechaUltimaCompra);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrC;
    }

    //Vendedor
    public String SincronizarVendedores()throws JSONException {
        //************VENDEDORES
        HttpHandler shV = new HttpHandler();
        String urlStringV = urlVendedores;
        String jsonStrV = shV.makeServiceCall(urlStringV);

        if (jsonStrV == null)
            return null;
        //Log.e(TAG, "Response from url: " + jsonStrC);

        VendedoresH.EliminaVendedores();
        JSONObject jsonObjV = new JSONObject(jsonStrV);
        // Getting JSON Array node
        JSONArray vendedores = jsonObjV.getJSONArray("ListaVendedoresResult");

        DbOpenHelper.database.beginTransaction();
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

            VendedoresH.GuardarTotalVendedores(CODIGO, NOMBRE, DEPARTAMENTO, MUNICIPIO, CIUDAD, TELEFONO, CELULAR, CORREO, COD_ZONA, RUTA, codsuper, Status, detalle, horeca, mayorista, Super);
        }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrV;
    }

    //CartillasBc
    public String SincronizarCartillasBc()throws JSONException {
        HttpHandler shCartillas = new HttpHandler();
        String urlStringCartillas = urlCartillasBc;
        String jsonStrCartillas = shCartillas.makeServiceCall(urlStringCartillas);

        if (jsonStrCartillas == null)
            return null;

        CartillasBcH.EliminaCartillasBc();
        JSONObject jsonObjCartillas = new JSONObject(jsonStrCartillas);
        // Getting JSON Array node
        JSONArray cartillas = jsonObjCartillas.getJSONArray("GetCartillasBCResult");

        DbOpenHelper.database.beginTransaction();
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

                CartillasBcH.GuardarCartillasBc(id,codigo,fechaini,fechafinal,tipo,aprobado);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrCartillas;
    }

    //CartillasBcDetalle
    public String SincronizarCartillasBcDetalle()throws JSONException {
        HttpHandler shCartillasD = new HttpHandler();
        String urlStringCartillasD = urlDetalleCartillasBc;
        String jsonStrCartillasD = shCartillasD.makeServiceCall(urlStringCartillasD);

        if (jsonStrCartillasD == null)
            return null;

        CartillasBcDetalleH.EliminaCartillasBcDetalle();
        JSONObject jsonObjCartillasD = new JSONObject(jsonStrCartillasD);
        // Getting JSON Array node
        JSONArray cartillasD = jsonObjCartillasD.getJSONArray("GetDetalleCartillasBCResult");

        DbOpenHelper.database.beginTransaction();
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

                CartillasBcDetalleH.GuardarCartillasBcDetalle(id,itemV,descripcionV,cantidad,itemB,descripcionB,cantidadB,codigo,tipo,activo);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrCartillasD;
    }

    //FormaPago
    public String SincronizarFormaPago()throws JSONException {
        HttpHandler shFormaPago = new HttpHandler();
        String urlStringFormaPago = urlFormasPago;
        String jsonStrFormaPago = shFormaPago.makeServiceCall(urlStringFormaPago);

        if (jsonStrFormaPago == null)
            return null;

        FormaPagoH.EliminaFormaPago();
        JSONObject jsonObjFormaPago = new JSONObject(jsonStrFormaPago);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjFormaPago.getJSONArray("FormasPagoResult");

        DbOpenHelper.database.beginTransaction();
        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String CODIGO = c.getString("CODIGO");
                String NOMBRE = c.getString("NOMBRE");
                String DIAS = c.getString("DIAS");
                String EMPRESA = c.getString("EMPRESA");

                FormaPagoH.GuardarTotalFormaPago(CODIGO,NOMBRE,DIAS,EMPRESA);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrFormaPago;
    }

    //PrecioEspecial
    public String SincronizarPrecioEspecial()throws JSONException {
        HttpHandler shPrecioEspecial = new HttpHandler();
        String urlStringPrecioEspecial = urlListPrecioEspecial;
        String jsonStrPrecioEspecial = shPrecioEspecial.makeServiceCall(urlStringPrecioEspecial);

        if (jsonStrPrecioEspecial == null)
            return null;

        PrecioEspecialH.EliminaPrecioEspecial();
        JSONObject jsonObjPrecioEspecial = new JSONObject(jsonStrPrecioEspecial);
        // Getting JSON Array node
        JSONArray PrecioEspecial = jsonObjPrecioEspecial.getJSONArray("ListPrecioEspecialResult");

        DbOpenHelper.database.beginTransaction();
        try {
            // looping through All Contacts
            for (int i = 0; i < PrecioEspecial.length(); i++) {
                JSONObject c = PrecioEspecial.getJSONObject(i);

                String Id = c.getString("Id");
                String CodigoArticulo = c.getString("CodigoArticulo");
                String IdCliente = c.getString("IdCliente");
                String Descuento = c.getString("Descuento");
                String Precio = c.getString("Precio");

                PrecioEspecialH.GuardarPrecioEspecial(Id,CodigoArticulo,IdCliente,Descuento,Precio);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrPrecioEspecial;
    }

    //ConfiguracionSistema
    public String SincronizarConfiguracionSistema()throws JSONException {
        HttpHandler shConfiguracionSistema = new HttpHandler();
        String urlStringConfiguracionSistema = urlGetConfiguraciones;
        String jsonStrConfiguracionSistema = shConfiguracionSistema.makeServiceCall(urlStringConfiguracionSistema);

        if (jsonStrConfiguracionSistema == null)
            return null;

        ConfigSistemasH.EliminaConfigSistema();
        JSONObject jsonObjConfiguracionSistema = new JSONObject(jsonStrConfiguracionSistema);
        // Getting JSON Array node
        JSONArray FormaPago = jsonObjConfiguracionSistema.getJSONArray("GetConfiguracionesResult");

        DbOpenHelper.database.beginTransaction();
        try {
            // looping through All Contacts
            for (int i = 0; i < FormaPago.length(); i++) {
                JSONObject c = FormaPago.getJSONObject(i);

                String Id = c.getString("Id");
                String Sistema = c.getString("Sistema");
                String Configuracion = c.getString("Configuracion");
                String Valor = c.getString("Valor");
                String Activo = c.getString("Activo");

                ConfigSistemasH.GuardarConfiguracionSistema(Id,Sistema,Configuracion,Valor,Activo);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrConfiguracionSistema;
    }

    //ClientesSucursal
    public String SincronizarClientesSucursal()throws JSONException {
        HttpHandler shClientesSucursal = new HttpHandler();
        String urlStringClientesSucursal = urlGetClienteSucursales;
        String jsonStrClientesSucursal = shClientesSucursal.makeServiceCall(urlStringClientesSucursal);

        if (jsonStrClientesSucursal == null)
            return null;

        ClientesSucH.EliminaClientesSucursales();
        JSONObject jsonObjClientesSucursal = new JSONObject(jsonStrClientesSucursal);
        // Getting JSON Array node
        JSONArray PrecioEspecial = jsonObjClientesSucursal.getJSONArray("GetClienteSucursalesResult");

        DbOpenHelper.database.beginTransaction();
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

              ClientesSucH.GuardarTotalClientesSucursal(CodSuc,CodCliente,Sucursal,Ciudad,DeptoID,Direccion,FormaPagoID);
            }
            DbOpenHelper.database.setTransactionSuccessful();
        }
        finally {
            DbOpenHelper.database.endTransaction();
        }
        return  jsonStrClientesSucursal;
    }

    public void SincronizarTodo()throws JSONException {
        SincronizarArticulos();
        SincronizarClientes();
        SincronizarVendedores();
        SincronizarCartillasBc();
        SincronizarCartillasBcDetalle();
        SincronizarFormaPago();
        SincronizarPrecioEspecial();
        SincronizarClientesSucursal();
        SincronizarConfiguracionSistema();
    }


}