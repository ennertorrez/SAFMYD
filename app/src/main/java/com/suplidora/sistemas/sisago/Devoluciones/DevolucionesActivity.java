package com.suplidora.sistemas.sisago.Devoluciones;

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
import com.suplidora.sistemas.sisago.Auxiliar.SpinnerDialog;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Articulo;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.ClienteSucursal;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCarga;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCargaDetalle;
import com.suplidora.sistemas.sisago.Entidades.Devoluciones;
import com.suplidora.sistemas.sisago.Entidades.DtoConsolidadoCargaFacturas;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Motivos;
import com.suplidora.sistemas.sisago.Entidades.PedidoDetalle;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class DevolucionesActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = DevolucionesActivity.class.getSimpleName();
    private boolean MensajeCaja;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    //region Declaracion de controles
    public String valorFacturaNew;
    private EditText txtDescuento;
    private EditText txtObservaciones;
    private TextView lblCantidad;
    private TextView lblNombCliente;
    private TextView lblCodCliente;
    private TextView txtCodigoArticulo;
    private TextView lblDescripcionArticulo;
    private TextView lblSubTotalCor;
    private TextView lblIvaCor;
    private TextView lblTotalCor;
    private TextView lblNewTotalFact;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private TextView lblSearch;
    private Button btnAgregar;
    private Button btnBuscaItem;
    private Button btnOK;
    private Button btnGuardar;
    private Button btnCancelar;
    private Button btnAgregarTodos;
    private EditText txtCantidad;
    private Spinner cboCarga;
    private Spinner cboMotivo;
    private ListView lv;
    private ListView lvItem;
    private SimpleAdapter adapter;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    private String CodigoArticulo;

    public final static String NuevoValorfactura="";
    //endregion

    //region Declaracion de variables
    String IMEI = "";
    private String focusedControl = "";

    private Articulo articulo;
    private DecimalFormat df;
    private FormaPago condicion;
    private ClienteSucursal sucursal;
    private ConsolidadoCarga carga = null;
    private ConsolidadoCargaDetalle cargadetalle;
    public static ArrayList<HashMap<String, String>> listaArticulos;
    public static ArrayList<HashMap<String, String>> listaCCargaArticulosItem;
    public boolean Estado;
    public double total;
    public double totalFactura;
    public double totalNewFactura;
    public double iva;
    public double subtotal;
    private Cliente cliente;
    private double tasaCambio = 0;
    private Devoluciones devoluciones;
    private boolean isOnline;

    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private PedidosDetalleHelper PedidoDetalleH;
    private CartillasBcHelper CartillasBcH;
    private PedidosHelper PedidoH;
    private ClientesSucursalHelper ClientesSucursalH;
    private ConfiguracionSistemaHelper ConfigH;
    private FormaPagoHelper FormaPagoH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private ConsolidadoCargaHelper ConsolidadoCargaH;
    private ConsolidadoCargaDetalleHelper ConsolidadoCargaDetalleH;
    private PrecioEspecialHelper PrecioEspecialH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private ConfiguracionSistemaHelper ConfiguracionSistemaH;
    private DevolucionesHelper DevolucionH;
    private DevolucionesDetalleHelper DevolucionDetalleH;
    private SincronizarDatos sd;
    private String CodigoLetra = "";
    private String jsonDevolucion = "";
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
    private boolean devolucionLocal;
    private DtoConsolidadoCargaFacturas Factura;
    java.util.ArrayList<String> CcFactura;
    List<HashMap<String, String>> ObtieneCcarga = null;
    SpinnerDialog spinnerDialog;
    private double CantidadPedido;

    //endregion


    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.devoluciones);
        devoluciones = new Devoluciones();

        ValidarUltimaVersion();


        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);

        listaArticulos = new ArrayList<HashMap<String, String>>();
        listaCCargaArticulosItem = new ArrayList<HashMap<String, String>>();

        DbOpenHelper = new DataBaseOpenHelper(DevolucionesActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ConsolidadoCargaDetalleH = new ConsolidadoCargaDetalleHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        ConfiguracionSistemaH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        ConsolidadoCargaH = new ConsolidadoCargaHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        DevolucionH = new DevolucionesHelper(DbOpenHelper.database);
        DevolucionDetalleH = new DevolucionesDetalleHelper(DbOpenHelper.database);
        cboCarga = (Spinner) findViewById(R.id.cboCarga);
        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
        cboMotivo = (Spinner) findViewById(R.id.cboMotivo);
        lblFooter = (TextView) findViewById(R.id.lblFooter);



        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH,
                FormaPagoH,
                PrecioEspecialH, ConfigH, ClientesSucursalH, ArticulosH, UsuariosH, PedidoH, PedidoDetalleH, DevolucionH, DevolucionDetalleH);


        // Displaying all values on the screen
        final TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        //final Spinner cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        //Obtenemos las referencias a los controles
        lblCodCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView) findViewById(R.id.lblNombCliente);
        txtCodigoArticulo = (TextView) findViewById(R.id.lblCodArticulo);
        lblDescripcionArticulo = (TextView) findViewById(R.id.lblDescArticulo);
        txtCantidad = (EditText) findViewById(R.id.txtCantidad);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscaItem = (Button) findViewById(R.id.btnBuscaItem);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        lblSearch = (TextView) findViewById(R.id.lblSearch);
        btnAgregarTodos =(Button) findViewById(R.id.btnAgregarTodos);
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


        txtObservaciones = (EditText) findViewById(R.id.txtObservaciones);
        lblSubTotalCor = (TextView) findViewById(R.id.lblSubTotalCor);
        lblIvaCor = (TextView) findViewById(R.id.lblIvaCor);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblNewTotalFact = (TextView) findViewById(R.id.lblNewTotalFact);

        // getting intent data
        Intent in = getIntent();

        if (in.getSerializableExtra(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion) != null) {

            if (in.getSerializableExtra(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion).toString().startsWith("-")) {
                devolucionLocal = true;

            } else {

                devolucionLocal = false;
            }

            editar = true;

            listaArticulos.clear();

            /*Seleccionamos el consolidado de carga*/
            List<ConsolidadoCarga> ccarga = ConsolidadoCargaH.BuscarConsolidadoCarga();
            int indice;
            for (int i = 0; i < ccarga.size(); i++) {
                if (ccarga.get(i).getIdConsolidado().equals(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_rango))) {
                    final int finalI = i;
                    cboCarga.post(new Runnable() {
                        public void run() {
                            cboCarga.setSelection(finalI);
                        }
                    });
                    break;
                }
            }

            if (editar==true) {
                ObtieneCcarga = ConsolidadoCargaH.ObtenerCcarga(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_factura));
            }
            lblSearch.setText(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_factura));
            devoluciones = DevolucionH.GetDevolucion(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion));
            listaArticulos = DevolucionDetalleH.ObtenerDevolucionDetalleArrayList(devoluciones.getNdevolucion());
            txtObservaciones.setText(devoluciones.getObservaciones());
            totalFactura=ConsolidadoCargaDetalleH.BuscarTotalFactura(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_factura));
            lblNewTotalFact.setText(Double.toString(totalFactura));
            RefrescarGrid();
            CalcularTotales();
            cboCarga.setEnabled(false);
            lblSearch.setEnabled(false);

        }else{
            lblSearch.setText("--Selecccione--");
        }

        // Loading spinner data from database
        CargaDatosCombo();
        if (editar==true) {
            cboMotivo.setSelection(getIndice(cboMotivo, in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_motivo)));
            txtObservaciones.setText(in.getStringExtra(variables_publicas.DEVOLUCIONES_COLUMN_Observaciones));
        }


        CargarListaFacturas();
        lblSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //provideSimpleDialog();
                spinnerDialog.showSpinerDialog();
            }
        });

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DevolucionesActivity.this.onBackPressed();
            }
        });
        txtCodigoArticulo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) || (actionId == EditorInfo.IME_ACTION_NEXT) || (actionId == EditorInfo.IME_ACTION_GO) || (actionId == EditorInfo.IME_ACTION_SEND)) {
                    btnBuscaItem.performClick();
                    focusedControl = "txtCodigoArticulo";
                    return false;
                }
                return true;
            }
        });

        btnBuscaItem.setOnClickListener(new OnClickListener() {
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

                                                  if (txtCodigoArticulo.getText().toString().isEmpty()) {
                                                      txtCantidad.setError("Primero seleccione un producto");
                                                      return;
                                                  }

                                                  if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                      txtCantidad.setError("Ingrese un valor");
                                                      return;
                                                  }

                                                  if (Double.parseDouble(txtCantidad.getText().toString()) < 1) {
                                                      txtCantidad.setError("Ingrese un valor mayor a 0");
                                                      return;
                                                  }

                                                  if (Double.parseDouble(txtCantidad.getText().toString()) > CantidadPedido) {
                                                      txtCantidad.setError("La cantidad maxima permitida para este producto es: " + CantidadPedido);
                                                      return;
                                                  }

                                                  boolean repetido = EsArticuloRepetido(txtCodigoArticulo.getText().toString());
                                                  if (repetido) {
                                                      MensajeAviso("Este artículo ya ha sigo agregado a la devolucion.");
                                                      return;
                                                  }


                                                  HashMap<String, String> itemDevolucion = new HashMap<>();
                                                  if (AgregarDetalle(itemDevolucion,txtCantidad.getText().toString())) {

                                                      LimipiarDatos(MensajeCaja);
                                                      lblSearch.setEnabled(false);
                                                      cboCarga.setEnabled(false);
                                                      CalcularTotales();
                                                      RefrescarGrid();

                                                      InputMethodManager inputManager = (InputMethodManager)
                                                              getSystemService(Context.INPUT_METHOD_SERVICE);

                                                      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                              InputMethodManager.RESULT_SHOWN);
                                                  }


                                              } catch (Exception e) {
                                                  MensajeAviso(e.getMessage());
                                              }
                                          }
                                      }
        );
        btnGuardar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (devoluciones.getMotivo().isEmpty()) {
                        MensajeAviso("Seleccione un motivo");
                        return;
                    }

                    Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });


        btnAgregarTodos.setOnClickListener(new OnClickListener() {
                                          public void onClick(View v) {

                                              try {

                                                  if(lblSearch.getText().toString().isEmpty() || lblSearch.getText().toString().equalsIgnoreCase("--Selecccione--") ){
                                                      MensajeAviso("Por favor seleccione una factura");
                                                      return;
                                                  }




                                                  String mensaje ="Esta seguro que desea agregar todos los productos a la devolucion?";

                                                  new AlertDialog.Builder(DevolucionesActivity.this)
                                                          .setTitle("Confirmación Requerida")
                                                          .setMessage(mensaje)
                                                          .setCancelable(false)
                                                          .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                                              public void onClick(DialogInterface dialog, int id) {
                                                                  listaArticulos = new ArrayList<HashMap<String, String>>();
                                                                  listaCCargaArticulosItem =ConsolidadoCargaDetalleH.BuscarConsolidadoCargaDetalleXFactura(lblSearch.getText().toString());
                                                                  /*Recorremos la lista de productos*/
                                                                  for (HashMap<String,String> item:listaCCargaArticulosItem) {
                                                                      cargadetalle = ConsolidadoCargaDetalleH.BuscarConsolidadoCargaDetalle(lblSearch.getText().toString(), item.get(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_ITEM));
                                                                      HashMap<String, String> itemDevolucion = new HashMap<>();
                                                                      AgregarDetalle(itemDevolucion,item.get(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_CANTIDAD)) ;
                                                                  }
                                                                  LimipiarDatos(MensajeCaja);
                                                                  lblSearch.setEnabled(false);
                                                                  cboCarga.setEnabled(false);
                                                                  CalcularTotales();
                                                                  RefrescarGrid();

                                                                  InputMethodManager inputManager = (InputMethodManager)
                                                                          getSystemService(Context.INPUT_METHOD_SERVICE);

                                                                  inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                                          InputMethodManager.RESULT_SHOWN);

                                                              }
                                                          })
                                                          .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                              @Override
                                                              public void onClick(DialogInterface dialog, int which) {

                                                              }
                                                          })
                                                          .show();




                                              } catch (Exception e) {
                                                  MensajeAviso(e.getMessage());
                                              }
                                          }
                                      }
        );

    }

    private int getIndice(Spinner cboMot, String cadena){

        //Creamos la variable posicion y lo inicializamos en 0
        int posicion = 0;
        //Recorre el spinner en busca del ítem que coincida con el parametro `String fruta`
        //que lo pasaremos posteriormente
        for (int i = 0; i < cboMot.getCount(); i++) {
            //Almacena la posición del ítem que coincida con la búsqueda
            if (cboMot.getItemAtPosition(i).toString().equalsIgnoreCase(cadena)) {
                posicion = i;
            }
        }
        //Devuelve un valor entero (si encontro una coincidencia devuelve la
        // posición 0 o N, de lo contrario devuelve 0 = posición inicial)
        return posicion;
    }
    private boolean EsArticuloRepetido(String s) {

        for (HashMap<String, String> item : listaArticulos) {
            if (item.get("item").equals(s) && item.get(variables_publicas.DEVOLUCIONES_DETALLE_COLUMN_tipo).equalsIgnoreCase("P")) {
                return true;
            }
        }
        return false;
    }

    private void CargarListaFacturas() {
        if (carga != null) {
            CcFactura = ConsolidadoCargaH.BuscarConsolidadoCargaFacturas(carga.getIdConsolidado());
        } else {
            CcFactura = new java.util.ArrayList<String>();
        }

        spinnerDialog = new SpinnerDialog(DevolucionesActivity.this, CcFactura, "Seleccione o busque la factura", R.style.DialogAnimations_SmileWindow);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //Toast.makeText(DevolucionesActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                ObtieneCcarga = ConsolidadoCargaH.ObtenerCcarga(item);

                lblNombCliente.setText(ObtieneCcarga.get(0).get("Cliente").toString());
                lblSearch.setText(item);
                totalFactura=ConsolidadoCargaDetalleH.BuscarTotalFactura(item);
                lblNewTotalFact.setText(df.format(totalFactura));
            }
        });

    }


    private void ValidarUltimaVersion() {
            String latestVersion = "";
            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            try {
                new GetLatestVersion().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
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

    private boolean SincronizarDevolucion(HashMap<String, String> devolucion) {
        Gson gson = new Gson();

        jsonDevolucion = gson.toJson(devolucion);
        try {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new SincronizardorDevoluciones().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new SincronizardorDevoluciones().execute();
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
            MensajeAviso("No se puede guardar la devolucion, Debe ingresar al menos 1 item");
            return false;
        }

        String mensaje = "Esta seguro que desea guardar la devolucion?";

        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DbOpenHelper.database.beginTransaction();
                        if (GuardarDevolucion()) {
                            DbOpenHelper.database.setTransactionSuccessful();
                            DbOpenHelper.database.endTransaction();
                            SincronizarDevolucion(DevolucionH.ObtenerDevolucion(devoluciones.getNdevolucion()));
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

    private boolean GuardarDevolucion() {
        String codSuc = sucursal == null ? "0" : sucursal.getCodSuc();
        IMEI = variables_publicas.IMEI;
        //Guardamos el Header

        devoluciones.setFactura(ObtieneCcarga.get(0).get(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Factura).toString());
        devoluciones.setCliente(ObtieneCcarga.get(0).get(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdCliente).toString());
        devoluciones.setNombrecliente(Funciones.Codificar(ObtieneCcarga.get(0).get(variables_publicas.CONSOLIDADO_CARGA_COLUMN_Cliente).toString()));
        devoluciones.setRango(ObtieneCcarga.get(0).get(variables_publicas.CONSOLIDADO_CARGA_COLUMN_IdConsolidado).toString());
        devoluciones.setUsuario(variables_publicas.usuario.getUsuario());
        devoluciones.setHoragraba(variables_publicas.FechaActual);
        devoluciones.setTipo("P");
        devoluciones.setIMEI(IMEI);
        devoluciones.setMotivo( cboMotivo.getItemAtPosition(cboMotivo.getSelectedItemPosition()).toString());
        devoluciones.setObservaciones(txtObservaciones.getText().toString());

        //Esto lo ponemos para cuando es editar

        DevolucionH.EliminaDevolucion(devoluciones.getNdevolucion());
        DevolucionDetalleH.EliminarDetalleDevolucion(devoluciones.getNdevolucion());


        if (IMEI == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("Es necesario configurar el permiso \"Administrar llamadas telefonicas\" para porder guardar una devolucion, Desea continuar ? ")
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


        boolean saved = DevolucionH.GuardarDevolucion(devoluciones.getNdevolucion(), devoluciones.getCliente(), devoluciones.getNombrecliente(), variables_publicas.FechaActual, devoluciones.getUsuario(),
                String.valueOf(subtotal), String.valueOf(iva), String.valueOf(total), "1", devoluciones.getRango(), devoluciones.getMotivo(),
                devoluciones.getFactura(), devoluciones.getTipo(), IMEI, variables_publicas.usuario.getCodigo(), Funciones.Codificar(devoluciones.getObservaciones().toString()),"0","0");

        if (!saved) {
            MensajeAviso("Ha Ocurrido un error al guardar los datos");
            return false;
        }
        //Guardamos el detalle de la devolucion
        for (HashMap<String, String> item : listaArticulos) {
            saved = DevolucionDetalleH.GuardarDetalleDevolucion(item);
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
            new AlertDialog.Builder(DevolucionesActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(DevolucionesActivity.this,
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

    /**
     * CARGAR COMBOS
     **/
    private void CargaDatosCombo() {

        //Combo Carga
        final List<ConsolidadoCarga> Ccarga;
        Ccarga = ConsolidadoCargaH.BuscarConsolidadoCarga();

        ArrayAdapter<ConsolidadoCarga> adapterCarga = new ArrayAdapter<ConsolidadoCarga>(this, android.R.layout.simple_spinner_item, Ccarga);
        adapterCarga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCarga.setAdapter(adapterCarga);
        if (editar == false) {
            GenerarCodigoDevolucion();
        }

        cboCarga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                carga = (ConsolidadoCarga) adapter.getItemAtPosition(position);
                CcFactura = ConsolidadoCargaH.BuscarConsolidadoCargaFacturas(carga.getIdConsolidado());
                CargarListaFacturas();

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        final List<Motivos> dtMotivos;
        dtMotivos = DevolucionH.ObtenerListaMotivos();
        ArrayAdapter<Motivos> adapterMotivos = new ArrayAdapter<Motivos>(this, android.R.layout.simple_spinner_item, dtMotivos);
        adapterMotivos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboMotivo.setAdapter(adapterMotivos);

        cboMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                devoluciones.setMotivo(((Motivos) adapter.getItemAtPosition(position)).getMotivo());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


    }

    private void GenerarCodigoDevolucion() {

        devoluciones.setNdevolucion("-" + GetFechaISO() + devoluciones.getCliente() + devoluciones.getNdevolucion());
        // lblNoPedido.setText("PEDIDO N°: " + pedido.getCodigoPedido());
    }

    private String GetFechaISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyMMddHHmms");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }


    private void LimipiarDatos(boolean MensajeCaja) {
        if (MensajeCaja) {
            //txtObservaciones.setText("");
            cargadetalle = null;
            txtCodigoArticulo.setText(null);
            txtCantidad.setError(null);
            txtCodigoArticulo.setText("");
            lblDescripcionArticulo.setText("");
            txtCantidad.setText("");

            lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));
            txtCodigoArticulo.requestFocus();
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }


    private boolean AgregarDetalle(HashMap<String, String> itemDevolucion,String cantidad) {
        itemDevolucion.put("IdVehiculo", cargadetalle.getIdVehiculo());
        itemDevolucion.put("ndevolucion", devoluciones.getNdevolucion());
        itemDevolucion.put("factura", lblSearch.getText().toString());
        itemDevolucion.put("item", cargadetalle.getITEM());
        itemDevolucion.put("Cod", cargadetalle.getITEM().split("-")[cargadetalle.getITEM().split("-").length - 1]);
        itemDevolucion.put(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, cargadetalle.getItem_Descripcion());
        itemDevolucion.put("cantidad", cantidad);
        itemDevolucion.put("precio", String.valueOf(cargadetalle.getPRECIO()));

        double subtotal, iva, total, descuento, isc, porIva;
        subtotal = Double.parseDouble(itemDevolucion.get("precio")) * Double.parseDouble(itemDevolucion.get("cantidad"));
        descuento = subtotal * (Double.parseDouble(cargadetalle.getDESCUENTO()) / 100);
        subtotal = subtotal - descuento;
        porIva = Double.parseDouble(cargadetalle.getIVA()) > 0 ? 0.15 : 0;
        itemDevolucion.put("poriva", df.format(porIva));

        iva = subtotal * porIva;
        total = subtotal + iva;
        itemDevolucion.put("descuento", df.format(descuento));
        itemDevolucion.put("tipo", "P");
        itemDevolucion.put("numero", " ");
        itemDevolucion.put("iva", df.format(iva));
        itemDevolucion.put("subtotal", df.format(subtotal));
        itemDevolucion.put("total", df.format(total));

        listaArticulos.add(itemDevolucion);

        PrecioItem = 0;
        RefrescarGrid();
        CalcularTotales();
        return true;

    }

    private void RefrescarGrid() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaArticulos,
                R.layout.devoluciones_list_item, new
                String[]{"Cod", variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion, "cantidad", "precio", "iva", "total"}, new
                int[]{R.id.lblDetalleCodProducto, R.id.lblDetalleDescripcion, R.id.lblDetalleCantidad, R.id.lblDetallePrecio, R.id.lblDetalleIva, R.id.lblDetalleTotal}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                if (currItem.get(variables_publicas.CONSOLIDADO_CARGA_DETALLE_COLUMN_Item_Descripcion).startsWith("**")) {
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

        iva = 0;
        total = 0;
        subtotal = 0;
        totalNewFactura= 0;
        for (int i = 0; i < listaArticulos.size(); i++) {
            HashMap<String, String> item = listaArticulos.get(i);

            try {
                subtotal += (df.parse(item.get("subtotal"))).doubleValue();
                iva += (df.parse(item.get("iva"))).doubleValue();
                total += (df.parse(item.get("total"))).doubleValue();
            } catch (ParseException e) {
                MensajeAviso(e.getMessage());
            }
        }
        //lblSubTotalCor.setText(df.format(subtotal));
        //lblIvaCor.setText(df.format(iva));
        totalNewFactura = totalFactura - total;
        lblTotalCor.setText(df.format(total));
        lblNewTotalFact.setText(df.format(totalNewFactura));
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
            //Se ha agregado esto para pasar el valor del nuevo total al layout     de resultado
            String auxValorNuevo=lblNewTotalFact.getText().toString();

            dialogView = inflater.inflate(R.layout.dialog_ok_dev_layout, null);
            Button btnOK = (Button) dialogView.findViewById(R.id.btnOkDialogo);
            TextView nuevoValor = (TextView) dialogView.findViewById(R.id.nuevoTotal);
            nuevoValor.setText(auxValorNuevo);
            nuevoValor.setTextColor(Color.parseColor("#FFBF5300"));
            btnOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            //finish();
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
        //txtBusquedaItem.setText(txtCodigoArticulo.getText());
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
                            if (lblSearch.getText().toString().equalsIgnoreCase("Seleccionar")) {
                                MensajeAviso("Seleccione una factura");
                                return;
                            } else {
                                listaCCargaArticulosItem = ConsolidadoCargaDetalleH.BuscarConsolidadoCargaDetalleXCodigo(lblSearch.getText().toString(), busqueda);
                            }
                            break;
                        case 2:
                            if (lblSearch.getText().toString().equalsIgnoreCase("Seleccionar")) {
                                MensajeAviso("Seleccione una factura");
                                return;
                            } else {
                                listaCCargaArticulosItem = ConsolidadoCargaDetalleH.BuscarConsolidadoCargaDetalleXNombre(lblSearch.getText().toString(), busqueda);
                            }
                            break;
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
                if (listaCCargaArticulosItem.size() == 0) {
                    MensajeAviso("No se encuentra ningun producto para esta factura ' " + lblSearch.getText().toString() + " '");
                }

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaCCargaArticulosItem,
                        R.layout.list_item_devolucion, new String[]{"ITEM", "Item_Descripcion", "CANTIDAD", "TOTAL"}, new int[]{R.id.Codigo, R.id.Nombre,
                        R.id.Cantidad, R.id.TotalItem});

                lvItem.setAdapter(adapter);
                lblFooterItem.setText("Articulos encontrados: " + String.valueOf(listaCCargaArticulosItem.size()));


            }
        });
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCantidad.setText("");
                String CodigoArticulo = ((TextView) view.findViewById(R.id.Codigo)).getText().toString();
                CantidadPedido = Double.parseDouble(((TextView) view.findViewById(R.id.Cantidad)).getText().toString());
                //String DescArticulo = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();

                cargadetalle = ConsolidadoCargaDetalleH.BuscarConsolidadoCargaDetalle(lblSearch.getText().toString(), CodigoArticulo);
                /*Validamos que permita vender codigo 1052*/

                txtCodigoArticulo.setText(CodigoArticulo);
                lblDescripcionArticulo.setText(cargadetalle.getItem_Descripcion());

                MensajeCaja = true;
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

            String HeaderMenu = obj.get("item") + "\n" + obj.get("Item_Descripcion");

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


                    CalcularTotales();
                    LimipiarDatos(true);
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);
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
                .setMessage("Esta seguro que desea cancelar la devolucion actual?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DevolucionesActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    //endregion

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.cancel();
        }
    }


    private class SincronizardorDevoluciones extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog


            pDialog = new ProgressDialog(DevolucionesActivity.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Funciones.TestInternetConectivity()) {
                if (Boolean.parseBoolean(SincronizarDatos.SincronizarDevolucion(DevolucionH, DevolucionDetalleH, ConsolidadoCargaH, devoluciones.getNdevolucion(), devoluciones.getRango(), devoluciones.getFactura(), jsonDevolucion, (editar == true && devolucionLocal == false)).split(",")[0])) {
                    guardadoOK = true;
                }
            } else {
                guardadoOK = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            MostrarMensajeGuardar();
        }
    }


    private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

         /*   pDialog = new ProgressDialog(DevolucionesActivity.this);
            pDialog.setMessage("consultando version del sistema, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CheckConnectivity();
                if(isOnline){
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
    /*        if (pDialog.isShowing())
                pDialog.dismiss();*/

            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(DevolucionesActivity.this);
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
                builder.show();
            }
        }


    }

}

