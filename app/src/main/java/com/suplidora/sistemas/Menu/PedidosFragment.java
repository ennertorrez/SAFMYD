package com.suplidora.sistemas.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.HttpHandler;
import com.suplidora.sistemas.Pedidos.PedidosActivity;
import com.suplidora.sistemas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by usuario on 20/3/2017.
 */

public class PedidosFragment extends Fragment {
    View myView;

    private String TAG = PedidosFragment.class.getSimpleName();
    private String busqueda = "1";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.masterclientes_layout,container,false);
        getActivity().setTitle("Maestro Clientes");
        lv = (ListView) myView.findViewById(R.id.list);
        registerForContextMenu(lv);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
        listaClientes = new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";

                if(TextUtils.isEmpty(busqueda)) {
                    txtBusqueda.setError("Ingrese un valor");
                    return;
                }
                new GetClientesPedidos().execute();
                lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));
            }
        });
        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
               String IdCliente = ((TextView) view.findViewById(R.id.IdCliente)).getText().toString();
//                String CodCv = ((TextView) view.findViewById(R.id.CodCv)).getText().toString();
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
//                String lblRuta = ((TextView) view.findViewById(R.id.lblRuta)).getText().toString();
                //String lblCanal = ((TextView) view.findViewById(R.id.lblCanal)).getText().toString();
                //String CboVendedor = ((Spinner) view.findViewById(R.id.cboVendedor)).getSelectedItem().toString();

//                String IdCliente = variables_publicas.IdCliente;
//                String Ruta = variables_publicas.Ruta;

                // Starting new intent
                Intent in = new Intent(getActivity().getApplicationContext(), PedidosActivity.class);

                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente );
////                in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, CodCv );
               in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre );
//                in.putExtra(variables_publicas.CLIENTES_COLUMN_Ruta, Ruta );
                //in.putExtra(variables_publicas.CLIENTES_COLUMN_Ca, lblCanal );
                startActivity(in);

            }
        });
        return myView;
    }
    private static String url = "http://186.1.18.75:8080/ServicioClientes.svc/BuscarClientes/";
    public static ArrayList<HashMap<String, String>> listaClientes;
    private ArticulosHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private class GetClientesPedidos extends AsyncTask<Void, Void, Void> {
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
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String urlString = url + busqueda.replace(" ","%20") + "/" + tipoBusqueda;
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

                       String IdCliente = c.getString("IdCliente");
                        String CodCv = c.getString("CodCv");
                        String Nombre = c.getString("Nombre");
                        String FechaIngreso = c.getString("FechaIngreso");
                        String ClienteNuevo = c.getString("ClienteNuevo");
                       String Ruta = c.getString("Ruta");
                        String Direccion = c.getString("Direccion");
                        String Cedula = c.getString("Cedula");
                        String IdVendedor = c.getString("IdVendedor");
                        String Vendedor = c.getString("Vendedor");
                        String IdSupervisor = c.getString("IdSupervisor");
                        String Supervisor = c.getString("Supervisor");
                        String Subruta = c.getString("Subruta");
                        String FechaUltimaCompra = c.getString("FechaUltimaCompra");

                        HashMap<String, String> cliente = new HashMap<>();

                        // adding each child node to HashMap key => value
                        cliente.put("Nombre",Nombre);
                        cliente.put("IdCliente",IdCliente);
                        cliente.put("CodCv",CodCv);
                        cliente.put("Direccion",Direccion);
                        listaClientes.add(cliente);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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
                    R.layout.list_cliente, new String[]{"IdCliente", "Nombre", "Direccion"}, new int[]{R.id.IdCliente, R.id.Nombre,
                    R.id.Direccion});
            lv.setAdapter(adapter);
            lblFooter.setText("Clientes Encontrados encontrados: " + String.valueOf(listaClientes.size()));
        }
    }
}
