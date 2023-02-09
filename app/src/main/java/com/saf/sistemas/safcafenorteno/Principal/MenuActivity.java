package com.saf.sistemas.safcafenorteno.Principal;


import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
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

import com.saf.sistemas.safcafenorteno.AccesoDatos.ArticulosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.CategoriasClienteHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.ClientesSucursalHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.ConfiguracionSistemaHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.EscalaPreciosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasLineasHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasPendientesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FormaPagoHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.PedidosDetalleHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.PedidosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.PromocionesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.RecibosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.UsuariosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.VendedoresHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.TPreciosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.RutasHelper;
import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;
import com.saf.sistemas.safcafenorteno.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Menu.ClientesFragment;
import com.saf.sistemas.safcafenorteno.Clientes.ClientesNew;
import com.saf.sistemas.safcafenorteno.Menu.ClientesInactivosFragment;
import com.saf.sistemas.safcafenorteno.Menu.FacturasFragment;
import com.saf.sistemas.safcafenorteno.Menu.HistoricoventasClienteFragment;
import com.saf.sistemas.safcafenorteno.Menu.ListaFacturasFragment;
import com.saf.sistemas.safcafenorteno.Menu.ListaPedidosClientesFragment;
import com.saf.sistemas.safcafenorteno.Menu.ListaPedidosFragment;
import com.saf.sistemas.safcafenorteno.Menu.ListaPedidosSupFragment;
import com.saf.sistemas.safcafenorteno.Menu.ListaTotalFacturado;
import com.saf.sistemas.safcafenorteno.Menu.MaestroProductoFragment;
import com.saf.sistemas.safcafenorteno.Menu.PedidosFragment;
import com.saf.sistemas.safcafenorteno.Pedidos.ImprimirActivity;
import com.saf.sistemas.safcafenorteno.Pedidos.Pagos;
import com.saf.sistemas.safcafenorteno.Pedidos.PedidosCliente;
import com.saf.sistemas.safcafenorteno.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
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
    private PromocionesHelper PromocionesH;
    private FormaPagoHelper FormaPagoH;
    private ConfiguracionSistemaHelper ConfigH;
    private ClientesSucursalHelper ClientesSucH;
    private ArticulosHelper ArticulosH;
    private PedidosDetalleHelper PedidoDetalleH;
    private PedidosHelper PedidoH;
    private RutasHelper RutasH;
    private FacturasHelper FacturasH;
    private FacturasLineasHelper FacturasLineasH;
    private TPreciosHelper TPreciosH;
    private CategoriasClienteHelper CategoriaH;
    private EscalaPreciosHelper EscalaPreciosH;
    private RecibosHelper RecibosH;

    private FacturasPendientesHelper FacturasPendientesH;
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
        PromocionesH = new PromocionesHelper(DbOpenHelper.database);
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH = new UsuariosHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        RutasH = new RutasHelper(DbOpenHelper.database);
        CategoriaH = new CategoriasClienteHelper(DbOpenHelper.database);
        TPreciosH = new TPreciosHelper(DbOpenHelper.database);
        EscalaPreciosH = new EscalaPreciosHelper(DbOpenHelper.database);
        FacturasH = new FacturasHelper(DbOpenHelper.database);
        FacturasLineasH = new FacturasLineasHelper(DbOpenHelper.database);
        RecibosH = new RecibosHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);

        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, PromocionesH,FormaPagoH,ConfigH, ClientesSucH,
                ArticulosH, UsuariosH, PedidoH, PedidoDetalleH,CategoriaH,TPreciosH,RutasH,EscalaPreciosH,FacturasH,FacturasLineasH,RecibosH,FacturasPendientesH);

        try {
            variables_publicas.info = "***** Usuario: " + variables_publicas.usuario.getNombre() + " / IMEI: " + (variables_publicas.IMEI == null ? "null" : variables_publicas.IMEI) + " / VersionSistema: " + variables_publicas.VersionSistema + " ******** ";
        } catch (Exception ex) {
            Log.e("error", ex.getMessage());
            ex.printStackTrace();
        }

        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Cliente")){
            navigationView.getMenu().getItem(0).setVisible(false); //Menu Articulos
            navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(false); //Lista Pedidos
            navigationView.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(false); //Nuevo Pedido
            navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setVisible(true); //Pedido Clientes
            navigationView.getMenu().getItem(1).getSubMenu().getItem(3).setVisible(true); //Lista Pedidos Cliente
            navigationView.getMenu().getItem(2).setVisible(false); //Menu Facturacion
            navigationView.getMenu().getItem(3).setVisible(false); //Menu Clientes
            navigationView.getMenu().getItem(4).setVisible(false); //Menu Pagos
            navigationView.getMenu().getItem(5).setVisible(false); //Menu Reportes
        }else{
            navigationView.getMenu().getItem(0).setVisible(true); //Menu Articulos
/*            navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setVisible(true); //Lista Pedidos
            navigationView.getMenu().getItem(1).getSubMenu().getItem(1).setVisible(true); //Nuevo Pedido
            navigationView.getMenu().getItem(1).getSubMenu().getItem(2).setVisible(false); //Pedido Clientes
            navigationView.getMenu().getItem(1).getSubMenu().getItem(3).setVisible(false); //Lista Pedidos Cliente*/
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(2).setVisible(true); //Menu Facturacion
            navigationView.getMenu().getItem(3).setVisible(true); //Menu Clientes
            navigationView.getMenu().getItem(4).setVisible(true); //Menu Pagos
            navigationView.getMenu().getItem(5).setVisible(true); //Menu Reportes
            navigationView.getMenu().getItem(5).setVisible(true); //Menu Reportes
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setVisible(false); //Reporte de preventa
        }
        //navigationView.getMenu().getItem(2).setVisible(false);
        if (variables_publicas.usuario.getAddCliente().equalsIgnoreCase("1")){
            navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(false); //Lista Clientes
            navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setVisible(true); //Agregar Clientes
            navigationView.getMenu().getItem(3).getSubMenu().getItem(2).setVisible(false); //Activar Clientes
        }else{
            navigationView.getMenu().getItem(3).getSubMenu().getItem(0).setVisible(false); //Lista Clientes
            navigationView.getMenu().getItem(3).getSubMenu().getItem(1).setVisible(false); //Agregar Clientes
            navigationView.getMenu().getItem(3).getSubMenu().getItem(2).setVisible(false); //Activar Clientes
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
            MensajeAviso("Datos actualizados correctamente");
        }
    }

    public void MensajeAviso(String texto) {
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

            case R.id.btnListadoPedidos:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaPedidosFragment());
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

            case R.id.btnPedidoCliente:
                // Starting new intent
                Intent in = new Intent(getApplicationContext(), PedidosCliente.class);
                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, variables_publicas.usuario.getCodigo() );
                in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, variables_publicas.usuario.getNombre() );
                in.putExtra(variables_publicas.vVisualizar,"False");
                startActivity(in);
                break;

            case R.id.btnFacturar:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new FacturasFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;

            case R.id.btnListadoFacturas:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaFacturasFragment());
                tran.addToBackStack(null);
                tran.commit();
                break;


            case R.id.btnListadoPedidosCliente:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaPedidosClientesFragment());
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

            case R.id.btnConfImpresora:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ImprimirActivity());
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

            case R.id.btnRecibos:

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                   MensajeAviso("Bluetooth no soportado por este dispositivo.");
                        break ;
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        MensajeAviso("Bluetooth no está habilitado.");
                            break ;
                    }
                }
                variables_publicas.vImpresoraBT=RecibosH.BuscarNombreImpresora();
                if (variables_publicas.vImpresoraBT==null || variables_publicas.vImpresoraBT.equalsIgnoreCase("")|| variables_publicas.vImpresoraBT.equalsIgnoreCase("0")){
                    MensajeAviso("No hay configurada una impresora. Debe seleccionar un dispositivo Bluetooth para poder registrar Recibos. ");
                    break ;
                }


                ArrayList<HashMap<String, String>> lista;
                    lista = RecibosH.ObtenerUltimoCodigoRecibo(variables_publicas.rutacargada, RecibosH.ObtenerSerieRuta(variables_publicas.rutacargada));
                if (lista.size() <= 0) {
                    MensajeAviso("El Vendedor Seleccionado no tiene serie de Recibos asignado.");
                    break ;
                }
                Intent newRecibo;
                newRecibo = new Intent(getApplicationContext(), Pagos.class);
                startActivity(newRecibo);
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
