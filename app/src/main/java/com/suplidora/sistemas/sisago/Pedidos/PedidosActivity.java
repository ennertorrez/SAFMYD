package com.suplidora.sistemas.sisago.Pedidos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Pedido;
import com.suplidora.sistemas.sisago.Entidades.PedidoDetalle;
import com.suplidora.sistemas.sisago.Entidades.PrecioEspecial;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.R;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PedidosActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = PedidosActivity.class.getSimpleName();
    private boolean MensajeCaja;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    //region Declaracion de controles
    private EditText txtCodigoArticulo;
    private EditText txtDescuento;
    private EditText txtObservaciones;
    private TextView lblCantidad;
    private TextView txtPrecioArticulo;
    private TextView lblDescripcion;
    private TextView lblNombCliente;
    private TextView lblCodCliente;
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
    //endregion

    //region Declaracion de variables
    String IMEI = "";
    String NoPedido = "";
    private String focusedControl = "";
    static final String KEY_IdCliente = "IdCliente";
    static final String KEY_NombreCliente = "Nombre";
    private String IdPedido;
    private Articulo articulo;
    private DecimalFormat df;
    private FormaPago condicion;
    private ClienteSucursal sucursal;
    private double valorPolitica = 3000;
    public static ArrayList<HashMap<String, String>> listaArticulos;
    public static ArrayList<HashMap<String, String>> listaArticulosItem;
    public boolean Estado;
    public double total;
    private Cliente cliente;
    private int IdCliente;
    private double tasaCambio = 0;
    private double subTotalPrecioSuper = 0;
    private String Nombre;
    private int IdVendedor;
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
    private PedidosHelper PedidoH;
    private String CodigoLetra = "";
    private String jsonPedido = "";
    private boolean finalizar = false;
    private String TipoPrecio = "";
    private boolean guardadoOK = false;
    private Vendedor vendedor = null;
    private double PrecioItem = 0;
    private String Tipo = "";
    private String busqueda = "1";
    private int tipoBusqueda =1;
    private boolean validarTipoBusqueda;
    private int IdDepartamento;
    //endregion

    //region OnCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pedidos);
        pedido = new Pedido();

       /* Locale locale = new Locale("en", "US");
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        getApplicationContext().createConfigurationContext(conf);
        res.updateConfiguration(conf, dm);*/

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
        ConfiguracionSistemaH= new ConfiguracionSistemaHelper(DbOpenHelper.database);
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
        if (variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle")) {
            txtDescuento.setEnabled(false);
        }
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
        IdCliente = Integer.parseInt(in.getStringExtra(KEY_IdCliente));
        Nombre = in.getStringExtra(KEY_NombreCliente);


        // Loading spinner data from database
        CargaDatosCombo();

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
        lblRuta.setText(variables_publicas.usuario.getRuta());
        lblCanal.setText(variables_publicas.usuario.getCanal());

        btnBuscar.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//
                BuscarArticulo();
                validarTipoBusqueda =true;
                btnOK.performClick();
                txtCantidad.requestFocus();
                focusedControl = "";
                // }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(txtCantidad, InputMethodManager.SHOW_IMPLICIT);

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
                                                  AgregarDetalle();
                                                  MensajeCaja=true;
                                                  ObtenerPrecio(null,articulo.getCodigo(),false);
                                                  subTotalPrecioSuper = 0;
                                                  for (HashMap<String, String> item : listaArticulos) {
                                                      subTotalPrecioSuper += Double.parseDouble(item.get("SubTotal").replace(",", ""));
                                                  }
                                                  CalcularTotales();


                                                  InputMethodManager inputManager = (InputMethodManager)
                                                          getSystemService(Context.INPUT_METHOD_SERVICE);

                                                  inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                          InputMethodManager.RESULT_SHOWN);

                                                LimipiarDatos();
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

                    Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });

        variables_publicas.PermitirVentaDetAMayoristaXCaja= ConfiguracionSistemaH.BuscarValorConfig("PermitirVentaDetAMayoristaXCaja").getValor();
        variables_publicas.AplicarPrecioMayoristaXCaja= ConfiguracionSistemaH.BuscarValorConfig("AplicarPrecioMayoristaXCaja").getValor();
    }

    private boolean ValidarDescuento() {

        double descuento = Double.parseDouble(txtDescuento.getText().toString().isEmpty() ? "0" : txtDescuento.getText().toString());
        double descuentoArticulo = Double.parseDouble(articulo.getDescuentoMaximo());
        double descuentoCliente = Double.parseDouble(cliente.getDescuento());
        double descuentoMayor = descuentoArticulo > descuentoCliente ? descuentoArticulo : descuentoCliente;
        if (descuento > descuentoMayor) {
            MensajeAviso("El descuento maximo permitido para este producto es de: " + String.valueOf(descuentoArticulo));
            txtDescuento.setText("");
            txtDescuento.requestFocus();
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

    private boolean Guardar() {
        if (lv.getCount() <= 0) {
            MensajeAviso("No se puede guardar el pedido, Debe ingresar al menos 1 item");
            return false;
        }

        String mensaje = "";
        if (Double.parseDouble(lblSubTotalCor.getText().toString().replace(",", "")) < valorPolitica && (cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo") ) ) {
            mensaje = "Este cliente es de tipo FORANEO, pero el pedido es menor a C$3,000 por lo que se guardará como tipo :DETALLE. Esta seguro que desea continuar?";
            Tipo = "Detalle";
        } else {
            Tipo = cliente.getTipo();
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
                            SincronizarPedido(PedidoH.ObtenerPedido(IdPedido));
                        }
                        else{
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

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
        } else {
            IMEI = getIMEI(PedidosActivity.this);
        }


        //Guardamos el Header
        boolean saved = PedidoH.GuardarPedido(IdPedido, String.valueOf(IdVendedor), String.valueOf(IdCliente), cliente.getCodCv(), Tipo,
                txtObservaciones.getText().toString(), condicion.getCODIGO(), codSuc,
                variables_publicas.FechaActual, variables_publicas.usuario.getUsuario(), IMEI, String.valueOf(total));

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

    private void CargaDatosCombo() {
        List<Vendedor> vendedores = VendedoresH.ObtenerListaVendedores();
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(this, android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboVendedor.setAdapter(adapterVendedor);

        cliente = ClientesH.BuscarCliente(String.valueOf(IdCliente));
        IdDepartamento= Integer.parseInt(cliente.getIdDepartamento());
        if (variables_publicas.usuario.getCodigo().equals("0")) {
            IdVendedor = Integer.parseInt(cliente.getIdVendedor());
        } else {
            IdVendedor = Integer.parseInt(variables_publicas.usuario.getCodigo());
        }

        if (cliente == null) {
            MensajeAviso("El cliente no se encuentra en la base de datos");
            finish();
        }
        IdPedido = "-" + cliente.getIdCliente() + String.valueOf(IdVendedor) + String.valueOf(PedidoH.ObtenerNuevoCodigoPedido());
        if (!variables_publicas.TipoUsuario.equals("Vendedor")) {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != IdVendedor; i++)
                vendedor = vendedores.get(i);
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        } else {
            Vendedor vendedor = vendedores.get(0);
            for (int i = 0; Integer.parseInt(vendedor.getCODIGO()) != Integer.parseInt(variables_publicas.usuario.getCodigo()); i++) {
                vendedor = vendedores.get(i);
                this.vendedor = vendedor;
            }
            cboVendedor.setSelection(adapterVendedor.getPosition(vendedor));
        }
        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        List<ClienteSucursal> sucursales = ClientesSucursalH.ObtenerClienteSucursales(String.valueOf(IdCliente));
        ArrayAdapter<ClienteSucursal> adapterSucursal = new ArrayAdapter<ClienteSucursal>(this, android.R.layout.simple_spinner_item, sucursales);
        adapterSucursal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSucursal.setAdapter(adapterSucursal);
        cboSucursal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                sucursal = (ClienteSucursal) adapter.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        cboSucursal.setSelection(0);
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

    private void ObtenerPrecio(final HashMap<String, String> item, String CodArticulo, final boolean ActualizarItem) {
        variables_publicas.lstDepartamentosForaneo1 = ConfiguracionSistemaH.BuscarValorConfig("lstDepartamentosForaneo1").getValor().split(",");
       String[] lstDepartamentosForaneo1 = variables_publicas.lstDepartamentosForaneo1;
        final HashMap<String,String> art = ArticulosH.BuscarArticuloHashMap(CodArticulo);
        //Esto para utilizarlo en el metodo SetPrecio
        Articulo articulo = ArticulosH.BuscarArticulo(CodArticulo);
        boolean AplicarPrecioDetalle  =Boolean.parseBoolean( articulo.getAplicaPrecioDetalle());
        int ModCantidadCajas, cantidadItems=0, FaltaParaCaja, cajas,UnidadCaja;
        boolean PrecioCajas= false;
        UnidadCaja = Integer.parseInt( articulo.getUnidadCaja());
        if (item!=null){
            if( (item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)).isEmpty()){
                cantidadItems = 0;
            }else{
                cantidadItems = Integer.parseInt( item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad));
            }
        }else{
            try {
                cantidadItems= Integer.parseInt( txtCantidad.getText().toString().isEmpty() ? "0" : txtCantidad.getText().toString());
            }catch (Exception e){
                cantidadItems=0;
            }

        }

        UnidadCaja =UnidadCaja == 0 ? 1 : Integer.parseInt( articulo.getUnidadCaja());
        ModCantidadCajas = (cantidadItems % UnidadCaja);
        if( cantidadItems >= UnidadCaja) {
            PrecioCajas = true;
        }
        FaltaParaCaja = UnidadCaja - ModCantidadCajas;
        cajas = cantidadItems / UnidadCaja ;

        String tipoprecio = "Super";
        if( Integer.parseInt(vendedor.getCODIGO()) == 9){  //Ventas Oficina
            IdDepartamento = 6;
        }
        if (cliente.getTipo().equalsIgnoreCase( "Detalle") ){
            if(Boolean.parseBoolean( cliente.getRutaForanea()) && ! AplicarPrecioDetalle) {
                tipoprecio = "Super";
            }else
            {
                tipoprecio = "Detalle";
            }
            //Si es ruta no determinada y departamento managua
            if (cliente.getRuta().equalsIgnoreCase("No Determinada" )) {
                if (IdDepartamento == 6) {
                    tipoprecio = "Detalle";
                } else {
                    tipoprecio = "Super";
                }
            }

            if (Integer.parseInt(vendedor.getCODIGO())==9){
                tipoprecio = "Detalle";
            }
        }else if (!cliente.getTipo().equalsIgnoreCase("Super") ) {

            String TipoForaneo = "Precio" + (Arrays.asList(lstDepartamentosForaneo1).contains(cliente.getIdDepartamento()) ? "Foraneo" : "Foraneo2");

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

        if(variables_publicas.AplicarPrecioMayoristaXCaja.equalsIgnoreCase("1") ){
            if(cantidadItems > 0 ){
                if(PrecioCajas && cliente.getTipo() != "Super" ){
                    if( FaltaParaCaja > 0 && ModCantidadCajas > 0 ){
                        if(variables_publicas.PermitirVentaDetAMayoristaXCaja.equalsIgnoreCase("1") || cliente.getTipo().equalsIgnoreCase("Detalle")) {
                            if(MensajeCaja){
                                final String finalTipoprecio = tipoprecio;
                                MensajeCaja=false;
                                new AlertDialog.Builder(this)
                                .setTitle("Confirmación Requerida")
                                .setMessage("Para dar precio mayorista se necesita " + String.valueOf(FaltaParaCaja) + " unidades para completar " + String.valueOf(cajas+1) + " cajas, Desea continuar ? ")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if(!ActualizarItem){
                                            MensajeCaja=true;
                                            setPrecio(art, finalTipoprecio,0);
                                            LimipiarDatos();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        if(!ActualizarItem){
                                            setPrecio(art, finalTipoprecio,0);
                                            if(listaArticulos.size()>0) {
                                                listaArticulos.remove(listaArticulos.size() - 1);
                                                RefrescarGrid();
                                            }
                                            txtCantidad.requestFocus();
                                        }
                                    }
                                })
                                .show();
                            }
                        }else{
                            if( MensajeCaja){
                                MensajeCaja = false;
                                listaArticulos.remove(listaArticulos.size()-1);
                                MensajeAviso("Para dar precio mayorista se necesita " + String.valueOf( FaltaParaCaja )+ " unidades para completar " + String.valueOf( cajas+1 )+ " cajas");
                                txtCantidad.requestFocus();
                            }
                        }

                    }else{
                        if((cliente.getTipo().equalsIgnoreCase("Detalle") && Boolean.parseBoolean(cliente.getRutaForanea()) && ! AplicarPrecioDetalle) || cliente.getTipo().equalsIgnoreCase( "Foraneo")){
                            tipoprecio = "Foraneo";
                        }else{
                            tipoprecio = "Mayorista";
                        }
                    }
                }else{
                    if (cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo")) {
                        if(! variables_publicas.PermitirVentaDetAMayoristaXCaja.equals("1")  && MensajeCaja ){
                            setPrecio(art,tipoprecio,0);
                            MensajeCaja = false;
                            listaArticulos.remove(listaArticulos.size()-1);
                            MensajeAviso("Para dar precio mayorista se necesita " + String.valueOf(FaltaParaCaja)+ " unidades para completar " + String.valueOf( cajas +1)+ " cajas");
                            txtCantidad.requestFocus();
                        }else {
                            setPrecio(art,tipoprecio,0);
                            MensajeCaja = true;
                        }

                    }else{
                        setPrecio(art,tipoprecio,0);
                        MensajeCaja = true;
                    }
                }
            }else{
                //Si itemDetalle.cantidad=0 entonces le damos el precio mas alto
                if(cliente.getTipo().equalsIgnoreCase( "Foraneo" )){
                    tipoprecio = "Super"; //Super es : Detalle Foraneo
                }else if (cliente.getTipo().equalsIgnoreCase("Mayorista")){
                    tipoprecio = "Detalle";
                }else{
                    tipoprecio = cliente.getTipo();
                }
            }


        }

        if(Boolean.parseBoolean(cliente.getEmpleado()) && Integer.parseInt(condicion.getCODIGO()) != 127 ){ // esto para validar que no sea producto abordo --Tramite de CK
            tipoprecio = "Mayorista";
        }
        double precioE = 0;

        if( Boolean.parseBoolean(cliente.getPrecioEspecial()) &&  (cliente.getTipo().equalsIgnoreCase( "Super") || cliente.getTipo().equalsIgnoreCase("Mayorista") || cliente.getTipo().equalsIgnoreCase("Foraneo")) ){
            txtDescuento.setEnabled(false);
            txtDescuento.setText("0.00");
            //Si existe precio especial
            tipoprecio = "Especial";
            PrecioEspecial precioEspecial =  PrecioEspecialH.BuscarPrecioEspecial(String.valueOf(IdCliente), articulo.getCodigo());
            if (precioEspecial != null) {
                if (precioEspecial.getFacturar().equals("0")) {
                    MensajeAviso("Este Producto no esta habilidado para venderlo a este cliente");
                    return;
                }
                precioE= Double.parseDouble(precioEspecial.getPrecio());
                item.put("Precio", precioEspecial.getPrecio());
                item.put("TipoPrecio", "Especial");
            }

        }

        if(!ActualizarItem){
            setPrecio(art, tipoprecio,0);
        }else{
            if(item!=null){
                if(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoArt).equals("P")) {
                    if(tipoprecio.equalsIgnoreCase("Especial")) {
                        item.put("Precio", String.valueOf(precioE));
                    }else{
                        item.put("Precio", art.get("Precio"+tipoprecio));
                    }
                }else{
                    tipoprecio = "Bonificacion";
                }

                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_TipoPrecio,  tipoprecio);
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Descuento ,
                        df.format(Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)) * (Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento)) / 100)));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal,
                        df.format(Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio)) - ((Integer.parseInt(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Cantidad)) * Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Precio))) * (Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_PorDescuento)) / 100))));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva ,
                        df.format( Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal).replace(",","")) * Double.parseDouble(articulo.getPorIva())));
                item.put(variables_publicas.PEDIDOS_DETALLE_COLUMN_Total,df.format( Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Subtotal).replace(",","")) +Double.parseDouble(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_Iva).replace(",",""))));
            }

        }
    }

    private void LimipiarDatos(){
        if(MensajeCaja){
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
        }

    }

    private void RecalcularDetalle() {

        MensajeCaja=false;
        for ( HashMap<String, String> item : listaArticulos) {
            ObtenerPrecio(item,item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo),true);
        }

    }


    private boolean EsArticuloRepetido(String s) {

        for (HashMap<String, String> item : listaArticulos) {
            if (item.get("CodigoArticulo").equals(s)) {
                return true;
            }
        }
        return false;
    }
    private void setPrecio(HashMap<String,String> articulo, String pTipoPrecio,double precio){
        if (pTipoPrecio.equalsIgnoreCase("Especial")) {
            txtPrecioArticulo.setText(String.valueOf(precio));
            PrecioItem = precio;
        }else{
            txtPrecioArticulo.setText(articulo.get("Precio"+pTipoPrecio));
            PrecioItem = Double.parseDouble(articulo.get("Precio"+pTipoPrecio));
        }
        TipoPrecio = pTipoPrecio;

    }

    private void AgregarDetalle() {
        double Precio = PrecioItem;
        String DescripcionArt = lblDescripcionArticulo.getText().toString();
        HashMap<String, String> itemPedidos = new HashMap<>();
        itemPedidos.put("CodigoPedido", IdPedido);
        itemPedidos.put("CodigoArticulo", articulo.getCodigo());
        itemPedidos.put("Cod", articulo.getCodigo().substring(articulo.getCodigo().length() - 3));
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


        HashMap<String, String> itemBonificado = CartillasBcDetalleH.BuscarBonificacion(itemPedidos.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo), variables_publicas.usuario.getCanal(), variables_publicas.FechaActual, itemPedidos.get("Cantidad"));
        Articulo articuloB = ArticulosH.BuscarArticulo(itemBonificado.get("itemB"));
        if (itemBonificado.size() > 0) {

            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 17 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que posee bonificacion y excede el limite de 18 productos para un pedido Mayorista");
            }
            listaArticulos.add(itemPedidos);
            HashMap<String, String> articuloBonificado = new HashMap<>();
            articuloBonificado.put("CodigoPedido", IdPedido);
            articuloBonificado.put("Cod", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB).substring(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB).length() - 3));
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
            articuloBonificado.put("BonificaA", "");
            articuloBonificado.put("Isc", "0");
            articuloBonificado.put("PorcentajeIva", "0");
            articuloBonificado.put("Descuento", "0");
            articuloBonificado.put("Iva", "0");
            articuloBonificado.put("SubTotal", "0");
            articuloBonificado.put("Total", "0");
            //Actualizamos el campo BonificaA del item que lo bonifica
            for (HashMap<String, String> item : listaArticulos) {
                if (item.get("CodigoArticulo").equals(itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemV)) && item.get("TipoArt").equals("P")) {
                    item.put("BonificaA", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB));
                    break;
                }
            }
            listaArticulos.add(articuloBonificado);
        } else {
            //Validamos que solamente se puedan ingresar 18 articulos
            if (listaArticulos.size() == 18 && cliente.getDetallista().equalsIgnoreCase("false")) {
                MensajeAviso("No se puede agregar el producto seleccionado,ya que excede el limite de 18 productos para un pedido Mayorista");
                return;
            }
            listaArticulos.add(itemPedidos);
        }
        PrecioItem = 0;
        RefrescarGrid();

        CalcularTotales();


    }

    private void RefrescarGrid() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaArticulos,
                R.layout.pedidos_list_item, new
                String[]{"Cod", "Cantidad", "Precio","TipoPrecio", "Descripcion", "PorDescuento", "Descuento", "SubTotal", "Iva", "Total"}, new
                int[]{R.id.lblDetalleCodProducto, R.id.lblDetalleCantidad, R.id.lblDetallePrecio,R.id.lblDetalleTipoPrecio, R.id.lblDetalleDescripcion, R.id.lblDetallePorDescuento, R.id.lblDetalleDescuento, R.id.lblDetalleSubTotal, R.id.lblDetalleIva, R.id.lblDetalleTotal}) {

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

        double subtotal = 0, iva = 0, descuento = 0;
        total = 0;
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
                  if(validarTipoBusqueda){
                      try {
                          int busquedaText= Integer.parseInt( busqueda);
                          rgGrupo.check(R.id.rbCodigo);

                      } catch (Exception ex) {

                          if(busqueda.contains("-"))
                          {
                              rgGrupo.check(R.id.rbCodigo);
                          }
                          else{
                              rgGrupo.check(R.id.rbDescripcion);}
                      }
                      validarTipoBusqueda=false;
                  }
                    int boton = rgGrupo.getCheckedRadioButtonId();// == R.id.rbCodigo ? "1" : "2";
                    switch (boton) {
                        case R.id.rbCodigo:
                            tipoBusqueda =1;
                            break;
                        case R.id.rbDescripcion:
                            tipoBusqueda =2;
                            break;
                    }
                    try {
                        switch (tipoBusqueda){
                            case 1:
                                listaArticulosItem=ArticulosH.BuscarArticuloCodigo(busqueda);
                                break;
                            case  2:
                                listaArticulosItem=ArticulosH.BuscarArticuloNombre(busqueda);
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
                                R.layout.list_item, new String[]{"Codigo", "Nombre","PrecioSuper", "PrecioDetalle","PrecioForaneo","PrecioForaneo2","PrecioMayorista"}, new int[]{R.id.Codigo, R.id.Nombre,
                                R.id.PrecioSuper, R.id.PrecioDetalle,R.id.PrecioForaneo,R.id.PrecioForaneo2,R.id.PrecioMayorista});

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
                HashMap<String,String> art =  ArticulosH.BuscarArticuloHashMap(CodigoArticulo);
                txtCodigoArticulo.setText(CodigoArticulo);
                lblDescripcionArticulo.setText(articulo.getNombre());
                MensajeCaja=true;
                ObtenerPrecio(null,CodigoArticulo,false);

                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    //endregion

    //region Eventos


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    IMEI = getIMEI(PedidosActivity.this);
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
                    listaArticulos.remove(info.position);

                    if (!itemArticulo.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA).equals("")) {
                        for (int i = 0; i < listaArticulos.size(); i++) {
                            HashMap<String, String> a = listaArticulos.get(i);
                            if (a.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo).equals(itemArticulo.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_BonificaA))) {
                                listaArticulos.remove(a);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);

                    RecalcularDetalle();
                    CalcularTotales();
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

           if (SincronizarDatos.SincronizarPedido(PedidosActivity.this, PedidoH, PedidoDetalleH, vendedor, cliente, IdPedido, jsonPedido)) {
                guardadoOK = true;
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


}

