package com.suplidora.sistemas.sisago.Clientes;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.StringDef;
import android.telephony.TelephonyManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.SpinnerDialog;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Devoluciones.DevolucionesActivity;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.ConsolidadoCarga;
import com.suplidora.sistemas.sisago.Entidades.DptpMuniBarrio;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;


/**
 * Created by Sistemas on 14/12/2017.
 */

public class ClientesNew extends Activity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private String TAG = ClientesNew.class.getSimpleName();
    private Button btnBuscar;
    private Cliente cliente;
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioClientes.svc/GetConfiguraciones";
    final String urlGetCedula = variables_publicas.direccionIp + "/ServicioClientes.svc/GetCedula/";
    final String urlGetIdClienteNuevo = variables_publicas.direccionIp + "/ServicioClientes.svc/ObtenerIdClienteNuevo/";
    private DataBaseOpenHelper DbOpenHelper;
    private ClientesHelper ClientesH;
    private SincronizarDatos sd;
    private boolean isOnline = false;
    static final String KEY_IdClienteV = "IdCliente";
    static final String KEY_NombreClienteV = "Nombre";
    private String focusedControl = "";
    public boolean vEditando = false;
    String IMEI = "";

    //private Spinner cboClienteVario;
    private TextView lblIdCv;
    private TextView txtNombreClienteV;
    private EditText txtCodCliente;
    private EditText txtCedula;
    private EditText txtNombreCliente;
    private EditText txtDireccion;
    private Spinner cboDpto;
    private Spinner cboMuni;
    private Spinner cboBarrio;
    private Spinner cboRuta;
    private Spinner cboDiaVisita;
    private Spinner cboTipoNeg;
    private EditText txtTelefono;
    private Button btnBuscarCed;
    private Button btnGuardar;
    private Button btnCancelar;
    private DptpMuniBarrio dptoMuniBarrio;
    private String cvId;
    private String vnomclientevario;
    private String vcedula;
    private String vtipo = "1";
    private String vFrecuencia = "LUNES";
    private String vRuta = "MA_101";
    private String vtipoNeg = "Pulpería";

    List<HashMap<String, String>> ObtieneCV = null;
    public static ArrayList<HashMap<String, String>> listaCed;
    java.util.ArrayList<String> CcDptos;
    SpinnerDialog spinnerDialog;
    public static ArrayList<HashMap<String, String>> lista;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clientes_agregar);

        dptoMuniBarrio = new DptpMuniBarrio();

        ValidarUltimaVersion();
        if (isOnline) {
            SincronizarConfig();
        }

        DbOpenHelper = new DataBaseOpenHelper(ClientesNew.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);

        //cboClienteVario = (Spinner) findViewById(R.id.cboClienteVario);
        lblIdCv = (TextView) findViewById(R.id.lblIdCV);
        txtNombreClienteV = (TextView) findViewById(R.id.lblNombreClienteV);
        txtCodCliente = (EditText) findViewById(R.id.txtCodCliente);
        txtCedula = (EditText) findViewById(R.id.txtCedulaB);
        txtNombreCliente = (EditText) findViewById(R.id.txtNombCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDirCliente);
        cboDpto = (Spinner) findViewById(R.id.cboDpto);
        cboMuni = (Spinner) findViewById(R.id.cboMun);
        cboBarrio = (Spinner) findViewById(R.id.cboBarrio);
        cboDiaVisita = (Spinner) findViewById(R.id.cboDiasVisita);
        cboRuta = (Spinner) findViewById(R.id.cboRutaCliente);
        cboTipoNeg = (Spinner) findViewById(R.id.cboTipoNeg);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCedula.setFocusable(true);
        txtCodCliente.setEnabled(false);

        btnBuscarCed = (Button) findViewById(R.id.btnBusCedCliente);
        btnGuardar = (Button) findViewById(R.id.btnGuardarCli);
        btnCancelar = (Button) findViewById(R.id.btnCancelarCli);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientesNew.this.onBackPressed();
            }
        });
        sd = new SincronizarDatos(DbOpenHelper, ClientesH);

        txtCedula.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    btnBuscarCed.performClick();
                    focusedControl = "txtNombreCliente";
                    return false;
                }
                return true;
            }
        });

        if (vEditando == false) {

           /* cvId = ClientesH.ObtenerClientesVariosId(variables_publicas.usuario.getCodigo());
            ClientesH.BuscarClientesVarios(variables_publicas.usuario.getCodigo());
            lblIdCv.setText("No. Clientes Varios: " + cvId);*/

            lista = new ArrayList<HashMap<String, String>>();
            lista = ClientesH.BuscarClientesVarios(variables_publicas.usuario.getCodigo());

            for (int i = 0; i < lista.size(); i++) {
                cvId = lista.get(i).get("IdCliente");
                vnomclientevario = lista.get(i).get("Nombre");
            }
            txtNombreClienteV.setText(vnomclientevario);
            lblIdCv.setText("No. Clientes Varios: " + cvId);

            if (cvId.equals("")) {
                vtipo = "2";
            }
            GetIdCliente();
            CargaDatosCombo();
        } else {
            CargaDatosComboEdit();
        }
        btnBuscarCed.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//
                GetDatosCedula();
                txtNombreCliente.setText(variables_publicas.nombreCed);
                txtDireccion.setText(variables_publicas.direccionCedula);
                txtNombreCliente.requestFocus();
                //focusedControl = "";
                // }
            }
        });
    }

    private void CargaDatosComboEdit() {

    }

    private void CargaDatosCombo() {

        //Combo Carga
        final List<DptpMuniBarrio> CDptos;
        CDptos = ClientesH.ObtenerListaDepartamentos();

        ArrayAdapter<DptpMuniBarrio> adapterDpto = new ArrayAdapter<DptpMuniBarrio>(this, android.R.layout.simple_spinner_item, CDptos);
        adapterDpto.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboDpto.setAdapter(adapterDpto);
        //cboDpto.setSelection(Funciones.getIndexSpinner(cboDpto, "MANAGUA"));
        dptoMuniBarrio.setNombre_Departamento("MANAGUA");  //(DptpMuniBarrio) Funciones.getIndexSpinner(cboDpto, "MANAGUA"));
        cboDpto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                int item = position;
                String itemval = adapter.getItemAtPosition(position).toString();
                CargarMunicipios(itemval);
                CargarBarrios(itemval);
                dptoMuniBarrio.setCodigo_Departamento(((DptpMuniBarrio) adapter.getItemAtPosition(position)).getCodigo_Departamento());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboMuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                dptoMuniBarrio.setCodigo_Municipio(((DptpMuniBarrio) adapter.getItemAtPosition(position)).getCodigo_Municipio());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        cboBarrio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                dptoMuniBarrio.setCodigo_Barrio(((DptpMuniBarrio) adapter.getItemAtPosition(position)).getCodigo_Barrio());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        final List<String> CRuta;
        CRuta = ClientesH.ObtenerListaRutas(variables_publicas.usuario.getCodigo());

        ArrayAdapter<String> adapterRuta = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CRuta);
        adapterRuta.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboRuta.setAdapter(adapterRuta);
        cboRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vRuta = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        String[] valores = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};
        cboDiaVisita.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));
        cboDiaVisita.setSelection(Funciones.getIndexSpinner(cboDiaVisita, "LUNES"));
        cboDiaVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vFrecuencia = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        String[] valoresTipoNeg = {"Pulpería", "Hogar con Venta", "Farmacia", "Comedor", "Variedades", "Distribuidora", "Restaurante", "Cafetín",
                "Hotel", "Ferretería", "Panadería", "Barbería", "Bar", "Veterinaria", "Gasolinera", "Licorería", "Tienda", "Otros"};
        cboTipoNeg.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valoresTipoNeg));
        cboTipoNeg.setSelection(Funciones.getIndexSpinner(cboTipoNeg, "Pulpería"));
        cboTipoNeg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vtipoNeg = adapter.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void CargarMunicipios(String vDepartamento) {
        final List<DptpMuniBarrio> CMuni;
        CMuni = ClientesH.ObtenerListaMunicipios(vDepartamento);
        ArrayAdapter<DptpMuniBarrio> adapterMuni = new ArrayAdapter<DptpMuniBarrio>(this, android.R.layout.simple_spinner_item, CMuni);
        adapterMuni.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboMuni.setAdapter(adapterMuni);
    }

    private void CargarBarrios(String vDepartamento) {
        final List<DptpMuniBarrio> CBarrio;
        CBarrio = ClientesH.ObtenerListaBarrios(vDepartamento);
        ArrayAdapter<DptpMuniBarrio> adapterBarrio = new ArrayAdapter<DptpMuniBarrio>(this, android.R.layout.simple_spinner_item, CBarrio);
        adapterBarrio.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboBarrio.setAdapter(adapterBarrio);
    }

    private void SincronizarConfig() {
        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetValorConfig().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new ClientesNew.GetValorConfig().execute();
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

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
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
            new AlertDialog.Builder(ClientesNew.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(ClientesNew.this,
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

    private void GetDatosCedula() {
        if (TextUtils.isEmpty(txtCedula.getText().toString())) {
            txtCedula.setError("Ingrese un número de Cédula.");
            txtNombreCliente.setText("");
            txtDireccion.setText("");
            return;
        }
        vcedula = txtCedula.getText().toString().trim();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetCedula + vcedula;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener los datos de la cédula, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray DatosCedula = jsonObj.getJSONArray("GetCedulaResult");
                for (int i = 0; i < DatosCedula.length(); i++) {
                    JSONObject c = DatosCedula.getJSONObject(i);

                    String CodCedula = c.getString("Cedula");
                    String nomCedula = c.getString("Nombre");
                    String dirCedula = c.getString("Direccion");
                    variables_publicas.noCedula = CodCedula;
                    variables_publicas.nombreCed = nomCedula;
                    variables_publicas.direccionCedula = dirCedula;
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener los datos de la cédula,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    private void GetIdCliente() {
        if (TextUtils.isEmpty(lblIdCv.getText().toString())) {
            vtipo = "2";
        }
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();

        String urlString = urlGetIdClienteNuevo + vtipo;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                String resultState = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[0];
                String NoCliente = (String) ((String) jsonObj.get("ObtenerIdClienteNuevoResult")).split(",")[1];
                if (resultState.equals("true")) {
                    txtCodCliente.setText(NoCliente);

                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el Nuevo Id de Cliente, Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación Requerida")
                .setMessage("Esta seguro que desea cancelar el Registro de Cliente?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClientesNew.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}

