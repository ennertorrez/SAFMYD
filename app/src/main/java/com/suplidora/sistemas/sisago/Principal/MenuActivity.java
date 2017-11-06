package com.suplidora.sistemas.sisago.Principal;


import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FormaPagoHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PrecioEspecialHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.UsuariosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Devoluciones.DevolucionesActivity;
import com.suplidora.sistemas.sisago.Menu.ClientesFragment;
import com.suplidora.sistemas.sisago.Menu.ListaDevolucionesFragment;
import com.suplidora.sistemas.sisago.Menu.ListaPedidosFragment;
import com.suplidora.sistemas.sisago.Menu.ListaPedidosSupFragment;
import com.suplidora.sistemas.sisago.Menu.MaestroProductoFragment;
import com.suplidora.sistemas.sisago.Menu.MapViewFragment;
import com.suplidora.sistemas.sisago.Menu.PedidosFragment;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONException;

import static android.R.id.toggle;
import static com.suplidora.sistemas.sisago.Auxiliar.variables_publicas.VersionSistema;
/*import com.suplidora.sistemas.sisago.app.ControladorArticulo;
import com.suplidora.sistemas.sisago.app.ControladorSincronizacion;*/


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = ClientesFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    TextView lblUsuarioHeader;
    TextView lblUsuarioHeaderCodigo;
    TextView lblVersion;
    TextView lblServidor;
    private DataBaseOpenHelper DbOpenHelper;

    private SincronizarDatos sd;
    private UsuariosHelper UsuariosH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;
    private ConsolidadoCargaHelper ConsolidadoCargaH;
    private ConsolidadoCargaDetalleHelper ConsolidadoCargaDetalleH;
    private CartillasBcHelper CartillasBcH;
    private CartillasBcDetalleHelper CartillasBcDetalleH;
    private FormaPagoHelper FormaPagoH;
    private PrecioEspecialHelper PrecioEspecialH;
    private ConfiguracionSistemaHelper ConfigH;
    private ClientesSucursalHelper ClientesSucH;
    private ArticulosHelper ArticulosH;
    private PedidosDetalleHelper PedidoDetalleH;
    private PedidosHelper PedidoH;
    private DevolucionesHelper DevolucionesH;
    private DevolucionesDetalleHelper DevolucionesDetalleH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
         lblUsuarioHeader = (TextView) header.findViewById(R.id.UsuarioHeader);
        lblUsuarioHeaderCodigo = (TextView) header.findViewById(R.id.UsuarioHeaderCodigo);
        lblVersion =(TextView) header.findViewById(R.id.lblVersionSistema);
        lblServidor = (TextView) header.findViewById(R.id.lblServidor);
        String userHeader="";
        String userHeaderCodigo="";
        String VersionSistema="";
        String Servidor="";
      try{
          userHeader = variables_publicas.usuario.getNombre();
          userHeaderCodigo = variables_publicas.usuario.getCodigo();
          VersionSistema = "Version: " +variables_publicas.VersionSistema;
          Servidor = variables_publicas.direccionIp.equals("http://186.1.18.75:8080")? "SERVIDOR: PRODUCCION" : "SERVIDOR: DESARROLLO";
      }catch (Exception ex){
          Log.e("Error:",ex.getMessage());
      }
        lblUsuarioHeader.setText(userHeader);
        lblUsuarioHeaderCodigo.setText("Codigo: "+ userHeaderCodigo);
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
        FormaPagoH = new FormaPagoHelper(DbOpenHelper.database);
        PrecioEspecialH = new PrecioEspecialHelper(DbOpenHelper.database);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);
        UsuariosH= new UsuariosHelper(DbOpenHelper.database);
        ConsolidadoCargaH = new ConsolidadoCargaHelper(DbOpenHelper.database);
        ConsolidadoCargaDetalleH = new ConsolidadoCargaDetalleHelper(DbOpenHelper.database);
        PedidoH = new PedidosHelper(DbOpenHelper.database);
        PedidoDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        DevolucionesH = new DevolucionesHelper(DbOpenHelper.database);
        DevolucionesDetalleH = new DevolucionesDetalleHelper(DbOpenHelper.database);

        sd = new SincronizarDatos(DbOpenHelper, ClientesH, VendedoresH, CartillasBcH,
                CartillasBcDetalleH,
                FormaPagoH,
                PrecioEspecialH, ConfigH, ClientesSucH, ArticulosH,UsuariosH, ConsolidadoCargaH,ConsolidadoCargaDetalleH,PedidoH,PedidoDetalleH,DevolucionesH,DevolucionesDetalleH);

        try{
            variables_publicas.info="***** Usuario: "+variables_publicas.usuario.getNombre() + " / IMEI: "+(variables_publicas.IMEI ==null? "null" : variables_publicas.IMEI )+ " / VersionSistema: "+ variables_publicas.VersionSistema + " ******** ";
        }catch (Exception ex){
            Log.e("error",ex.getMessage());
            ex.printStackTrace();
        }

        if( variables_publicas.usuario.getTipo().equalsIgnoreCase("Vehiculo")){
            navigationView.getMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(1).getSubMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(1).getSubMenu().getItem(1).setEnabled(false);
            navigationView.getMenu().getItem(2).getSubMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(4).setEnabled(false);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(5).getSubMenu().getItem(1).setEnabled(false);

        }else
        {
            navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setEnabled(false);
            navigationView.getMenu().getItem(4).getSubMenu().getItem(1).setEnabled(false);
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
                if(variables_publicas.TipoUsuario.equalsIgnoreCase("Vehiculo"))
                {
                    sd.SincronizarDevoluciones();
                }
                else {sd.SincronizarTodo();}
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

       try{
           int id = item.getItemId();
           if (id == R.id.SincronizarDatos) {
               //Esto sirve para permitir realizar conexion a internet en el Hilo principal
               StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
               StrictMode.setThreadPolicy(policy);
               boolean isOnline =Funciones.TestServerConectivity();
               if(isOnline){
                   if (Build.VERSION.SDK_INT >= 11) {
                       //--post GB use serial executor by default --
                       new SincronizaDatos().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                   } else {
                       //--GB uses ThreadPoolExecutor by default--
                       new SincronizaDatos().execute();
                   }
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
       }catch (Exception e){
           Funciones.MensajeAviso(getApplicationContext(), e.getMessage());
       }
       finally {
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
            case R.id.btnMapa:
                fragmentManager.executePendingTransactions();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.content_frame, new MapViewFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.btnMaestroClientes:
                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ClientesFragment());
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
            case R.id.btnNuevaDevoluciones:

                Intent newActi = new Intent(getApplicationContext(), DevolucionesActivity.class);
                startActivity(newActi);

              /*  Intent newAct = new Intent(getApplicationContext(), ControladorSincronizacion.class);
                startActivity(newAct);
                Intent newAct = new Intent(getApplicationContext(), AndroidJSONParsingActivity.class);
                startActivity(newAct);*/
                break;
            case R.id.btnListaDevoluciones:

                fragmentManager.executePendingTransactions();
                tran = getFragmentManager().beginTransaction();
                tran.add(R.id.content_frame, new ListaDevolucionesFragment());
                tran.addToBackStack(null);
                tran.commit();

                break;
            case R.id.btnNuevoPedido:
               /* Intent newAct = new Intent(getApplicationContext(), ControladorSincronizacion.class);
                startActivity(newAct);
                //Para pruebas
                Intent newActi = new Intent(getApplicationContext(), ConsultaArticulosActivity.class);
                startActivity(newActi);
*/
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
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void removeAllFragments(FragmentManager fragmentManager) {

        if(fragmentManager.getBackStackEntryCount() > 0 || getSupportFragmentManager().getBackStackEntryCount()>0) {

            while (fragmentManager.getBackStackEntryCount() > 0) {
                fragmentManager.popBackStackImmediate();
            }

            while (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStackImmediate();
            }

        }else{
               new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Confirmación requerida")
                    .setMessage("Está seguro que desea salir de la aplicación?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener()
                    {
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
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(MenuActivity.this);
    }

}
