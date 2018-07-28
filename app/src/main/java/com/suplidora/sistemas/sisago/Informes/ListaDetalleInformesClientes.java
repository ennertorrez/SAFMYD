package com.suplidora.sistemas.sisago.Informes;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.FacturasPendientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Menu.ListaInformesFragment;
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
    private String ReciboId="";
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

    private ProgressDialog pDialog;
    private boolean guardadoOK = true;
    private String jsonAnulaRecibo;
    private FacturasPendientesHelper FacturasPendientesH;

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

        DbOpenHelper = new DataBaseOpenHelper(getApplicationContext());
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
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
                    hasRecibos.put("Monto", df.format(Double.parseDouble(vMonto)));
                    hasRecibos.put("Facturas", vFacturas);
                    hasRecibos.put("Estado", vEstado);
                    listaInformes.add(hasRecibos);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    //region ServiceAnularInforme
    private class AnulaRecibo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            pDialog = new ProgressDialog(ListaDetalleInformesClientes.this);
            pDialog.setMessage("Anulando Recibo...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(ListaDetalleInformesClientes.this==null) return null;
            HttpHandler sh = new HttpHandler();

            final String url = variables_publicas.direccionIp + "/ServicioRecibos.svc/AnularRecibo/" + InformeId + "/" + ReciboId;
           // final String url = variables_publicas.direccionIp + "/ServicioRecibos.svc/AnularRecibo/" + IdInforme + "/" + IdRecibo;

            String urlString = url;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCall(encodeUrl);

            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    String resultState = ((String) result.get("AnularReciboResult")).split(",")[0];
                    final String mensaje = ((String) result.get("AnularReciboResult")).split(",")[1];
                    if (resultState.equals("false")) {
                        if(ListaDetalleInformesClientes.this==null) return null;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getApplicationContext(),
                                        mensaje,
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        guardadoOK = true;
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al Anular el Recibo. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                    if(ListaDetalleInformesClientes.this==null) return null;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < 2; i++) {
                                Toast.makeText(getApplicationContext(),
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al Anular el Recibo. Respuesta nulla GET", variables_publicas.info + urlStr, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                if(ListaDetalleInformesClientes.this==null) return null;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 2; i++) {

                            Toast.makeText(getApplicationContext(),
                                    "No se ha podido obtener los datos del servidor ",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {

                // Dismiss the progress dialog
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();
             //   btnBuscar.performClick();


            } catch (final Exception ex) {
                if(ListaDetalleInformesClientes.this==null) return ;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getApplicationContext(),
                                "Anular Recibo onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }
    private boolean AnularRecibo(HashMap<String, String> detalleInforme) {
        Gson gson = new Gson();

        jsonAnulaRecibo = gson.toJson(detalleInforme);
        try {
            new AnulaRecibo().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getApplicationContext(), ex.getMessage());
        }
        return false;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = "Recibo: " +obj.get("Recibo") + " Cliente: " + obj.get("Cliente");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.recibos_list_menu_context, menu);

            if (obj.get("Estado").equalsIgnoreCase("PENDIENTE"))
            {
                ((MenuItem) menu.getItem(0)).setEnabled(true);

            }else {
                ((MenuItem) menu.getItem(0)).setEnabled(false);
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> informeDetalle = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemAnularRecibo:
                    listaInformes.clear();

                    CargarRecibos();

                    ActualizarFooter();

                    HashMap<String, String> itemInforme = listaInformes.get(info.position);
                    informeDetalle = InformesDetalleH.ObtenerInformeDetalleHas(InformeId);

                    ReciboId = itemInforme.get("Recibo");
                    if (Funciones.checkInternetConnection(ListaDetalleInformesClientes.this)) {

                        final HashMap<String, String> finalInforme = itemInforme;
                        new AlertDialog.Builder(ListaDetalleInformesClientes.this)
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea Anular el recibo?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnularRecibo (finalInforme);
                                        CargarRecibos();
                                        ActualizarFooter();
                                        FacturasPendientesH.SincronizarFacturasSaldos(variables_publicas.usuario.getCodigo(),"0");
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                    }
                                })
                                .show();

                    } else {
                        mensajeAviso("No es posible connectarse con el servidor, por favor verifique su conexión a internet");
                    }

                    return true;

               default:
                    return super.onContextItemSelected(item);
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
        return false;
    }

    public void mensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(ListaDetalleInformesClientes.this);
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
}
