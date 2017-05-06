package com.suplidora.sistemas.Principal;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.suplidora.sistemas.AndroidJSONParsingActivity;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.ConsultaArticuloActivity;
import com.suplidora.sistemas.Menu.MapViewFragment;
import com.suplidora.sistemas.Menu.PedidosFragment;
import com.suplidora.sistemas.R;
import com.suplidora.sistemas.Menu.ClientesFragment;
import com.suplidora.sistemas.Menu.MaestroProductoFragment;
/*import com.suplidora.sistemas.app.ControladorArticulo;
import com.suplidora.sistemas.app.ControladorSincronizacion;*/


public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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


       TextView lblUsuarioHeader = (TextView) findViewById(R.id.UsuarioHeader);
        String Userheader = variables_publicas.UsuarioLogin;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Salir) {
            finish();//return true;
        }
        if (id == R.id.CerrarSesion) {
            Intent newAct = new Intent(getApplicationContext(), Login.class);
            startActivity(newAct);//return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        switch (id) {

            case R.id.btnMaestroProductos:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new MaestroProductoFragment())
                        .commit();
                break;
            case R.id.btnMapa:
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, new MapViewFragment())
                        .commit();
                break;
            case R.id.btnClientes:
                fragmentManager.beginTransaction().replace(R.id.content_frame, new ClientesFragment()).commit();
                break;
            case R.id.btnDevoluciones:
                /*Intent newAct = new Intent(getApplicationContext(), ControladorSincronizacion.class);
                startActivity(newAct);*/
                Intent newAct = new Intent(getApplicationContext(), AndroidJSONParsingActivity.class);
                startActivity(newAct);
                break;
            case R.id.btnPedidos:
                /*Intent newAct = new Intent(getApplicationContext(), ControladorSincronizacion.class);
                startActivity(newAct);*/
                //Para pruebas
                Intent newActi = new Intent(getApplicationContext(), ConsultaArticuloActivity.class);
              startActivity(newActi);
                //fragmentManager.beginTransaction().replace(R.id.content_frame, new PedidosFragment()).commit();
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
