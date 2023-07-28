package com.saf.sistemas.safmyd.Menu;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.sistemas.safmyd.AccesoDatos.ArticulosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Clientes.ClientesNew;
import com.saf.sistemas.safmyd.Entidades.MotivosNoVenta;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.Pedidos.FacturasActivity;
import com.saf.sistemas.safmyd.Pedidos.FacturasActivityNew;
import com.saf.sistemas.safmyd.R;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

public class FacturasFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private boolean guardadoOK = true;
    private ClientesHelper ClientesH;
    private String TAG = FacturasFragment.class.getSimpleName();
    private String busqueda = "";
    private String tipoBusqueda = "2";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private  String ClienteId;
    private  String vLatitud;
    private  String vLongitud;
    private String vObservacion="";
    private String Longitud="";
    private String Latitud="";
    private TextView tvVisita;
    private Spinner cboDiaVisita;
    public static ArrayList<HashMap<String, String>> listaClientes;
      private String diaseleccionado="LUNES";
    private String vMotivoNoVenta = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.masterclientes_layout,container,false);
        getActivity().setTitle("Nueva Factura");
        lv = myView.findViewById(R.id.list);
        registerForContextMenu(lv);
        btnBuscar =  myView.findViewById(R.id.btnBuscar);
        lblFooter = myView.findViewById(R.id.lblFooter);
        rgGrupo = myView.findViewById(R.id.rgGrupo);
        cboDiaVisita = myView.findViewById(R.id.cboDiasVisita);
        String dSemana= Funciones.getDayOfWeek(Calendar.getInstance().getTime());
        diaseleccionado=dSemana;
        //Combo Dias de Visita
        String[] valores = {"LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SABADO", "DOMINGO"};
        cboDiaVisita.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, valores));
        cboDiaVisita.setSelection(getIndex(cboDiaVisita, dSemana));

        cboDiaVisita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                diaseleccionado = adapter.getItemAtPosition(position).toString();
                btnBuscar.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        rgGrupo.setOnCheckedChangeListener((group, checkedId) -> {
            if(group.getCheckedRadioButtonId()== R.id.rbCodigo){
                txtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            else {
                txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        });
        txtBusqueda = myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                btnBuscar.performClick();
            }
            return false;
        });
        listaClientes = new ArrayList<>();

/*        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetClientesFacturas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new GetClientesFacturas().execute();
        }*/

        lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));

        btnBuscar.setOnClickListener(v -> {

            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
            busqueda = txtBusqueda.getText().toString();
            tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";
            if (Build.VERSION.SDK_INT >= 11) {
                //--post GB use serial executor by default --
                new GetClientesFacturas().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                //--GB uses ThreadPoolExecutor by default--
                new GetClientesFacturas().execute();
            }

            lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));
        });
        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener((parent, view, position, id) -> {
        });

        if(rgGrupo.getCheckedRadioButtonId()== R.id.rbCodigo){
            txtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else {
            txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
        }


        return myView;
    }
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Configuración GPS");
        // Setting Dialog Message
        alertDialog.setMessage("GPS no está habilitado. Favor activarlo");
        // On pressing Settings button
        alertDialog.setPositiveButton("Activar", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            dialog.cancel();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String CodigoCliente = obj.get("IdCliente");
            String nombre = obj.get("Nombre");
            String Codigo;
            Codigo=CodigoCliente;
            String HeaderMenu = "Cliente: "+ Codigo + "\n" + nombre;

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.clientes_list_menu_context, menu);

        } catch (Exception e) {

        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> clientes = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemEditarCliente:{

                    //Editar
                    HashMap<String, String> obj = listaClientes.get(info.position);
                    String CodigoCliente = obj.get("IdCliente");
                    clientes = ClientesH.ObtenerClienteGuardado(CodigoCliente);
                    if (clientes == null) {
                        Funciones.MensajeAviso(getActivity(), "No se ha encontrado Información del Cliente");
                        return true;
                    }

                    String IdCliente = clientes.get("IdCliente");
                    String Nombre = clientes.get("Nombre");
                    String FechaCreacion = clientes.get("FechaCreacion");
                    String Telefono = clientes.get("Telefono");
                    String Direccion = clientes.get("Direccion");
                    String IdDepartamento = clientes.get("IdDepartamento");
                    String IdMunicipio = clientes.get("IdMunicipio");
                    String Ciudad = clientes.get("Ciudad");
                    String Ruc = clientes.get("Ruc");
                    String Cedula = clientes.get("Cedula");
                    String LimiteCredito = clientes.get("LimiteCredito");
                    String IdFormaPago = clientes.get("IdFormaPago");
                    String IdVendedor = clientes.get("IdVendedor");
                    String Excento = clientes.get("Excento");
                    String CodigoLetra = clientes.get("CodigoLetra");
                    String Ruta = clientes.get("Ruta");
                    String NombreRuta = clientes.get("NombreRuta");
                    String Frecuencia = clientes.get("Frecuencia");
                    String PrecioEspecial = clientes.get("PrecioEspecial");
                    String FechaUltimaCompra = clientes.get("FechaUltimaCompra");
                    String Tipo = clientes.get("Tipo");
                    String TipoPrecio = clientes.get("TipoPrecio");
                    String Descuento = clientes.get("Descuento");
                    String Empleado = clientes.get("Empleado");
                    String IdSupervisor = clientes.get("IdSupervisor");
                    String Empresa = clientes.get("Empresa");
                    String Cod_Zona = clientes.get("Cod_Zona");
                    String Cod_SubZona = clientes.get("Cod_SubZona");
                    String Pais_Id = clientes.get("Pais_Id");
                    String Pais_Nombre = clientes.get("Pais_Nombre");
                    String IdTipoNegocio = clientes.get("IdTipoNegocio");
                    String TipoNegocio = clientes.get("TipoNegocio");
                    String Saldo = clientes.get("Saldo");
                    String NoEnRuta = clientes.get("NoEnRuta");
                    String Lat = clientes.get("Latitud");
                    String Lng = clientes.get("Longitud");
                    String Visita = clientes.get("Visita");

                    Intent in = new Intent(getActivity().getApplicationContext(), ClientesNew.class);

                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_FechaCreacion, FechaCreacion);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Telefono, Telefono);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Direccion, Direccion);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdDepartamento, IdDepartamento);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdMunicipio, IdMunicipio);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Ciudad, Ciudad);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Ruc, Ruc);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Cedula, Cedula);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_LimiteCredito, LimiteCredito);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdFormaPago, IdFormaPago);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdVendedor, IdVendedor);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Excento, Excento);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_CodigoLetra, CodigoLetra);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Ruta, Ruta);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_NombreRuta, NombreRuta);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia, Frecuencia);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, PrecioEspecial);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, FechaUltimaCompra);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Tipo, Tipo);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio, TipoPrecio);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Descuento, Descuento);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Empleado, Empleado);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdSupervisor, IdSupervisor);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Empresa, Empresa);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Cod_Zona, Cod_Zona);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, Cod_SubZona);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Pais_Id, Pais_Id);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, Pais_Nombre);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, IdTipoNegocio);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio, TipoNegocio);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Saldo, Saldo);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_NoEnRuta, NoEnRuta);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Lat, Lat);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Lng, Lng);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Visita, Visita);
                    // Starting new intent
                    variables_publicas.vEditando= true;
                    startActivity(in);
                    return true;
                }
                case R.id.itemGeolocalizacion:{
                    HashMap<String, String> obj = listaClientes.get(info.position);
                    String vCliente = obj.get("IdCliente");
                    String vNombre = obj.get("Nombre");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Location location = null;
                        LocationListener mlocListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                            }
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        };

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                        if (locationManager != null) {
                            //Existe GPS_PROVIDER obtiene ubicación
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }

                        if(location == null){ //Trata con NETWORK_PROVIDER
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                            if (locationManager != null) {
                                //Existe NETWORK_PROVIDER obtiene ubicación
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                        }
                        if(location != null) {
                            Latitud = String.valueOf(location.getLatitude());
                            Longitud = String.valueOf(location.getLongitude());
                        }else {//Volvemos a preguntar por una segunda ocacion hasta encontrar la ultima ubicacion
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                            if (locationManager != null) {
                                //Existe GPS_PROVIDER obtiene ubicación
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }

                            if(location == null){ //Trata con NETWORK_PROVIDER
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                                if (locationManager != null) {
                                    //Existe NETWORK_PROVIDER obtiene ubicación
                                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }
                            }
                            if(location != null) {
                                Latitud = String.valueOf(location.getLatitude());
                                Longitud = String.valueOf(location.getLongitude());
                            }else {
                                Toast.makeText(getActivity(), "No se pudo obtener geolocalización", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    MostrarMensajeGuardar(vCliente,vNombre,Latitud,Longitud);
                    return true;
                }
                case R.id.itemVenta:{
                    // getting values from selected ListItem
                    HashMap<String, String> obj = listaClientes.get(info.position);
                    String IdCliente = obj.get("IdCliente");
                    String Nombre = obj.get("Nombre");

                    // Starting new intent
                    Intent in = new Intent(getActivity().getApplicationContext(), FacturasActivityNew.class);

                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente );
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre );
                    in.putExtra(variables_publicas.vVisualizar,"False");
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {

                    }else {
                        LocationManager locManager;
                        boolean isGPSEnabled = false;
                        locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!isGPSEnabled){
                            showSettingsAlert();
                            return true;
                        }
                    }
                    startActivity(in);
                    return true;
                }
                case R.id.itemVisita:{
                    HashMap<String, String> obj = listaClientes.get(info.position);
                    String vCliente = obj.get("IdCliente");
                    String vNombre = obj.get("Nombre");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        }
                        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                        Location location = null;
                        LocationListener mlocListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                            }
                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                            }
                            @Override
                            public void onProviderEnabled(String provider) {
                            }
                            @Override
                            public void onProviderDisabled(String provider) {
                            }
                        };

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                        if (locationManager != null) {
                            //Existe GPS_PROVIDER obtiene ubicación
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }

                        if(location == null){ //Trata con NETWORK_PROVIDER
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                            if (locationManager != null) {
                                //Existe NETWORK_PROVIDER obtiene ubicación
                                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            }
                        }
                        if(location != null) {
                            Latitud = String.valueOf(location.getLatitude());
                            Longitud = String.valueOf(location.getLongitude());
                        }else {//Volvemos a preguntar por una segunda ocacion hasta encontrar la ultima ubicacion
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
                            if (locationManager != null) {
                                //Existe GPS_PROVIDER obtiene ubicación
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            }

                            if(location == null){ //Trata con NETWORK_PROVIDER
                                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlocListener);
                                if (locationManager != null) {
                                    //Existe NETWORK_PROVIDER obtiene ubicación
                                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                }
                            }
                            if(location != null) {
                                Latitud = String.valueOf(location.getLatitude());
                                Longitud = String.valueOf(location.getLongitude());
                            }else {
                                Toast.makeText(getActivity(), "No se pudo obtener geolocalización", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                    MostrarMensajeGuardar2(vCliente,vNombre,Latitud,Longitud);
                    return  true;
                }
                default:
                    return super.onContextItemSelected(item);
            }
        } catch (Exception e) {
            //mensajeAviso(e.getMessage());
        }
        return false;
    }
    public void MostrarMensajeGuardar( String valorcodigocliente,String valornombrecliente,final String valorLat,final String valorLng) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = null;
        dialogBuilder.setCancelable(false);
        dialogView = inflater.inflate(R.layout.cordenadasactualizar_layout, null);
        Button btnOK = dialogView.findViewById(R.id.btnGuardar);
        Button btnNOK = dialogView.findViewById(R.id.btnCancelar);
        TextView txtCliente = dialogView.findViewById(R.id.txtCliente);
        TextView txtLatitud = dialogView.findViewById(R.id.txtLatitud);
        TextView txtLongitud = dialogView.findViewById(R.id.txtLongitud);
        txtCliente.setText(valornombrecliente);
        txtLatitud.setText(valorLat);
        txtLongitud.setText(valorLng);

        dialogBuilder.setView(dialogView);

        final  AlertDialog alertDialog = dialogBuilder.create();
        btnOK.setOnClickListener(v -> {
            if (txtLatitud.getText().toString().equals("") || txtLatitud.getText().toString().isEmpty() || txtLatitud.equals("0")) {
                MensajeAviso("La Latitud no debe ser vacía o Nula.");
                return;
            }
            if (txtLongitud.getText().toString().equals("") || txtLongitud.getText().toString().isEmpty() || txtLongitud.equals("0")) {
                MensajeAviso("La Longitud no debe ser vacía o Nula.");
                return;
            }
            ClienteId=valorcodigocliente;
            vLatitud=txtLatitud.getText().toString();
            vLongitud=txtLongitud.getText().toString();

            try{

                new ActualizaCoordenada().execute();


            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
            if (guardadoOK) {
                try {
                    alertDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }else{
                MensajeAviso("Hubo un error al actualizar la localización. Intente más tarde.");
                alertDialog.dismiss();
            }
        });

        btnNOK.setOnClickListener(v -> alertDialog.dismiss());


        alertDialog.show();
    }

    public void MostrarMensajeGuardar2( String codigocliente,final String nombrecliente,final String valorLat,final String valorLng) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = null;
        dialogBuilder.setCancelable(false);
        dialogView = inflater.inflate(R.layout.clientenoventa, null);
        Button btnOK = dialogView.findViewById(R.id.btnGuardar);
        Button btnNOK = dialogView.findViewById(R.id.btnCancelar);
        TextView txtNombre = dialogView.findViewById(R.id.txtNombreCliente);
        final Spinner cboMotivo= dialogView.findViewById(R.id.cboMotivoNoVenta);
        EditText txtObservaciones = dialogView.findViewById(R.id.txtObservacion);

        txtNombre.setText(nombrecliente);

        ClienteId=codigocliente;
        vLatitud=valorLat;
        vLongitud=valorLng;

        final List<MotivosNoVenta> CMotivos;
        CMotivos = ClientesH.ObtenerListaMotivosNoVenta();
        ArrayAdapter<MotivosNoVenta> adapterMotivos = new ArrayAdapter<MotivosNoVenta>(getActivity(), android.R.layout.simple_spinner_item, CMotivos);
        adapterMotivos.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboMotivo.setAdapter(adapterMotivos);
        cboMotivo.setSelection(0);

        cboMotivo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                MotivosNoVenta mSelected = (MotivosNoVenta) adapter.getItemAtPosition(position);
                vMotivoNoVenta=mSelected.getCodigo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        dialogBuilder.setView(dialogView);

        final  AlertDialog alertDialog = dialogBuilder.create();

        btnOK.setOnClickListener(v -> {
            try{
                vObservacion=txtObservaciones.getText().toString().equals("") ? "-" : txtObservaciones.getText().toString();
                new ActualizaMotivoNoVenta().execute();


            }catch (Exception e){
                Log.e("Error",e.getMessage());
            }
            if (guardadoOK) {
                try {

                    btnBuscar.performClick();
                    alertDialog.dismiss();
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
            }else{
                MensajeAviso("Hubo un error al actualizar el motivo No Venta. Intente más tarde.");
                alertDialog.dismiss();
            }
        });

        btnNOK.setOnClickListener(v -> alertDialog.dismiss());


        alertDialog.show();
    }

    private class ActualizaMotivoNoVenta extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            final String url = variables_publicas.direccionIp + "/ServicioClientes.svc/ActualizarMotivosNoVenta/" + ClienteId + "/" + variables_publicas.usuario.getEmpresa_ID() + "/" + vMotivoNoVenta + "/" +variables_publicas.usuario.getCodigo() + "/" + variables_publicas.rutacargada + "/" + vObservacion + "/" + vLatitud + "/" + vLongitud;

            String urlString = url;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCall(encodeUrl);

            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    String resultState = ((String) result.get("ActualizarMotivosNoVentaResult")).split(",")[0];
                    final String mensaje = ((String) result.get("ActualizarMotivosNoVentaResult")).split(",")[1];
                    if (resultState.equals("false")) {
                        if(getActivity()==null) return null;
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                                mensaje,
                                Toast.LENGTH_LONG).show());
                        guardadoOK = false;
                    } else {
                        guardadoOK = true;
                        DbOpenHelper.database.beginTransaction();
                        ClientesH.ActualizarVisita(ClienteId,"NoCompra");
                        DbOpenHelper.database.setTransactionSuccessful();
                        DbOpenHelper.database.endTransaction();
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al actualizar la Visita. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com.ni", variables_publicas.correosErrores);
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(() -> {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor",
                                Toast.LENGTH_LONG).show();
                        //  }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al actualizar la visita. Respuesta nula GET", variables_publicas.info + urlStr, "dlunasistemas@gmail.com.ni", variables_publicas.correosErrores);
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                        "No es posible conectarse al servidor.",
                        Toast.LENGTH_LONG).show());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, (dialog, whichButton) -> {
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private class ActualizaCoordenada extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();

            final String url = variables_publicas.direccionIp + "/ServicioClientes.svc/ActualizarClienteCoordenada/" + ClienteId + "/" + variables_publicas.usuario.getEmpresa_ID() + "/" + variables_publicas.usuario.getUsuario() + "/" + vLatitud + "/" + vLongitud;

            String urlString = url;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCall(encodeUrl);

            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    String resultState = ((String) result.get("ActualizarClienteCoordenadaResult")).split(",")[0];
                    final String mensaje = ((String) result.get("ActualizarClienteCoordenadaResult")).split(",")[1];
                    if (resultState.equals("false")) {
                        if(getActivity()==null) return null;
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                                mensaje,
                                Toast.LENGTH_LONG).show());
                        guardadoOK = false;
                    } else {
                        guardadoOK = true;
                        DbOpenHelper.database.beginTransaction();
                        ClientesH.ActualizarCoordenadas(ClienteId,vLatitud,vLongitud);
                        DbOpenHelper.database.setTransactionSuccessful();
                        DbOpenHelper.database.endTransaction();
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al actualizar la Geolocalización. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com.ni", variables_publicas.correosErrores);
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(() -> {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor",
                                Toast.LENGTH_LONG).show();
                        //  }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al actualizar la Geolocalización. Respuesta nula GET", variables_publicas.info + urlStr, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                        "No es posible conectarse al servidor.",
                        Toast.LENGTH_LONG).show());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    //
    private class GetClientesFacturas extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if(getActivity().isFinishing()) return null;
                DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                ClientesH = new ClientesHelper(DbOpenHelper.database);
                switch (tipoBusqueda){
                    case "1":
                        listaClientes=ClientesH.BuscarClientesCodigoDia(busqueda,diaseleccionado);
                        break;
                    case  "2":
                        listaClientes=ClientesH.BuscarClientesNombreDia(busqueda,diaseleccionado);
                        break;
                }
            } catch (final Exception e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG)
                        .show());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try{
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                /**
                 * Updating parsed JSON data into ListView
                 * */
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), listaClientes,
                        R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente,"Ciudad", "Nombre", variables_publicas.CLIENTES_COLUMN_Direccion,"Saldo","Visita"}, new int[]{R.id.IdCliente,R.id.Ciudad, R.id.Nombre,
                        R.id.Direccion,R.id.Saldo,R.id.Visita}){
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View currView = super.getView(position, convertView, parent);
                        HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                        tvVisita = currView.findViewById(R.id.tvVisita);
                        if (currItem.get("Visita").equalsIgnoreCase("Compra")) {
                            tvVisita.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                        } else if (currItem.get("Visita").equalsIgnoreCase("NoCompra")) {
                            tvVisita.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                        }else {
                            tvVisita.setBackground(getResources().getDrawable(R.drawable.rounded_corner_blue));
                        }
                        return currView;
                    }
                };
                if(adapter!=null){
                    lv.setAdapter(adapter);
                }
                lblFooter.setText("Clientes Encontrados: " + String.valueOf(listaClientes.size()));
            }catch (final Exception ex){
                if(getActivity()==null) return ;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                                "error: " + ex.getMessage(),
                                Toast.LENGTH_LONG)
                        .show());
            }
        }
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
