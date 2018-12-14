package com.suplidora.sistemas.sisago.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.CartillasBcDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Sistemas on 8/2/2018.
 */

public class PromocionesSisaFragment extends Fragment{
    View myView;
    private String TAG = PromocionesSisaFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private ClientesHelper ClienteH;
    private DataBaseOpenHelper DbOpenHelper;
    private CartillasBcDetalleHelper CartillasBCDetalleH;
    private static String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetPromoSISAGO/";
    public static ArrayList<HashMap<String, String>> listaPromociones;
    private SimpleAdapter adapter;
    private String vCanal;
    private boolean isOnline = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.lista_otras_promociones, container, false);
        getActivity().setTitle("Otras Promociones");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        ClienteH = new ClientesHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.listaPromociones);
        registerForContextMenu(lv);

        lblFooter = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        listaPromociones = new ArrayList<>();
/*
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.resumen_otras_promociones_list_item, null);*/

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        CartillasBCDetalleH = new CartillasBcDetalleHelper(DbOpenHelper.database);
        listaPromociones.clear();
        List<HashMap<String, String>> ListaLocal = null;

        if(variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor") && variables_publicas.usuario.getCanal().equalsIgnoreCase("Mayorista")){
            vCanal="MAYORISTA";
        }else if(variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor") && variables_publicas.usuario.getCanal().equalsIgnoreCase("Detalle")){
            vCanal="DETALLE";
        }else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor") && variables_publicas.usuario.getCanal().equalsIgnoreCase("Horeca")){
            vCanal="HORECA";
        }else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor") && variables_publicas.usuario.getCanal().equalsIgnoreCase("Super")){
            vCanal="SUPER";
        }else {
            vCanal="TODOS";
        }
        listaPromociones = new ArrayList<>();
        try{
            new GetListaPromo().execute();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        lv.setTextFilterEnabled(true);
        lv.setFastScrollEnabled(true);

        ActualizarLista();
        lblFooter.setText("Items Encontrados: " + String.valueOf(listaPromociones.size()));
        return myView;
    }

    private class GetListaPromo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                listaPromociones.clear();
                if(getActivity()==null) return null;

                CheckConnectivity();
                if (isOnline) {
                    GetPromoService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor! ",
                                    Toast.LENGTH_LONG).show();
                            //  }
                        }
                    });
                }
            } catch (final Exception e) {
                if(getActivity()==null) return null;
                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //   for (int i = 0; i < 2; i++) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor! ",
                                Toast.LENGTH_LONG).show();
                        //  }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            ActualizarLista();

        }
    }
    private void CheckConnectivity() {
        isOnline =Funciones.TestServerConectivity();
    }
    private void GetPromoService() throws Exception {
        String urlString= "";
        String vResultado="";

        urlString = url + "/" + vCanal;
        vResultado="GetPromoSISAGOResult";

        String encodeUrl = "";

        HttpHandler sh = new HttpHandler();

        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            String jsonStr = sh.makeServiceCall(encodeUrl);
            if (jsonStr == null) {
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de Promociones SISAGO. Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);
                //listainforme.clear();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Promociones = jsonObj.getJSONArray(vResultado);

                for (int i = 0; i < Promociones.length(); i++) {
                    JSONObject c = Promociones.getJSONObject(i);
                    String Promocion = c.getString("PROMOCION");
                    String Requisitos = c.getString("REQUISITOS");
                    String Beneficio = c.getString("BENEFICIO");
                    String Canales = c.getString("CANALES");

                    HashMap<String, String> promo = new HashMap<>();

                    promo.put("Promocion", Promocion);
                    promo.put("Requisitos", Requisitos);
                    promo.put("Beneficio", Beneficio);
                    promo.put("Canales", Canales);
                    listaPromociones.add(promo);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de Promociones SISAGO. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void ActualizarLista() {
        try {
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            adapter = new SimpleAdapter(
                    getActivity(), listaPromociones,
                    R.layout.resumen_otras_promociones_list_item, new String[]{"Promocion", "Requisitos",
                    "Beneficio", "Canales"},
                    new int[]{R.id.lblPromociones, R.id.lblRequisitos, R.id.lblBeneficio, R.id.lblCanal}) {
            };
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (final Exception ex) {
            if(getActivity()==null) return ;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ListaPromociones OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
