package com.safi_d.sistemas.safiapp.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.safi_d.sistemas.safiapp.AccesoDatos.ArticulosHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 20/1/2017.
 */

public class MaestroProductoFragment extends Fragment {
    View myView;
    private String TAG = MaestroProductoFragment.class.getSimpleName();
    private String busqueda = "";
    private String tipoBusqueda = "2";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private EditText txtBusqueda;
    private RadioGroup rgGrupo;
    private Button btnBuscar;

    private DataBaseOpenHelper DbOpenHelper;
    private ArticulosHelper ArticulosH;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.masterproductos_layout,container,false);
        getActivity().setTitle("Listado de Productos");
        lv = (ListView) myView.findViewById(R.id.list);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);

        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);
        rgGrupo = (RadioGroup) myView.findViewById(R.id.rgGrupo);
        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                }
                return false;
            }
        });
        new GetArticulos().execute();
        listaArticulos = new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                tipoBusqueda = rgGrupo.getCheckedRadioButtonId() == R.id.rbCodigo ? "1" : "2";

                new GetArticulos().execute();
                lblFooter.setText("Productos encontrados: " + String.valueOf(listaArticulos.size()));
            }
        });
        return myView;
    }

    // URL to get contacts JSON
    private static String url = variables_publicas.direccionIp +"/ServicioTotalArticulos.svc/BuscarArticulo/";
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
            try {
                if(getActivity()==null) return null;
                DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                ArticulosH = new ArticulosHelper(DbOpenHelper.database);
                switch (tipoBusqueda){
                    case "1":
                        listaArticulos=ArticulosH.BuscarArticuloCodigo(busqueda);
                        break;
                    case  "2":
                        listaArticulos=ArticulosH.BuscarArticuloNombre(busqueda);
                        break;
                }
            } catch (final Exception e) {
                if(getActivity()==null) return null;
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(getActivity()==null) return;
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), listaArticulos,
                    R.layout.list_item, new String[]{"Codigo", "Nombre","Precio"}, new int[]{R.id.Codigo, R.id.Nombre,
                    R.id.Precio});

            lv.setAdapter(adapter);
            lblFooter.setText("Productos encontrados: " + String.valueOf(listaArticulos.size()));
        }
    }
}
