package com.saf.sistemas.safmyd.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
//import android.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import com.saf.sistemas.safmyd.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Clientes.ListaVentasHistClientes;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.R;
/**
 * Created by Sistemas on 8/2/2018.
 */

public class HistoricoventasClienteFragment extends Fragment{
    View myView;
    private String TAG = HistoricoventasClienteFragment.class.getSimpleName();
    private String busqueda = "";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private Spinner cboMeses;
    private ClientesHelper ClienteH;
    private DataBaseOpenHelper DbOpenHelper;
    public static ArrayList<HashMap<String, String>> listaVentas;
    private String dias="30";
    private static String url = variables_publicas.direccionIp + "/ServicioClientes.svc/BuscarClientes/";
    public static ArrayList<HashMap<String, String>> listaClientes;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.historicoclientesmeses, container, false);
        getActivity().setTitle("Histórico Ventas Cliente");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        ClienteH = new ClientesHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.list);
        registerForContextMenu(lv);

        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText) myView.findViewById(R.id.txtBusqueda);

        listaVentas = new ArrayList<>();

        cboMeses = (Spinner) myView.findViewById(R.id.cboMeses);

        String[] valores = {"1 Mes","2 Meses","3 Meses","4 Meses","5 Meses","6 Meses"};

        cboMeses.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, valores));

        cboMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                String text = cboMeses.getSelectedItem().toString();
                if (text.equals("1 Mes")){
                    dias="30";
                }else if (text.equals("2 Meses")){
                    dias="60";
                }else if (text.equals("3 Meses")) {
                    dias = "90";
                }else if (text.equals("4 Meses")) {
                    dias = "120";
                }else if (text.equals("5 Meses")) {
                    dias = "150";
                }else if (text.equals("6 Meses")) {
                    dias = "180";
                }
                // btnBuscar.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        listaClientes = new ArrayList<>();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getActivity().getApplicationContext(), ListaVentasHistClientes.class);

                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                //in.putExtra(variables_publicas.CLIENTES_COLUMN_CodCv, CodCV );
                in.putExtra(variables_publicas.diasventas, dias );
                in.putExtra(variables_publicas.descMeses,cboMeses.getSelectedItem().toString());

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
                lblFooter.setText("Cliente Encontrados: " + String.valueOf(listaClientes.size()));
            }
                /*InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";

                if (TextUtils.isEmpty(busqueda)) {
                    txtBusqueda.setError("Ingrese un valor");
                    return;
                }
                GetVentasService();
                lblFooter.setText("Artículos Encontrados: " + String.valueOf(listaVentas.size()));
            }*/
        });
        return myView;
    }
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
            String urlString = url + busqueda.replace(" ", "%20") + "/"  + variables_publicas.usuario.getCodigo()  + "/" + tipoBusqueda;
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    listaClientes = new ArrayList<>();
                    // Getting JSON Array node
                    JSONArray clientes = jsonObj.getJSONArray("BuscarClientesResult");

                    // looping through All Contacts
                    for (int i = 0; i < clientes.length(); i++) {
                        JSONObject c = clientes.getJSONObject(i);

                        HashMap<String, String> cliente = new HashMap<>();
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Cedula, c.getString("Cedula"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ciudad, c.getString("Ciudad"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_CodigoLetra, c.getString("CodigoLetra"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Descuento, c.getString("Descuento"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Direccion, c.getString("Direccion"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Empleado, c.getString("Empleado"));
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
                        cliente.put(variables_publicas.CLIENTES_COLUMN_PrecioEspecial, c.getString("PrecioEspecial"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruc, c.getString("Ruc"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Ruta, c.getString("Ruta"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Telefono, c.getString("Telefono"));
                        cliente.put(variables_publicas.CLIENTES_COLUMN_Tipo, c.getString("Tipo"));
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
                    R.layout.list_cliente, new String[]{variables_publicas.CLIENTES_COLUMN_IdCliente, "Nombre", "Ciudad",variables_publicas.CLIENTES_COLUMN_Direccion}, new int[]{R.id.IdCliente,  R.id.Nombre,
                    R.id.Ciudad,R.id.Direccion});

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