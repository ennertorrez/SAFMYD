package com.suplidora.sistemas.sisago.Informes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.FacturasPendientesHelper;
import com.suplidora.sistemas.sisago.Auxiliar.SpinnerDialog;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesHelper;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Bancos;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.Departamentos;
import com.suplidora.sistemas.sisago.Entidades.Informe;
import com.suplidora.sistemas.sisago.Entidades.InformeDetalle;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URL;
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
import java.util.regex.Pattern;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;

/**
 * Created by Sistemas on 19/3/2018.
 */

public class AgregarRecibo extends Activity {

    private TextView lblNoInforme;
    private TextView lblTc;
    private TextView lblNoRecibo;
    private TextView txtFechaRecibo;
    private String fechaRecibo = "";
    private String fechaDoc = "";
    private TextView lblIdCliente;
    private TextView lblDescFormapago;
    private EditText txtNombreCliente;
    private Button btnBuscarCliente;
    private TextView lblSearch;
    private TextView lblSaldo;
    private EditText txtMonto;
    private RadioButton rbEfectivo;
    private RadioButton rbCheque;
    private RadioButton rbDeposito;
    private RadioGroup rgFormaPago;
    private EditText txtValorMinuta;
    private TextView txtFechaDocPago;
    private Spinner cboBancoOrigen;
    private Spinner cboBancoDestino;
    private Button btnAgregar;
    private Button btnOKCliente;
    private EditText txtObservacion;
    private ListView lv;
    public static ArrayList<HashMap<String, String>> listaClientesItem;
    private ListView lvItem;
    private TextView lblTotalDol;
    private TextView lblTotalCor;
    private TextView lblFooter;
    private TextView lblFooterItem;
    private Button btnGuardar;
    private Button btnCancelar;
    private String busqueda = "1";
    private String tipoBusqueda = "2";
    private ClientesHelper ClientesH;
    private InformesHelper InformesH;
    public static ArrayList<HashMap<String, String>> listaRecibos;
    private double total;
    private double tasaCambio = 0;
    private String vTipo;
    private SimpleAdapter adapter;
    private Informe informe;
    private InformeDetalle informedetalle;
    String IMEI = "";
    private boolean guardadoOK = false;
    private InformesDetalleHelper InformesDetalleH;
    private FacturasPendientesHelper FacturasPendientesH;
    private DataBaseOpenHelper DbOpenHelper;
    private boolean finalizar = false;
    private Cliente cliente;
    private boolean isOnline = false;
    private String TAG = AgregarRecibo.class.getSimpleName();
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    private SincronizarDatos sd;
    java.util.ArrayList<String> CcFactura;
    SpinnerDialog spinnerDialog;
    List<HashMap<String, String>> ObtieneCFactura = null;
    public double saldoFactura;
    private DecimalFormat df;
    public Calendar myCalendar = Calendar.getInstance();
    private String vNoInforme;
    private String vVendedor;
    private String nVendedor;
    private String vCodLetraCliente="";
    private String vFacturatemp;
    private String vRecibo;
    private String vIdSerie;

    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private ProgressDialog pDialog;
    AlertDialog alertDialog;

    public final static String TEXTO_CAPTURADO = "";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibos);

        //Inicializando las clases de Entidades
        informe = new Informe();
        informedetalle = new InformeDetalle();
        cliente = new Cliente();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);

        //inicializando componentes de Formulario
        listaRecibos = new ArrayList<HashMap<String, String>>();
        listaRecibos.clear();
        DbOpenHelper = new DataBaseOpenHelper(AgregarRecibo.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        InformesH = new InformesHelper(DbOpenHelper.database);
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);

        lblNoInforme = (TextView) findViewById(R.id.lblNoInforme);
        lblTc = (TextView) findViewById(R.id.lblTC);
        lblNoRecibo = (TextView) findViewById(R.id.lblNoRecibo);
        txtFechaRecibo = (TextView) findViewById(R.id.txtFechaRecibo);
        lblIdCliente = (TextView) findViewById(R.id.lblIdCliente);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreCliente);
        btnBuscarCliente = (Button) findViewById(R.id.btnBuscarCliente);
        lblSearch = (TextView) findViewById(R.id.lblSearch);
        lblSaldo = (TextView) findViewById(R.id.txtSaldo);
        txtValorMinuta = (EditText) findViewById(R.id.txtValorMinuta);
        txtMonto = (EditText) findViewById(R.id.txtMonto);
        rgFormaPago = (RadioGroup) findViewById(R.id.rgFormaPago);
        txtFechaDocPago = (TextView) findViewById(R.id.txtFecha);
        cboBancoOrigen = (Spinner) findViewById(R.id.cboBancoEmisor);
        cboBancoDestino = (Spinner) findViewById(R.id.cboBancoDepositado);
        btnAgregar = (Button) findViewById(R.id.btnAgregar);
        txtObservacion =(EditText) findViewById(R.id.txtObservacion);
        lv = (ListView) findViewById(R.id.listFacurasRecibos);
        lblTotalCor = (TextView) findViewById(R.id.lblTotalCor);
        lblTotalDol = (TextView) findViewById(R.id.lblTotalDol);
        lblFooter = (TextView) findViewById(R.id.lblFooter);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        rgFormaPago = (RadioGroup) findViewById(R.id.rgFormaPago);
        rbEfectivo = (RadioButton) findViewById(R.id.rbEfectivo);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);
        rbDeposito = (RadioButton) findViewById(R.id.rbDeposito);
        lblDescFormapago = (TextView) findViewById(R.id.lblDescFormapago);

        //definición de valores iniciales para componentes del Formulario
        vTipo="Efectivo";
        cboBancoOrigen.setEnabled(false);
        cboBancoOrigen.setSelection(getIndex(cboBancoOrigen, "SELECCIONE"));

        txtFechaRecibo.setText(getDatePhone());
        fechaRecibo = txtFechaRecibo.getText().toString();

        txtFechaDocPago.setText(getDatePhone());
        fechaDoc = txtFechaDocPago.getText().toString();

         //Obteniendo el valor de la Tasa de cambio.
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));

        //Inicializando los valores de los Helper
        sd = new SincronizarDatos(DbOpenHelper,InformesH,InformesDetalleH,ClientesH,FacturasPendientesH);

        CargarCombos();

        rgFormaPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(group.getCheckedRadioButtonId()== R.id.rbEfectivo){
                    lblDescFormapago.setText("No. Minuta");
                    cboBancoOrigen.setEnabled(false);
                    vTipo="Efectivo";
                    cboBancoDestino.setSelection(1);
                    txtValorMinuta.requestFocus();
                }
                else if (group.getCheckedRadioButtonId()== R.id.rbCheque){
                    lblDescFormapago.setText("No. Cheque");
                    cboBancoOrigen.setEnabled(true);
                    vTipo="Cheque";
                    txtValorMinuta.requestFocus();
                }else {
                    lblDescFormapago.setText("No. Deposito");
                    cboBancoOrigen.setEnabled(false);
                    vTipo="Deposito";
                    txtValorMinuta.requestFocus();
                }
            }

        });
        rbEfectivo.setChecked(true);

        Intent in = getIntent();

        //Obteniendo el No de Informe del Formulario de Informes. si mismo el ID y Nombre del venedor
        vNoInforme=in.getStringExtra(variables_publicas.INFORMES_COLUMN_CodInforme).toString();
        lblNoInforme.setText("No. Informe: "+ vNoInforme);
        vVendedor = in.getStringExtra(variables_publicas.KEY_IdVendedor);
        nVendedor =in.getStringExtra(variables_publicas.KEY_NombreVendedor);
        vRecibo= String.valueOf( Integer.parseInt(in.getStringExtra(variables_publicas.KEY_ultRecibo))+1);
        vIdSerie= in.getStringExtra(variables_publicas.KEY_idSerie);

        //Obteniendo el Id del Recibo
        lblNoRecibo.setText(vRecibo);

        //obtenerIdRecibo();

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
                if(listaRecibos.size()>0){
                    MensajeAviso("Ya tiene un cliente asociado. Cada recibo es para Un cliente.");
                    lblSearch.requestFocus();
                }else {
                    BuscarCliente();
                    btnOKCliente.performClick();
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
                if(s.length() != 0){
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
                spinnerDialog.showSpinerDialog();
            }
        });

        //Evento para validar el monto que se está pagando
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
                double vsaldo = 0;
                if (lblSaldo.getText().toString().replace(",", "").isEmpty() ||lblSaldo.getText().toString().replace(",", "").equals("") ){
                    vsaldo=0;
                }else {
                    vsaldo=Double.parseDouble(lblSaldo.getText().toString().replace(",", ""));
                }
                if (!s.toString().isEmpty()){
                    vmonto = Double.parseDouble(s.toString());
                }else {
                    vmonto=0;
                }
                if (vmonto> vsaldo){
                    MensajeAviso("El Abono debe ser menor o Igual al saldo de la Factura.");
                    txtMonto.setText("0.00");
                    return;
                }
            }
        });

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

        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }
        };

        txtFechaRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AgregarRecibo.this, date1, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)AgregarRecibo.this.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(AgregarRecibo.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
        txtFechaDocPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AgregarRecibo.this, date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        AgregarRecibo.this.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(AgregarRecibo.this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        //Boton de cancelar y salir
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarRecibo.this.onBackPressed();
            }
        });
        //Botón para agregar el detalle del recibo.
        btnAgregar.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {

                                              try {

                                                  if (Double.parseDouble(txtMonto.getText().toString().equals("") ? "0" :txtMonto.getText().toString()) < 1) {
                                                      MensajeAviso("Ingrese un Monto mayor a 0");
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

                                                  if (txtValorMinuta.getText().toString().equals("") || txtValorMinuta.getText().toString().isEmpty() || txtValorMinuta.getText().toString().equals("0")) {
                                                      MensajeAviso("Debe indicar el número de documento.");
                                                      return;
                                                  }
                                                  String vBancoI = cboBancoOrigen.getSelectedItem().toString();
                                                  String vBancoF = cboBancoDestino.getSelectedItem().toString();


                                                  if (rbEfectivo.isChecked() && vBancoF.equals("SELECCIONE")){
                                                      MensajeAviso("Debe seleccionar un Banco Destino");
                                                      cboBancoDestino.requestFocus();
                                                      return ;
                                                  }

                                                  if (rbCheque.isChecked() && (vBancoI.equals("SELECCIONE") || vBancoF.equals("SELECCIONE"))){
                                                      MensajeAviso("Debe seleccionar un Banco Emisor y Destino");
                                                      cboBancoOrigen.requestFocus();
                                                      return ;
                                                  }
                                                  if (rbDeposito.isChecked() && vBancoF.equals("SELECCIONE")){
                                                      MensajeAviso("Debe seleccionar un Banco Destino");
                                                      cboBancoDestino.requestFocus();
                                                      return ;
                                                  }
                                                  vFacturatemp= lblSearch.getText().toString();
                                                  HashMap<String, String> itemRecibos = new HashMap<>();
                                                  if (AgregarDetalle(itemRecibos)) {
                                                      CalcularTotales();
                                                      FacturasPendientesH.ActualizarFacturasPendientes2(vFacturatemp,"true");
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
                    DbOpenHelper.database.endTransaction();
                    MensajeAviso(e.getMessage());
                }
            }
        });
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
                        R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente, "CodCv2", "NombreCompleto", variables_publicas.CLIENTES_COLUMN_Direccion}, new int[]{R.id.IdCliente, R.id.CodCv, R.id.Nombre,
                        R.id.Direccion});

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
                String CodCV = ((TextView) view.findViewById(R.id.CodCv)).getText().toString().replace("Cod_Cv: ", "");
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                if (CodCV.equals("")){
                    NombreCliente=Nombre;
                }else {
                    NombreCliente = Nombre.substring(Nombre.indexOf(patron) + patron.length());
                }
                String codCliente="";
                if (CodCV.equals("")){
                    codCliente=IdCliente;
                }else {
                    codCliente=CodCV;
                }

                vCodLetraCliente=ClientesH.ObtenerCodLetra(codCliente);

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

        String mensaje = "";
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
            pDialog = new ProgressDialog(AgregarRecibo.this);
            pDialog.setMessage("Guardando datos, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            DbOpenHelper.database.beginTransaction();
            if (GuardarDetalleInforme()) {
                guardadoOK=true;
                DbOpenHelper.database.setTransactionSuccessful();
                DbOpenHelper.database.endTransaction();
            } else {
                guardadoOK=false;
                DbOpenHelper.database.endTransaction();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (AgregarRecibo.this.isFinishing() == false) {
                MostrarMensajeGuardar();
            }

        }
    }

    //Funcion para guardar localmente el recibo en el detalle de informe.
    private boolean GuardarDetalleInforme() {
        double  montoTotal;
        String valorLetra="";

        valorLetra=Funciones.Convertir(lblTotalCor.getText().toString().replace(",", ""),true);

        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaActual = new Date();
        Date fechaDocumento = new Date();

        try {
            fechaActual = dateFormat.parse(variables_publicas.FechaActual);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        montoTotal = Double.parseDouble(lblTotalCor.getText().toString().replace(",", ""));

        IMEI = variables_publicas.IMEI;
        informe.setCodigoInforme(lblNoInforme.getText().toString().replace("No. Informe: ",""));
        informedetalle.setCodInforme(lblNoInforme.getText().toString().replace("No. Informe: ",""));
        informedetalle.setRecibo(lblNoRecibo.getText().toString());
        informedetalle.setIdvendedor(vVendedor);
        informedetalle.setIdCliente(lblIdCliente.getText().toString());
        informedetalle.setMonto(String.valueOf(montoTotal));
        informedetalle.setMoneda("01");

        if (IMEI == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("Es necesario configurar el permiso \"Administrar llamadas telefonicas\" para porder guardar un Recibo, Desea continuar ? ")
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

        informedetalle.setVendedor(nVendedor);
        informedetalle.setCliente(txtNombreCliente.getText().toString());
        informedetalle.setCodigoLetra(vCodLetraCliente);
        informedetalle.setCantLetra(valorLetra);

        InformesDetalleH.EliminarDetalleInforme2(informedetalle.getCodInforme(),informedetalle.getRecibo());

        boolean saved;
        String tipo;

        //Guardamos el detalle del Informe
        for (HashMap<String, String> item : listaRecibos) {
            informedetalle.setFactura(item.get("Factura"));
            informedetalle.setSaldo(item.get("Saldo").replace(",",""));
            informedetalle.setAbono(item.get("Abono").replace(",",""));
            informedetalle.setNoCheque(item.get("Documento"));
            tipo= item.get("Tipo");
            if (tipo.equals("Efectivo")){
                informedetalle.setBancoE("");
                informedetalle.setEfectivo("true");
            }else {
                informedetalle.setBancoE(item.get("BancoE"));
                informedetalle.setEfectivo("false");
            }
            informedetalle.setBancoR(item.get("BancoR"));
            informedetalle.setFechaCK(item.get("FechaDoc").replace("-",""));
            informedetalle.setFechaDep(item.get("FechaDoc").replace("-",""));
            try {
                fechaDocumento = dateFormat.parse(item.get("FechaDoc"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (fechaActual.before(fechaDocumento)){
                informedetalle.setPosfechado("true");
            }else {
                informedetalle.setPosfechado("false");
            }
            informedetalle.setAprobado("false");
            informedetalle.setProcesado("false");
            informedetalle.setUsuario("SISAGO");
            informedetalle.setObservacion(txtObservacion.getText().toString());
            if (Double.parseDouble(item.get("Saldo").replace(",",""))==0){
                informedetalle.setConcepto("CANCELA Y ABONA A FACTURA No: "+ item.get("Factura"));
            }else {
                informedetalle.setConcepto("ABONO A FACTURA No: "+ item.get("Factura"));
            }

            saved=InformesDetalleH.GuardarDetalleInforme(informedetalle.getCodInforme(),informedetalle.getRecibo(),informedetalle.getIdvendedor(),informedetalle.getIdCliente(),informedetalle.getFactura(),informedetalle.getSaldo(),
                    informedetalle.getMonto(),informedetalle.getAbono(),informedetalle.getNoCheque(),informedetalle.getBancoE(),informedetalle.getBancoR(),informedetalle.getFechaCK(),informedetalle.getFechaDep(),informedetalle.getEfectivo(),
                    informedetalle.getMoneda(),informedetalle.getAprobado(),informedetalle.getPosfechado(),informedetalle.getProcesado(),informedetalle.getUsuario(),informedetalle.getVendedor(),informedetalle.getCliente(),
                    informedetalle.getCodigoLetra(),informedetalle.getCantLetra(),informedetalle.getObservacion(),informedetalle.getConcepto());
            FacturasPendientesH.ActualizarFacturasPendientes(informedetalle.getFactura(),"true", Double.parseDouble(informedetalle.getSaldo()),Double.parseDouble(informedetalle.getMonto()));

            if (!saved) {
                break;
            }
        }
        InformesDetalleH.ActualizarCodigoRecibo(vIdSerie,informedetalle.getRecibo(),vVendedor);
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
            new AlertDialog.Builder(AgregarRecibo.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(AgregarRecibo.this,
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

    //Funcion para Obtener el id del recibo.
    private void obtenerIdRecibo() {

        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerConsecutivoRecibo/";
        urlString = urlString  + vVendedor;
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            String jsonStr = sh.makeServiceCall(encodeUrl);
            if (jsonStr == null) {
                new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id del Recibo, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultState = (String) ((String) jsonObj.get("ObtenerConsecutivoReciboResult")).split(",")[0];
                String NoRecibo = (String) ((String) jsonObj.get("ObtenerConsecutivoReciboResult")).split(",")[1];
                if (resultState.equals("true")) {
                    lblNoRecibo.setText(NoRecibo);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id del Recibo, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    private void CargarCombos(){
        //Combo Departamentos
        final List<Bancos> CBancoO;
        CBancoO = InformesDetalleH.ObtenerListaBancos();
        ArrayAdapter<Bancos> adapterBancoO = new ArrayAdapter<Bancos>(this, android.R.layout.simple_spinner_item, CBancoO);
        adapterBancoO.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboBancoOrigen.setAdapter(adapterBancoO);

        final List<Bancos> CBancoD;
        CBancoD = InformesDetalleH.ObtenerListaBancos();
        ArrayAdapter<Bancos> adapterBancoD = new ArrayAdapter<Bancos>(this, android.R.layout.simple_spinner_item, CBancoD);
        adapterBancoD.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboBancoDestino.setAdapter(adapterBancoD);
    }
    private void CargarFacturasPendientes() {
        if (!lblIdCliente.equals("")) {
            CcFactura = FacturasPendientesH.ObtenerFacturasPendientesArrayList(variables_publicas.usuario.getCodigo(),lblIdCliente.getText().toString().replace("00000","0"));
        } else {
            CcFactura = new java.util.ArrayList<String>();
        }

        spinnerDialog = new SpinnerDialog(AgregarRecibo.this, CcFactura, "Seleccione o busque la factura", R.style.DialogAnimations_SmileWindow);

        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //Toast.makeText(AgregarRecibos.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                ObtieneCFactura = FacturasPendientesH.ObtenerDatosFacturaPendiente(item);
                lblSearch.setText(item);
                saldoFactura=FacturasPendientesH.BuscarSaldoFactura(item);
                lblSaldo.setText(df.format(saldoFactura));
                txtMonto.requestFocus();
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
                                    "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es")
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(AgregarRecibo.this);
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
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaRecibo.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabel2() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaDocPago.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el Recibo actual?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        for (int i = 0; i < listaRecibos.size(); i++) {
                            HashMap<String, String> a = listaRecibos.get(i);
                            FacturasPendientesH.ActualizarFacturasPendientes2(a.get(variables_publicas.DETALLEINFORMES_COLUMN_Factura),"false");
                        }
                        Intent intentRetornoDatos = new Intent();
                        intentRetornoDatos.putExtra(TEXTO_CAPTURADO, lblNoInforme.getText().toString().replace("No. Informe: ",""));
                        setResult(Activity.RESULT_OK, intentRetornoDatos);
                        AgregarRecibo.this.finish();
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

        if (tasaCambio > 0) {
            lblTotalDol.setText(String.valueOf(df.format(total / tasaCambio)));
        }
        lblFooter.setText("Total items:" + String.valueOf(listaRecibos.size()));

    }
    private boolean AgregarDetalle(HashMap<String, String> itemRecibos) {

        double nuevosaldo, saldo,monto;

        saldo = Double.parseDouble(lblSaldo.getText().toString().replace(",", ""));
        monto = Double.parseDouble(txtMonto.getText().toString().replace(",", ""));
        nuevosaldo = saldo - monto;

        itemRecibos.put("Factura", lblSearch.getText().toString());
        itemRecibos.put("Fecha", txtFechaRecibo.getText().toString());
        itemRecibos.put("Saldo", String.valueOf(df.format(nuevosaldo)));
        itemRecibos.put("Abono", String.valueOf(df.format(monto)));
        itemRecibos.put("Tipo", vTipo);
        itemRecibos.put("Documento", txtValorMinuta.getText().toString());
        itemRecibos.put("FechaDoc", txtFechaDocPago.getText().toString());
        itemRecibos.put("BancoE", cboBancoOrigen.getSelectedItem().toString());
        itemRecibos.put("BancoR", cboBancoDestino.getSelectedItem().toString());
        itemRecibos.put("Observacion", txtObservacion.getText().toString());
        listaRecibos.add(itemRecibos);
        RefrescarGrid();
        CalcularTotales();
        return true;

    }
    private void RefrescarGrid() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaRecibos,
                R.layout.recibos_list_item, new
                String[]{"Factura", "Fecha", "Saldo", "Abono", "Tipo", "Documento", "FechaDoc", "BancoE", "BancoR", "Observacion"}, new
                int[]{R.id.lblDetalleFactura, R.id.lblDetalleFecha, R.id.lblDetalleSaldo, R.id.lblDetalleAbono, R.id.lblDetalleTipo, R.id.lblDetalleDocumento, R.id.lblDetalleFechaDoc, R.id.lblDetalleBancoE, R.id.lblDetalleBancoD, R.id.lblDetalleObservacion}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                return currView;
            }
        };

        lv.setAdapter(adapter);
    }
    private void LimipiarDatos() {
            //lblIdCliente.setText("00000");
            //txtNombreCliente.setText(null);
            lblSearch.setText(null);
            lblSaldo.setText("0.00");
            txtMonto.setText("0.00");
            rbEfectivo.setChecked(true);
            lblDescFormapago.setText("No. Documento");
            txtValorMinuta.setText("0");
            txtFechaDocPago.setText(getDatePhone());
            cboBancoOrigen.setSelection(getIndex(cboBancoOrigen, "SELECCIONE"));
            cboBancoDestino.setSelection(getIndex(cboBancoDestino, "SELECCIONE"));
            txtObservacion.setText("");
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
        View dialogView = null;
        dialogBuilder.setCancelable(false);
        if (guardadoOK) {
            dialogView = inflater.inflate(R.layout.dialog_ok_layout, null);

            Button btnOK = (Button) dialogView.findViewById(R.id.btnOkDialogo);
            btnOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRetornoDatos = new Intent();
                    intentRetornoDatos.putExtra(TEXTO_CAPTURADO, lblNoInforme.getText().toString().replace("No. Informe: ",""));
                    setResult(Activity.RESULT_OK, intentRetornoDatos);
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
                    FacturasPendientesH.ActualizarFacturasPendientes2(itemRecibo.get(variables_publicas.DETALLEINFORMES_COLUMN_Factura),"false");
                    for (int i = 0; i < listaRecibos.size() - 1; i++) {
                        HashMap<String, String> a = listaRecibos.get(i);
                        if (a.get(variables_publicas.DETALLEINFORMES_COLUMN_Factura).equals(itemRecibo.get(variables_publicas.DETALLEINFORMES_COLUMN_Factura))) {
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
}
