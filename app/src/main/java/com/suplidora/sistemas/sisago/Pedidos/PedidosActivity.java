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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesSucursalHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Articulo;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.ClienteSucursal;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Pedido;
import com.suplidora.sistemas.sisago.Entidades.PedidoDetalle;
import com.suplidora.sistemas.sisago.Entidades.PrecioEspecial;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.suplidora.sistemas.sisago.Auxiliar.Funciones.Codificar;

public class PedidosActivity extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

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
    private Button btnAgregar;
    private Button btnBuscar;
    private Button btnGuardar;
    private Button btnCancelar;
    private EditText txtCantidad;
    private Spinner cboVendedor;
    private Spinner cboSucursal;
    private Spinner cboCondicion;
    private ListView lv;
    private SimpleAdapter adapter;
    private ProgressDialog pDialog;
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
    private PedidosHelper PedidoH;
    private String CodigoLetra = "";
    private String jsonPedido = "";
    private boolean finalizar = false;
    private String TipoPrecio = "";
    private boolean guardadoOK = false;
    private Vendedor vendedor = null;
    private double PrecioItem = 0;
    private String Tipo = "";
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
                if (TextUtils.isEmpty(txtCodigoArticulo.getText().toString())) {
                    txtCodigoArticulo.setError("Ingrese un valor");
                    return;
                }
                String CodigoArticulo = txtCodigoArticulo.getText().toString();
                articulo = ArticulosH.BuscarArticulo(CodigoArticulo);
                if (articulo == null) {
                    MensajeAviso("El codigo de articulo ingresado no existe en la base de datos o esta deshabilitado para su venta");
                    return;
                }
                //Recorremos los resultados para mostrarlos en pantalla
                txtCodigoArticulo.setText("");
                lblDescripcionArticulo.setText("");
                txtCodigoArticulo.setText(articulo.getCodigo());
                lblDescripcionArticulo.setText(articulo.getNombre());

                ObtenerPrecio();

                //if (!focusedControl.equalsIgnoreCase("txtCodigoArticulo")) {
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
                                                  RecalcularDetalle();
                                                  CalcularTotales();
                                                  txtPrecioArticulo.setText("");

                                                  InputMethodManager inputManager = (InputMethodManager)
                                                          getSystemService(Context.INPUT_METHOD_SERVICE);

                                                  inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                          InputMethodManager.RESULT_SHOWN);
                                                  articulo = null;
                                                  txtCodigoArticulo.setText(null);
                                                  txtCantidad.setError(null);
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
                    DbOpenHelper.database.beginTransaction();
                    Guardar();
                } catch (Exception e) {
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });
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
            new SincronizardorPedidos().execute().get();
        } catch (Exception ex) {
            MensajeAviso(ex.getMessage());
        }

        return false;
    }

    private boolean Guardar() {
        if (lv.getCount() <= 0) {
            MensajeAviso("No se puede guardar el pedido, Debe ingresar al menos 1 item");
            return false;
        }

        String mensaje = "";
        if (Double.parseDouble(lblSubTotalCor.getText().toString().replace(",", "")) < 3000.00) {
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
                        if (GuardarPedido()) {
                            DbOpenHelper.database.setTransactionSuccessful();
                            DbOpenHelper.database.endTransaction();
                            SincronizarPedido(PedidoH.ObtenerPedido(IdPedido));
                            MostrarMensajeGuardar();
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

    private void ObtenerPrecio() {
        //Si es cliente Detalle
        if (cliente.getTipo().equals("Detalle")) {
            //Si es Ruta Foranea y no aplica PrecioDetalle
            if (variables_publicas.usuario.getRutaForanea().equals("1") && !articulo.getAplicaPrecioDetalle().equals("true")) {
                txtPrecioArticulo.setText(articulo.getPrecioSuper());
                TipoPrecio = "PrecioSuper";
                PrecioItem = Double.parseDouble(articulo.getPrecioSuper());
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioDetalle());
                PrecioItem = Double.parseDouble(articulo.getPrecioDetalle());
                TipoPrecio = "PrecioDetalle";
            }
        }

        //Si es cliente Mayorista foraneo
        if (cliente.getTipo().equals("Foraneo")) {
            if (subTotalPrecioSuper < valorPolitica) {
                txtPrecioArticulo.setText(articulo.getPrecioSuper());
                PrecioItem = Double.parseDouble(articulo.getPrecioSuper());
                TipoPrecio = "PrecioSuper";
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioForaneo());
                PrecioItem = Double.parseDouble(articulo.getPrecioForaneo());
                TipoPrecio = "PrecioForaneo";
            }
        }
        //Si es cliente Mayorista
        if (cliente.getTipo().equals("Mayorista")) {
            if (subTotalPrecioSuper < valorPolitica) {
                txtPrecioArticulo.setText(articulo.getPrecioDetalle());
                PrecioItem = Double.parseDouble(articulo.getPrecioDetalle());
                TipoPrecio = "PrecioDetalle";
            } else {
                txtPrecioArticulo.setText(articulo.getPrecioMayorista());
                PrecioItem = Double.parseDouble(articulo.getPrecioMayorista());
                TipoPrecio = "PrecioMayorista";
            }
        }

        //Validamos que si es empleado, damos a precio mayorista
        if (cliente.getEmpleado().equals("1") && Integer.parseInt(condicion.getCODIGO()) != 127) {
            txtPrecioArticulo.setText(articulo.getPrecioMayorista());
            TipoPrecio = "PrecioMayorista";
        }

        if (cliente.getPrecioEspecial().equals("true") && (cliente.getTipo().equals("Super") || cliente.getTipo().equals("Mayorista"))) {
            txtDescuento.setEnabled(false);
            PrecioEspecial precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(String.valueOf(IdCliente), articulo.getCodigo());
            if (precioEspecial != null) {
                if (precioEspecial.getFacturar().equals("0")) {
                    MensajeAviso("Este Producto no esta habilidado para venderlo a este cliente");
                    return;
                }
                txtPrecioArticulo.setText(precioEspecial.getPrecio());
                txtDescuento.setText(precioEspecial.getDescuento());
                TipoPrecio = "PrecioEspecial";
            }
        }

    }

    private void RecalcularDetalle() {
        subTotalPrecioSuper = 0;
        for (HashMap<String, String> item : listaArticulos) {
            subTotalPrecioSuper = Double.parseDouble(item.get("SubTotal").replace(",", ""));
        }

        for (HashMap<String, String> item : listaArticulos) {
            Articulo articulo = ArticulosH.BuscarArticulo(item.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoArticulo));
            //Si es cliente Mayorista foraneo
            if (cliente.getTipo().equals("Foraneo")) {
                if (subTotalPrecioSuper < valorPolitica) {
                    item.put("Precio", articulo.getPrecioSuper());
                    item.put("TipoPrecio", "PrecioSuper");
                } else {
                    item.put("Precio", articulo.getPrecioForaneo());
                    item.put("TipoPrecio", "PrecioForaneo");
                }
            }
            //Si es cliente Mayorista
            if (cliente.getTipo().equals("Mayorista")) {
                if (subTotalPrecioSuper < valorPolitica) {
                    item.put("Precio", articulo.getPrecioSuper());
                    item.put("TipoPrecio", "PrecioDetalle");
                } else {
                    item.put("Precio", articulo.getPrecioMayorista());
                    item.put("TipoPrecio", "PrecioMayorista");
                }
            }

            //Validamos que si es empleado, damos a precio mayorista
            if (cliente.getEmpleado().equals("1") && Integer.parseInt(condicion.getCODIGO()) != 127) {
                item.put("Precio", articulo.getPrecioMayorista());
                item.put("TipoPrecio", "PrecioMayorista");
            }

            if (cliente.getPrecioEspecial().equals("true") && (cliente.getTipo().equals("Super") || cliente.getTipo().equals("Mayorista"))) {
                PrecioEspecial precioEspecial = PrecioEspecialH.BuscarPrecioEspecial(String.valueOf(IdCliente), articulo.getCodigo());
                if (precioEspecial != null) {
                    if (precioEspecial.getFacturar().equals("0")) {
                        MensajeAviso("Este Producto no esta habilidado para venderlo a este cliente");
                        return;
                    }
                    item.put("Precio", precioEspecial.getPrecio());
                    item.put("TipoPrecio", "PrecioEspecial");
                }
            }
            double subtotal, iva, total, descuento, porIva;
            subtotal = Double.parseDouble(item.get("Precio")) * Double.parseDouble(item.get("Cantidad"));
            descuento = subtotal * (Double.parseDouble(item.get("PorDescuento")) / 100);
            subtotal = subtotal - descuento;
            porIva = Double.parseDouble(articulo.getPorIva());
            iva = subtotal * porIva;
            total = subtotal + iva;
            item.put("Descuento", df.format(descuento));
            item.put("PorcentajeIva", articulo.getPorIva());
            item.put("Um", articulo.getUnidad());
            item.put("Iva", df.format(iva));
            item.put("SubTotal", df.format(subtotal));
            item.put("Total", df.format(total));
            subTotalPrecioSuper += Double.parseDouble(item.get("SubTotal").replace(",", ""));
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
            articuloBonificado.put("CodigoArticulo", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB));
            itemPedidos.put("Cod", itemBonificado.get(variables_publicas.CARTILLAS_BC_DETALLE_COLUMN_itemB).substring(articulo.getCodigo().length() - 3, 3));
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
        adapter = new SimpleAdapter(
                getApplicationContext(), listaArticulos,
                R.layout.pedidos_list_item, new
                String[]{"Cod", "Cantidad", "Precio", "Descripcion", "PorDescuento", "Descuento", "SubTotal", "Iva", "Total"}, new
                int[]{R.id.lblDetalleCodProducto, R.id.lblDetalleCantidad, R.id.lblDetallePrecio, R.id.lblDetalleDescripcion, R.id.lblDetallePorDescuento, R.id.lblDetalleDescuento, R.id.lblDetalleSubTotal, R.id.lblDetalleIva, R.id.lblDetalleTotal}) {

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

        CalcularTotales();

        txtCodigoArticulo.setText("");
        lblDescripcionArticulo.setText("");
        txtCantidad.setText("");
        txtDescuento.setText("");
        lblFooter.setText("Total items:" + String.valueOf(listaArticulos.size()));
        txtCodigoArticulo.requestFocus();
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
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedido/";
            String urlString = url + jsonPedido;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCallPost(encodeUrl);


            /**********************************Actualizamos los datos del pedido**************************************/
            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    NoPedido = (String) result.get("SincronizarPedidoResult");
                    if (NoPedido.equals("false")) {
                        guardadoOK = false;
                        return null;
                    }
                    PedidoH.ActualizarPedido(IdPedido, NoPedido);
                    PedidoDetalleH.ActualizarCodigoPedido(IdPedido, NoPedido);

                    Gson gson = new Gson();
                    List<HashMap<String, String>> pedidoDetalle = PedidoDetalleH.ObtenerPedidoDetalle(NoPedido);
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
                    //    jsonPedidoDetalle = URLEncoder.encode(jsonPedidoDetalle,"UTF-8");
                    final String urlDetalle = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedidoDetalle/";
                    String urlStringDetalle = urlDetalle + cliente.getCodigoLetra() + "/" + vendedor.getCODIGO() + "/" + jsonPedidoDetalle;

                    try {
                        URL Url = new URL(urlStringDetalle);
                        URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                        encodeUrl = uri.toURL().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String jsonStrDetalle = sh.makeServiceCallPost(encodeUrl);
                    if (jsonStrDetalle == null) {
                        MensajeAviso("Ha ocurrido un error al sincronizar el detalle del pedido");
                    } else {
                        result = new JSONObject(jsonStrDetalle);
                        // Getting JSON Array node
                        guardadoOK = ((String) result.get("SincronizarPedidoDetalleResult")).equalsIgnoreCase("true");
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
            }
          /*  else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "No se ha podido establecer contacto con el servidor, su pedido se ha guardado en la base de datos local",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }
}

