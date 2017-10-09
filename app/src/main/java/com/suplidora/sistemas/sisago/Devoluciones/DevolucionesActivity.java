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
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConfiguracionSistemaHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConsolidadoCargaDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ConsolidadoCargaHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
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
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCarga;
import com.suplidora.sistemas.sisago.Entidades.Devoluciones;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Pedido;
import com.suplidora.sistemas.sisago.Entidades.PedidoDetalle;
import com.suplidora.sistemas.sisago.Entidades.PrecioEspecial;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

public class DevolucionesActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = DevolucionesActivity.class.getSimpleName();
    private boolean MensajeCaja;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    //region Declaracion de controles

    private EditText txtDescuento;
    private EditText txtObservaciones;
    private TextView lblCantidad;
    private TextView txtPrecioArticulo;
    private TextView lblDescripcion;
    private TextView lblNombCliente;
    private TextView lblCodCliente;
    private TextView txtCodigoArticulo;
    private TextView lblDescripcionArticulo;
    private TextView lblSubTotalCor;
    private TextView lblIvaCor;
    private TextView lblTotalCor;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private Button btnAgregar;
    private Button btnBuscaItem;
    private Button btnOK;
    private Button btnGuardar;
    private Button btnCancelar;
    private EditText txtCantidad;
    private Spinner cboCarga;
    private Spinner cboNoFactura;
    private ListView lv;
    private ListView lvItem;
    private SimpleAdapter adapter;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;
    private String CodigoArticulo;

    //endregion

    //region Declaracion de variables
    String IMEI = "";
    private String focusedControl = "";

    private Articulo articulo;
    private DecimalFormat df;
    private FormaPago condicion;
    private ClienteSucursal sucursal;
    private ConsolidadoCarga carga = null;
    public static ArrayList<HashMap<String, String>> listaArticulos;
    public static ArrayList<HashMap<String, String>> listaArticulosItem;
    public boolean Estado;
    public double total;
    public double subtotal;
    private Cliente cliente;
    private double tasaCambio = 0;
    private Devoluciones devoluciones;

    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private ClientesSucursalHelper ClientesSucursalH;
    private FormaPagoHelper FormaPagoH;
    private ArticulosHelper ArticulosH;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private ConsolidadoCargaHelper ConsolidadoCargaH;
    private ConsolidadoCargaDetalleHelper ConsolidadoCargaDetalleH;
    private PrecioEspecialHelper PrecioEspecialH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private ConfiguracionSistemaHelper ConfiguracionSistemaH;

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
        listaArticulosItem = new ArrayList<HashMap<String, String>>();

        DbOpenHelper = new DataBaseOpenHelper(DevolucionesActivity.this);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ClientesSucursalH = new ClientesSucursalHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        ConfiguracionSistemaH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        ConsolidadoCargaH = new ConsolidadoCargaHelper(DbOpenHelper.database);

        cboCarga = (Spinner) findViewById(R.id.cboCarga);
        cboNoFactura = (Spinner) findViewById(R.id.cboNoFactura);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        // Displaying all values on the screen
        final TextView lblCodigoCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        //final Spinner cboVendedor = (Spinner) findViewById(R.id.cboVendedor);
        TextView lblNombre = (TextView) findViewById(R.id.lblNombCliente);
        //Obtenemos las referencias a los controles
        lblCodCliente = (TextView) findViewById(R.id.lblCodigoCliente);
        lblNombCliente = (TextView) findViewById(R.id.lblNombCliente);
        txtCodigoArticulo = (TextView) findViewById(R.id.lblCodArticulo);
        lblDescripcionArticulo = (TextView) findViewById(R.id.lblDescArticulo);
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


        txtObservaciones = (EditText) findViewById(R.id.txtObservacion);
        txtPrecioArticulo = (TextView) findViewById(R.id.txtPrecioArticulo);
        lblSubTotalCor = (TextView) findViewById(R.id.lblSubTotalCor);
        lblIvaCor = (TextView) findViewById(R.id.lblIvaCor);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        // getting intent data
        Intent in = getIntent();


        // Loading spinner data from database
        CargaDatosCombo();

        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnBuscaItem = (Button) findViewById(R.id.btnBuscaItem);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
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


        lblNombre.setText(Nombre);

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

                                                  if (TextUtils.isEmpty(txtCantidad.getText().toString())) {
                                                      txtCantidad.setError("Ingrese un valor");
                                                      return;
                                                  }

                                                  if (Double.parseDouble(txtCantidad.getText().toString()) < 1) {
                                                      txtCantidad.setError("Ingrese un valor mayor a 0");
                                                      return;
                                                  }


                                                  if (PrecioItem == 0) {
                                                      MensajeAviso("Ha ocurrido un error por favor seleccione nuevamente el articulo");
                                                      return;
                                                  }
                                                  HashMap<String, String> itemPedidos = new HashMap<>();
                                                 if( AgregarDetalle(itemPedidos)){
                                                     MensajeCaja = true;

                                                     LimipiarDatos(MensajeCaja);
                                                     for (HashMap<String, String> item : listaArticulos) {
                                                     }
                                                     RecalcularDetalle();
                                                     CalcularTotales();

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
                    CodigoLetra = lblCodigoCliente.getText().toString();

                   // Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });

       // cboVendedor.setEnabled(false);
    }

    private void ValidarUltimaVersion() {
        boolean isOnline = new Funciones().checkInternetConnection(DevolucionesActivity.this);

        if (isOnline) {
            String latestVersion = "";
            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            try {
                new GetLatestVersion().execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private boolean ValidarDescuento() {

        double descuento = Double.parseDouble(txtDescuento.getText().toString().isEmpty() ? "0" : txtDescuento.getText().toString());
        double descuentoArticulo = Double.parseDouble(articulo.getDescuentoMaximo());
        double descuentoCliente = Double.parseDouble(cliente.getDescuento());
        double descuentoMayor = descuentoArticulo > descuentoCliente ? descuentoArticulo : descuentoCliente;
        if (descuento > descuentoMayor) {
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
            new SincronizardorPedidos().execute();
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

    /**CARGAR COMBOS**/
    private void CargaDatosCombo() {

        //Combo Carga
        final List<ConsolidadoCarga> Ccarga;
        Ccarga= ConsolidadoCargaH.BuscarConsolidadoCarga();

        ArrayAdapter<ConsolidadoCarga> adapterCarga = new ArrayAdapter<ConsolidadoCarga>(this,android.R.layout.simple_spinner_item,Ccarga);
        adapterCarga.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboCarga.setAdapter(adapterCarga);

        cboCarga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                carga = (ConsolidadoCarga) adapter.getItemAtPosition(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        //Combo Carga Fact
        /*final List<ConsolidadoCarga> CcargaFact;
        CcargaFact= ConsolidadoCargaH.BuscarConsolidadoCargaFacturas();

        ArrayAdapter<ConsolidadoCarga> adapterCargaFact = new ArrayAdapter<ConsolidadoCarga>(this,android.R.layout.simple_spinner_item,CcargaFact);
        adapterCargaFact.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboNoFactura.setAdapter(adapterCargaFact);

        cboNoFactura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                carga = (ConsolidadoCarga) adapter.getItemAtPosition(position);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });*/


    }

    private void GenerarCodigoPedido() {

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
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }

    private void RecalcularDetalle() {


    }


    private void setPrecio(HashMap<String, String> articulo, String pTipoPrecio, double precio,boolean settxtPrecioArticulo) {
        if (pTipoPrecio.equalsIgnoreCase("Especial")) {
            if(settxtPrecioArticulo){
                txtPrecioArticulo.setText(String.valueOf(precio));
            }
            PrecioItem = precio;
        } else {
            if(settxtPrecioArticulo){
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


        itemPedidos.put("CodigoArticulo", articulo.getCodigo());
        itemPedidos.put("Cod", articulo.getCodigo().split("-")[articulo.getCodigo().split("-").length - 1]);
        itemPedidos.put("Cantidad", txtCantidad.getText().toString());
        itemPedidos.put("Precio", String.valueOf(Precio));
        itemPedidos.put("TipoPrecio", TipoPrecio);
        itemPedidos.put("Descripcion", DescripcionArt);
        itemPedidos.put("Costo", String.valueOf(Double.parseDouble(articulo.getCosto())));
        itemPedidos.put("PorDescuento", txtDescuento.getText().toString().equals("") ? "0" : txtDescuento.getText().toString());
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


        HashMap<String, String> itemBonificado = CartillasBcDetalleH.BuscarBonificacion(itemPedidos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo), variables_publicas.usuario.getCanal(), variables_publicas.FechaActual, itemPedidos.get("Cantidad"));
        Articulo articuloB = ArticulosH.BuscarArticulo(itemBonificado.get("itemB"));

        if (itemBonificado.size() > 0) {

            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 17 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que posee bonificacion y excede el limite de 18 productos para un pedido Mayorista");
                return false;
            }
            listaArticulos.add(itemPedidos);
            HashMap<String, String> articuloBonificado = new HashMap<>();

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
        } else {
            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 18 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que excede el limite de 18 productos para un pedido Mayorista");
                return false;
            }
            listaArticulos.add(itemPedidos);
        }
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
                        R.layout.list_item, new String[]{"Codigo", "Nombre", "PrecioSuper", "PrecioDetalle", "PrecioForaneo", "PrecioForaneo2", "PrecioMayorista"}, new int[]{R.id.Codigo, R.id.Nombre,
                        R.id.PrecioSuper, R.id.PrecioDetalle, R.id.PrecioForaneo, R.id.PrecioForaneo2, R.id.PrecioMayorista});

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

                HashMap<String, String> art = ArticulosH.BuscarArticuloHashMap(CodigoArticulo);
                txtCodigoArticulo.setText(CodigoArticulo);
                lblDescripcionArticulo.setText(articulo.getNombre());

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
                    for (int i = 0; i < listaArticulos.size(); i++) {
                        HashMap<String, String> a = listaArticulos.get(i);
                        if (a.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA).equals(itemArticulo.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo))) {
                            listaArticulos.remove(a);
                        }
                    }

                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);

                    RecalcularDetalle();
                    CalcularTotales();
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
                        DevolucionesActivity.this.finish();
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
            pDialog = new ProgressDialog(DevolucionesActivity.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (Funciones.TestInternetConectivity()) {

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
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();


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

