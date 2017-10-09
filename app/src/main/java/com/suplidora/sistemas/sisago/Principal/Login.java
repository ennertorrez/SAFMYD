package com.suplidora.sistemas.sisago.Principal;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcHelper;
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
import com.suplidora.sistemas.sisago.Entidades.Usuario;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by usuario on 20/3/2017.
 */

public class Login extends Activity {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    private String TAG = Login.class.getSimpleName();
    private Button btnIngresar;
    private EditText txtUsuario;
    private EditText txtPassword;
    private String Usuario = "";
    private String Contrasenia = "";
    private ProgressDialog pDialog;
    private String tipoBusqueda = "3";
private boolean isOnline=false;
    // URL to get contacts JSON

    final String url = variables_publicas.direccionIp + "/ServicioLogin.svc/BuscarUsuario/";
    final String urlGetConfiguraciones = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones";

    final String urlFormaPago = variables_publicas.direccionIp + "/ServicioPedidos.svc/FormasPago/";
    final String urlVendedores = variables_publicas.direccionIp + "/ServicioPedidos.svc/ListaVendedores/";

    private DataBaseOpenHelper DbOpenHelper;

    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;

    private CartillasBcHelper CartillasBcH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private ConsolidadoCargaHelper ConsolidadoCargaH;
    private ConsolidadoCargaDetalleHelper ConsolidadoCargaDetalleH;
    private FormaPagoHelper FormaPagoH;
    private PrecioEspecialHelper PrecioEspecialH;
    private ConfiguracionSistemaHelper ConfigH;
    private ClientesSucursalHelper ClientesSucH;
    private ArticulosHelper ArticulosH;
    private PedidosDetalleHelper PedidoDetalleH;
    private PedidosHelper PedidoH;
    private SincronizarDatos sd;
    String MsjLoging = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.iniciosesion);

        MsjLoging = variables_publicas.MensajeLogin;
        if (MsjLoging != "") {
            mensajeAviso(MsjLoging);
        }


        DbOpenHelper = new DataBaseOpenHelper(Login.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ConfigH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        ClientesSucH = new ClientesSucursalHelper(DbOpenHelper.database);
        CartillasBcH = new CartillasBcHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        ConsolidadoCargaH = new ConsolidadoCargaHelper(DbOpenHelper.database);
        ConsolidadoCargaDetalleH = new ConsolidadoCargaDetalleHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);

        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH,
                FormaPagoH,
                PrecioEspecialH, ConfigH, ClientesSucH, ArticulosH, UsuariosH, ConsolidadoCargaH,ConsolidadoCargaDetalleH,PedidoH,PedidoDetalleH);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        Usuario UltimoUsuario = UsuariosH.BuscarUltimoUsuario();
        if (UltimoUsuario != null) {
            txtUsuario.setText(UltimoUsuario.getUsuario());
            txtPassword.requestFocus();
        }
        TextView lblVersion = (TextView) findViewById(R.id.login_version);
        lblVersion.setText("Versión " + getCurrentVersion());
        /*NO CAMBIAR ESTA DIRECCION: VERIFICA SI ES SERVIDOR DE PRUEBAS*/
        if (variables_publicas.direccionIp == "http://186.1.18.75:8085") {
            lblVersion.setText("Versión " + getCurrentVersion() + " Desarrollo");
        }
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnIngresar.performClick();
                }
                return false;
            }
        });
        btnIngresar = (Button) findViewById(R.id.btnIngresar);

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtUsuario.getWindowToken(), 0);
                inputMethodManager.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);

               Funciones.GetLocalDateTime();

                Usuario = txtUsuario.getText().toString();
                Contrasenia = txtPassword.getText().toString();

                if (TextUtils.isEmpty(Usuario)) {
                    txtUsuario.setError("Ingrese el nombre de usuario");
                    return;
                }
                if (TextUtils.isEmpty(Contrasenia)) {
                    txtPassword.setError("Ingrese la contraseña");
                    return;
                }

                boolean isOnline = new Funciones().checkInternetConnection(Login.this);
                variables_publicas.usuario = UsuariosH.BuscarUsuarios(Usuario, Contrasenia);
                String VersionDatos = "VersionDatos";
                variables_publicas.Configuracion = ConfigH.BuscarValorConfig(VersionDatos);


                if(isOnline){
                 new GetValorConfig().execute();
                }
                if (!isOnline && variables_publicas.usuario != null) {
                    variables_publicas.MensajeLogin = "";
                    variables_publicas.LoginOk = true;
                    Intent intent = new Intent("android.intent.action.Barra_cargado");
                    startActivity(intent);
                    finish();
                } else if (!isOnline && variables_publicas.usuario == null) {
                    mensajeAviso("Usuario o contraseña invalido\n O para conectar un nuevo usuario debe conectarse a internet");
                }

            }
        });

        variables_publicas.usuario = UltimoUsuario;
        isOnline= new Funciones().checkInternetConnection(Login.this);
        /*if (variables_publicas.usuario != null) {
            if (isOnline) {
                new SincronizardorPedidos().execute();
            }
        }*/

        ValidarUltimaVersion();
        loadIMEI();
        try {
            Configuration config = getResources().getConfiguration();
            restartInLocale(config.locale);
        } catch (Exception ex) {
            Funciones.MensajeAviso(getApplicationContext(), ex.getMessage());
        }



    }

    private void restartInLocale(Locale locale) {
        if (!locale.getDisplayName().equalsIgnoreCase("español (Estados Unidos)")) {
            locale = new Locale("es", "US");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            Resources resources = getResources();
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            recreate();
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

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(Login.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(Login.this,
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            // Received permission result for READ_PHONE_STATE permission.est.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // READ_PHONE_STATE permission has been granted, proceed with displaying IMEI Number
                //alertAlert(getString(R.string.permision_available_read_phone_state));
                doPermissionGrantedStuffs();
            } else {
                alertAlert("Se necesita permiso para acceder al estado del telefono");
            }
        }
    }

    private void alertAlert(String msg) {
        new AlertDialog.Builder(Login.this)
                .setTitle("Permission Request")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do somthing here
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    public void doPermissionGrantedStuffs() {
        //Have an  object of TelephonyManager
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Get IMEI Number of Phone  //////////////// for this example i only need the IMEI
        variables_publicas.IMEI = tm.getDeviceId();


    }


/*

    public String getIMEI(Activity activity) {
        TelephonyManager telephonyManager = (TelephonyManager) activity
                .getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
*/

    private void ValidarUltimaVersion() {
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

    //region ObtieneUsuario
    public class GetUser extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            Intent intent = new Intent("android.intent.action.Barra_cargado");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            //************USUARIOS
            HttpHandler sh = new HttpHandler();
            String urlString = url + Usuario + "/" + Funciones.Codificar(Contrasenia);
            String encodeUrl = "";
            try {
                URL Url = new URL(urlString);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }


            String jsonStr = sh.makeServiceCall(encodeUrl);

            Log.e(TAG, "Response from url: " + jsonStr);

            /**********************************USUARIOS**************************************/
            if (jsonStr != null) {

                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray Usuarios = jsonObj.getJSONArray("BuscarUsuarioResult");
                    if (Usuarios.length() == 0) {
                        variables_publicas.LoginOk = false;
                        variables_publicas.MensajeLogin = "Usuario o contraseña invalido";
                        return null;
                    }
                    UsuariosH.EliminaUsuarios();
                    // looping through All Contacts

                    for (int i = 0; i < Usuarios.length(); i++) {
                        JSONObject c = Usuarios.getJSONObject(i);
                        variables_publicas.CodigoVendedor = c.getString("Codigo");
                        variables_publicas.NombreVendedor = c.getString("Nombre");
                        variables_publicas.UsuarioLogin = c.getString("Usuario");
                        variables_publicas.TipoUsuario = c.getString("Tipo");
                        String Contrasenia = c.getString("Contrasenia");
                        String Tipo = c.getString("Tipo");
                        variables_publicas.RutaCliente = c.getString("Ruta");
                        variables_publicas.Canal = c.getString("Canal");
                        String TasaCambio = c.getString("TasaCambio");
                        String RutaForanea = c.getString("RutaForanea");
                        String FechaActualiza = Funciones.getDatePhone();
                        UsuariosH.GuardarUsuario(variables_publicas.CodigoVendedor, variables_publicas.NombreVendedor,
                                variables_publicas.UsuarioLogin, Contrasenia, Tipo, variables_publicas.RutaCliente, variables_publicas.Canal, TasaCambio, RutaForanea, FechaActualiza);

                        variables_publicas.LoginOk = true;
                        variables_publicas.MensajeLogin = "";
                        variables_publicas.usuario = UsuariosH.BuscarUsuarios(Usuario, Contrasenia);

                        //SINCRONIZAR DATOS
                        try {
                            if(variables_publicas.TipoUsuario.equalsIgnoreCase("Vehiculo"))
                            {
                                sd.SincronizarDevoluciones();
                            }
                            else {sd.SincronizarTodo();}
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    for (int i=0; i < 2; i++)
//                                    {
//                                        Toast.makeText(getApplicationContext(),
//                                                "No es posible conectarse al servidor. \n Por favor verifique su conexion a internet!: ",
//                                                Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            });
                            variables_publicas.LoginOk = false;
                            variables_publicas.MensajeLogin = "Ha ocurrido un error al sincronizar los datos. Por favor intente nuevamente";
                            UsuariosH.EliminaUsuarios();
                        }
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "error: " + "No se ha podido establecer contacto con el servidor");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "error: " + e.getMessage(),
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
                                "error: " + "No se ha podido establecer contacto con el servidor",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog!=null && pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    //endregion

    //region ObtieneValorConfiguracion
    private class GetValorConfig extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
          /*  pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Inicializando el sistema, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String urlString = urlGetConfiguraciones;

            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);


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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           /* if (pDialog.isShowing())
                pDialog.dismiss();*/

            if (isOnline && variables_publicas.usuario != null && variables_publicas.Configuracion != null) {
                try {

                    String FechaLocal = variables_publicas.usuario.getFechaActualiza();
                    String FechaActual = Funciones.getDatePhone();
                    int ValorConfigLocal = Integer.parseInt(variables_publicas.Configuracion.getValor());
                    int ValorConfigServidor = Integer.parseInt(variables_publicas.ValorConfigServ);
                    if (!FechaLocal.equals(FechaActual) || ValorConfigLocal < ValorConfigServidor) {
                        new GetUser().execute();
                    } else {
                        variables_publicas.MensajeLogin = "";
                        variables_publicas.LoginOk = true;
                        Intent intent = new Intent("android.intent.action.Barra_cargado");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    mensajeAviso(e.getMessage());
                }

            } else if (isOnline && (variables_publicas.usuario == null || variables_publicas.Configuracion == null)) {
                new GetUser().execute();
            }



        }
    }
    //endregion




    public void mensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }





    private class GetLatestVersion extends AsyncTask<Void, Void, Void> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            /*
            pDialog = new ProgressDialog(Login.this);
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


            String currentVersion = getCurrentVersion();
            variables_publicas.VersionSistema = currentVersion;
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
                if(isFinishing()){return;}
                builder.show();
            }
        }


    }

    private class SincronizardorPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Sincronizando pedidos locales, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                sd.SincronizarPedidosLocales();
            }catch (Exception e){
                Log.e("Error:", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        if (pDialog!=null)
            pDialog.dismiss();
    }
}
