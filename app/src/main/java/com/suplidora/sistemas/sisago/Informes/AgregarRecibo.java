package com.suplidora.sistemas.sisago.Informes;

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
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
    private TextView lblMontoLetras;
    private RadioButton rbEfectivo;
    private RadioButton rbCheque;
    private RadioButton rbDeposito;
    private RadioGroup rgFormaPago;
    private TextView lblDescTipoPago;
    private EditText txtValorDocPago;
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
    private int vEfectivo=0;
    private String vTipo;
    private SimpleAdapter adapter;

    private InformesDetalleHelper InformesDetalleH;
    private FacturasPendientesHelper FacturasPendientesH;
    private DataBaseOpenHelper DbOpenHelper;
    private boolean finalizar = false;
    private Cliente cliente;
    AlertDialog alertDialog;
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

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibos);

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
        lblMontoLetras = (TextView) findViewById(R.id.lblMontoLetras);
        rgFormaPago = (RadioGroup) findViewById(R.id.rgFormaPago);
        lblDescTipoPago = (TextView) findViewById(R.id.lblDescFormapago);
        txtValorDocPago =(EditText) findViewById(R.id.txtValorMinuta);
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

        rbEfectivo.setChecked(true);
        vTipo="Efectivo";
        cboBancoOrigen.setEnabled(false);
        cboBancoOrigen.setSelection(getIndex(cboBancoOrigen, "SELECCIONE"));

        txtFechaRecibo.setText(getDatePhone());
        fechaRecibo = txtFechaRecibo.getText().toString();

        txtFechaDocPago.setText(getDatePhone());
        fechaDoc = txtFechaDocPago.getText().toString();
        tasaCambio = Double.parseDouble(variables_publicas.usuario.getTasaCambio());
        lblTc.setText(df.format(Double.parseDouble(variables_publicas.usuario.getTasaCambio())));
        sd = new SincronizarDatos(DbOpenHelper,InformesH,InformesDetalleH,ClientesH,FacturasPendientesH);

        CargarCombos();

        rgFormaPago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(group.getCheckedRadioButtonId()== R.id.rbEfectivo){
                    lblDescFormapago.setText("No. Minuta");
                    cboBancoOrigen.setEnabled(false);
                    vEfectivo=1;
                    vTipo="Efectivo";
                    txtValorMinuta.requestFocus();
                   // lblDescFormapago.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else if (group.getCheckedRadioButtonId()== R.id.rbCheque){
                    lblDescFormapago.setText("No. Cheque");
                    cboBancoOrigen.setEnabled(true);
                    vEfectivo=0;
                    vTipo="Cheque";
                    txtValorMinuta.requestFocus();
                    //txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
                }else {
                    lblDescFormapago.setText("No. Deposito");
                    cboBancoOrigen.setEnabled(false);
                    vEfectivo=0;
                    vTipo="Deposito";
                    txtValorMinuta.requestFocus();
                }
            }

        });


        Intent in = getIntent();
        vNoInforme=in.getStringExtra(variables_publicas.INFORMES_COLUMN_CodInforme).toString();
        lblNoInforme.setText("No. Informe: "+ vNoInforme);
        vVendedor = in.getStringExtra(variables_publicas.CodigoVendedor).toString();
        obtenerIdRecibo();
        //Funciones funciones= new Funciones();
        btnBuscarCliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuscarCliente();
                btnOKCliente.performClick();
            }
        });

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

        lblSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //provideSimpleDialog();
                spinnerDialog.showSpinerDialog();
            }
        });


        txtMonto.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Funciones.Convertir(txtMonto.getText().toString(),true);
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    Funciones.Convertir(txtMonto.getText().toString(),true);
                    if (!txtMonto.equals("0.00")) {
                        lblMontoLetras.setText(Funciones.Convertir(txtMonto.getText().toString(),true));
                    }
                    return false;
                }
                return true;
            }
        });

        txtMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double vmonto=0;
                double vsaldo = Double.parseDouble(lblSaldo.getText().toString().replace(",", ""));
                if (!s.equals("")){
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarRecibo.this.onBackPressed();
            }
        });
        btnAgregar.setOnClickListener(new View.OnClickListener() {
                                          public void onClick(View v) {

                                              try {

                                                  if (Double.parseDouble(txtMonto.getText().toString()) < 1) {
                                                      txtMonto.setError("Ingrese un Monto mayor a 0");
                                                      return;
                                                  }

                                                  boolean repetido = EsFacturaRepetida(lblSearch.getText().toString());
                                                  if (repetido) {
                                                      MensajeAviso("Esta Factura ya ha sigo agregada al Recibo.");
                                                      return;
                                                  }

                                                  String vBancoI = cboBancoOrigen.getSelectedItem().toString();
                                                  String vBancoF = cboBancoDestino.getSelectedItem().toString();

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
                                                  HashMap<String, String> itemRecibos = new HashMap<>();
                                                  if (AgregarDetalle(itemRecibos)) {


                                                    /*  for (HashMap<String, String> item : listaRecibos) {
                                                          subTotalPrecioSuper += Double.parseDouble(item.get("SubTotal").replace(",", ""));
                                                      }*/
                                                      CalcularTotales();
                                                      LimipiarDatos();
                                                      InputMethodManager inputManager = (InputMethodManager)
                                                              getSystemService(Context.INPUT_METHOD_SERVICE);

                                                      inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                                              InputMethodManager.RESULT_SHOWN);
                                                  }


                                              } catch (Exception e) {
                                                  //cliente = ClientesH.BuscarCliente(pedido.getIdCliente(), pedido.getCod_cv());
                                                  MensajeAviso(e.getMessage());
                                              }
                                          }
                                      }
        );
    }
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
                NombreCliente= Nombre.substring(Nombre.indexOf(patron) + patron.length());

                String codCliente="";
                if (CodCV.equals("")){
                    codCliente=IdCliente;
                }else {
                    codCliente=CodCV;
                }
                lblIdCliente.setText(codCliente);
                txtNombreCliente.setText(NombreCliente);

                alertDialog.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);

        alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
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
            lblIdCliente.setText("00000");
            txtNombreCliente.setText(null);
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
}
