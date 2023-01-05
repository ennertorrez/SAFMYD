package com.saf.sistemas.safasuncion.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saf.sistemas.safasuncion.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safasuncion.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;
import com.saf.sistemas.safasuncion.Clientes.ClientesInactivosEdit;
import com.saf.sistemas.safasuncion.HttpHandler;
import com.saf.sistemas.safasuncion.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import android.widget.Toolbar;

/**
 * Created by usuario on 20/3/2017.
 */

public class ClientesInactivosFragment extends Fragment {
    View myView;
    private String TAG = ClientesInactivosFragment.class.getSimpleName();
    private String busqueda = "1";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private ClientesHelper ClienteH;
    private DataBaseOpenHelper DbOpenHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.masterclientes_layout, container, false);
        getActivity().setTitle("Activar Clientes");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        ClienteH = new ClientesHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.list);
        registerForContextMenu(lv);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText) myView.findViewById(R.id.txtBusqueda);

        listaClientes = new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";

                if (TextUtils.isEmpty(busqueda)) {
                    txtBusqueda.setError("Ingrese un valor");
                    return;
                }
                new GetClientes().execute();
                lblFooter.setText("Clientes Encontrados: " + String.valueOf(listaClientes.size()));
            }
        });
        return myView;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String CodigoCliente = obj.get("IdCliente");
            String CodigoCV= obj.get("CodCv");
            String nombre = obj.get("NombreCliente");
            String Codigo;
            if (CodigoCV.equals("") || CodigoCV.isEmpty()){
                Codigo=CodigoCliente;
            }else {
                Codigo=CodigoCV;
            }

            String HeaderMenu = "Cliente: "+ Codigo + "\n" + nombre;

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.clientes_list_menu_context, menu);
            MenuItem tv = menu.getItem(0); //Boton Editar


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
                    String CodigoCV= obj.get("CodCv");
                    String Codigo;
                    if (CodigoCV.equals("") || CodigoCV.isEmpty()){
                        Codigo=CodigoCliente;
                    }else {
                        Codigo=CodigoCV;
                    }

                    String IdCliente = obj.get("IdCliente");
                    String CodCv = obj.get("CodCv");
                    String Nombre = obj.get("Nombre");
                    String NombreCliente = obj.get("NombreCliente");
                    String FechaCreacion = obj.get("FechaCreacion");
                    String Telefono = obj.get("Telefono");
                    String Direccion = obj.get("Direccion");
                    String IdDepartamento = obj.get("IdDepartamento");
                    String IdMunicipio = obj.get("IdMunicipio");
                    String Ciudad = obj.get("Ciudad");
                    String Ruc = obj.get("Ruc");
                    String Cedula = obj.get("Cedula");
                    String LimiteCredito = obj.get("LimiteCredito");
                    String IdFormaPago = obj.get("IdFormaPago");
                    String IdVendedor = obj.get("IdVendedor");
                    String Excento = obj.get("Excento");
                    String CodigoLetra = obj.get("CodigoLetra");
                    String Ruta = obj.get("Ruta");
                    String Frecuencia = obj.get("Frecuencia");
                    String PrecioEspecial = obj.get("PrecioEspecial");
                    String FechaUltimaCompra = obj.get("FechaUltimaCompra");
                    String Tipo = obj.get("Tipo");
                    String CodigoGalatea = obj.get("CodigoGalatea");
                    String Descuento = obj.get("Descuento");
                    String Empleado = obj.get("Empleado");
                    String Detallista = obj.get("Detallista");
                    String RutaForanea = obj.get("RutaForanea");
                    String EsClienteVarios = obj.get("EsClienteVarios");
                    String IdBarrio = obj.get("IdBarrio");
                    String TipoNegocio = obj.get("TipoNegocio");

                    Intent in = new Intent(getActivity().getApplicationContext(), ClientesInactivosEdit.class);

                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_CodCv, CodCv);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_NombreCliente, NombreCliente);
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
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Frecuencia, Frecuencia);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, PrecioEspecial);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, FechaUltimaCompra);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Tipo, Tipo);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, CodigoGalatea);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Descuento, Descuento);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Empleado, Empleado);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_Detallista, Detallista);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_RutaForanea, RutaForanea);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, EsClienteVarios);
                   // in.putExtra(variables_publicas.CLIENTES_COLUMN_IdBarrio, IdBarrio);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_TipoNegocio, TipoNegocio);
                    // Starting new intent
                    startActivity(in);
                    return true;
                }
                default:
                    return super.onContextItemSelected(item);
            }
        } catch (Exception e) {
            //mensajeAviso(e.getMessage());
        }
        return false;
    }

    // URL to get contacts JSON
    private static String url = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientesInactivos/";
    public static ArrayList<HashMap<String, String>> listaClientes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class GetClientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            if(getActivity()==null) return null;
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String urlString = url + busqueda.replace(" ", "%20") + "/" + tipoBusqueda;
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    listaClientes = new ArrayList<>();
                    // Getting JSON Array node
                    JSONArray clientes = jsonObj.getJSONArray("BuscarClientesInactivosResult");

                    HashMap<String, String> client = null;
                    // looping through All Contacts
                    for (int i = 0; i < clientes.length(); i++) {
                        JSONObject c = clientes.getJSONObject(i);

                        HashMap<String, String> cliente = new HashMap<>();
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString("Cedula"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString("Ciudad"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_CodCv, c.getString("CodCv"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoGalatea, c.getString("CodigoGalatea"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString("CodigoLetra"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString("Descuento"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_Detallista, c.getString("Detallista"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString("Direccion"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString("Empleado"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_EsClienteVarios, c.getString("EsClienteVarios"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Excento, c.getString("Excento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion, c.getString("FechaCreacion"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString("FechaUltimaCompra"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString("Frecuencia"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString("IdCliente"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento, c.getString("IdDepartamento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago, c.getString("IdFormaPago"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio, c.getString("IdMunicipio"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString("IdVendedor"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito, c.getString("LimiteCredito"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString("Nombre"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_NombreCliente, c.getString("NombreCliente"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString("PrecioEspecial"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString("Ruc"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString("Ruta"));
                        //cliente.put(variables_publicas.CLIENTES_COLUMN_RutaForanea, c.getString("RutaForanea"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString("Telefono"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString("Tipo"));
/*
                        if (c.get(variables_publicas.CLIENTES_COLUMN_EsClienteVarios).toString().equalsIgnoreCase("false")) {
                            cliente.put("CodCv2", "");
                            cliente.put("NombreCompleto", c.getString("NombreCliente"));
                        } else {
                            cliente.put("CodCv2","Cod_Cv: "+ c.getString("CodCv"));
                            cliente.put("NombreCompleto",c.getString("Nombre")+ " / " + c.getString("NombreCliente"));
                        }
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdBarrio, c.getString("IdBarrio"));*/
                        cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString("TipoNegocio"));

                        listaClientes.add(cliente);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
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
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listaClientes,
                    R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente, "CodigoLetra", "Nombre", variables_publicas.CLIENTES_COLUMN_Direccion}, new int[]{R.id.IdCliente, R.id.CodLetra, R.id.Nombre,
                    R.id.Direccion});

            lv.setAdapter(adapter);
            lblFooter.setText("Clientes Encontrados: " + String.valueOf(listaClientes.size()));
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //finish();//return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
