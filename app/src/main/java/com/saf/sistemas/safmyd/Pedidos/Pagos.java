package com.saf.sistemas.safmyd.Pedidos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdView;
import com.saf.sistemas.safmyd.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasPendientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.RecibosHelper;
import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safmyd.Auxiliar.SpinnerDialog;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Bancos;
import com.saf.sistemas.safmyd.Entidades.Cliente;
import com.saf.sistemas.safmyd.Entidades.Recibos;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

public class Pagos extends Activity {
    private TextView lblTc;
    private TextView lblTitulo;
    private TextView lblNoRecibo;
    private TextView txtFechaRecibo;
    private TextView lblIdCliente;
    private TextView lblDescFormapago;
    private EditText txtNombreCliente;
    private Button btnBuscarCliente;
    private TextView lblSearch;
    private TextView lblSaldo;
    private EditText txtMonto;
    private EditText txtCobradoTotal;
    private RadioButton rbEfectivo;
    private RadioButton rbCheque;
    private RadioButton rbDeposito;
    private RadioGroup rgFormaPago;
    private EditText txtValorMinuta;
    private Spinner cboBancoOrigen;
    private Button btnAgregar;
    private Button btnTodo;
    private Button btnOKCliente;
    private ListView lv;
    public static ArrayList<HashMap<String, String>> listaClientesItem;
    private ListView lvItem;
    private TextView lblTotalCor;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private Button btnGuardar;
    private Button btnCancelar;
    private String busqueda = "1";
    private String tipoBusqueda = "2";
    private ClientesHelper ClientesH;
    private RecibosHelper RecibosH;
    public static ArrayList<HashMap<String, String>> listaRecibos;
    private double total;
    private double tasaCambio = 0;
    private String vTipo;
    private SimpleAdapter adapter;
    private Recibos Recibo;
    private String Longitud="";
    private String Latitud="";
    private String DireccionGeo="";
    private String LongitudGuardada="";
    private String LatitudGuardada="";
    private String DireccionGeoGuardada="";
    String IMEI = "";
    private boolean guardadoOK = false;
    private FacturasPendientesHelper FacturasPendientesH;
    private DataBaseOpenHelper DbOpenHelper;
    private boolean finalizar = false;
    private Cliente cliente;
    private boolean isOnline = false;
    private String TAG = Pagos.class.getSimpleName();
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    private SincronizarDatos sd;
    java.util.ArrayList<String> CcFactura;
    SpinnerDialog spinnerDialog;
    List<HashMap<String, String>> ObtieneCFactura = null;
    public double saldoFactura;
    private DecimalFormat df;
    public Calendar myCalendar = Calendar.getInstance();
    private String vVendedor;
    private String vFacturatemp;
    private String vRecibo;
    private String vSerie;
    private double vTotalRecibo = 0;

    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;

    public final static String TEXTO_CAPTURADO = "";

    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    AdView viewAdmob;

    byte FONT_TYPE;
    public static ArrayList<HashMap<String, String>> lista;

    private String jsonRecibo = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibos);

        //Inicializando las clases de Entidades
        Recibo = new Recibos();
        cliente = new Cliente();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location location = null;
            LocationListener mlocListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            if (locationManager != null) {
                //Existe GPS_PROVIDER obtiene ubicación
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if(location == null){ //Trata con NETWORK_PROVIDER
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                if (locationManager != null) {
                    //Existe NETWORK_PROVIDER obtiene ubicación
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            }
            if(location != null) {
                Latitud = String.valueOf(location.getLatitude());
                Longitud = String.valueOf(location.getLongitude());
                Address address = null;
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                String errorMessage;
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocation(
                            location.getLatitude(),
                            location.getLongitude(),
                            // In this sample, get just a single address.
                            1);
                } catch (IOException ioException) {
                    // Catch network or other I/O problems.
                    errorMessage = "IOException>>" + ioException.getMessage();
                } catch (IllegalArgumentException illegalArgumentException) {
                    // Catch invalid latitude or longitude values.
                    errorMessage = "IllegalArgumentException>>" + illegalArgumentException.getMessage();
                }
                if (addresses != null && !addresses.isEmpty()) {
                    DireccionGeo = String.valueOf(addresses.get(0).getAddressLine(0));
                }
            }else {//Volvemos a preguntar por una segunda ocacion hasta encontrar la ultima ubicacion
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                if (locationManager != null) {
                    //Existe GPS_PROVIDER obtiene ubicación
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if(location == null){ //Trata con NETWORK_PROVIDER
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                    if (locationManager != null) {
                        //Existe NETWORK_PROVIDER obtiene ubicación
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                if(location != null) {
                    Latitud = String.valueOf(location.getLatitude());
                    Longitud = String.valueOf(location.getLongitude());
                    Address address = null;
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    String errorMessage;
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(
                                location.getLatitude(),
                                location.getLongitude(),
                                // In this sample, get just a single address.
                                1);
                    } catch (IOException ioException) {
                        // Catch network or other I/O problems.
                        errorMessage = "IOException>>" + ioException.getMessage();
                    } catch (IllegalArgumentException illegalArgumentException) {
                        // Catch invalid latitude or longitude values.
                        errorMessage = "IllegalArgumentException>>" + illegalArgumentException.getMessage();
                    }
                    if (addresses != null && !addresses.isEmpty()) {
                        DireccionGeo = String.valueOf(addresses.get(0).getAddressLine(0));
                    }
                }else {
                    Toast.makeText(this, "No se pudo obtener geolocalización", Toast.LENGTH_LONG).show();
                }
            }
        }

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);

        //inicializando componentes de Formulario
        listaRecibos = new ArrayList<>();
        listaRecibos.clear();
        DbOpenHelper = new DataBaseOpenHelper(Pagos.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        RecibosH = new RecibosHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);

        lblTitulo = (TextView) findViewById(R.id.lblTitulo);
        lblTc = (TextView) findViewById(R.id.lblTC);
        lblNoRecibo = (TextView) findViewById(R.id.lblNoRecibo);
        txtFechaRecibo = (TextView) findViewById(R.id.txtFechaRecibo);
        lblIdCliente = (TextView) findViewById(R.id.lblIdCliente);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreCliente);
        btnBuscarCliente = (Button) findViewById(R.id.btnBuscarCliente);
        lblSearch = (TextView) findViewById(R.id.lblSearch);
        lblSaldo = (TextView) findViewById(R.id.txtSaldo);
        txtValorMinuta = (EditText) findViewById(R.id.txtValorMinuta);
        txtCobradoTotal = (EditText) findViewById(R.id.txtValorCobradoTotal);
        cboBancoOrigen = (Spinner) findViewById(R.id.cboBanco);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        btnTodo = (Button) findViewById(R.id.btnTodo);
        lv = (ListView) findViewById(R.id.listFacurasRecibos);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        rgFormaPago = (RadioGroup) findViewById(R.id.rgFormaPago);
        rbEfectivo = (RadioButton) findViewById(R.id.rbEfectivo);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);
        rbDeposito = (RadioButton) findViewById(R.id.rbDeposito);
        lblDescFormapago = (TextView) findViewById(R.id.lblDescFormapago);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        //definición de valores iniciales para componentes del Formulario
        vTipo = "1";
        cboBancoOrigen.setEnabled(false);
        cboBancoOrigen.setSelection(getIndex(cboBancoOrigen, "SELECCIONE"));

        txtFechaRecibo.setText(getDatePhone());

        //Obteniendo el valor de la Tasa de cambio.
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));

        //Inicializando los valores de los Helper
        sd = new SincronizarDatos(DbOpenHelper, RecibosH, ClientesH, FacturasPendientesH);

        CargarCombos();

        cboBancoOrigen.setEnabled(true);
        vTipo = "Efectivo";
        cboBancoOrigen.setSelection(1);

        rgFormaPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (group.getCheckedRadioButtonId() == R.id.rbEfectivo) {
                    lblDescFormapago.setText("DOC");
                    vTipo = "1";
                } else if (group.getCheckedRadioButtonId() == R.id.rbCheque) {
                    lblDescFormapago.setText("CHK C$");
                    vTipo = "2";
                } else {
                    lblDescFormapago.setText("MIN C$");
                    vTipo = "3";
                }
            }

        });

        rbEfectivo.setChecked(true);
        vVendedor = variables_publicas.usuario.getCodigo();

        lista = new ArrayList<HashMap<String, String>>();
        vSerie = RecibosH.ObtenerSerieRuta(variables_publicas.rutacargada);
        lista = RecibosH.ObtenerUltimoCodigoRecibo(variables_publicas.rutacargada, vSerie);

        for (int i = 0; i < lista.size(); i++) {
            vRecibo = String.valueOf(Integer.parseInt(lista.get(i).get("ULTRECIBO")) + 1);
        }

        //Obteniendo el Id del Recibo
        String cadena = String.format("%05d", Integer.parseInt(vRecibo));
        lblNoRecibo.setText(vSerie + cadena);

        //Definición de la Lista de Recibos
        registerForContextMenu(lv);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollView1);
        //Eventos para a lista de Recibos
        lv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

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

        //Buscar Clientes
        btnBuscarCliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (listaRecibos.size() > 0) {
                    MensajeAviso("Ya tiene un cliente asociado. Cada recibo es para Un cliente.");
                    lblSearch.requestFocus();
                } else {
                    BuscarCliente();
                    btnOKCliente.performClick();
                }
            }
        });
        //Para agregar el monto total del saldo de la factura
        btnTodo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    if (Double.parseDouble(lblSaldo.getText().toString().replace(",", "")) > 0) {
                        txtCobradoTotal.setText(lblSaldo.getText());
                    } else {
                        return;
                    }
                } catch (Exception e) {
                    MensajeAviso(e.getMessage());
                }

            }
        });
        //Eventos para Lista de clientes
        lblIdCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    CargarFacturasPendientes();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Eventos para cargar la lista de Facturas
        lblSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //provideSimpleDialog();
                if (lblSearch.getText().equals("Seleccionar")) {
                    return;
                }
                spinnerDialog.showSpinerDialog();
            }
        });

        //Evento para validar el monto que se está pagando
        txtCobradoTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                double vmonto;
                double vsaldo;
                if (lblSaldo.getText().toString().replace(",", "").isEmpty() || lblSaldo.getText().toString().replace(",", "").equals("")) {
                    vsaldo = 0;
                } else {
                    vsaldo = Double.parseDouble(lblSaldo.getText().toString().replace(",", ""));
                }

                if (!s.toString().isEmpty()) {
                    if (s.toString().equalsIgnoreCase(".")) {
                        vmonto = 0;
                    } else {
                        vmonto = Double.parseDouble(s.toString().replace(",", ""));
                    }
                } else {
                    vmonto = 0;
                }
                if (vmonto > vsaldo) {
                    MensajeAviso("El Abono debe ser menor o Igual al saldo de la Factura.");
                    txtCobradoTotal.setText("0.00");
                    txtCobradoTotal.selectAll();
                }
            }
        });

       /* //Evento para validar el monto total con el monto dolares que se está pagando
        txtMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
            @Override
            public void afterTextChanged(Editable s) {
                double vmonto=0;
                double vmontodol=0;
                total = 0;
                for (int i = 0; i < listaRecibos.size(); i++) {
                    HashMap<String, String> item = listaRecibos.get(i);

                    try {
                        total += (df.parse(item.get("Abono"))).doubleValue();
                    } catch (ParseException e) {
                        MensajeAviso(e.getMessage());
                    }
                }

                if (!s.toString().isEmpty()){
                    if ( s.toString().replace(",", "").equals(".")){
                        vmonto=0;
                    }else {
                        vmonto = Double.parseDouble(s.toString().replace(",", ""));
                    }
                }else {
                    vmonto=0;
                }

                if (tasaCambio>0){
                    vmontodol=(total-vmonto)  /tasaCambio;
                }else{
                    vmontodol=(total-vmonto);
                }
                if (getCurrentFocus() == txtMonto) {

                    txtMontoD.setText(String.valueOf(df.format(vmontodol)));
                }
            }
        });*/

        //Eventos para definir y validar los valores de las fechas de Recibo y de Documentos
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        txtFechaRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Pagos.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager) Pagos.this.getSystemService(Context.INPUT_METHOD_SERVICE);

                View focusedView = Pagos.this.getCurrentFocus();
                if (focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

            }
        });

        //Boton de cancelar y salir
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pagos.this.onBackPressed();
            }
        });
        //Botón para agregar el detalle del recibo.
        btnAgregar.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {

                                              try {

                                                  if (Double.parseDouble(txtCobradoTotal.getText().toString().replace(",", "").equals("") ? "0" : txtCobradoTotal.getText().toString().replace(",", "")) < 0.01) {
                                                      MensajeAviso("Ingrese un Monto mayor a 0.");
                                                      return;
                                                  }
                                                  if (lblSearch.getText().toString().equals("") || lblSearch.getText().toString().isEmpty()) {
                                                      MensajeAviso("Debe seleccionar un número de factura.");
                                                      return;
                                                  }
                                                  boolean repetido = EsFacturaRepetida(lblSearch.getText().toString());
                                                  if (repetido) {
                                                      MensajeAviso("Esta Factura ya ha sigo agregada al Recibo.");
                                                      return;
                                                  }

                                                  vFacturatemp = lblSearch.getText().toString();
                                                  HashMap<String, String> itemRecibos = new HashMap<>();
                                                  if (AgregarDetalle(itemRecibos)) {
                                                      CalcularTotales();
                                                      FacturasPendientesH.ActualizarFacturasPendientes2(vFacturatemp, "true");
                                                      LimipiarDatos();
                                                      CargarFacturasPendientes();
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

        //Evento para guardar los datos del detalle de Informe
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    Guardar();

                } catch (Exception e) {

                    MensajeAviso(e.getMessage());
                }
            }
        });
    }

    protected void printRecibo(String tImpresion) {

        OutputStream opstream = null;
        try {
            opstream = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmOutputStream = opstream;

        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String vRpc = "";
            String vId = "";
            String vCliente = "";
            String vMonto = "";
            String vTipo = "";
            String vFecha = "";
            String vDoc = "";
            String vConcepto = "";
            String vDocumento = "";
            String vBanco = "";
            String vFacturas = "";
            String vVend = "";
            String vLetra;
            String vCantLetra;
            lista = new ArrayList<>();
            lista = RecibosH.ObtenerRecibo(vRecibo, vSerie);

            for (int i = 0; i < lista.size(); i++) {
                vRpc = lista.get(i).get("ReciboI");
                vId = lista.get(i).get("IdCliente");
                vCliente = lista.get(i).get("Cliente");
                vFecha = lista.get(i).get("Fecha");
                vDoc = lista.get(i).get("NoCheque");
                vConcepto = lista.get(i).get("Concepto");
            }
            lista = new ArrayList<>();
            lista = RecibosH.ObtenerReciboMontosRecibo(vRecibo, vSerie);

            for (int i = 0; i < lista.size(); i++) {
                vMonto = lista.get(i).get("Monto");
            }
            vLetra = Funciones.Convertir(vMonto, true);

            vCantLetra = vLetra;
            vDocumento = vDoc;

            double saldocliente=0;
            saldocliente=FacturasPendientesH.BuscarSaldoCliente(vId);

            //Formato inicial.
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
            Date d = formato.parse(vFecha);

            //Aplica formato requerido.
            formato.applyPattern("dd/MM/yyyy");
            String vFechaPago = formato.format(d);

            mmOutputStream = mmSocket.getOutputStream();

            //printNewLine();
            printNewLine();
            printNewLine();
            printNewLine();
            //print title
            printUnicode();
            //print normal text
            printPhoto(R.mipmap.logomydbmp);
            printCustom("DIST. M&D, S.A." , 6, 1);
            printNewLine();
            printCustom("Carretera a Veracruz. Plaza", 4, 1);
            printCustom("Montana. Managua, Nicaragua,", 4, 1);
            printCustom("Tel 5794-1666", 4, 1);
            printCustom("RUC: 4491912900001Q", 4, 1);
            printUnicode();
            printNewLine();
            printCustom("RECIBO NO. " + vRpc, 4, 1);
            printNewLine();
            //printUnicode();
            printCustom("CLIENTE: " + vId + "-"+Funciones.formateartexto(vCliente), 4, 0);
            printNewLine();
            printCustom("ABONO C$: " + df.format(Double.parseDouble(vMonto)), 4, 0);
            //printNewLine();
            //printCustom("Recibimos de: ", 4, 0);
            //printCustom(Funciones.formateartexto(vCliente), 4, 0);
            printNewLine();
            //printCustom("La Suma de: ", 4, 0);
            //printCustom(Funciones.formateartexto(vCantLetra), 4, 0);
            //printNewLine();
            printCustom("CONCEPTO: ", 4, 0);
            printCustom(vConcepto, 4, 0);
            //printNewLine();
            //printUnicode();
            //printCustom("Tipo Pago: " + Funciones.formateartexto(vTipo), 4, 0);
            //printCustom("Referencia: " + vDocumento, 4, 0);
            //printCustom("Banco:" + Funciones.formateartexto(vBanco), 4, 0);
            //printCustom("Fecha Pago: " + vFechaPago, 4, 0);
            //printUnicode();
            printNewLine();
            printCustom("SALDO C$: " + df.format(saldocliente), 4, 0);
            printNewLine();
            printCustom("Vendedor: " + Funciones.formateartexto(variables_publicas.usuario.getNombre()), 4, 0);
            printNewLine();
            String fecha[] = getDateTime();
            printCustom("Impreso: " + fecha[0] + " " + fecha[1], 4, 0);
            printUnicode();
            printNewLine();
            printCustom(">>>>  " + tImpresion + "  <<<<", 4, 1);
            printNewLine();
            printNewLine();
            printNewLine();
            printNewLine();
            mmOutputStream.flush();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //}
    }

    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B, 0x21, 0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B, 0x21, 0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B, 0x21, 0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B, 0x21, 0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B, 0x21, 0x10}; // 3- bold with large text
        try {
            switch (size) {
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(cc1);
                    break;
            }

            switch (align) {
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if (bmp != null) {
                byte[] command = Utils.decodeBitmap(bmp);
                byte[] bb3 = new byte[]{27, 97, 1};
                mmOutputStream.write(bb3);
                try {
                    // Print normal text
                    mmOutputStream.write(command);
                    printNewLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //printText(command);
            } else {
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode() {
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] getDateTime() {
        Calendar c = new GregorianCalendar();
        String dateTime[] = new String[2];
        dateTime[0] = c.get(Calendar.DATE) + "/" + String.valueOf(c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        return dateTime;
    }

    private String GetFechaISO() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyMMddHHmms");
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());
        return nowAsISO;
    }

    //Funcion para Buscar el cliente. se habilita la ventana emergente para la busqueda de los mismos.
    public void BuscarCliente() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = null;

        dialogView = inflater.inflate(R.layout.clientesrecibos_layout, null);
        btnOKCliente = (Button) dialogView.findViewById(R.id.btnBuscar);
        final RadioGroup rgGrupo = (RadioGroup) dialogView.findViewById(R.id.rgGrupo);
        rgGrupo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

            }
        });

        final EditText txtBusquedaItem = (EditText) dialogView.findViewById(R.id.txtBusqueda);
        lvItem = (ListView) dialogView.findViewById(R.id.list);
        lblFooterItem = (TextView) dialogView.findViewById(R.id.lblFooter);
        txtNombreCliente.setText("");
        lblIdCliente.setText("");
        lblSearch.setText("");
        lblSaldo.setText("0.00");
        txtBusquedaItem.setText(txtNombreCliente.getText());
        btnOKCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusquedaItem.getWindowToken(), 0);
                busqueda = txtBusquedaItem.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";
                try {
                    switch (tipoBusqueda) {
                        case "1":
                            listaClientesItem = ClientesH.BuscarClientesCodigo(busqueda);
                            break;
                        case "2":
                            listaClientesItem = ClientesH.BuscarClientesNombre(busqueda);
                            break;
                    }
                } catch (Exception ex) {
                    MensajeAviso(ex.getMessage());
                }
                if (listaClientesItem.size() == 0) {
                    MensajeAviso("El Cliente ingresado no existe en la base de datos o esta inactivo");
                }

                ListAdapter adapter = new SimpleAdapter(
                        getApplicationContext(), listaClientesItem,
                        R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente, "Nombre", variables_publicas.CLIENTES_COLUMN_Direccion, "Ciudad","Saldo"}, new int[]{R.id.IdCliente, R.id.Nombre,
                        R.id.Direccion, R.id.Ciudad,R.id.Saldo});

                lvItem.setAdapter(adapter);
                lblFooterItem.setText("Clientes Encontrados: " + String.valueOf(listaClientesItem.size()));

            }
        });
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txtNombreCliente.setText("");
                lblIdCliente.setText("00000");

                String NombreCliente;
                String patron = "/";
                String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                NombreCliente = Nombre;
                String codCliente;
                codCliente = IdCliente;
                lblIdCliente.setText(codCliente);
                txtNombreCliente.setText(NombreCliente);

                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    //Funcion para Guardar
    private boolean Guardar() {
        if (lv.getCount() <= 0) {
            MensajeAviso("No se puede guardar el Recibo, Debe ingresar al menos 1 item");
            return false;
        }
        double vCor = Double.parseDouble(txtMonto.getText().toString().replace(",", "").equals("") ? "0" : txtMonto.getText().toString().replace(",", ""));

        if ((vCor + 0.5) < Math.round(total * 100.0) / 100.0) {
            MensajeAviso("El monto cobrado es menor al total del monto pagado por cada factura. Favor revisar.");
            return false;
        }
        if (Double.parseDouble(txtMonto.getText().toString().replace(",", "").equals("") ? "0" : txtMonto.getText().toString().replace(",", "")) > 0) {
            if ((txtValorMinuta.getText().toString().equals("") || txtValorMinuta.getText().toString().isEmpty() || txtValorMinuta.getText().toString().equals("0")) && (rbCheque.isChecked() || rbDeposito.isChecked())) {
                MensajeAviso("Debe indicar el número de documento Córdobas.");
                return false;
            }
            if ((txtValorMinuta.getText().toString().equals("") || txtValorMinuta.getText().toString().isEmpty() || txtValorMinuta.getText().toString().equals("0")) && rbEfectivo.isChecked()) {
                txtValorMinuta.setText("0");
            }
        } else {
            txtMonto.setText("0");
            txtValorMinuta.setText("0");
        }

        String vBancoI = cboBancoOrigen.getSelectedItem().toString();


        if (rbEfectivo.isChecked() && vBancoI.equals("SELECCIONE")) {
            MensajeAviso("Debe seleccionar un Banco");
            cboBancoOrigen.requestFocus();
            return false;
        }

        if (rbCheque.isChecked() && (vBancoI.equals("SELECCIONE"))) {
            MensajeAviso("Debe seleccionar un Banco");
            cboBancoOrigen.requestFocus();
            return false;
        }
        if (rbDeposito.isChecked() && vBancoI.equals("SELECCIONE")) {
            MensajeAviso("Debe seleccionar un Banco");
            cboBancoOrigen.requestFocus();
            return false;
        }

        String mensaje;
        mensaje = "Esta seguro que desea guardar el recibo?";

        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Guardandorecibo();

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

    //Funcion para  llamar el evento asincrono de guardado localmente
    private boolean Guardandorecibo() {

        try {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new GuardandoDetalleinforme().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new GuardandoDetalleinforme().execute();
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
        }

        return false;
    }

    //Funcion asincrona para guardar el detalle
    private class GuardandoDetalleinforme extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Pagos.this);
            pDialog.setMessage("Guardando recibo oficial de caja, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            DbOpenHelper.database.beginTransactionNonExclusive();
            try {
                if (GuardarDetalleRecibo()) {
                    guardadoOK = true;
                    RecibosH.ActualizarCodigoRecibo(vSerie, Recibo.getRecibo(), variables_publicas.rutacargada);
                    findBT();
                    openBT();
                    printRecibo("Original");
                    RecibosH.ActualizarImpresionRecibo(vSerie, vRecibo, "1");
                    //printRecibo("Copia");
                    closeBT();

                    DbOpenHelper.database.setTransactionSuccessful();
                } else {
                    guardadoOK = false;
                }
            } catch (IOException ex) {
                guardadoOK = false;
                ex.printStackTrace();
            } finally {
                DbOpenHelper.database.endTransaction();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (Pagos.this.isFinishing() == false) {
                GuardarInforme();
                MostrarMensajeGuardar();
            }

        }
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    if (device.getName().equals(variables_publicas.vImpresoraBT)) {
                        mmDevice = device;
                        break;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // this will send text data to be printed by the bluetooth printer
    void sendData(String mensaje) throws IOException {
        try {

            // the text typed by the user
            String msg = mensaje;
            msg += "\n";

            mmOutputStream.write(msg.getBytes());

            // tell the user data were sent
            //myLabel.setText("Data sent.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // close the connection to bluetooth printer.
    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Funcion para guardar localmente el recibo en el detalle de informe.
    private boolean GuardarDetalleRecibo() {
        double  montoTotal;
        boolean saved;
        String tipo;

        Funciones.GetLocalDateTime();

        montoTotal = Double.parseDouble(txtMonto.getText().toString().replace(",", ""));

        Recibo.setRecibo(vRecibo);
        Recibo.setSerie(vSerie);
        Recibo.setMonto(String.valueOf(montoTotal));
        Recibo.setNoCheque(txtValorMinuta.getText().toString());
        Recibo.setBancoR(cboBancoOrigen.getSelectedItem().toString());
        Recibo.setMoneda("01");
        tipo= vTipo;
        Recibo.setTipoPago(tipo);
        Recibo.setIdVendedor(vVendedor);
        Recibo.setIdCliente(lblIdCliente.getText().toString());
        Recibo.setUsuario(variables_publicas.usuario.getUsuario());
        Recibo.setImpresion("0");
        Recibo.setLatitud(Latitud);
        Recibo.setLongitud(Longitud);
        Recibo.setDireccionGeo(DireccionGeo);

        String vConcepto="";
        String vConceptoCancela="";
        String vConceptoAbono="";
        //Guardamos el detalle del Informe
        for (HashMap<String, String> item : listaRecibos) {

            Recibo.setFactura(item.get("Factura"));
            Recibo.setSaldo(item.get("Saldo").replace(",",""));
            Recibo.setAbono(item.get("Abono").replace(",",""));
            Recibo.setFecha(txtFechaRecibo.getText().toString().replace("-",""));
            if (Double.parseDouble(item.get("Saldo").replace(",",""))==0){
                vConceptoCancela=vConceptoCancela + item.get("Factura") + ", ";
            }else {
                vConceptoAbono=vConceptoAbono + item.get("Factura") + ", ";
            }
            if (vConceptoCancela.length()>0){
                vConceptoCancela=vConceptoCancela.substring(0, vConceptoCancela.length()-1);
            }
            if(vConceptoAbono.length()>0){
                vConceptoAbono =vConceptoAbono.substring(0, vConceptoAbono.length()-1);
            }

            vConcepto=vConceptoCancela + vConceptoAbono;

            Recibo.setConcepto(vConcepto);

            saved=RecibosH.GuardarRecibo(Recibo.getSerie(),Recibo.getRecibo(),Recibo.getFactura(),Recibo.getFecha(),Recibo.getMonto(),Recibo.getNoCheque(),
                    Recibo.getBancoR(),Recibo.getAbono(),Recibo.getMoneda(),Recibo.getTipoPago(),Recibo.getConcepto(),Recibo.getIdVendedor(),Recibo.getIdCliente(),
                    Recibo.getSaldo(),Recibo.getUsuario(),Recibo.getImpresion(),"false",Recibo.getLatitud(),Recibo.getLongitud(),Recibo.getDireccionGeo());
            FacturasPendientesH.ActualizarFacturasPendientes(Recibo.getFactura(),"true", Double.parseDouble(Recibo.getSaldo()),Double.parseDouble(Recibo.getAbono()));

            if (!saved) {
                break;
            }
        }
        RecibosH.ActualizarCodigoRecibo(vSerie,Recibo.getRecibo(),variables_publicas.rutacargada);

        //Para el concepto de todo el recibo.
        vConceptoAbono=vConceptoAbono.replaceAll(",$", "");
        vConceptoCancela=vConceptoCancela.replaceAll(",$", "");

        if (vConceptoCancela.equals("") && !vConceptoAbono.equals("")){
            vConcepto= "ABONO A FACTURA(s): " + vConceptoAbono;
        }else if(!vConceptoCancela.equals("") && vConceptoAbono.equals("")){
            vConcepto= "CANCELACION DE FACTURA(s): " + vConceptoCancela;
        }else{
            vConcepto="CANCELACION DE FACTURA(s): " + vConceptoCancela + " Y ABONO A FACTURA(s): "+vConceptoAbono;
        }
        RecibosH.ActualizarConceptoRecibo(vSerie,Recibo.getRecibo(),vConcepto);
        return true;
    }


    private boolean GuardarInforme() {
        if (vRecibo==null || vRecibo.isEmpty()|| vRecibo.equalsIgnoreCase("")) {
            MensajeAviso("No se puede guardar el Recibo en Sistema, Debe ingresar al menos 1 recibo");
            return false;
        }

        SincronizarInforme();
        return true;
    }
    private boolean SincronizarInforme() {

        try {
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new SincronizardorInformes().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new SincronizardorInformes().execute();
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
        }

        return false;
    }
    private class SincronizardorInformes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            CheckConnectivity();
            if (isOnline) {
                if (Boolean.parseBoolean(SincronizarDatos.SincronizarRecibo(RecibosH,vRecibo,vSerie).split(",")[0])) {
                    guardadoOK = true;

                }else {
                    guardadoOK = false;
                }
            }else {
                guardadoOK = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if(guardadoOK) {
                SincronizarFacturasSaldos(variables_publicas.rutacargada, "0");
            }

        }
    }


    private boolean SincronizarFacturasSaldos(String vRuta, String vCliente)  {
        HttpHandler shC = new HttpHandler();
        String urlGetFacturasPendientes = variables_publicas.direccionIp + "/ServicioRecibos.svc/SpObtieneFacturasSaldoPendiente/";
        //String urlStringC = urlGetFacturasPendientes + vRuta + "/" + vCliente + "/" + variables_publicas.usuario.getEmpresa_ID();
        String urlStringC = urlGetFacturasPendientes + variables_publicas.usuario.getCodigo() + "/" + vCliente + "/" + variables_publicas.usuario.getEmpresa_ID();
        String encodeUrl = "";
        try {
            URL Url = new URL(urlStringC);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonStrC = shC.makeServiceCall(encodeUrl);

        if (jsonStrC != null) {

            try {
                //DbOpenHelper.database.beginTransactionNonExclusive();
                JSONObject jsonObj = new JSONObject(jsonStrC);
                // Getting JSON Array node
                JSONArray articulos = jsonObj.getJSONArray("SpObtieneFacturasSaldoPendienteResult");
                if (articulos.length() == 0) {
                    return false;
                }
                DbOpenHelper.database.beginTransactionNonExclusive();
                FacturasPendientesH.EliminaFacturasPendientes();
                // looping through All Contacts
                for (int i = 0; i < articulos.length(); i++) {
                    JSONObject c = articulos.getJSONObject(i);
                    String codvendedor = c.getString("codvendedor");
                    String No_Factura = c.getString("No_Factura");
                    String CodigoCliente = c.getString("CodigoCliente");
                    String Fecha = c.getString("Fecha");
                    String Total = c.getString("Total");
                    String Abono = c.getString("Abono");
                    String Saldo = c.getString("Saldo");
                    String Ruta = c.getString("Ruta");
                    String Guardada = c.getString("Guardada");
                    FacturasPendientesH.GuardarFacturasPendientes(codvendedor, Fecha, No_Factura, CodigoCliente,  Total, Abono, Saldo,Ruta, Guardada);
                }
                DbOpenHelper.database.setTransactionSuccessful();
                DbOpenHelper.database.endTransaction();
                DbOpenHelper.database.beginTransactionNonExclusive();
                FacturasPendientesH.ActualizarSaldoCliente(vCliente);
                DbOpenHelper.database.setTransactionSuccessful();
                DbOpenHelper.database.endTransaction();
                return true;
                // DbOpenHelper.database.setTransactionSuccessful();
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                new Funciones().SendMail("Ha ocurrido un error al obtener el listado de facturas pendientes. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
                return false;
            }

        } else {
            new Funciones().SendMail("Ha ocurrido un error al actualizar listado de facturas pendientes. Ha ocurrido un error al sincronizar las Facturas Pendientes,Respuesta nula", variables_publicas.info + urlStringC, "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
            return false;
        }
    }
    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(Pagos.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(Pagos.this,
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

    private void CargarCombos(){
        //Combo Departamentos
        final List<Bancos> CBancoO;
        CBancoO = RecibosH.ObtenerListaBancos();
        ArrayAdapter<Bancos> adapterBancoO = new ArrayAdapter<Bancos>(this, android.R.layout.simple_spinner_item, CBancoO);
        adapterBancoO.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboBancoOrigen.setAdapter(adapterBancoO);
    }
    private void CargarFacturasPendientes() {
        if (!lblIdCliente.getText().equals("")) {
            CcFactura = FacturasPendientesH.ObtenerFacturasPendientesArrayList(lblIdCliente.getText().toString().replace("00000","0"));
        } else {
            CcFactura = new java.util.ArrayList<String>();
        }

        spinnerDialog = new SpinnerDialog(Pagos.this, CcFactura, "Seleccione o busque la factura", R.style.DialogAnimations_SmileWindow);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //Toast.makeText(AgregarRecibos.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                ObtieneCFactura = FacturasPendientesH.ObtenerDatosFacturaPendiente(item);
                lblSearch.setText(item);
                saldoFactura=FacturasPendientesH.BuscarSaldoFactura(item);
                lblSaldo.setText(df.format(saldoFactura));
                txtCobradoTotal.requestFocus();
            }
        });

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
    private void ValidarUltimaVersion() {

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
    private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                CheckConnectivity();
                if (isOnline) {
                    //It retrieves the latest version by scraping the content of current version from play store at runtime
/*                    String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es";
                    Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                    latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();*/
                    Document doc2 = Jsoup
                            .connect(
                                    "https://play.google.com/store/apps/details?id=com.saf.sistemas.safmyd&hl=es")
                            .get()
                            ;

                    Elements Version = doc2.select(".htlgb ");

                    for (int i = 0; i < 7 ; i++) {
                        latestVersion = Version.get(i).text();
                        if (Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", latestVersion)) {
                            break;
                        }
                    }
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(Pagos.this);
                builder.setTitle("Nueva version disponible");
                builder.setMessage("Es necesario actualizar la aplicacion para poder continuar.");
                builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.saf.sistemas.safdiscomert&hl=es")));
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
    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
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
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
    private void updateLabel() {
        String myFormat = ("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaRecibo.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el registro de Recibo actual?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        for (int i = 0; i < listaRecibos.size(); i++) {
                            HashMap<String, String> a = listaRecibos.get(i);
                            FacturasPendientesH.ActualizarFacturasPendientes2(a.get(variables_publicas.RECIBOS_COLUMN_Factura),"false");
                        }
                        int valrecibo = Integer.parseInt(vRecibo);
                        if (valrecibo>0){
                            RecibosH.ActualizarCodigoRecibo(vSerie,String.valueOf(valrecibo-1),variables_publicas.rutacargada);
                        }
                        RecibosH.EliminarRecibo(vRecibo,vSerie);
                        Pagos.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    private boolean EsFacturaRepetida(String s) {

        for (HashMap<String, String> item : listaRecibos) {
            if (item.get("Factura").equals(s) ) {
                return true;
            }
        }
        return false;
    }
    private void CalcularTotales() {

        total = 0;
        for (int i = 0; i < listaRecibos.size(); i++) {
            HashMap<String, String> item = listaRecibos.get(i);

            try {
                total += (df.parse(item.get("Abono"))).doubleValue();
            } catch (ParseException e) {
                MensajeAviso(e.getMessage());
            }
        }
        lblTotalCor.setText(df.format(total));
        txtMonto.setText(df.format(total));
        lblFooter.setText("Total items:" + String.valueOf(listaRecibos.size()));

    }
    private boolean AgregarDetalle(HashMap<String, String> itemRecibos) {

        double nuevosaldo, saldo,monto;

        saldo = Double.parseDouble(lblSaldo.getText().toString().replace(",", ""));
        monto = Double.parseDouble(txtCobradoTotal.getText().toString().replace(",", ""));
        nuevosaldo = saldo - monto;

        itemRecibos.put("Factura", lblSearch.getText().toString());
        itemRecibos.put("Fecha", txtFechaRecibo.getText().toString());
        itemRecibos.put("Abono", df.format(monto));
        itemRecibos.put("Saldo", df.format(nuevosaldo));
        itemRecibos.put("Fecha", txtFechaRecibo.getText().toString());
        listaRecibos.add(itemRecibos);
        RefrescarGrid();
        CalcularTotales();
        return true;

    }
    private void RefrescarGrid() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaRecibos,
                R.layout.recibos_list_item, new
                String[]{"Factura", "Fecha",  "Abono", "Saldo"}, new
                int[]{R.id.lblDetalleFactura, R.id.lblDetalleFecha, R.id.lblDetalleAbono, R.id.lblDetalleSaldo}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                return currView;
            }
        };

        lv.setAdapter(adapter);
    }
    private void LimipiarDatos() {
        lblSearch.setText(null);
        lblSaldo.setText("0.00");
        txtCobradoTotal.setText("0.00");
        lblFooter.setText("Total items:" + String.valueOf(listaRecibos.size()));
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            String nn=spinner.getItemAtPosition(i).toString();

            if (nn.equals(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    public void MostrarMensajeGuardar() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView;
        dialogBuilder.setCancelable(false);
        if (guardadoOK) {
            dialogView = inflater.inflate(R.layout.dialog_ok_recibo_layout, null);
            String auxValorNuevo=vSerie + String.format("%05d" , Integer.parseInt(vRecibo));

            Button btnOK = (Button) dialogView.findViewById(R.id.btnOkDialogo);
            TextView nuevoValor = (TextView) dialogView.findViewById(R.id.nuevoIdRecibo);
            nuevoValor.setText(auxValorNuevo);
            nuevoValor.setTextColor(Color.parseColor("#FFBF5300"));

            btnOK.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = obj.get("Factura");
            //String HeaderMenu = obj.get("Factura") + "\n" + obj.get("Descripcion");
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
                    HashMap<String, String> itemRecibo = listaRecibos.get(info.position);
                    listaRecibos.remove(itemRecibo);
                    FacturasPendientesH.ActualizarFacturasPendientes2(itemRecibo.get(variables_publicas.RECIBOS_COLUMN_Factura),"false");
                    for (int i = 0; i < listaRecibos.size() - 1; i++) {
                        HashMap<String, String> a = listaRecibos.get(i);
                        if (a.get(variables_publicas.RECIBOS_COLUMN_Factura).equals(itemRecibo.get(variables_publicas.RECIBOS_COLUMN_Factura))) {
                            listaRecibos.remove(a);
                        }
                    }
                    adapter.notifyDataSetChanged();
                    lv.setAdapter(adapter);

                    RefrescarGrid();
                    CalcularTotales();
                    CargarFacturasPendientes();
                    LimipiarDatos();

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
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}
