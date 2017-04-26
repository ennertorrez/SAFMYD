package com.suplidora.sistemas;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.text.InputType;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by usuario on 20/3/2017.
 */

public class ClientesFragment extends Fragment {
    View myView;
    String [] result;
    int [] imageId;
    private String TAG = ClientesFragment.class.getSimpleName();
    private String busqueda = "1";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private RadioButton rbCodigo;
    private   RadioButton rbDescripcion;


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

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//               Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.content_frame,new MaestroProductoFragment()).commit();

//               Intent intent = new Intent(getActivity(),MenuActivity.class);
//               startActivity(intent);
            }
        });
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
                new GetClientes().execute();
                lblFooter.setText("Clientes encontrados: " + String.valueOf(listaClientes.size()));
            }
        });
        return myView;
    }
//    public void onRadioButtonClicked(View view) {
//        // Is the button now checked?
//        boolean checked = ((RadioButton) view).isChecked();
//
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.rbCodigo:
//                if (checked)
//                    // Pirates are the best
//                    txtBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
//                    break;
//            case R.id.rbDescripcion:
//                if (checked)
//                    // Ninjas rule
//                    txtBusqueda.setInputType(InputType.TYPE_CLASS_TEXT);
//                    break;
//        }
//    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list) {
            /*ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(acmi.position);

            menu.add("One");
            menu.add("Two");
            menu.add("Three");
            menu.add(obj.get("Codigo"));*/
            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getActivity().getMenuInflater();
            EditText et ;
            inflater.inflate(R.menu.clientes_opciones, menu);

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);
            long itemID = info.position;
            menu.setHeaderTitle(obj.get("Nombre"));
            //menu.add(obj.getClass(MaestroProductoFragment.))
        }
    }
    // URL to get contacts JSON
    private static String url = "http://186.1.18.75:8080/ServicioClientes.svc/BuscarClientes/";

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
                        String Cliente = c.getString("Cliente");
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

                        cliente.put("IdCliente",IdCliente);
                        cliente.put("CodCv",CodCv);
                        cliente.put("Nombre",Nombre);
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
