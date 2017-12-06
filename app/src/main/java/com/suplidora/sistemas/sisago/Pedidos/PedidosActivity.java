package com.suplidora.sistemas.sisago.Pedidos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConfiguracionSistemaHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConsolidadoCargaDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConsolidadoCargaHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Articulo;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.ClienteSucursal;
import com.suplidora.sistemas.sisago.Entidades.Configuraciones;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Pedido;
import com.suplidora.sistemas.sisago.Entidades.PedidoDetalle;
import com.suplidora.sistemas.sisago.Entidades.PrecioEspecial;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PedidosActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = PedidosActivity.class.getSimpleName();
    private boolean MensajeCaja;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    //region Declaracion de controles
    private EditText txtCodigoArticulo;
    private EditText txtDescuento;
    private EditText txtObservaciones;
    private TextView lblCantidad;
    private TextView txtPrecioArticulo;
    private TextView lblDescripcion;
    private TextView lblNombCliente;
    private TextView lblCodCliente;
    private TextView lblNoPedido;
    private TextView lblDescripcionArticulo;
    private TextView lblTc;
    private TextView lblSubTotalCor;
    private TextView lblIvaCor;
    private TextView lblTotalCor;
    private TextView lblSubTotalDol;
    private TextView lblIvaDol;
    private TextView lblTotalDol;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private TextView lblUM;
    private TextView lblExistentias;
    private Button btnAgregar;
    private Button btnBuscar;
    private Button btnOK;
    private Button btnGuardar;
    private Button btnCancelar;
    private EditText txtCantidad;
    private Spinner cboVendedor;
    private Spinner cboSucursal;
    private Spinner cboCondicion;
    private ListView lv;
    private ListView lvItem;
    private SimpleAdapter adapter;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    private String CodigoArticulo;
    private String existencia = "N/A";
    private String CodigoItemAgregado = "";
    private SincronizarDatos sd;
    private boolean isOnline = false;

    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones";
    //endregion

    //region Declaracion de variables
    Configuraciones ConfigPromo024;
    Configuraciones ConfigPromoSalnica;
    String IMEI = "";
    String NoPedido = "";
    private String focusedControl = "";
    static final String KEY_IdCliente = "IdCliente";
    static final String KEY_NombreCliente = "Nombre";
    static final String KEY_NombreCodCv = "CodCv";

    private Articulo articulo;
    private DecimalFormat df;
    private FormaPago condicion;
    private ClienteSucursal sucursal;
    private double valorPolitica = 3000;
    public static ArrayList<HashMap<String, String>> listaArticulos;
    public static ArrayList<HashMap<String, String>> listaArticulosItem;
    public boolean Estado;
    public double total;
    public double subtotal;
    private Cliente cliente;
    private double tasaCambio = 0;
    private double subTotalPrecioSuper = 0;
    private Pedido pedido;
    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private ClientesSucursalHelper ClientesSucursalH;
    private FormaPagoHelper FormaPagoH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private PrecioEspecialHelper PrecioEspecialH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private PedidosDetalleHelper PedidoDetalleH;
    private ConfiguracionSistemaHelper ConfiguracionSistemaH;
    private CartillasBcHelper CartillasBcH;
    private ConsolidadoCargaHelper ConsolidadoCargaH;
    private ConsolidadoCargaDetalleHelper ConsolidadoCargaDetalleH;
    private ConfiguracionSistemaHelper ConfigSistemaH;
    private DevolucionesHelper DevolucionH;
    private DevolucionesDetalleHelper DevolucionDetalleH;
    private PedidosHelper PedidoH;
    private String CodigoLetra = "";
    private String jsonPedido = "";
    private boolean finalizar = false;
    private String TipoPrecio = "";
    private boolean guardadoOK = false;
    private Vendedor vendedor = null;
    private double PrecioItem = 0;

    private String busqueda = "1";
    private int tipoBusqueda = 1;
    private boolean validarTipoBusqueda;
    private int IdDepartamento;
    private String Nombre;
    private boolean editar = false;
    private boolean pedidoLocal;
    private ConfiguracionSistemaHelper ConfigH;
    static final int DEFAULT_THREAD_POOL_SIZE = 4;
    ExecutorService executorService = Executors.newCachedThreadPool();
    //endregion


    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);

        pedido = new Pedido();
        DbOpenHelper = new DataBaseOpenHelper(PedidosActivity.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ConfigH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        CartillasBcH = new CartillasBcHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        ConfigSistemaH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        DevolucionH = new DevolucionesHelper(DbOpenHelper.database);
        DevolucionDetalleH = new DevolucionesDetalleHelper(DbOpenHelper.database);
//        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
//                CartillasBcDetalleH,
//                FormaPagoH,
//                PrecioEspecialH, ConfigH, ClientesSucursalH, ArticulosH, UsuariosH,  PedidoH, PedidoDetalleH);

        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH,
                FormaPagoH,
                PrecioEspecialH, ConfigH, ClientesSucursalH, ArticulosH, UsuariosH, PedidoH, PedidoDetalleH, DevolucionH, DevolucionDetalleH);


        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }


        /*Esto lo dejamos manual por falta de tiempo :V */
        ConfigPromo024 = ConfigSistemaH.BuscarValorConfig("Promo 024");
        ConfigPromoSalnica = ConfigSistemaH.BuscarValorConfig("Promo Salnicsa");

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);

        listaArticulos = new ArrayList<HashMap<String, String>>();
        listaArticulosItem = new ArrayList<HashMap<String, String>>();
        DbOpenHelper = new DataBaseOpenHelper(PedidosActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        ConfiguracionSistemaH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        cboSucursal = (Spinner) findViewById(R.id.cboSucursal);
        cboCondicion = (Spinner) findViewById(R.id.cboCondicion);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        lblTc = (TextView) findViewById(R.id.lblTC);
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        // Displaying all values on the screen
        final TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        TextView lblRuta = (TextView) findViewById(R.id.lblRuta);
        TextView lblCanal = (TextView) findViewById(R.id.lblCanal);
        final Spinner cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        TextView lblNombre = (TextView) findViewById(R.id.lblNombreCliente);
        //Obtenemos las referencias a los controles
        txtCodigoArticulo = (EditText) findViewById(R.id.txtCodigoArticulo);
        lblCodCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView) findViewById(R.id.lblNombreCliente);
        lblDescripcionArticulo = (TextView) findViewById(R.id.lblDescripcionArticulo);
        lblUM = (TextView) findViewById(R.id.lblUMArticulo);
        lblExistentias = (TextView) findViewById(R.id.lblExistencia);
        lblNoPedido = (TextView) findViewById(R.id.lblNoPedido);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        txtCantidad.setFocusable(true);
        txtCantidad.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    if (!txtDescuento.isEnabled()) {
                        btnAgregar.performClick();
                    }
                    focusedControl = "txtCantidad";
                    return false;
                }
                return true;
            }
        });
        Spinner prueba = (Spinner) findViewById(R.id.cboCondicion);
        lv = (ListView) findViewById(R.id.listPedido);

        registerForContextMenu(lv);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);

        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

// Disallow the touch request for parent scroll on touch of child view
                scrollView.requestDisallowInterceptTouchEvent(true);

                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
            }
        });
        txtDescuento = (EditText) findViewById(R.id.txtDescuento);

        txtDescuento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if (!hasFocus && txtDescuento.getText().length() > 0) {
                        if (articulo == null) {
                            txtDescuento.setText("0");
                        } else {
                            ValidarDescuento();
                        }
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
            }
        });
        txtObservaciones = (EditText) findViewById(R.id.txtObservacion);
        txtPrecioArticulo = (TextView) findViewById(R.id.txtPrecioArticulo);
        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));
        lblSubTotalCor = (TextView) findViewById(R.id.lblSubTotalCor);
        lblIvaCor = (TextView) findViewById(R.id.lblIvaCor);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblSubTotalDol = (TextView) findViewById(R.id.lblSubTotalDol);
        lblIvaDol = (TextView) findViewById(R.id.lblIvaDol);
        lblTotalDol = (TextView) findViewById(R.id.lblTotalDol);
        // getting intent data
        Intent in = getIntent();


        // Get XML values from previous intent
        pedido.setIdCliente(in.getStringExtra(KEY_IdCliente));
        Nombre = in.getStringExtra(KEY_NombreCliente);
        pedido.setCod_cv(in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodCv).toString().replace("Cod_Cv: ", ""));

        if (in.getSerializableExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido) != null) {

            if (in.getSerializableExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido).toString().startsWith("-")) {
                pedidoLocal = true;

            } else {

                pedidoLocal = false;
            }

            editar = true;

            listaArticulos.clear();
            pedido = PedidoH.GetPedido(in.getStringExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
            listaArticulos = PedidoDetalleH.ObtenerPedidoDetalleArrayList(pedido.getCodigoPedido());
            for (HashMap<String, String> item : listaArticulos) {
                Articulo art = ArticulosH.BuscarArticulo(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo));
                item.put("Cod", item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).substring(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).length() - 3));
                item.put("IdProveedor", art.getIdProveedor());
                item.put("UnidadCajaVenta", art.getUnidadCajaVenta());
            }
            txtObservaciones.setText(pedido.getObservacion());
            lblNoPedido.setText("PEDIDO N°: " + pedido.getCodigoPedido());

            List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(pedido.getIdCliente());
            int indice;
            for (int i = 0; i < sucursales.size(); i++) {
                if (sucursales.get(i).getCodSuc().equals(pedido.getIdSucursal())) {
                    final int finalI = i;
                    cboSucursal.post(new Runnable() {
                        public void run() {
                            cboSucursal.setSelection(finalI);
                        }
                    });
                    break;
                }
            }


            RefrescarGrid();
            CalcularTotales();
        } else {
            cboSucursal.setSelection(0);
        }

        // Loading spinner data from database
        CargaDatosCombo();

        if (variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle") && variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor") && cliente.getEmpleado().equalsIgnoreCase("0")) {
            txtDescuento.setEnabled(false);
        }

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscar = (Button) findViewById(R.id.btnBuscar);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PedidosActivity.this.onBackPressed();
            }
        });
        txtCodigoArticulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    btnBuscar.performClick();
                    focusedControl = "txtCodigoArticulo";
                    return false;
                }
                return true;
            }
        });

        lblCodigoCliente.setText(cliente.getCodigoLetra());
        lblNombre.setText(Nombre);
        lblRuta.setText(cliente.getRuta());
        lblCanal.setText(cliente.getTipo());

        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//
                BuscarArticulo();
                validarTipoBusqueda = true;
                btnOK.performClick();
                txtCantidad.requestFocus();
                focusedControl = "";
                // }


            }
        });
        final List<PedidoDetalle> lstPedidoDetalle = new ArrayList<>();
        btnAgregar.setOnClickListener(new OnClickListener() {
                                          public void onClick(View v) {

                                              try {

                                                  if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                      txtCantidad.setError("Ingrese un valor");
                                                      return;
                                                  }

                                                  if (Double.parseDouble(txtCantidad.getText().toString()) < 1) {
                                                      txtCantidad.setError("Ingrese un valor mayor a 0");
                                                      return;
                                                  }
                                                  if (articulo == null) {
                                                      txtCodigoArticulo.setError("Ingrese un valor");
                                                      return;
                                                  }
                                                  boolean repetido = EsArticuloRepetido(txtCodigoArticulo.getText().toString());
                                                  if (repetido) {
                                                      MensajeAviso("Este artículo ya ha sigo agregado al pedido.");
                                                      return;
                                                  }

                                                  if (!ValidarDescuento()) {
                                                      return;
                                                  }

                                                  double cantidad = Double.parseDouble(txtCantidad.getText().toString());

                                                  if (PrecioItem == 0) {
                                                      MensajeAviso("Ha ocurrido un error por favor seleccione nuevamente el articulo");
                                                      return;
                                                  }
                                                  HashMap<String, String> itemPedidos = new HashMap<>();
                                                  if (AgregarDetalle(itemPedidos)) {
                                                      MensajeCaja = true;
                                                      ObtenerPrecio(itemPedidos, articulo.getCodigo(), false);
                                                      LimipiarDatos(MensajeCaja);
                                                      subTotalPrecioSuper = 0;
                                                      for (HashMap<String, String> item : listaArticulos) {
                                                          subTotalPrecioSuper += Double.parseDouble(item.get("SubTotal").replace(",", ""));
                                                      }
                                                      RecalcularDetalle();
                                                      CalcularTotales();
                                                      AplicarBonificacionCartillas();
                                                      AplicarPromocionAmsa();
                                                      AplicarPromocion024();
                                                      AplicarPromocionSalnica();
                                                      RefrescarGrid();
                                                      CalcularTotales();
                                                      InputMethodManager inputManager = (InputMethodManager)
                                                              getSystemService(Context.INPUT_METHOD_SERVICE);

                                                      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                              InputMethodManager.RESULT_SHOWN);
                                                  }


                                              } catch (Exception e) {
                                                  cliente = ClientesH.BuscarCliente(pedido.getIdCliente(), pedido.getCod_cv());
                                                  MensajeAviso(e.getMessage());
                                              }
                                          }
                                      }
        );
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CodigoLetra = lblCodigoCliente.getText().toString();

                    Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });

        variables_publicas.PermitirVentaDetAMayoristaXCaja = ConfiguracionSistemaH.BuscarValorConfig("PermitirVentaDetAMayoristaXCaja").getValor();
        variables_publicas.AplicarPrecioMayoristaXCaja = ConfiguracionSistemaH.BuscarValorConfig("AplicarPrecioMayoristaXCaja").getValor();

        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
            cboVendedor.setEnabled(false);
        } else {
            cboVendedor.setEnabled(true);

        }

    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
    }

    private void AplicarBonificacionCartillas() {


        if (cliente.getTipo().equalsIgnoreCase("Super")) {
            return;
        }

        ArrayList<HashMap<String, String>> listaTemp = new ArrayList<HashMap<String, String>>();
        ;
        /*Primero eliminamos todas la bonificaciones para poder recalcular*/
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> item = listaArticulos.get(i);
            if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {
                listaTemp.add(item);
            }
        }
        listaArticulos = listaTemp;
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> itemPedidos = listaArticulos.get(i);
            /*Esta validacion esta de mas pero alli la dejamos por si las moscas*/
            if (itemPedidos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {
                HashMap<String, String> itemBonificado = CartillasBcDetalleH.BuscarBonificacion(itemPedidos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo), variables_publicas.usuario.getCanal(), variables_publicas.FechaActual, itemPedidos.get("Cantidad"));
                Articulo articuloB = ArticulosH.BuscarArticulo(itemBonificado.get("itemB"));

                 /*Aqui validamos la bonificacion por cartillas promocionales*/
                if (itemBonificado.size() > 0) {

                    /*Es se pone para evitar error si el articulo bonificado esta desactivado*/
                    if (articuloB != null) {
                        boolean existe = false;
                        for (HashMap<String, String> item : listaArticulos) {
                            /*Si ya existe actualizamos la cantidad bonificada*/
                            if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(itemBonificado.get("itemB")) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("B")) {
                                existe = true;
                                int factor = (int) Math.floor(Double.parseDouble(itemPedidos.get("Cantidad")) / Double.parseDouble(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad)));
                                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, String.valueOf(((int) Double.parseDouble(item.get("Cantidad"))) + ((int) (factor * Double.parseDouble(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB))))));
                                break;
                            }
                        }
                         /*Si no existe lo agregamos*/
                        if (existe == false) {
                            //Validamos que solamente se puedan ingresar 18 articulos
                            if (listaArticulos.size() == 18 && cliente.getDetallista().equalsIgnoreCase("false")) {

                                /*Eliminamos el ultimo item NO BONIFICADO agregado*/
                                HashMap<String, String> ultimoItemP = null;
                                for (HashMap<String, String> itemsel : listaArticulos) {
                                    if (itemsel.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {
                                        ultimoItemP = itemsel;
                                    }
                                }
                                if (ultimoItemP != null) {
                                    listaArticulos.remove(ultimoItemP);
                                }
                                MensajeAviso("No se puede agregar el producto seleccionado,ya que posee bonificacion y excede el limite de 18 productos para un pedido Mayorista");
                                break;
                            }

                            HashMap<String, String> articuloBonificado = new HashMap<>();
                            articuloBonificado.put("CodigoPedido", pedido.getCodigoPedido());
                            articuloBonificado.put("Cod", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB).split("-")[itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB).split("-").length - 1]);
                            articuloBonificado.put("CodigoArticulo", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB));
                            articuloBonificado.put("Um", articuloB == null ? "UNIDAD" : articuloB.getUnidad());
                            int factor = (int) Math.floor(Double.parseDouble(itemPedidos.get("Cantidad")) / Double.parseDouble(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidad)));
                            articuloBonificado.put("Cantidad", String.valueOf((int) (factor * Double.parseDouble(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_cantidadB)))));
                            articuloBonificado.put("Precio", "0");
                            articuloBonificado.put("TipoPrecio", "0");
                            articuloBonificado.put("Descripcion", "**" + itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_descripcionB));
                            articuloBonificado.put("Costo", "0");
                            articuloBonificado.put("PorDescuento", "0");
                            articuloBonificado.put("TipoArt", "B");
                            articuloBonificado.put("BonificaA", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV));
                            articuloBonificado.put("Isc", "0");
                            articuloBonificado.put("PorcentajeIva", "0");
                            articuloBonificado.put("Descuento", "0");
                            articuloBonificado.put("Iva", "0");
                            articuloBonificado.put("SubTotal", "0");
                            articuloBonificado.put("Total", "0");
                            articuloBonificado.put("TipoPrecio", "Bonificacion");
                            articuloBonificado.put("IdProveedor", articuloB.getIdProveedor());
                            articuloBonificado.put("UnidadCajaVenta", articuloB.getUnidadCajaVenta());
                            listaArticulos.add(articuloBonificado);
                        }
                    }

                }
            }


        }


    }

    private void AplicarPromocion024() {

        if (cliente.getTipo().equalsIgnoreCase("Super")) {
            return;
        }

          /*Estas puras mamadas de android para comparar 2 fechas :V*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fechaActual = new Date();
        Date fechaLimite = new Date();
        try {
            fechaActual = dateFormat.parse(variables_publicas.FechaActual);
            /*Esta fecha limite la definimos en base a correo de GALA con fecha: 2017-10-07 Titulo:  RE: PROMOCION HENKEL 9 OCTUBRE AL 31 DE DICIEMBRE. */
            fechaLimite = dateFormat.parse("2018-01-01 00:00:00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          /*Si no hay bonificacion en cartilla.. Verificamos si existe bonificacion 024 ... esto queda hehizo por falta de tiempo :V */
        if (fechaActual.before(fechaLimite) && (cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo")) && ConfigPromo024 != null && ConfigPromo024.getActivo().equalsIgnoreCase("true")) {
            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 17 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que posee bonificacion y excede el limite de 18 productos para un pedido Mayorista");
                return;
            }


            Articulo articuloB = ArticulosH.BuscarArticulo("4000-01-01-01-024");
            List<String> items = Arrays.asList(ConfigPromo024.getValor().split(","));

            boolean existe = false;
            int cantidad = 0;

            /*Primero sumamos las cantidades de los items promocionados*/
            for (HashMap<String, String> item : listaArticulos) {
                if (items.contains(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo)) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P") && Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) >= 120) {
                    cantidad += (int) Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
                    CodigoItemAgregado = item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo);
                }
            }

            for (HashMap<String, String> item : listaArticulos) {
                   /*Si ya existe actualizamos la cantidad bonificada actualizamos el valor o borramos segun si aplica a la bonificacion*/
                if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(articuloB.getCodigo()) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("B") && cantidad >= 120) {
                    existe = true;
                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, String.valueOf(((int) Math.floor(cantidad * 0.2))));
                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, CodigoItemAgregado);
                    break;
                }
            }
            //lo borramos si no cumple con la promocion
            if (cantidad < 120) {
                for (HashMap<String, String> item : listaArticulos) {
                    if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(articuloB.getCodigo()) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("B")) {
                        listaArticulos.remove(item);
                    }
                }
            } else {

               /*Si no existe lo agregamos*/
                if (existe == false) {

                    HashMap<String, String> articuloBonificado = new HashMap<>();
                    articuloBonificado.put("CodigoPedido", pedido.getCodigoPedido());
                    articuloBonificado.put("Cod", "024");
                    articuloBonificado.put("CodigoArticulo", articuloB.getCodigo());
                    articuloBonificado.put("Um", "UNIDAD");
                    articuloBonificado.put("Cantidad", String.valueOf((int) Math.floor(cantidad * 0.2))); //Bonificamos el 20% de la cantidad comprada
                    articuloBonificado.put("Precio", "0");
                    articuloBonificado.put("TipoPrecio", "0");
                    articuloBonificado.put("Descripcion", "**" + "GEL XTREME ESENCIA ATRACTION 12/250 GR");
                    articuloBonificado.put("Costo", "0");
                    articuloBonificado.put("PorDescuento", "0");
                    articuloBonificado.put("TipoArt", "B");
                    articuloBonificado.put("BonificaA", CodigoItemAgregado);
                    articuloBonificado.put("Isc", "0");
                    articuloBonificado.put("PorcentajeIva", "0");
                    articuloBonificado.put("Descuento", "0");
                    articuloBonificado.put("Iva", "0");
                    articuloBonificado.put("SubTotal", "0");
                    articuloBonificado.put("Total", "0");
                    articuloBonificado.put("TipoPrecio", "Bonificacion");
                    articuloBonificado.put("IdProveedor", articuloB.getIdProveedor());
                    articuloBonificado.put("UnidadCajaVenta", articuloB.getUnidadCajaVenta());
                    listaArticulos.add(articuloBonificado);
                    CodigoItemAgregado = "";
                }
            }

            RefrescarGrid();
            CalcularTotales();

        }
    }

    private void AplicarPromocionSalnica() {

        if (cliente.getTipo().equalsIgnoreCase("Super")) {
            return;
        }

          /*Estas puras mamadas de android para comparar 2 fechas :V*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date fechaActual = new Date();
        Date fechaLimite = new Date();
        try {
            fechaActual = dateFormat.parse(variables_publicas.FechaActual);
            /*Esta fecha limite la definimos en base a correo de Veronica con fecha: 2017-12-04 Titulo:  RE: Cartilla Salnicsa. */
            fechaLimite = dateFormat.parse("2018-01-01 00:00:00");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          /*Si no hay bonificacion en cartilla.. Verificamos si existe bonificacion 1069 ... esto queda hehizo por falta de tiempo :V */
        if (fechaActual.before(fechaLimite) && (cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Detalle")) && ConfigPromoSalnica != null && ConfigPromoSalnica.getActivo().equalsIgnoreCase("true")) {
            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 17 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que posee bonificacion y excede el limite de 18 productos para un pedido Mayorista");
                return;
            }


            Articulo articuloB = ArticulosH.BuscarArticulo("4000-02-01-04-1069");
            List<String> items = Arrays.asList(ConfigPromoSalnica.getValor().split(","));

            boolean existe = false;
            int cantidad = 0;
            int cantidadB =0;
            /*Primero sumamos las cantidades de los items promocionados*/
            for (HashMap<String, String> item : listaArticulos) {
                if (items.contains(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo)) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {
                    cantidad += (int) Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
                    CodigoItemAgregado = item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo);
                }

            }
            if (cantidad >=1 && cantidad <=49) {
                cantidadB = cantidad*2;
            }else if (cantidad >=50 && cantidad <=99){
                cantidadB = (int) Math.floor(cantidad*2.5);
            }
            else if (cantidad >=100)
            {
                cantidadB = cantidad*3;
            }
            for (HashMap<String, String> item : listaArticulos) {
                   /*Si ya existe actualizamos la cantidad bonificada actualizamos el valor o borramos segun si aplica a la bonificacion*/
                if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(articuloB.getCodigo()) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("B") && cantidad >= 120) {
                    existe = true;
                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad, String.valueOf(cantidadB));
                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA, CodigoItemAgregado);
                    break;
                }
            }

            //lo borramos si no cumple con la promocion
            if (cantidad < 1) {
                for (HashMap<String, String> item : listaArticulos) {
                    if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(articuloB.getCodigo()) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("B")) {
                        listaArticulos.remove(item);
                    }
                }
            } else {

               /*Si no existe lo agregamos*/
                if (existe == false) {

                    HashMap<String, String> articuloBonificado = new HashMap<>();
                    articuloBonificado.put("CodigoPedido", pedido.getCodigoPedido());
                    articuloBonificado.put("Cod", "1069");
                    articuloBonificado.put("CodigoArticulo", articuloB.getCodigo());
                    articuloBonificado.put("Um", "UNIDAD");
                    articuloBonificado.put("Cantidad", String.valueOf(cantidadB)); //Bonificamos el 20% de la cantidad comprada
                    articuloBonificado.put("Precio", "0");
                    articuloBonificado.put("TipoPrecio", "0");
                    articuloBonificado.put("Descripcion", "**" + "SAL DOÑA VERO 1/1/400 GR PROMO");
                    articuloBonificado.put("Costo", "0");
                    articuloBonificado.put("PorDescuento", "0");
                    articuloBonificado.put("TipoArt", "B");
                    articuloBonificado.put("BonificaA", CodigoItemAgregado);
                    articuloBonificado.put("Isc", "0");
                    articuloBonificado.put("PorcentajeIva", "0");
                    articuloBonificado.put("Descuento", "0");
                    articuloBonificado.put("Iva", "0");
                    articuloBonificado.put("SubTotal", "0");
                    articuloBonificado.put("Total", "0");
                    articuloBonificado.put("TipoPrecio", "Bonificacion");
                    articuloBonificado.put("IdProveedor", articuloB.getIdProveedor());
                    articuloBonificado.put("UnidadCajaVenta", articuloB.getUnidadCajaVenta());
                    listaArticulos.add(articuloBonificado);
                    CodigoItemAgregado = "";
                }
            }

            RefrescarGrid();
            CalcularTotales();

        }
    }
    private void SincronizarConfig() {
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetValorConfig().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new GetValorConfig().execute();
        }
    }

    private void ValidarUltimaVersion() {

        String latestVersion = "";
        String currentVersion = getCurrentVersion();
        variables_publicas.VersionSistema = currentVersion;
        try {

            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new GetLatestVersion().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new GetLatestVersion().execute();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }

    private void AplicarPromocionAmsa() {
        int cantidadBonificada = 0;
        double Subtotal = 0.0;


        boolean promoActiva = ConfiguracionSistemaH.BuscarValorConfig("Promo Amsa 4000-01-01-01-811").getActivo().equalsIgnoreCase("true");

        /*Validamos que el cliente sea canal detalle sino salimos*/
        if (!cliente.getTipo().equalsIgnoreCase("Detalle") || promoActiva == false) {
            return;
        }

        HashMap<String, String> itemBonificado = null;

        for (HashMap<String, String> item : listaArticulos) {
            if (item.get("IdProveedor").equals("207") && item.get("TipoArt").equals("P")) { //Si es producto de amsa
                Subtotal += Double.parseDouble(item.get("SubTotal").replace(",", ""));
            }

           /*Ubicamos el item bonificado*/
            if (item.get("CodigoArticulo").equals("4000-01-01-01-811") && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("B")) {
                itemBonificado = item;
            }

        }
        cantidadBonificada = (int) Math.floor(Subtotal / 100.00);
        /*Si ya existe validamos la cantidad*/
        if (itemBonificado != null) {
            //Si la bonificacion es mayor a 0: actualizamos la cantidad
            if (cantidadBonificada > 0) {
                itemBonificado.put("Cantidad", String.valueOf(cantidadBonificada));
            } else { //Si es 0 eliminamos bonificacion
                listaArticulos.remove(itemBonificado);
            }

        } else { /*Si la bonificacion no esta en la lista validamos si agregarla*/
               /*Si es mayor a 0 agregamos la bonificacion*/
            if (cantidadBonificada > 0) {
                Articulo articuloB = ArticulosH.BuscarArticulo("4000-01-01-01-811");
                HashMap<String, String> articuloBonificado = new HashMap<>();
                articuloBonificado.put("CodigoPedido", pedido.getCodigoPedido());
                articuloBonificado.put("Cod", "811");
                articuloBonificado.put("CodigoArticulo", "4000-01-01-01-811");
                articuloBonificado.put("Um", articuloB == null ? "UNIDAD" : articuloB.getUnidad());
                articuloBonificado.put("Cantidad", String.valueOf(cantidadBonificada));
                articuloBonificado.put("Precio", "0");
                articuloBonificado.put("TipoPrecio", "0");
                articuloBonificado.put("Descripcion", "**" + articuloB.getNombre());
                articuloBonificado.put("Costo", "0");
                articuloBonificado.put("PorDescuento", "0");
                articuloBonificado.put("TipoArt", "B");
                articuloBonificado.put("BonificaA", "4000-01-01-01-811");
                articuloBonificado.put("Isc", "0");
                articuloBonificado.put("PorcentajeIva", "0");
                articuloBonificado.put("Descuento", "0");
                articuloBonificado.put("Iva", "0");
                articuloBonificado.put("SubTotal", "0");
                articuloBonificado.put("Total", "0");
                articuloBonificado.put("TipoPrecio", "Bonificacion");
                articuloBonificado.put("IdProveedor", articuloB.getIdProveedor());
                articuloBonificado.put("UnidadCajaVenta", articuloB.getUnidadCajaVenta());
                listaArticulos.add(articuloBonificado);
            } else {
                //Eliminamos la bonificacion
                for (HashMap<String, String> item : listaArticulos) {
                    if (item.get("CodigoArticulo").equalsIgnoreCase("4000-01-01-01-811") && item.get("TipoArt").equalsIgnoreCase("B")) {
                        listaArticulos.remove(item);
                        break;
                    }
                }

            }

        }
        RefrescarGrid();
    }

    private boolean ValidarDescuento() {

        double descuento = Double.parseDouble(txtDescuento.getText().toString().isEmpty() ? "0" : txtDescuento.getText().toString());
        double descuentoArticulo = Double.parseDouble(articulo.getDescuentoMaximo());
        double descuentoCliente = Double.parseDouble(cliente.getDescuento());
        double descuentoMayor = descuentoArticulo > descuentoCliente ? descuentoArticulo : descuentoCliente;
        if (descuento > descuentoMayor && !cliente.getPrecioEspecial().equalsIgnoreCase("true")) {
            MensajeAviso("El descuento maximo permitido para este producto es de: " + String.valueOf(descuentoMayor));
            txtDescuento.setText("");
//            txtDescuento.requestFocus();
            return false;
        }
        return true;
    }

    private void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv.setSelection(adapter.getCount() - 1);
            }
        });
    }
    //endregion

    //region Metodos

    private boolean SincronizarPedido(HashMap<String, String> pedido) {
        Gson gson = new Gson();

        jsonPedido = gson.toJson(pedido);

        try {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new SincronizardorPedidos().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new SincronizardorPedidos().execute();
            }
        } catch (final Exception ex) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            ex.getMessage(),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
            //MensajeAviso(ex.getMessage());
        }

        return false;
    }

    private boolean Guardar() {
        if (lv.getCount() <= 0) {
            MensajeAviso("No se puede guardar el pedido, Debe ingresar al menos 1 item");
            return false;
        }

        String mensaje = "";
        if (Double.parseDouble(lblSubTotalCor.getText().toString().replace(",", "")) < valorPolitica && (cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo")) && variables_publicas.AplicarPrecioMayoristaXCaja.equalsIgnoreCase("0")) {
            mensaje = "Este cliente es de tipo FORANEO, pero el pedido es menor a C$3,000 por lo que se guardará como tipo :DETALLE. Esta seguro que desea continuar?";
            pedido.setTipo("Detalle");
        } else {
            pedido.setTipo(cliente.getTipo());
            mensaje = "Esta seguro que desea guardar el pedido?";
        }
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DbOpenHelper.database.beginTransaction();
                        if (GuardarPedido()) {
                            DbOpenHelper.database.setTransactionSuccessful();
                            DbOpenHelper.database.endTransaction();
                            SincronizarPedido(PedidoH.ObtenerPedido(pedido.getCodigoPedido()));
                        } else {
                            DbOpenHelper.database.endTransaction();
                        }

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

        return true;
    }

    private boolean GuardarPedido() {
        String codSuc = sucursal == null ? "0" : sucursal.getCodSuc();
        pedido.setIdSucursal(codSuc);
        IMEI = variables_publicas.IMEI;
        //Guardamos el Header


        pedido.setIdVendedor(String.valueOf(pedido.getIdVendedor()));
        pedido.setIdCliente(String.valueOf(pedido.getIdCliente()));
//        pedido.setCod_cv(cliente.getCodCv());
        pedido.setObservacion(Funciones.Codificar(txtObservaciones.getText().toString()));
        pedido.setIdFormaPago(condicion.getCODIGO());
        pedido.setFecha(variables_publicas.FechaActual);
        pedido.setUsuario(variables_publicas.usuario.getUsuario());
        pedido.setIMEI(IMEI);

        //Esto lo ponemos para cuando es editar
        PedidoH.EliminaPedido(pedido.getCodigoPedido());
        PedidoDetalleH.EliminarDetallePedido(pedido.getCodigoPedido());


        if (IMEI == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("Es necesario configurar el permiso \"Administrar llamadas telefonicas\" para porder guardar un pedido, Desea continuar ? ")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                            loadIMEI();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

            return false;

        }
        Funciones.GetLocalDateTime();


        boolean saved = PedidoH.GuardarPedido(pedido.getCodigoPedido(), pedido.getIdVendedor(), pedido.getIdCliente(), pedido.getCod_cv(), pedido.getTipo(),
                txtObservaciones.getText().toString(), condicion.getCODIGO(), pedido.getIdSucursal(),
                variables_publicas.FechaActual, variables_publicas.usuario.getUsuario(), IMEI, String.valueOf(subtotal), String.valueOf(total));

        if (!saved) {
            MensajeAviso("Ha Ocurrido un error al guardar los datos");
            return false;
        }
        //Guardamos el detalle del pedido
        for (HashMap<String, String> item : listaArticulos) {
            saved = PedidoDetalleH.GuardarDetallePedido(item);
            if (!saved) {
                break;
            }
        }

        return true;
    }

    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            doPermissionGrantedStuffs();
        }
    }

    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        variables_publicas.IMEI = tm.getDeviceId();


    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(PedidosActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(PedidosActivity.this,
                                    new String[]{Manifest.permission.READ_PHONE_STATE},
                                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            // READ_PHONE_STATE permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }
    }

    private void CargaDatosCombo() {
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboVendedor.setAdapter(adapterVendedor);

        cliente = ClientesH.BuscarCliente(pedido.getIdCliente(), pedido.getCod_cv());
        IdDepartamento = Integer.parseInt(cliente.getIdDepartamento());
        /*Si no es vendedor o es ventas oficina*/
        if (variables_publicas.usuario.getCodigo().equals("0") || cliente.getIdVendedor().equals("9") || cliente.getEmpleado().equals("1")) {
            pedido.setIdVendedor(cliente.getIdVendedor());
        } else {
            pedido.setIdVendedor(variables_publicas.usuario.getCodigo());
        }

        if (cliente == null) {
            MensajeAviso("El cliente no se encuentra en la base de datos");
            finish();
        }
        if (editar == false) {
            GenerarCodigoPedido();
        }
        if (variables_publicas.usuario.getTipo().equals("Vendedor")) {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != Integer.parseInt(pedido.getIdVendedor()); i++)
                try {
                    this.vendedor = vendedor;
                    vendedor = vendedores.get(i);
                } catch (Exception ex) {
                    new Funciones().SendMail("Ha ocurrido un error al seleccionar el vendedor en CargarDatosCombo PedidosActivity Tipo 'Vendedor', Excepcion controlada", ex.getStackTrace().toString() + " *** " + variables_publicas.info, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                }
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        } else {

            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != Integer.parseInt(cliente.getIdVendedor()); i++) {
                try {
                    this.vendedor = vendedor;
                    vendedor = vendedores.get(i);
                } catch (Exception ex) {
                    new Funciones().SendMail("Ha ocurrido un error al seleccionar el vendedor en CargarDatosCombo PedidosActivity Tipo 'No vendedor', Excepcion controlada", ex.getStackTrace().toString() + " *** " + variables_publicas.info, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                }
            }
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        }
        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                if (!editar) {
                    pedido.setIdVendedor(vendedor.getCODIGO().toString());
                    GenerarCodigoPedido();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(pedido.getIdCliente());
        ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
        adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSucursal.setAdapter(adapterSucursal);
        cboSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                sucursal = (ClienteSucursal) adapter.getItemAtPosition(position);
                if (!sucursal.getCodSuc().equals("0")) {
                    cliente.setIdDepartamento(sucursal.getDeptoID());
                    IdDepartamento = Integer.parseInt(sucursal.getDeptoID());
                }
                RecalcularDetalle();
                RefrescarGrid();
                CalcularTotales();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        List<FormaPago> lstFormasPago = FormaPagoH.ObtenerListaFormaPago();
        ArrayAdapter<FormaPago> adapterFormaPago = new ArrayAdapter<FormaPago>(this, android.R.layout.simple_spinner_item, lstFormasPago);
        adapterFormaPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCondicion.setAdapter(adapterFormaPago);
        condicion = lstFormasPago.get(0);
        for (int i = 0; !(condicion.getCODIGO().equals(cliente.getIdFormaPago())); i++)
            condicion = lstFormasPago.get(i);
        cboCondicion.setSelection(adapterFormaPago.getPosition(condicion));
        cboCondicion.setEnabled(false);
    }

    private void GenerarCodigoPedido() {
        pedido.setCodigoPedido("-" + GetFechaISO() + cliente.getIdCliente() + cliente.getCodCv() + pedido.getIdVendedor());
        lblNoPedido.setText("PEDIDO N°: " + pedido.getCodigoPedido());
    }

    private String GetFechaISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyMMddHHmms");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    private void ObtenerPrecio(final HashMap<String, String> item, String CodArticulo, final boolean ActualizarItem) {
        double precio = 0;
        variables_publicas.lstDepartamentosForaneo1 = ConfiguracionSistemaH.BuscarValorConfig("lstDepartamentosForaneo1").getValor().split(",");
        String[] lstDepartamentosForaneo1 = variables_publicas.lstDepartamentosForaneo1;
        final HashMap<String, String> art = ArticulosH.BuscarArticuloHashMap(CodArticulo);
        //Esto para utilizarlo en el metodo SetPrecio
        Articulo articulo = ArticulosH.BuscarArticulo(CodArticulo);
        boolean AplicarPrecioDetalle = Boolean.parseBoolean(articulo.getAplicaPrecioDetalle());
        int ModCantidadCajas, cantidadItems = 0, FaltaParaCaja, cajas, UnidadCaja, ModMultiplo50=0,FaltaParaCaja50=0;
        boolean multiplo50= false;
        int factorMult50 = 0;
        boolean PrecioCajas = false;
        UnidadCaja = Integer.parseInt(articulo.getUnidadCajaVenta());
        factorMult50 = 50 / Integer.parseInt(articulo.getUnidadCajaVenta());
        if (item != null) {
            if ((item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)).isEmpty()) {
                cantidadItems = 0;
            } else {
                cantidadItems = Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
                ModMultiplo50 = (cantidadItems % (UnidadCaja * factorMult50));
                if (ModMultiplo50==0) {
                    multiplo50 =true;
                }else {
                    multiplo50 =false;
                }
             }
        } else {
            try {
                cantidadItems = Integer.parseInt(txtCantidad.getText().toString().isEmpty() ? "0" : txtCantidad.getText().toString());
            } catch (Exception e) {
                cantidadItems = 0;
            }

        }
        String TipoForaneo = "";
        UnidadCaja = UnidadCaja == 0 ? 1 : Integer.parseInt(articulo.getUnidadCajaVenta());
        ModCantidadCajas = (cantidadItems % UnidadCaja);
        if (cantidadItems >= UnidadCaja) {
            PrecioCajas = true;
        }
        FaltaParaCaja = ModCantidadCajas == 0 ? 0 : (UnidadCaja - ModCantidadCajas);
        cajas = cantidadItems / UnidadCaja;

        /*ojo: no quitar, esto para permitir vender mas de 3 sacos de 50 pero sin limitar x multiplo de caja*/
        if (PrecioCajas && CodArticulo.equals("4000-01-01-03-081")) {
            FaltaParaCaja = 0;
        }
        /* Esto es para validar los multiplos de 50 para el articulo 4000-02-01-04-1068*/
        if (PrecioCajas && CodArticulo.equals("4000-02-01-04-1068") && cantidadItems>=50 && multiplo50== true ) {
            FaltaParaCaja50 = 0;
        }
        else if (PrecioCajas && CodArticulo.equals("4000-02-01-04-1068") && cantidadItems<50 && multiplo50== false) {
            FaltaParaCaja = 0;
        }else
        {
            FaltaParaCaja50 = 1;
        }

        String tipoprecio = "Super";

        if (Integer.parseInt(vendedor.getCODIGO()) == 9) {  //Ventas Oficina
            IdDepartamento = 6;
        }
        TipoForaneo = "Precio" + (Arrays.asList(lstDepartamentosForaneo1).contains(cliente.getIdDepartamento()) ? "Foraneo" : "Foraneo2");
        if (cliente.getTipo().equalsIgnoreCase("Detalle")) {
            if (Boolean.parseBoolean(cliente.getRutaForanea()) && !AplicarPrecioDetalle) {
                tipoprecio = "Super"; //detalle foraneo
            } else {
                tipoprecio = "Detalle";
            }
            //Si es ruta no determinada y departamento managua
            if (cliente.getRuta().equalsIgnoreCase("No Determinada")) {
                if (IdDepartamento == 6) {
                    tipoprecio = "Detalle";
                } else {
                    tipoprecio = "Super";
                }
            }

            if (Integer.parseInt(vendedor.getCODIGO()) == 9) {
                tipoprecio = "Detalle";
            }
        } else if (!cliente.getTipo().equalsIgnoreCase("Super")) {


            if (cliente.getTipo().equalsIgnoreCase("Foraneo")) {
                if (subTotalPrecioSuper < valorPolitica) {
                    tipoprecio = "Super";
                } else {
                    tipoprecio = TipoForaneo.replace("Precio", "");
                }
            }

            if (cliente.getTipo().equalsIgnoreCase("Mayorista")) {
                if (subTotalPrecioSuper < valorPolitica) {
                    if (IdDepartamento == 6) { //Managua
                        tipoprecio = "Detalle";
                    } else {
                        tipoprecio = "Super";
                    }
                } else {
                    if (IdDepartamento == 6) { //Managua
                        tipoprecio = "Mayorista";
                    } else {
                        tipoprecio = TipoForaneo.replace("Precio", "");
                    }
                }
            }
        }

        if (variables_publicas.AplicarPrecioMayoristaXCaja.equalsIgnoreCase("1") && cliente.getEmpleado().equals("0") && !vendedor.getCODIGO().equalsIgnoreCase("9")) {
            if (cantidadItems > 0 && (item != null && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("P"))) {
                if (PrecioCajas && !cliente.getTipo().equalsIgnoreCase("Super")) {
                    if (FaltaParaCaja50 > 0 && ModMultiplo50 > 0 && CodArticulo.equals("4000-02-01-04-1068") ) {
                        if (variables_publicas.PermitirVentaDetAMayoristaXCaja.equalsIgnoreCase("1") || cliente.getTipo().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getTipo().equalsIgnoreCase("User")) {
                            if (MensajeCaja && !ActualizarItem) {
                                final String finalTipoprecio = tipoprecio;
                                if (!ActualizarItem) {
                                    MensajeCaja = false;
                                }

                                listaArticulos.remove(listaArticulos.size() - 1);
                                AplicarBonificacionCartillas();
                                AplicarPromocionAmsa();
                                AplicarPromocion024();
                                AplicarPromocionSalnica();

                                MensajeAviso("No se puede Facturar cantidades que no sean multiplos de 50.");
                                txtCantidad.requestFocus();
                            }
                        }
                    }
                    else if (FaltaParaCaja > 0 && ModCantidadCajas > 0) {
                        if (variables_publicas.PermitirVentaDetAMayoristaXCaja.equalsIgnoreCase("1") || cliente.getTipo().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getTipo().equalsIgnoreCase("User")) {
                            if (MensajeCaja && !ActualizarItem) {
                                final String finalTipoprecio = tipoprecio;
                                if (!ActualizarItem) {
                                    MensajeCaja = false;
                                }
                                new AlertDialog.Builder(this)
                                        .setTitle("Confirmación Requerida")
                                        .setMessage("Para dar precio mayorista se necesita " + String.valueOf(FaltaParaCaja) + " unidades para completar " + String.valueOf(cajas + 1) + " cajas, Desea continuar ? ")
                                        .setCancelable(false)
                                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (!ActualizarItem) {
                                                    MensajeCaja = true;
                                                    setPrecio(art, finalTipoprecio, 0, item == null);
                                                    LimipiarDatos(true);
                                                }
                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                                if (!ActualizarItem) {
                                                    setPrecio(art, finalTipoprecio, 0, item == null);
                                                    if (listaArticulos.size() > 0) {
                                                        listaArticulos.remove(listaArticulos.size() - 1);
                                                        AplicarBonificacionCartillas();
                                                        AplicarPromocionAmsa();
                                                        AplicarPromocion024();
                                                        AplicarPromocionSalnica();
                                                        RefrescarGrid();
                                                        CalcularTotales();
                                                    }
                                                    txtCantidad.requestFocus();
                                                }
                                            }
                                        })
                                        .show();
                            } else {
                                //Si la cantidad no es caja entonces le damos el precio detalle o detalle foraneo
                                if (cliente.getTipo().equalsIgnoreCase("Foraneo")) {
                                    tipoprecio = "Super";
                                } else if (cliente.getTipo().equalsIgnoreCase("Mayorista") && IdDepartamento == 6) {
                                    tipoprecio = "Detalle";

                                } else if (cliente.getTipo().equalsIgnoreCase("Detalle")) {

                                    if (Boolean.parseBoolean(cliente.getRutaForanea()) && !AplicarPrecioDetalle) {
                                        tipoprecio = "Super";
                                    } else {
                                        tipoprecio = "Detalle";
                                    }
                                } else {
                                    tipoprecio = "Super";
                                }
                            }
                        } else {
                            if (MensajeCaja) {
                                MensajeCaja = false;
                                listaArticulos.remove(listaArticulos.size() - 1);
                                AplicarBonificacionCartillas();
                                AplicarPromocionAmsa();
                                AplicarPromocion024();
                                AplicarPromocionSalnica();

                                MensajeAviso("Para dar precio mayorista se necesita " + String.valueOf(FaltaParaCaja) + " unidades para completar " + String.valueOf(cajas + 1) + " cajas");
                                txtCantidad.requestFocus();
                            }
                        }
                    } else {
                        /*Damos precio Mayorista*/
                        if (cliente.getTipo().equalsIgnoreCase("Detalle")) {
                            if (Boolean.parseBoolean(cliente.getRutaForanea())) {
                                if (AplicarPrecioDetalle) {
                                    tipoprecio = "Mayorista";
                                } else {
                                    tipoprecio = TipoForaneo.replace("Precio", "");
                                }
                            } else {
                                tipoprecio = "Mayorista";
                            }
                        } else if (cliente.getTipo().equalsIgnoreCase("Foraneo") || (cliente.getTipo().equalsIgnoreCase("Mayorista") && IdDepartamento != 6)) {
                            tipoprecio = TipoForaneo.replace("Precio", "");
                        } else {
                            tipoprecio = "Mayorista";
                        }
                    }
                } else {

                    //Recalculamos al precio mas caro por venta x unidades o es super
                    if (cliente.getTipo().equalsIgnoreCase("Mayorista")) {
                        if (IdDepartamento == 6) tipoprecio = "Detalle";
                        else tipoprecio = "Super";
                    }
                    if (cliente.getTipo().equalsIgnoreCase("Foraneo") || cliente.getTipo().equalsIgnoreCase("Super")) {
                        tipoprecio = "Super";
                    }

                    if (cliente.getTipo().equalsIgnoreCase("Detalle")) {
                        if (Boolean.parseBoolean(cliente.getRutaForanea())) {
                            if (AplicarPrecioDetalle) {
                                tipoprecio = "Detalle";
                            } else {
                                tipoprecio = "Super";
                            }
                        } else {
                            tipoprecio = "Detalle";
                        }
                    }

                    /*Aqui mostramos mensaje en caso que aplique precio x unidades*/
                    if ((cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo")) && !(cliente.getTipo().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle"))) {
                        if ((variables_publicas.PermitirVentaDetAMayoristaXCaja.equals("0") && !variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca")) && MensajeCaja) {
                            setPrecio(art, tipoprecio, 0, item == null);
                            MensajeCaja = false;
                            listaArticulos.remove(listaArticulos.size() - 1);
                            AplicarBonificacionCartillas();
                            AplicarPromocionAmsa();
                            AplicarPromocion024();
                            AplicarPromocionSalnica();
                            MensajeAviso("Para dar precio mayorista se necesita " + String.valueOf(FaltaParaCaja) + " unidades para completar " + String.valueOf(cajas + 1) + " cajas");
                            txtCantidad.requestFocus();
                        } else {
                            setPrecio(art, tipoprecio, 0, item == null);
                            MensajeCaja = true;
                        }

                    } else {
                        setPrecio(art, tipoprecio, precio, item == null);
                        MensajeCaja = true;
                    }
                }
            } else {
                //Si itemDetalle.cantidad=0 entonces le damos el precio mas alto
                if (cliente.getTipo().equalsIgnoreCase("Foraneo")) {
                    tipoprecio = "Super"; //Super es : Detalle Foraneo
                } else if (cliente.getTipo().equalsIgnoreCase("Mayorista")) {
                    tipoprecio = "Detalle";
                } else {
                    tipoprecio = cliente.getTipo();
                }
            }
        }
        if (cliente.getEmpleado().equals("1") && Integer.parseInt(condicion.getCODIGO()) != 127) { // esto para validar que no sea producto abordo --Tramite de CK
            tipoprecio = "Mayorista";
        }
        double precioE = 0;
        double descuentoE = 0;

        if (Boolean.parseBoolean(cliente.getPrecioEspecial()) && (cliente.getTipo().equalsIgnoreCase("Super") || cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo"))) {
            txtDescuento.setEnabled(false);
            txtDescuento.setText("0.00");
            //Si existe precio especial

            PrecioEspecial precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(pedido.getIdCliente(), articulo.getCodigo());
            if (precioEspecial != null) {
                tipoprecio = "Especial";
                txtDescuento.setText(String.valueOf(Double.parseDouble(precioEspecial.getDescuento())));
                if (precioEspecial.getFacturar().equals("0")) {
                    MensajeAviso("Este Producto no esta habilidado para venderlo a este cliente");
                    return;
                }
                precioE = Double.parseDouble(precioEspecial.getPrecio());
                descuentoE = Double.parseDouble(precioEspecial.getDescuento());
                if (ActualizarItem) {
                    item.put("Precio", precioEspecial.getPrecio());
                    item.put("TipoPrecio", "Especial");
                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, String.valueOf(descuentoE));
                    item.put("PorDescuentoOriginal", String.valueOf(descuentoE));
                } else {
                    tipoprecio = "Especial";
                    precio = Double.parseDouble(precioEspecial.getPrecio());
                    setPrecio(art, tipoprecio, precio, item == null);
                    MensajeCaja = true;
                }
            } else {
                tipoprecio = cliente.getTipo();
                setPrecio(art, tipoprecio, precio, item == null);
                MensajeCaja = true;
            }
        }
        if (!ActualizarItem) {
            setPrecio(art, tipoprecio, precio, item == null);
        } else {
            if (item != null) {
                if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("P")) {
                    if (tipoprecio.equalsIgnoreCase("Especial")) {
                        item.put("Precio", String.valueOf(precioE));
                        item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, String.valueOf(descuentoE));
                        item.put("PorDescuentoOriginal", String.valueOf(descuentoE));
                    } else {
                        item.put("Precio", art.get("Precio" + tipoprecio));

                    }
                } else {
                    tipoprecio = "Bonificacion";
                }

                if (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {

                    if (item.get("PorDescuentoOriginal") == null) {
                        item.put("PorDescuentoOriginal", "0.00");
                    }

                    item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento, String.valueOf(Double.parseDouble(item.get("PorDescuentoOriginal")) + Double.parseDouble((sucursal != null ? sucursal.getDescuento() : "0"))));
                }

                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio, tipoprecio);
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento,
                        df.format(Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)) * (Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento))) / 100));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal,
                        df.format(Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)) - ((Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio))) * ((Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento))) / 100))));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva,
                        df.format(Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal).replace(",", "")) * Double.parseDouble(articulo.getPorIva())));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total, df.format(Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal).replace(",", "")) + Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva).replace(",", ""))));
            }

        }
    }

    private void LimipiarDatos(boolean MensajeCaja) {
        if (MensajeCaja) {
            txtPrecioArticulo.setText("0.00");
            articulo = null;
            txtCodigoArticulo.setText(null);
            txtCantidad.setError(null);
            txtCodigoArticulo.setText("");
            lblDescripcionArticulo.setText("");
            txtCantidad.setText("");
            txtDescuento.setText("");
            lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));
            txtCodigoArticulo.requestFocus();
            lblUM.setText("N/A");
            lblExistentias.setText("N/A");
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private void RecalcularDetalle() {

        MensajeCaja = false;
        for (HashMap<String, String> item : listaArticulos) {
            ObtenerPrecio(item, item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo), true);
        }

    }

    private boolean EsArticuloRepetido(String s) {

        for (HashMap<String, String> item : listaArticulos) {
            if (item.get("CodigoArticulo").equals(s) && item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equalsIgnoreCase("P")) {
                return true;
            }
        }
        return false;
    }

    private void setPrecio(HashMap<String, String> articulo, String pTipoPrecio, double precio, boolean settxtPrecioArticulo) {
        if (pTipoPrecio.equalsIgnoreCase("Especial")) {
            if (settxtPrecioArticulo) {
                txtPrecioArticulo.setText(String.valueOf(precio));
            }
            PrecioItem = precio;
        } else {
            if (settxtPrecioArticulo) {
                txtPrecioArticulo.setText(articulo.get("Precio" + pTipoPrecio));
            }
            PrecioItem = Double.parseDouble(articulo.get("Precio" + pTipoPrecio));
        }
        TipoPrecio = pTipoPrecio;

    }

    private boolean AgregarDetalle(HashMap<String, String> itemPedidos) {
        double Precio = PrecioItem;
        String DescripcionArt = lblDescripcionArticulo.getText().toString();

        //Validamos que solamente se puedan ingresar 18 articulos
        if (listaArticulos.size() == 18 && cliente.getDetallista().equalsIgnoreCase("false")) {
            MensajeAviso("No se puede agregar el producto seleccionado,ha alcanzado el limite de 18 productos por pedido para factura grande (Mayorista)");
            return false;
        }

        itemPedidos.put("CodigoPedido", pedido.getCodigoPedido());
        itemPedidos.put("CodigoArticulo", articulo.getCodigo());
        itemPedidos.put("Cod", articulo.getCodigo().split("-")[articulo.getCodigo().split("-").length - 1]);
        itemPedidos.put("Cantidad", txtCantidad.getText().toString());
        itemPedidos.put("Precio", String.valueOf(Precio));
        itemPedidos.put("TipoPrecio", TipoPrecio);
        itemPedidos.put("Descripcion", DescripcionArt);
        itemPedidos.put("Costo", String.valueOf(Double.parseDouble(articulo.getCosto())));
        itemPedidos.put("PorDescuento", String.valueOf(Double.parseDouble((txtDescuento.getText().toString().equals("") ? "0" : txtDescuento.getText().toString()))));
        itemPedidos.put("PorDescuentoOriginal", String.valueOf(Double.parseDouble((txtDescuento.getText().toString().equals("") ? "0" : txtDescuento.getText().toString()))));
        itemPedidos.put("TipoArt", "P");
        itemPedidos.put("BonificaA", "");
        itemPedidos.put("Isc", articulo.getIsc());
        itemPedidos.put("PorIva", articulo.getPorIva());
        double subtotal, iva, total, descuento, isc, porIva;
        subtotal = Double.parseDouble(itemPedidos.get("Precio")) * Double.parseDouble(itemPedidos.get("Cantidad"));
        descuento = subtotal * (Double.parseDouble(itemPedidos.get("PorDescuento")) / 100);
        subtotal = subtotal - descuento;
        porIva = Double.parseDouble(articulo.getPorIva());
        iva = subtotal * porIva;
        total = subtotal + iva;
        itemPedidos.put("Descuento", df.format(descuento));
        itemPedidos.put("PorcentajeIva", articulo.getPorIva());
        itemPedidos.put("Um", articulo.getUnidad());
        itemPedidos.put("Iva", df.format(iva));
        itemPedidos.put("SubTotal", df.format(subtotal));
        itemPedidos.put("Total", df.format(total));
        itemPedidos.put("IdProveedor", articulo.getIdProveedor());
        itemPedidos.put("UnidadCajaVenta", articulo.getUnidadCajaVenta());


        //Validamos que solamente se puedan ingresar 18 articulos
        if (listaArticulos.size() == 18 && cliente.getDetallista().equalsIgnoreCase("false")) {
            MensajeAviso("No se puede agregar el producto seleccionado,ya que excede el limite de 18 productos para un pedido Mayorista");
            return false;
        }

        CodigoItemAgregado = articulo.getCodigo();
        listaArticulos.add(itemPedidos);

        PrecioItem = 0;
        RefrescarGrid();
        CalcularTotales();
        return true;

    }

    private void RefrescarGrid() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaArticulos,
                R.layout.pedidos_list_item, new
                String[]{"Cod", "Cantidad", "Precio", "TipoPrecio", "Descripcion", "PorDescuento", "Descuento", "SubTotal", "Iva", "Total"}, new
                int[]{R.id.lblDetalleCodProducto, R.id.lblDetalleCantidad, R.id.lblDetallePrecio, R.id.lblDetalleTipoPrecio, R.id.lblDetalleDescripcion, R.id.lblDetallePorDescuento, R.id.lblDetalleDescuento, R.id.lblDetalleSubTotal, R.id.lblDetalleIva, R.id.lblDetalleTotal}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                if (currItem.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descripcion).startsWith("**")) {
                    currView.setBackgroundColor(Color.RED);
                } else {
                    currView.setBackgroundColor(Color.WHITE);
                }
                return currView;
            }
        };

        lv.setAdapter(adapter);
    }

    private void CalcularTotales() {

        double iva = 0, descuento = 0;
        total = 0;
        subtotal = 0;
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> item = listaArticulos.get(i);

            try {
                subtotal += (df.parse(item.get("SubTotal"))).doubleValue();
                iva += (df.parse(item.get("Iva"))).doubleValue();
                total += (df.parse(item.get("Total"))).doubleValue();
            } catch (ParseException e) {
                MensajeAviso(e.getMessage());
            }
        }
        lblSubTotalCor.setText(df.format(subtotal));
        lblIvaCor.setText(df.format(iva));
        lblTotalCor.setText(df.format(total));

        if (tasaCambio > 0) {
            lblSubTotalDol.setText(String.valueOf(df.format(subtotal / tasaCambio)));
            lblIvaDol.setText(String.valueOf(df.format(iva / tasaCambio)));
            lblTotalDol.setText(String.valueOf(df.format(total / tasaCambio)));
        }
        lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));

    }

    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (finalizar) {
                    finish();
                }
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public void MostrarMensajeGuardar() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;
        dialogBuilder.setCancelable(false);
        if (guardadoOK) {
            dialogView = inflater.inflate(R.layout.dialog_ok_layout, null);

            Button btnOK = (Button) dialogView.findViewById(R.id.btnOkDialogo);
            btnOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {

            dialogView = inflater.inflate(R.layout.offline_layout, null);
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
        }
        dialogBuilder.setView(dialogView);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void BuscarArticulo() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;

        dialogView = inflater.inflate(R.layout.masterproductos_layout, null);
        btnOK = (Button) dialogView.findViewById(R.id.btnBuscar);
        final RadioGroup rgGrupo = (RadioGroup) dialogView.findViewById(R.id.rgGrupo);
        rgGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        final EditText txtBusquedaItem = (EditText) dialogView.findViewById(R.id.txtBusqueda);
        lvItem = (ListView) dialogView.findViewById(R.id.list);
        lblFooterItem = (TextView) dialogView.findViewById(R.id.lblFooter);
        txtBusquedaItem.setText(txtCodigoArticulo.getText());
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusquedaItem.getWindowToken(), 0);
                busqueda = txtBusquedaItem.getText().toString();
                if (validarTipoBusqueda) {
                    try {
                        int busquedaText = Integer.parseInt(busqueda);
                        rgGrupo.check(R.id.rbCodigo);

                    } catch (Exception ex) {

                        if (busqueda.contains("-")) {
                            rgGrupo.check(R.id.rbCodigo);
                        } else {
                            rgGrupo.check(R.id.rbDescripcion);
                        }
                    }
                    validarTipoBusqueda = false;
                }
                int boton = rgGrupo.getCheckedRadioButtonId();// == R.id.rbCodigo ? "1" : "2";
                switch (boton) {
                    case R.id.rbCodigo:
                        tipoBusqueda = 1;
                        break;
                    case R.id.rbDescripcion:
                        tipoBusqueda = 2;
                        break;
                }
                try {
                    switch (tipoBusqueda) {
                        case 1:
                            listaArticulosItem = ArticulosH.BuscarArticuloCodigo(busqueda);
                            break;
                        case 2:
                            listaArticulosItem = ArticulosH.BuscarArticuloNombre(busqueda);
                            break;
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
                if (listaArticulosItem.size() == 0) {
                    MensajeAviso("El codigo de articulo ingresado no existe en la base de datos o esta deshabilitado para su venta");
                }

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaArticulosItem,
                        R.layout.list_item, new String[]{"Codigo", "Nombre", "PrecioSuper", "PrecioDetalle", "PrecioForaneo", "PrecioForaneo2", "PrecioMayorista", "Existencia"}, new int[]{R.id.Codigo, R.id.Nombre,
                        R.id.PrecioSuper, R.id.PrecioDetalle, R.id.PrecioForaneo, R.id.PrecioForaneo2, R.id.PrecioMayorista, R.id.Existencias});

                lvItem.setAdapter(adapter);
                lblFooterItem.setText("Articulos encontrados: " + String.valueOf(listaArticulosItem.size()));


            }
        });
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                String CodigoArticulo = ((TextView) view.findViewById(R.id.Codigo)).getText().toString();

                articulo = ArticulosH.BuscarArticulo(CodigoArticulo);
                /*Validamos que permita vender codigo 1052*/
                List<String> lstDeptoAutorizado = Arrays.asList(ConfiguracionSistemaH.BuscarValorConfig("Venta Autorizada 1052").getValor().split(","));
                if (CodigoArticulo.equals("4000-01-01-04-1052") && !lstDeptoAutorizado.contains(String.valueOf(IdDepartamento)) && cliente.getTipo().equalsIgnoreCase("Detalle")) {
                    alertDialog.dismiss();
                    MensajeAviso("Este producto no esta autorizado para venderlo a este cliente");
                    return;
                }

                HashMap<String, String> art = ArticulosH.BuscarArticuloHashMap(CodigoArticulo);
                txtCodigoArticulo.setText(CodigoArticulo);
                lblDescripcionArticulo.setText(articulo.getNombre());
                lblUM.setText(articulo.getUnidadCajaVenta());
                existencia = articulo.getExistencia();
                lblExistentias.setText(String.valueOf((int) (Double.parseDouble(existencia))));
//                if (variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle") || variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca")) {

                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    new ConsultarExistencias().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    new ConsultarExistencias().execute();
                }


                MensajeCaja = true;
                ObtenerPrecio(null, CodigoArticulo, false);

                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    loadIMEI();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = obj.get("CodigoArticulo") + "\n" + obj.get("Descripcion");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.eliminar_item_pedido, menu);
        } catch (Exception e) {
            MensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.Elimina_Item:
                    HashMap<String, String> itemArticulo = listaArticulos.get(info.position);
                    listaArticulos.remove(itemArticulo);
                    for (int i = 0; i < listaArticulos.size() - 1; i++) {
                        HashMap<String, String> a = listaArticulos.get(i);
                        if (a.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA).equals(itemArticulo.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo))) {
                            listaArticulos.remove(a);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);

                    AplicarBonificacionCartillas();
                    AplicarPromocionAmsa();
                    AplicarPromocion024();
                    AplicarPromocionSalnica();

                    RecalcularDetalle();
                    CalcularTotales();
                    RefrescarGrid();
                    LimipiarDatos(true);

                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        } catch (Exception e) {
            MensajeAviso(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el pedido actual?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PedidosActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    //endregion


    private class SincronizardorPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(PedidosActivity.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CheckConnectivity();
            if (isOnline) {
                if (Boolean.parseBoolean(SincronizarDatos.SincronizarPedido(PedidoH, PedidoDetalleH, vendedor, cliente, pedido.getCodigoPedido(), jsonPedido, (editar == true && pedidoLocal == false)).split(",")[0])) {
                    guardadoOK = true;
                }
            } else {
                guardadoOK = false;
               /* runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        variables_publicas.MensajeError = "Ha ocurrido un error al obtener las existencias,Respuesta nula";
                        Toast.makeText(getApplicationContext(),
                                "Ha ocurrido un error al obtener las existencias,Respuesta nula",
                                Toast.LENGTH_LONG).show();
                    }
                });*/
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (PedidosActivity.this.isFinishing() == false) {
                MostrarMensajeGuardar();
            }

        }
    }

    private class ConsultarExistencias extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

          /*  if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new ProgressDialog(PedidosActivity.this);
            pDialog.setMessage("consultando existencias, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                CheckConnectivity();
                if (isOnline) {
                    existencia = SincronizarDatos.ConsultarExistencias(PedidosActivity.this, PedidoH, ArticulosH, articulo.getCodigo());
                }
            } catch (Exception ex) {
                Log.e("error", ex.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          /*  if (pDialog.isShowing())
                pDialog.dismiss();*/

            if (existencia != "N/A") {
                lblExistentias.setText(String.valueOf((int) (Double.parseDouble(existencia))));
            } else {
                lblExistentias.setText(articulo.getExistencia());
            }
        }
    }

    private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new ProgressDialog(PedidosActivity.this);
            pDialog.setMessage("consultando version del sistema, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CheckConnectivity();
                if (isOnline) {
                    //It retrieves the latest version by scraping the content of current version from play store at runtime
                    String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es";
                    Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                    latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
          /*  if (pDialog.isShowing())
                pDialog.dismiss();
*/
            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(PedidosActivity.this);
                builder.setTitle("Nueva version disponible");
                builder.setMessage("Es necesario actualizar la aplicacion para poder continuar.");
                builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.suplidora.sistemas.sisago&hl=es")));
                        dialog.dismiss();
                    }
                });
                builder.setCancelable(false);
                if (isFinishing()) {
                    return;
                }
                builder.show();
            }
        }


    }
/*
    private class SincronizardorDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                sd.SincronizarTodo();
            } catch (Exception e) {
                Log.e("Error:", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/

    //region ObtieneValorConfiguracion
    private class GetValorConfig extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String urlString = urlGetConfiguraciones;

            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            /**********************************USUARIOS**************************************/
            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray Usuarios = jsonObj.getJSONArray("GetConfiguracionesResult");

                    for (int i = 0; i < Usuarios.length(); i++) {
                        JSONObject c = Usuarios.getJSONObject(i);
                        String Valor = c.getString("Valor");
                        String Configuracion = c.getString("Configuracion");
                        String ConfigVDatos = "VersionDatos";
                        if (Configuracion.equals(ConfigVDatos)) {
                            variables_publicas.ValorConfigServ = Valor;

                            int ValorConfigLocal = Integer.parseInt(variables_publicas.Configuracion.getValor());
                            int ValorConfigServidor = Integer.parseInt(variables_publicas.ValorConfigServ);

                            if (ValorConfigLocal < ValorConfigServidor) {
                                sd.SincronizarTablas();
                            }

                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "No se ha podido establecer contacto con el servidor");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "No se ha podido establecer contacto con el servidor",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {

                Log.e(TAG, "No se ha podido establecer contacto con el servidor");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No se ha podido establecer contacto con el servidor",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }
    }
    //endregion


    @Override
    protected void onResume() {
        super.onResume();
    }
}


