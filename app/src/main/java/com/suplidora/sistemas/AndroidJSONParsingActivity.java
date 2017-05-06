package com.suplidora.sistemas;

/**
 * Created by usuario on 19/4/2017.
 */
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.ArticulosHelper;
import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.Pedidos.PedidosActivity;

public class AndroidJSONParsingActivity extends ListActivity {

    private String busqueda = "001";
    private String tipoBusqueda = "1";
    private ProgressDialog pDialog;

    private String TAG = AndroidJSONParsingActivity.class.getSimpleName();
    // contacts JSONArray
    JSONArray contacts = null;

    private DataBaseOpenHelper DbOpenHelper ;
    private  ArticulosHelper ArticulosH;


    public static ArrayList<HashMap<String, String>> listaArticulos;
    //private static String url = "http://186.1.18.75:8080/ServicioTotalArticulos.svc/BuscarTotalArticulo";
    private static String url = "http://186.1.18.75:8080/ServicioTotalArticulos.svc/BuscarTotalArticulo";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list);

        DbOpenHelper=new DataBaseOpenHelper(AndroidJSONParsingActivity.this);
        ArticulosH = new ArticulosHelper(DbOpenHelper.database);

        new GetArticulos().execute();

        // selecting single ListView item
        ListView lv = getListView();

        // Launching new screen on Selecting Single ListItem
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String Id = ((TextView) view.findViewById(R.id.Id)).getText().toString();
                String Codigo = ((TextView) view.findViewById(R.id.Codigo)).getText().toString();
                String Nombre = ((TextView) view.findViewById(R.id.Nombre)).getText().toString();
                String PrecioSuper = ((TextView) view.findViewById(R.id.PrecioSuper)).getText().toString();
                String PrecioDetalle = ((TextView) view.findViewById(R.id.PrecioDetalle)).getText().toString();
                String PrecioForaneo = ((TextView) view.findViewById(R.id.PrecioForaneo)).getText().toString();
                String PrecioMayorista = ((TextView) view.findViewById(R.id.PrecioMayorista)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(), SingleMenuItemActivity.class);

                //in.putExtra(vp.ARTICULO_COLUMN_Id, Id);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_Codigo, Codigo);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_Nombre, Nombre);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_PrecioSuper, PrecioSuper);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_PrecioDetalle, PrecioDetalle);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_PrecioForaneo, PrecioForaneo);
                in.putExtra(variables_publicas.ARTICULO_COLUMN_PrecioMayorista, PrecioMayorista);
                startActivity(in);

            }
        });
    }
    private class GetArticulos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            // String urlString = url;
            String urlString = url ;
            String jsonStr = sh.makeServiceCall(urlString);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                ArticulosH.EliminaArticulos();
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    listaArticulos = new ArrayList<>();
                    // Getting JSON Array node
                    JSONArray articulos = jsonObj.getJSONArray("BuscarTotalArticuloResult");

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
                        String Bonificable = c.getString("Bonificable");
                        String AplicaPrecioDetalle = c.getString("AplicaPrecioDetalle");
                        String DESCUENTO_MAXIMO = c.getString("DESCUENTO_MAXIMO");
                        String detallista = c.getString("detallista");

                        ArticulosH.GuardarTotalArticulos(Codigo, Nombre, PrecioSuper, PrecioDetalle, PrecioForaneo, PrecioMayorista,Bonificable,
                                AplicaPrecioDetalle,
                                DESCUENTO_MAXIMO,
                                detallista);

                        HashMap<String, String> articulo = new HashMap<>();

                        // adding each child node to HashMap key => value
                        //articulo.put("Id", Id);
                        articulo.put("Codigo", Codigo);
                        articulo.put("Nombre", Nombre);
                        articulo.put("PrecioSuper", "Super: " + PrecioSuper);
                        articulo.put("PrecioDetalle", "Detalle: " + PrecioDetalle);
                        articulo.put("PrecioForaneo", "Foraneo: " + PrecioForaneo);
                        articulo.put("PrecioMayorista", "Mayorista: " + PrecioMayorista);

                        listaArticulos.add(articulo);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    AndroidJSONParsingActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                AndroidJSONParsingActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
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
//            if (pDialog.isShowing())
//                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(getApplicationContext(),listaArticulos,R.layout.single_list_item,
                    new String[]{"Codigo", "Nombre", "PrecioSuper", "PrecioDetalle", "PrecioForaneo", "PrecioMayorista"},
                    new int[]{R.id.Codigo, R.id.Nombre, R.id.PrecioSuper, R.id.PrecioDetalle, R.id.PrecioForaneo, R.id.PrecioMayorista});

            setListAdapter(adapter);
        }
    }
}