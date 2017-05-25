package com.suplidora.sistemas.sisago.Principal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FormaPagoHelper;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

/**
 * Created by usuario on 20/3/2017.
 */

public class Login extends Activity {
    private String TAG = Login.class.getSimpleName();
    private Button btnIngresar;
    private EditText txtUsuario;
    private EditText txtPassword;
    private String Usuario = "";
    private String Contrasenia = "";
    private ProgressDialog pDialog;
    private String tipoBusqueda = "3";

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
    private FormaPagoHelper FormaPagoH;
    private PrecioEspecialHelper PrecioEspecialH;
    private ConfiguracionSistemaHelper ConfigH;
    private ClientesSucursalHelper ClientesSucH;
    private ArticulosHelper ArticulosH;
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
        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH,
                FormaPagoH,
                PrecioEspecialH, ConfigH, ClientesSucH, ArticulosH);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        Usuario UltimoUsuario = UsuariosH.BuscarUltimoUsuario();
        if (UltimoUsuario != null) {
            txtUsuario.setText(UltimoUsuario.getUsuario());
            txtPassword.requestFocus();
        }
        TextView lblVersion = (TextView) findViewById(R.id.login_version);

        lblVersion.setText("Versión " + getCurrentVersion());
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

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                variables_publicas.FechaActual = sdf.format(c.getTime());

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

                boolean isOnline = checkInternetConnection();
                variables_publicas.usuario = UsuariosH.BuscarUsuarios(Usuario, Contrasenia);
                String VersionDatos = "VersionDatos";
                variables_publicas.Configuracion = ConfigH.BuscarValorConfig(VersionDatos);


                if (isOnline && variables_publicas.usuario != null && variables_publicas.Configuracion != null) {
                    try {
                        new GetValorConfig().execute().get();
                    } catch (Exception e) {
                        mensajeAviso(e.getMessage());
                    }
                    String FechaLocal = variables_publicas.usuario.getFechaActualiza();
                    String FechaActual = getDatePhone();
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
                } else if (isOnline && (variables_publicas.usuario == null || variables_publicas.Configuracion == null)) {
                    new GetUser().execute();
                }
                if (!isOnline && variables_publicas.usuario != null) {
                    variables_publicas.MensajeLogin = "";
                    variables_publicas.LoginOk = true;
                    Intent intent = new Intent("android.intent.action.Barra_cargado");
                    startActivity(intent);
                    finish();
                } else if (!isOnline && variables_publicas.usuario == null) {
                    mensajeAviso("Usuario o contraseña invalido\nVerifique la conexion a internet");
                }
            }
        });

        ValidarUltimaVersion();

    }

    private void ValidarUltimaVersion() {
        if (checkInternetConnection()) {
            String latestVersion = "";
            String currentVersion = getCurrentVersion();
            try {
                latestVersion = new GetLatestVersion().execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            //If the versions are not the same
            if (latestVersion != null && !currentVersion.equals(latestVersion)) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Nueva version disponible");
                builder.setMessage("Es necesario actualizar la aplicacion para poder continuar.");
                builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Click button action
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.suplidora.sistemas.sisago&hl=es")));
                        dialog.dismiss();
                        ValidarUltimaVersion();
                    }
                });
                builder.setCancelable(false);
                builder.show();
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
    private class GetUser extends AsyncTask<Void, Void, Void> {
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
                        String FechaActualiza = getDatePhone();
                        UsuariosH.GuardarUsuario(variables_publicas.CodigoVendedor, variables_publicas.NombreVendedor,
                                variables_publicas.UsuarioLogin, Contrasenia, Tipo, variables_publicas.RutaCliente, variables_publicas.Canal, TasaCambio, RutaForanea, FechaActualiza);

                        variables_publicas.LoginOk = true;
                        variables_publicas.MensajeLogin = "";
                        variables_publicas.usuario = UsuariosH.BuscarUsuarios(Usuario, Contrasenia);
                        //SINCRONIZAR DATOS
                        try {
                            sd.SincronizarTodo();
                        } catch (final JSONException e) {
                            Log.e(TAG, "Json parsing error: " + e.getMessage());
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
    }

    //endregion

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
    //endregion

    public Boolean isOnlineNet() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Log.v(TAG, "No tiene conexión a internet");
            return false;
        }
    }

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

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    public static String getHourPhone() {
        Date dt = new Date();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        String formatteHour = df.format(dt.getTime());
        return formatteHour;
    }


    private class GetLatestVersion extends AsyncTask<String, String, String> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.suplidora.sistemas.sisago&hl=es";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return latestVersion;
        }
    }


}
