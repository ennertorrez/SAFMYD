package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesHelper;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 19/3/2018.
 */

public class AgregarRecibo extends Activity {

    private TextView lblNoInforme;
    private TextView lblTc;
    private TextView lblNoRecibo;
    private TextView txtFechaRecibo;
    private TextView lblIdCliente;
    private EditText txtNombreCliente;
    private Button btnBuscarCliente;
    private Spinner cboFactura;
    private TextView lblSaldo;
    private EditText txtMonto;
    private TextView lblMontoLetras;
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
    private DataBaseOpenHelper DbOpenHelper;
    private boolean finalizar = false;
    private Cliente cliente;
    AlertDialog alertDialog;
    private boolean isOnline = false;
    private String TAG = AgregarRecibo.class.getSimpleName();
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    private SincronizarDatos sd;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recibos);

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        DbOpenHelper = new DataBaseOpenHelper(AgregarRecibo.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        InformesH = new InformesHelper(DbOpenHelper.database);
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);

        lblNoInforme = (TextView) findViewById(R.id.lblNoInforme);
        lblTc = (TextView) findViewById(R.id.lblTC);
        lblNoRecibo = (TextView) findViewById(R.id.lblNoRecibo);
        txtFechaRecibo = (TextView) findViewById(R.id.txtFechaRecibo);
        lblIdCliente = (TextView) findViewById(R.id.lblIdCliente);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreCliente);
        btnBuscarCliente = (Button) findViewById(R.id.btnBuscarCliente);
        cboFactura = (Spinner) findViewById(R.id.cboFactura);
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

        sd = new SincronizarDatos(DbOpenHelper,InformesH,InformesDetalleH,ClientesH);

        //Funciones funciones= new Funciones();
        btnBuscarCliente.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                BuscarCliente();
                btnOKCliente.performClick();
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(ClientesNew.this);
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
}
