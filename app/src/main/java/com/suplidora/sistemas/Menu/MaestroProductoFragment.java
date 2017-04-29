package com.suplidora.sistemas.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.HttpHandler;
import com.suplidora.sistemas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 20/1/2017.
 */

public class MaestroProductoFragment extends Fragment {
    View myView;
    private String TAG = MaestroProductoFragment.class.getSimpleName();
    private String busqueda = "1";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;
    private ArticulosHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.masterproductos_layout,container,false);
       getActivity().setTitle("Maestro Productos");
        lv = (ListView) myView.findViewById(R.id.list);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);

        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
        listaArticulos = new ArrayList<>();

/*prueba*/
//        btnConsulta.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(),.class);
//               startActivity(intent);
//            }
//        });

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

                new GetArticulos().execute();
                lblFooter.setText("Articulos encontrados: " + String.valueOf(listaArticulos.size()));
            }
        });
        return myView;
    }

    // URL to get contacts JSON
   // private static String url = "http://186.1.18.75:8080/ServiciosSisa.svc/BuscarArticulo/";
    private static String url = "http://186.1.18.75:8080/ServicioTotalArticulos.svc/BuscarArticulo/";
    ArrayList<HashMap<String, String>> listaArticulos;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new GetArticulos().execute();
    }
    private class GetArticulos extends AsyncTask<Void, Void, Void> {

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
                    listaArticulos = new ArrayList<>();
                    // Getting JSON Array node
                    JSONArray articulos = jsonObj.getJSONArray("BuscarArticuloResult");

                    // looping through All Contacts
                    for (int i = 0; i < articulos.length(); i++) {
                        JSONObject c = articulos.getJSONObject(i);

                        //String Id = c.getString("Id");
                        String Codigo = c.getString("CODIGO_ARTICULO");
                        String Nombre = c.getString("NOMBRE");
                        String PrecioSuper = c.getString("PrecioSuper");
                        String PrecioDetalle = c.getString("PrecioDetalle");
                        String PrecioForaneo = c.getString("PrecioForaneo");
                        String PrecioMayorista = c.getString("PrecioMayorista");

                        //databaseHelper.GuardarTotalArticulos(Codigo,Nombre,PrecioSuper,PrecioDetalle,PrecioForaneo,PrecioMayorista);

                        HashMap<String, String> articulo = new HashMap<>();

                        // adding each child node to HashMap key => value
                        articulo.put("Codigo", Codigo);
                        articulo.put("Codigo", Codigo);
                        articulo.put("Nombre", Nombre);
                        articulo.put("PrecioSuper","Super: "+ PrecioSuper);
                        articulo.put("PrecioDetalle","Detalle: "+  PrecioDetalle);
                        articulo.put("PrecioForaneo", "Foraneo: "+PrecioForaneo);
                        articulo.put("PrecioMayorista", "Mayorista: "+ PrecioMayorista);

                        listaArticulos.add(articulo);
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
                    getActivity(), listaArticulos,
                    R.layout.list_item, new String[]{"Codigo", "Nombre", "PrecioSuper", "PrecioDetalle","PrecioForaneo","PrecioMayorista"}, new int[]{R.id.Codigo, R.id.Nombre,
                    R.id.PrecioSuper, R.id.PrecioDetalle,R.id.PrecioForaneo,R.id.PrecioMayorista});

            lv.setAdapter(adapter);
            lblFooter.setText("Articulos encontrados: " + String.valueOf(listaArticulos.size()));
        }
    }
}
