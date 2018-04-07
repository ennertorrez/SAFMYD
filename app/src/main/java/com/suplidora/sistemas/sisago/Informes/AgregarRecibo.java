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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
    private  String tipoBusqueda = "2";
    private ClientesHelper ClientesH;
    private InformesHelper InformesH;
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
        rbEfectivo = (RadioButton) findViewById(R.id.rbEfectivo);
        rbCheque = (RadioButton) findViewById(R.id.rbCheque);
        rbDeposito = (RadioButton) findViewById(R.id.rbDeposito);

        txtFechaRecibo.setText(getDatePhone());
        fechaRecibo = txtFechaRecibo.getText().toString();

        txtFechaDocPago.setText(getDatePhone());
        fechaDoc = txtFechaDocPago.getText().toString();

        sd = new SincronizarDatos(DbOpenHelper,InformesH,InformesDetalleH,ClientesH,FacturasPendientesH);

        Intent in = getIntent();
        vNoInforme=in.getStringExtra(variables_publicas.INFORMES_COLUMN_CodInforme).toString();
        lblNoInforme.setText("No. Informe: "+ vNoInforme);
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
                lblFooterItem.setText("ClienteS Encontrados: " + String.valueOf(listaClientesItem.size()));

            }
        });
        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                txtNombreCliente.setText("");
                lblIdCliente.setText("");

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
}
