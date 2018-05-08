package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesHelper;
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
 * Created by Sistemas on 9/2/2018.
 */

public class ListaDetalleInformesClientes extends Activity {
    static final String KEY_IdInforme= "Informe";
    static final String KEY_EstadoInforme = "Estado";

    private DataBaseOpenHelper DbOpenHelper;
    private InformesDetalleHelper InformesDetalleH;
    private boolean isOnline = false;

    private String InformeId="";
    private String EstadoInf="";
    private String TAG = ListaDetalleInformesClientes.class.getSimpleName();
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView lblIdInforme;
    private TextView lblTitulo;
    public static ArrayList<HashMap<String, String>> listaInformes;
    private SimpleAdapter adapter;
    private DecimalFormat df;
    final String urlGetDetalleInforme = variables_publicas.direccionIp + "/ServicioRecibos.svc/BuscarRecibos/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_recibos_informes_layout);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        lv = (ListView) findViewById(R.id.listDetalleRecibos);
        registerForContextMenu(lv);

        Intent in = getIntent();

        InformeId= in.getStringExtra(KEY_IdInforme);
        EstadoInf= in.getStringExtra(KEY_EstadoInforme);

        lblFooterCantidad = (TextView) findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) findViewById(R.id.lblFooterSubtotal);
        lblIdInforme = (TextView) findViewById(R.id.lblIdInforme);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

      //  lblTitulo.setText("Histórico de Ventas en "+ meses);
        lblIdInforme.setText(InformeId + " - " + EstadoInf);

        listaInformes = new ArrayList<>();
        CargarRecibos();
    }
    private void CargarRecibos() {
        lv.setAdapter(adapter);
        DbOpenHelper = new DataBaseOpenHelper(getApplicationContext());
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        listaInformes.clear();
        List<HashMap<String, String>> ListaLocal = null;

        ListaLocal = InformesDetalleH.ObtenerDetalleInformesLocales(InformeId);

        for (HashMap<String, String> item : ListaLocal) {
            HashMap<String, String> iteminforme = new HashMap<>();
            iteminforme.put("Recibo", item.get("Recibo"));
            iteminforme.put("Id", item.get("Id"));
            iteminforme.put("Cliente", item.get("Cliente"));
            iteminforme.put("Monto", item.get("Monto"));
            iteminforme.put("Facturas", item.get("Facturas"));
            iteminforme.put("Estado", item.get("Estado"));
            listaInformes.add(iteminforme);
        }
        CheckConnectivity();
        if (isOnline) {
            GetRecibosService();
        }
        ActualizarLista();
    }

    private void CheckConnectivity() {
        isOnline =Funciones.TestServerConectivity();
    }
    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaInformes.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> devdet : listaInformes) {
            subtotal += Double.parseDouble(devdet.get("Monto").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaInformes,
                R.layout.resumen_recibos_list_item, new
                String[]{"Recibo","Id", "Cliente", "Monto", "Facturas", "Estado"}, new
                int[]{R.id.lblRecibo,R.id.lblIdCliente, R.id.lblCliente, R.id.lblMonto, R.id.lblFacturas,R.id.lblEstado}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View currView = super.getView(position, convertView, parent);
                HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                if (currItem.get("Estado").equals("APLICADO")) {
                    currView.setBackgroundColor(Color.GRAY);
                } else if (currItem.get("Estado").equals("ANULADO")) {
                    currView.setBackgroundColor(Color.RED);
                } else {
                    currView.setBackgroundColor(Color.WHITE);
                }
                return currView;
            }
        };

        lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
        ActualizarFooter();
    }
    private void GetRecibosService()  {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlGetDetalleInforme + InformeId ;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Respuesta nula GET", variables_publicas.info+urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray recibosInforme = jsonObj.getJSONArray("BuscarRecibosResult");
                listaInformes.clear();
                for (int i = 0; i < recibosInforme.length(); i++) {
                    JSONObject c = recibosInforme.getJSONObject(i);

                    String vRecibo = c.getString("Recibo");
                    String vIdCliente = c.getString("Id");
                    String vCliente = c.getString("Cliente");
                    String vMonto = c.getString("Monto");
                    String vFacturas =c.getString("Facturas");
                    String vEstado = c.getString("Estado");

                    HashMap<String, String> hasRecibos = new HashMap<>();

                    hasRecibos.put("Recibo", vRecibo);
                    hasRecibos.put("Id", vIdCliente);
                    hasRecibos.put("Cliente", vCliente);
                    hasRecibos.put("Monto", vMonto);
                    hasRecibos.put("Facturas", vFacturas);
                    hasRecibos.put("Estado", vEstado);
                    listaInformes.add(hasRecibos);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
}
