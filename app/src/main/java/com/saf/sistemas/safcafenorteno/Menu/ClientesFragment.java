package com.saf.sistemas.safcafenorteno.Menu;

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
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.saf.sistemas.safcafenorteno.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Clientes.ClientesNew;
import com.saf.sistemas.safcafenorteno.HttpHandler;
import com.saf.sistemas.safcafenorteno.Pedidos.PedidosActivity;
import com.saf.sistemas.safcafenorteno.R;

/**
 * Created by usuario on 20/3/2017.
 */

public class ClientesFragment extends Fragment {
    View myView;
    private String TAG = ClientesFragment.class.getSimpleName();
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
        getActivity().setTitle("Listado Clientes");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        ClienteH = new ClientesHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.list);
        registerForContextMenu(lv);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText) myView.findViewById(R.id.txtBusqueda);

        listaClientes = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getActivity().getApplicationContext(), PedidosActivity.class);

                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);

                /*Guardamos el cliente seleccionado*/
                for (HashMap<String, String> cliente : listaClientes) {
                    if (cliente.get(variables_publicas.CLIENTES_COLUMN_IdCliente).equals(IdCliente) ) {
                        ClienteH.EliminaCliente(IdCliente);
                        ClienteH.GuardarTotalClientes(cliente);

                    }
                }

                startActivity(in);
            }
        });
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
                lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));
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

            String Codigo = obj.get("IdCliente");
            String nombre = obj.get("Nombre");

            String HeaderMenu = "Cliente: "+ Codigo + "\n" + nombre;

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.clientes_list_menu_context, menu);
            MenuItem tv = menu.getItem(0); //Boton Editar

            if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")) {
                tv.setEnabled(false);
            }
            else {
                tv.setEnabled(true);
            }


        } catch (Exception e) {
            //mensajeAviso(e.getMessage());
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> clientes = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemEditarCliente:{

                   // busqueda = txtBusqueda.getText().toString();
                     //Editar
                    HashMap<String, String> obj = listaClientes.get(info.position);
                    String Codigo = obj.get("IdCliente");
                    //String vValorFiltro = ClienteH.ObtenerDescripcion(variables_publicas.CLIENTES_COLUMN_NombreCliente,variables_publicas.TABLE_CLIENTES,variables_publicas.CLIENTES_COLUMN_IdCliente,Codigo);
                    clientes = ClienteH.ObtenerClienteGuardado(Codigo);
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
                    // Starting new intent
                    variables_publicas.vEditando= true;
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
    private static String url = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes/";
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
            String urlString = url + busqueda.replace(" ", "%20") + "/" + variables_publicas.usuario.getCodigo()  + "/" +  tipoBusqueda;
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    listaClientes = new ArrayList<>();
                    // Getting JSON Array node
                    JSONArray clientes = jsonObj.getJSONArray("BuscarClientesResult");

                    HashMap<String, String> client = null;
                    // looping through All Contacts
                    for (int i = 0; i < clientes.length(); i++) {
                        JSONObject c = clientes.getJSONObject(i);

                        HashMap<String, String> cliente = new HashMap<>();
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdCliente, c.getString("IdCliente"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Nombre, c.getString("Nombre"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_FechaCreacion, c.getString("FechaCreacion"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString("Telefono"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString("Direccion"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdDepartamento, c.getString("IdDepartamento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdMunicipio, c.getString("IdMunicipio"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString("Ciudad"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString("Ruc"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString("Cedula"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_LimiteCredito, c.getString("LimiteCredito"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdFormaPago, c.getString("IdFormaPago"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdVendedor, c.getString("IdVendedor"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Excento, c.getString("Excento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString("CodigoLetra"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString("Ruta"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_NombreRuta, c.getString("NombreRuta"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Frecuencia, c.getString("Frecuencia"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString("PrecioEspecial"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_FechaUltimaCompra, c.getString("FechaUltimaCompra"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString("Tipo"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_TipoPrecio, c.getString("TipoPrecio"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString("Descuento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString("Empleado"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdSupervisor, c.getString("IdSupervisor"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Empresa, c.getString("EMPRESA"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_Zona, c.getString("COD_ZONA"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Cod_SubZona, c.getString("COD_SUBZONA"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Id, c.getString("Pais_Id"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Pais_Nombre, c.getString("Pais_Nombre"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_IdTipoNegocio, c.getString("IdTipoNegocio"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_TipoNegocio, c.getString("TipoNegocio"));

                        listaClientes.add(cliente);

                        if (variables_publicas.usuario.getTipo().equals("Supervisor")||variables_publicas.usuario.getTipo().equals("User")) {
                            String Codigo;
                            Codigo = c.getString("IdCliente");

                            client = ClienteH.ObtenerClienteGuardado(Codigo);
                            if (client == null) {
                                DbOpenHelper.database.beginTransaction();
                                ClienteH.GuardarClientes(c.getString("IdCliente"), c.getString("Nombre"), c.getString("FechaCreacion"),
                                        c.getString("Telefono"), c.getString("Direccion"), c.getString("IdDepartamento"), c.getString("IdMunicipio"), c.getString("Ciudad"), c.getString("Ruc"), c.getString("Cedula"), c.getString("LimiteCredito"),
                                        c.getString("IdFormaPago"), c.getString("IdVendedor"), c.getString("Excento"), c.getString("CodigoLetra"), c.getString("Ruta"),c.getString("NombreRuta"), c.getString("Frecuencia"), c.getString("PrecioEspecial"), c.getString("FechaUltimaCompra"),
                                        c.getString("Tipo"),c.getString("TipoPrecio"), c.getString("Descuento"), c.getString("Empleado"), c.getString("IdSupervisor"),c.getString("EMPRESA"),
                                        c.getString("COD_ZONA"), c.getString("COD_SUBZONA"),c.getString("Pais_Id"),c.getString("Pais_Nombre"), c.getString("IdTipoNegocio"),c.getString("TipoNegocio"));
                                DbOpenHelper.database.setTransactionSuccessful();
                                DbOpenHelper.database.endTransaction();
                            }
                        }
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
            lblFooter.setText("Clientes Encontrado: " + String.valueOf(listaClientes.size()));
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
