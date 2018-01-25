package com.suplidora.sistemas.sisago.Devoluciones;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesDetalleHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by usuario on 20/3/2017.
 */

public class ListaArtDevolucionFragment extends Activity  {
    private DataBaseOpenHelper DbOpenHelper;
    private String TAG = ListaArtDevolucionFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView lblConsolidadoId;

    private DevolucionesDetalleHelper DevolucionesDetalleH;
    public static ArrayList<HashMap<String, String>> listaDev;
    private SimpleAdapter adapter;
    private DecimalFormat df;

    final String urlArtDevueltos= variables_publicas.direccionIp + "/ServicioDevoluciones.svc/Sp_ObtieneDetalleDevueltoRango";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listaresumendevolucion_layout);


        DbOpenHelper = new DataBaseOpenHelper(ListaArtDevolucionFragment.this);
        DevolucionesDetalleH = new DevolucionesDetalleHelper(DbOpenHelper.database);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        lv = (ListView) findViewById(R.id.listResumenDevolucion);
        registerForContextMenu(lv);

        lblFooterCantidad = (TextView) findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) findViewById(R.id.lblFooterSubtotal);
        lblConsolidadoId = (TextView) findViewById(R.id.lblConsolidado);

        lblConsolidadoId.setText(variables_publicas.usuario.getCodigo());
        listaDev = new ArrayList<>();
        CargarDevolucionesDet();
    }

    private void CargarDevolucionesDet() {
        lv.setAdapter(adapter);
        listaDev.clear();
        GetDevoluciondatos();
        if (listaDev.size()== 0) {
            GetDevArtService();
        }
        ActualizarLista();
    }

    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaDev.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> devdet : listaDev) {
            subtotal += Double.parseDouble(devdet.get("Total").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
            adapter = new SimpleAdapter(
                    getApplicationContext(), listaDev,
                    R.layout.resumen_devolucion_list_item, new
                    String[]{"Codigo", "descripcion", "Cantidad", "Total"}, new
                    int[]{R.id.lblItemDev, R.id.lblDetArt, R.id.lblDetCantidad, R.id.lblDetSubtotal}) {

            };

            lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
            ActualizarFooter();
    }

    private void GetDevoluciondatos()  { //Obtiene lista locales

        listaDev.clear();
        List<HashMap<String, String>> ListaLocal = null;

        ListaLocal = DevolucionesDetalleH.ObtenerProductosDevoluciones(variables_publicas.usuario.getCodigo());

        for (HashMap<String, String> item : ListaLocal) {
            HashMap<String, String> itemdeartDev = new HashMap<>();
            // item.get("").substring(" ");
            //String Fecha = item.get("horagraba").substring(item.get("horagraba").length() - 10);

            itemdeartDev.put("Codigo", item.get("Codigo").split("-")[item.get("Codigo").split("-").length - 1]);
            itemdeartDev.put("descripcion", Funciones.Decodificar( item.get("descripcion")));
            itemdeartDev.put("Cantidad", item.get("Cantidad"));
            itemdeartDev.put("Total", item.get("Total"));
            listaDev.add(itemdeartDev);
        }
    }

        private void GetDevArtService()  {
        String Rango = variables_publicas.usuario.getCodigo();

        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlArtDevueltos + "/" + Rango;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de artículos devueltos,Respuesta nula GET", variables_publicas.info+urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray ArtDevueltos = jsonObj.getJSONArray("Sp_ObtieneDetalleDevueltoRangoResult");
                listaDev.clear();
                for (int i = 0; i < ArtDevueltos.length(); i++) {
                    JSONObject c = ArtDevueltos.getJSONObject(i);

                    String CodArt = c.getString("item").split("-")[c.getString("item").split("-").length - 1];
                    String Descr = c.getString("Nombre");
                    String Cant = c.getString("Cantidad");
                    String SubTot = c.getString("Total");

                    HashMap<String, String> hasArtDev = new HashMap<>();

                    hasArtDev.put("Codigo", CodArt);
                    hasArtDev.put("descripcion", Descr);
                    hasArtDev.put("Cantidad", Cant);
                    hasArtDev.put("Total", SubTot);
                    listaDev.add(hasArtDev);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de articulos devueltos,Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    //endregion


   @Override
    public void onResume() {
        super.onResume();
   /*      try {
//            if (adapter != null) {
            GetDevoluciondatos();
//            }
        } catch (Exception ex) {

        }*/
    }
}