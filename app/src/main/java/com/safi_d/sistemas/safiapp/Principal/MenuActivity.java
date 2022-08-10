package com.safi_d.sistemas.safiapp.Principal;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import com.google.android.material.navigation.NavigationView;
import androidx.multidex.MultiDex;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.text.ParseException;

import android.os.Handler;

import com.safi_d.sistemas.safiapp.AccesoDatos.ArticulosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.CartillasBcDetalleHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.CartillasBcHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.CategoriasClienteHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.ClientesHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.ClientesSucursalHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.ConfiguracionSistemaHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.EscalaPreciosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.FacturasPendientesHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.FormaPagoHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.InformesDetalleHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.InformesHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.PedidosDetalleHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.PedidosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.PromocionesHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.UsuariosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.VendedoresHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.TPreciosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.RutasHelper;
import com.safi_d.sistemas.safiapp.Auxiliar.Funciones;
import com.safi_d.sistemas.safiapp.Auxiliar.SincronizarDatos;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Informes.InformesActivity;
import com.safi_d.sistemas.safiapp.Menu.ClientesFragment;
import com.safi_d.sistemas.safiapp.Clientes.ClientesNew;
import com.safi_d.sistemas.safiapp.Menu.ClientesInactivosFragment;
import com.safi_d.sistemas.safiapp.Menu.FacturasMoraClienteFragment;
import com.safi_d.sistemas.safiapp.Menu.HistoricoventasClienteFragment;
import com.safi_d.sistemas.safiapp.Menu.ListaInformesFragment;
import com.safi_d.sistemas.safiapp.Menu.ListaPedidosFragment;
import com.safi_d.sistemas.safiapp.Menu.ListaPedidosSupFragment;
import com.safi_d.sistemas.safiapp.Menu.ListaRecibosPendFragment;
import com.safi_d.sistemas.safiapp.Menu.ListaPedidovsFacturado;
import com.safi_d.sistemas.safiapp.Menu.ListaTotalFacturado;
import com.safi_d.sistemas.safiapp.Menu.MaestroProductoFragment;
import com.safi_d.sistemas.safiapp.Menu.PedidosFragment;
import com.safi_d.sistemas.safiapp.R;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.time.LocalDateTime;
public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Handler handler = new Handler();
    private String TAG = ClientesFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    TextView lblUsuarioHeader;
    TextView lblUsuarioHeaderCodigo;
    TextView lblVersion;
    TextView lblServidor;
    TextView lblRuta;
    private DataBaseOpenHelper DbOpenHelper;

    private SincronizarDatos sd;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private CartillasBcHelper CartillasBcH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private PromocionesHelper PromocionesH;
    private FormaPagoHelper FormaPagoH;
    private ConfiguracionSistemaHelper ConfigH;
    private ClientesSucursalHelper ClientesSucH;
    private ArticulosHelper ArticulosH;
    private PedidosDetalleHelper PedidoDetalleH;
    private InformesHelper InformesH;
    private InformesDetalleHelper InformesDetalleH;
    private FacturasPendientesHelper FacturasPendientesH;
    private PedidosHelper PedidoH;
    private RutasHelper RutasH;
    private TPreciosHelper TPreciosH;
    private CategoriasClienteHelper CategoriaH;
    private EscalaPreciosHelper EscalaPreciosH;

    String IMEI = "";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 0;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = true;
    protected LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!isGPSEnabled) {
            showSettingsAlert();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryGreen)));
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimaryGreen)));


        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        lblUsuarioHeader = (TextView) header.findViewById(R.id.UsuarioHeader);
        lblUsuarioHeaderCodigo = (TextView) header.findViewById(R.id.UsuarioHeaderCodigo);
        lblVersion = (TextView) header.findViewById(R.id.lblVersionSistema);
        lblServidor = (TextView) header.findViewById(R.id.lblServidor);
        lblRuta = (TextView) header.findViewById(R.id.UsuarioHeaderRuta);
        String userHeader = "";
        String userHeaderCodigo = "";
        String VersionSistema = "";
        String Servidor = "";

        try {
            userHeader = variables_publicas.usuario.getNombre();
            userHeaderCodigo = variables_publicas.usuario.getCodigo();
            VersionSistema = "Version: " + variables_publicas.VersionSistema;
            Servidor = variables_publicas.direccionIp.equals("http://190.212.127.107:8088") ? "SERVIDOR: PRODUCCION" : "SERVIDOR: DESARROLLO";
        } catch (Exception ex) {
            Log.e("Error:", ex.getMessage());
        }
        lblUsuarioHeader.setText(userHeader);
        lblUsuarioHeaderCodigo.setText("Código: " + userHeaderCodigo);
        lblRuta.setText("Ruta: " + variables_publicas.rutacargadadescripcion);
        lblVersion.setText(VersionSistema);
        lblServidor.setText(Servidor);

        DbOpenHelper = new DataBaseOpenHelper(MenuActivity.this);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        ConfigH = new ConfiguracionSistemaHelper(DbOpenHelper.database);
        ClientesSucH = new ClientesSucursalHelper(DbOpenHelper.database);
        CartillasBcH = new CartillasBcHelper(DbOpenHelper.database);
        CartillasBcDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        PromocionesH = new PromocionesHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        InformesH = new InformesHelper(DbOpenHelper.database);
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
        RutasH = new RutasHelper(DbOpenHelper.database);
        CategoriaH = new CategoriasClienteHelper(DbOpenHelper.database);
        TPreciosH = new TPreciosHelper(DbOpenHelper.database);
        EscalaPreciosH = new EscalaPreciosHelper(DbOpenHelper.database);

        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH, PromocionesH,FormaPagoH,ConfigH, ClientesSucH,
                ArticulosH, UsuariosH, PedidoH, PedidoDetalleH,InformesH,InformesDetalleH,FacturasPendientesH,CategoriaH,TPreciosH,RutasH,EscalaPreciosH);

        try {
            variables_publicas.info = "***** Usuario: " + variables_publicas.usuario.getNombre() + " / IMEI: " + (variables_publicas.IMEI == null ? "null" : variables_publicas.IMEI) + " / VersionSistema: " + variables_publicas.VersionSistema + " ******** ";
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
            ex.printStackTrace();
        }

        //navigationView.getMenu().getItem(2).getSubMenu().getItem(1).setVisible(false); //Clientes nuevos
        //navigationView.getMenu().getItem(2).getSubMenu().getItem(2).setVisible(false); //Activar Clientes
        navigationView.getMenu().getItem(3).setVisible(false); //Recibos
        if (variables_publicas.usuario.getAddCliente().equalsIgnoreCase("1")){
            navigationView.getMenu().getItem(2).setVisible(true);
        }else{
            navigationView.getMenu().getItem(2).setVisible(false);
        }
        navigationView.getMenu().getItem(2).getSubMenu().getItem(2).setVisible(false); //Activar Clientes

        if ((!variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle")&& variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) || variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor") || variables_publicas.usuario.getTipo().equalsIgnoreCase("User") ) {
            navigationView.getMenu().getItem(3).setVisible(true); //Recibos
            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor") || variables_publicas.usuario.getTipo().equalsIgnoreCase("User")){
                navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(false); //Agregar Recibos
                navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setVisible(true); //Listado de Recibos
                navigationView.getMenu().getItem(3).getSubMenu().getItem(2).setVisible(true); //Estado de cuenta
                navigationView.getMenu().getItem(3).getSubMenu().getItem(3).setVisible(false); //Estado de cuenta
            }else {
                navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(true); //Agregar Recibos
                navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setVisible(true); //Listado de Recibos
                navigationView.getMenu().getItem(3).getSubMenu().getItem(2).setVisible(true); //Estado de cuenta
                navigationView.getMenu().getItem(3).getSubMenu().getItem(3).setVisible(true); //Estado de cuenta
            }
        }

        IMEI = variables_publicas.IMEI;
        if (IMEI == null) {

            new AlertDialog.Builder(this)
                    .setTitle("Confirmación Requerida")
                    .setMessage("Es necesario configurar el permiso \"Administrar llamadas telefonicas\" para porder guardar un Cliente, Desea continuar ? ")
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
        }

    }

    protected void  onActivate(Bundle savedInstanceState){

    }
    public void loadIMEI() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // READ_PHONE_STATE permission has not been granted.
            requestReadPhoneStatePermission();
        } else {
            // READ_PHONE_STATE permission is already been granted.
            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                variables_publicas.IMEI = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                variables_publicas.IMEI = tm.getImei();
            } else {
                variables_publicas.IMEI = tm.getDeviceId();
            }
            if (variables_publicas.IMEI  == null || variables_publicas.IMEI.isEmpty()) {
                variables_publicas.IMEI = android.os.Build.SERIAL;
            }
        }
    }

    private void requestReadPhoneStatePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_PHONE_STATE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            new AlertDialog.Builder(MenuActivity.this)
                    .setTitle("Permission Request")
                    .setMessage("Se necesita permiso para acceder al estado del telefono")
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //re-request
                            ActivityCompat.requestPermissions(MenuActivity.this,
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            removeAllFragments(getFragmentManager());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class SincronizaDatos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MenuActivity.this);
            pDialog.setMessage("Cargando datos, por favor espere......");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //SINCRONIZAR DATOS
            try {

                boolean isOnline = Funciones.TestServerConectivity();
                if (isOnline) {
                    sd.SincronizarTodo();
                }

            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
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
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            mensajeAviso("Datos actualizados correctamente");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            int id = item.getItemId();
            if (id == R.id.SincronizarDatos) {

                if (Build.VERSION.SDK_INT >= 11) {
                    //--post GB use serial executor by default --
                    new SincronizaDatos().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                } else {
                    //--GB uses ThreadPoolExecutor by default--
                    new SincronizaDatos().execute();
                }
            }
            //noinspection SimplifiableIfStatement
            if (id == R.id.Salir) {
                finish();//return true;
            }
            if (id == R.id.CerrarSesion) {
                Intent newAct = new Intent(getApplicationContext(), Login.class);
                newAct.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newAct);//return true;
                finish();
            }
            return super.onOptionsItemSelected(item);
        } catch (Exception e) {
            Funciones.MensajeAviso(getApplicationContext(), e.getMessage());
        } finally {
            return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentTransaction tran;
        FragmentManager fragmentManager = getFragmentManager();

        switch (id) {

            case R.id.btnMaestroProductos:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new MaestroProductoFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;
            case R.id.btnMaestroClientes:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ClientesFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;
            case R.id.btnNuevoCliente:
                Intent newCli = new Intent(getApplicationContext(), ClientesNew.class);
                startActivity(newCli);
                break;
            case R.id.btnActivarCliente:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ClientesInactivosFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnNuevoInforme:
                Intent newRecibo = new Intent(getApplicationContext(), InformesActivity.class);
                startActivity(newRecibo);
                break;

            case R.id.btnListadoPedidos:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaPedidosFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnListaInforme:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaInformesFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnReciboPend:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaRecibosPendFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnEstadoCta:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new FacturasMoraClienteFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnNuevoPedido:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new PedidosFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnReporteVentasAlDia:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaPedidosSupFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnReporteHistVentas:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new HistoricoventasClienteFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnReporteFacturacion:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaTotalFacturado() );
                tran.addToBackStack(null);
                tran.commit();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void removeAllFragments(FragmentManager fragmentManager) {

        if (fragmentManager.getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount() > 0) {

            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }

        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmación requerida")
                    .setMessage("Está seguro que desea salir de la aplicación?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MenuActivity.this);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        // Setting Dialog Title
        alertDialog.setTitle("Configuración GPS");
        // Setting Dialog Message
        alertDialog.setMessage("GPS no está habilitado. Favor activarlo");
        // On pressing Settings button
        alertDialog.setPositiveButton("Activar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Necesita permiso de Localización")
                        .setMessage("Esta aplicación necesita permiso de Localizacion.Presione Aceptar para poder usarla.")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MenuActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );

                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
  }
