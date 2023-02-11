package com.saf.sistemas.safcafenorteno.Pedidos;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.saf.sistemas.safcafenorteno.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasPendientesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.RecibosHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.RutasHelper;
import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;
import com.saf.sistemas.safcafenorteno.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.HttpHandler;
import com.saf.sistemas.safcafenorteno.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ListaPagosFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private String TAG = ListaPagosFragment.class.getSimpleName();
    private String busqueda = "0";
    private String fecha = "";
    private String fecha2 = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView tvSincroniza;
    private TextView tvEstado;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private Button btnSincronizar;
    private TextView txtFechaRecibo;
    private TextView txtFechaRecibo2;
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listainforme;
    public Calendar myCalendar = Calendar.getInstance();
    final String urlReciboVendedor = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerRecibosRuta";
    final String urlReciboTodos = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerRecibosTodos";
    private String jsonAnulaRecibo;
    private String IdRecibo;
    private String vSerie;
    private String IdInforme;
    private boolean guardadoOK = true;
    private DecimalFormat df;
    private boolean isOnline = false;

    private RecibosHelper RecibosH;
    private String SerieRecibos="A";
    private FacturasPendientesHelper FacturasPendientesH;
    private RutasHelper RutasH;
    public static ArrayList<HashMap<String, String>> lista;
    private HashMap<String, String> itemrecibo;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;

    //byte FONT_TYPE;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listarecibos_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Recibos");
        lv = (ListView) myView.findViewById(R.id.listrecibosdia);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        btnSincronizar = (Button) myView.findViewById(R.id.btnSincronizar);
        txtFechaRecibo = (EditText) myView.findViewById(R.id.txtFechaDesde);
        txtFechaRecibo2 = (EditText) myView.findViewById(R.id.txtFechaHasta);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_recibos_guardados, null);
        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        RecibosH = new RecibosHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
        RutasH = new RutasHelper(DbOpenHelper.database);

        SerieRecibos= RutasH.ObtenerSerieReciboRuta(Integer.parseInt(variables_publicas.rutacargada),Integer.parseInt(variables_publicas.usuario.getCodigo()));

        txtFechaRecibo.setText(getDatePhone());
        fecha = txtFechaRecibo.getText().toString();
        txtFechaRecibo2.setText(getDatePhone());
        fecha2 = txtFechaRecibo2.getText().toString();

        final DatePickerDialog.OnDateSetListener dateDesde = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        final DatePickerDialog.OnDateSetListener dateHasta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }
        };
        txtFechaRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateDesde, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                View focusedView = getActivity().getCurrentFocus();
                if (focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
        txtFechaRecibo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateHasta, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                View focusedView = getActivity().getCurrentFocus();
                if (focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        txtBusqueda = (EditText) myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                }
                return false;
            }
        });
        listainforme = new ArrayList<>();

        try {

            new GetListaRecibos().execute();


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SincronizarRecibos();


            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarRecibos();

            }
        });

        return myView;
    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
    }

    private void CargarRecibos() {
        fecha = txtFechaRecibo.getText().toString();
        listainforme.clear();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
        busqueda = txtBusqueda.getText().toString();
        new GetListaRecibos().execute();
    }

    private void ActualizarFooter() {

        try {
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> informe : listainforme) {
                subtotal += Double.parseDouble(informe.get("Monto").replace("C$", "").replace(",", ""));
                cantidad += 1;
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de recibos. Excepcion controlada", variables_publicas.info + " --- " + ex.getMessage(), variables_publicas.correoError, variables_publicas.correosErrores);
            Log.e("Error:", ex.getMessage());
            ex.printStackTrace();
        }


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean SincronizarRecibos() {
        try {
            new SincronizadorRecibos().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private boolean AnularRecibo(HashMap<String, String> informe) {
        Gson gson = new Gson();

        jsonAnulaRecibo = gson.toJson(informe);
        try {
            new AnulaRecibo().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private class GetListaRecibos extends AsyncTask<Void, Void, Void> {
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
                if (getActivity() == null) return null;
                DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                RecibosH = new RecibosHelper(DbOpenHelper.database);
                listainforme.clear();
                List<HashMap<String, String>> ListaLocal;

                CheckConnectivity();
                if (isOnline) {
                    ListaLocal = RecibosH.ObtenerRecibosLocales(fecha, fecha2, busqueda, SerieRecibos);

                    for (HashMap<String, String> item : ListaLocal) {
                        HashMap<String, String> iteminforme = new HashMap<>();
                        iteminforme.put("ReciboI", item.get("ReciboI"));
                        iteminforme.put("Cliente", item.get("Cliente"));
                        iteminforme.put("Fecha", item.get("Fecha"));
                        iteminforme.put("Monto", item.get("Monto"));
                        iteminforme.put("Facturas", item.get("Facturas"));
                        iteminforme.put("Estado", item.get("Estado"));
                        iteminforme.put("Recibo", item.get("Recibo"));
                        iteminforme.put("Serie", item.get("Serie"));
                        listainforme.add(iteminforme);
                    }
                    GetRecibosService();
                } else {
                    ListaLocal = RecibosH.ObtenerRecibosLocalesTodos(fecha, fecha2, SerieRecibos);

                    for (HashMap<String, String> item : ListaLocal) {
                        HashMap<String, String> iteminforme = new HashMap<>();
                        iteminforme.put("ReciboI", item.get("ReciboI"));
                        iteminforme.put("Cliente", item.get("Cliente"));
                        iteminforme.put("Fecha", item.get("Fecha"));
                        iteminforme.put("Monto", item.get("Monto"));
                        iteminforme.put("Facturas", item.get("Facturas"));
                        iteminforme.put("Estado", item.get("Estado"));
                        iteminforme.put("Recibo", item.get("Recibo"));
                        iteminforme.put("Serie", item.get("Serie"));
                        listainforme.add(iteminforme);
                    }
                    if (getActivity() == null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran los recibos locales que no se han sincronizados! ",
                                    Toast.LENGTH_LONG).show();
                            //  }
                        }
                    });
                }
            } catch (final Exception e) {
                if (getActivity() == null) return null;
                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //   for (int i = 0; i < 2; i++) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor. \n Solo se mostraran los recibos locales que no se han sincronizados! ",
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

    private void ActualizarLista() {
        try {
            //ActualizarFooter();
            // Dismiss the progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            DecimalFormat df = new DecimalFormat("C$ #0.00");
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            df.setDecimalFormatSymbols(fmts);
            for (HashMap<String, String> item : listainforme) {
                double subtotal = Double.parseDouble(item.get("Monto").replace("C$", "").replace(",", ""));
                item.put("Monto", df.format(subtotal));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listainforme,
                    R.layout.list_recibos_guardados, new String[]{"ReciboI", "Cliente",
                    "Fecha", "Monto", "Facturas", "Estado", "Recibo"},
                    new int[]{R.id.ReciboI, R.id.Cliente, R.id.Fecha, R.id.TotalRecibo, R.id.txtFacturas,
                            R.id.Estado, R.id.CodigoRecibo}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    tvEstado = (TextView) currView.findViewById(R.id.Estado);
                    if (currItem.get("Estado").equalsIgnoreCase("NO ENVIADO")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                        //tvEstado.setBackgroundColor(Color.parseColor("#FFB9B9B9"));
                        tvEstado.setTextColor(Color.parseColor("#FF6C6C6C"));
                    } else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    if (currItem.get("Estado").equals("NO ENVIADO")) {
                        tvEstado.setTextColor(Color.parseColor("#FFBF5300"));
                    }
                    if (currItem.get("Estado").equals("ANULADO")) {
                        tvEstado.setTextColor(Color.parseColor("#FFFF0000"));
                    }
                    if (currItem.get("Estado").equals("APLICADO")) {
                        tvEstado.setTextColor(Color.parseColor("#FF2D8600"));
                    }
                    return currView;
                }
            };
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ActualizarFooter();

        } catch (final Exception ex) {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "GetListaRecibos OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetRecibosService() throws Exception {
        String urlString;
        String vResultado;
        busqueda = busqueda.isEmpty() ? "0" : busqueda;
        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor")) {
            urlString = urlReciboTodos + "/" + fecha + "/" + fecha2 + "/" + busqueda + "/" + variables_publicas.usuario.getEmpresa_ID();
            vResultado = "ObtenerRecibosTodosResult";
        } else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("User")) {
            urlString = urlReciboTodos + "/" + fecha + "/" + fecha2 + "/" + busqueda + "/" + variables_publicas.usuario.getEmpresa_ID();
            vResultado = "ObtenerRecibosTodosResult";
        } else {
            urlString = urlReciboVendedor + "/" + variables_publicas.rutacargada + "/" + fecha + "/" + fecha2 + "/" + busqueda + "/" + variables_publicas.usuario.getEmpresa_ID();
            vResultado = "ObtenerRecibosRutaResult";
        }

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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);
                //listainforme.clear();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Informes = jsonObj.getJSONArray(vResultado);

                for (int i = 0; i < Informes.length(); i++) {
                    JSONObject c = Informes.getJSONObject(i);
                    String Estado = c.getString("Estado");
                    String Fecha = c.getString("Fecha");
                    String Recibo = c.getString("Recibo");
                    String ReciboI = c.getString("ReciboI");
                    String Monto = c.getString("Monto");
                    String Facturas = c.getString("Facturas");
                    String Cliente = c.getString("Cliente");
                    String Serie = c.getString("Serie");

                    HashMap<String, String> informes = new HashMap<>();

                    informes.put("Estado", Estado);
                    informes.put("Fecha", Fecha);
                    informes.put("Recibo", Recibo);
                    informes.put("ReciboI", ReciboI);
                    informes.put("Monto", df.format(Double.parseDouble(Monto)));
                    informes.put("Facturas", Facturas);
                    informes.put("Cliente", Cliente);
                    informes.put("Serie", Serie);
                    listainforme.add(informes);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    //endregion

    private class SincronizadorRecibos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sincronizando recibos...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            if (getActivity() == null) return null;

            CheckConnectivity();
            if (isOnline) {
                List<HashMap<String, String>> RecibosLocal = RecibosH.ObtenerRecibosLocales(fecha, fecha2, "0", vSerie);
                for (HashMap<String, String> item : RecibosLocal) {
                    Gson gson = new Gson();
                    guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarRecibo(RecibosH, item.get("Recibo"), item.get("Serie")).split(",")[0]);
                }
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing()) return;
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse con el servidor, por favor verifique su conexion a internet",
                                Toast.LENGTH_LONG).show();
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
                if (pDialog.isShowing())
                    pDialog.dismiss();

                btnBuscar.performClick();


            } catch (final Exception ex) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing()) return;
                        Toast.makeText(getActivity().getApplicationContext(),
                                "SincronizarInformes onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
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

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Anulando Recibo...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (getActivity() == null) return null;
            HttpHandler sh = new HttpHandler();
            String urlStr = variables_publicas.direccionIp + "/ServicioRecibos.svc/AnularRecibo/" + vSerie + "/" + IdRecibo + "/" + variables_publicas.usuario.getEmpresa_ID();
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
                        if (getActivity() == null) return null;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getActivity().getApplicationContext(),
                                        mensaje,
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        guardadoOK = true;
                        RecibosH.ActualizarReciboGuardado(vSerie, IdRecibo, "anulado");
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al Anular el Recibo. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                    if (getActivity() == null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            for (int i = 0; i < 2; i++) {
                                Toast.makeText(getActivity().getApplicationContext(),
                                        ex.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al Anular el Recibo. Respuesta nulla GET", variables_publicas.info + urlStr, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
                if (getActivity() == null) return null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 2; i++) {

                            Toast.makeText(getActivity().getApplicationContext(),
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
                btnBuscar.performClick();


            } catch (final Exception ex) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Anular Recibo onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    //endregion
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = "Recibo: " + obj.get("ReciboI") + " Facturas: " + obj.get("Facturas");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.recibos_list_menu_context, menu);


            if (obj.get("Estado").equalsIgnoreCase("NO ENVIADO")) {
                ((MenuItem) menu.getItem(0)).setEnabled(false);
                if (variables_publicas.Impresora.equalsIgnoreCase("1")) {
                    ((MenuItem) menu.getItem(1)).setEnabled(true);
                } else {
                    ((MenuItem) menu.getItem(1)).setEnabled(false);
                }

            } else if (obj.get("Estado").equalsIgnoreCase("NO ENVIADO")) {
                ((MenuItem) menu.getItem(0)).setEnabled(true);
                ((MenuItem) menu.getItem(1)).setEnabled(true);

            } else if (obj.get("Estado").equalsIgnoreCase("APLICADO")) {
                ((MenuItem) menu.getItem(0)).setEnabled(true);
                ((MenuItem) menu.getItem(1)).setEnabled(true);

            } else if (obj.get("Estado").equalsIgnoreCase("ANULADO")) {
                ((MenuItem) menu.getItem(0)).setEnabled(false);
                ((MenuItem) menu.getItem(1)).setEnabled(false);
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemAnularRecibo:
                    fecha = txtFechaRecibo.getText().toString();
                    listainforme.clear();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();

                    new GetListaRecibos().execute().get();

                    ActualizarFooter();
                    HashMap<String, String> itemInforme = listainforme.get(info.position);
                    IdRecibo = itemInforme.get("Recibo");
                    vSerie = itemInforme.get("Serie");
                    if (Funciones.checkInternetConnection(getActivity())) {

                        final HashMap<String, String> finalInforme = itemInforme;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea Anular el recibo?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnularRecibo(finalInforme);
                                        FacturasPendientesH.SincronizarFacturasSaldos(variables_publicas.rutacargada, "0");
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

                case R.id.itemImprimirRecibo: {
                    variables_publicas.vImpresoraBT = RecibosH.BuscarNombreImpresora();
                    if (variables_publicas.vImpresoraBT == null || variables_publicas.vImpresoraBT.equalsIgnoreCase("")) {
                        mensajeAviso("No hay configurada una impresora. Debe seleccionar un dispositivo Bluetooth para poder imprimir el Recibo. ");
                        return false;
                    }
                    itemrecibo = listainforme.get(info.position);
                    try {
                        if (Build.VERSION.SDK_INT >= 11) {
                            //--post GB use serial executor by default --
                            new ImprimiendoRecibo().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                        } else {
                            //--GB uses ThreadPoolExecutor by default--
                            new ImprimiendoRecibo().execute();
                        }
                    } catch (final Exception ex) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(),
                                                ex.getMessage(),
                                                Toast.LENGTH_LONG)
                                        .show();
                            }
                        });
                    }


                    return true;
                }
                default:
                    return super.onContextItemSelected(item);
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
        return false;
    }

    //Funcion asincrona para guardar el detalle
    private class ImprimiendoRecibo extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Imprimiendo recibo, por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            IdRecibo = itemrecibo.get("Recibo");
            String tImpresion = "Copia";
            String vRpc = itemrecibo.get("ReciboI");
            String vId = itemrecibo.get("IdCliente");
            String vCliente = itemrecibo.get("Cliente");
            String vMonto = "";
            String vFecha = itemrecibo.get("Fecha");
            String vDoc = itemrecibo.get("NoCheque");
            String vLetra;
            String vDocumento = "";
            String vCantLetra = "";
            String vConcepto = itemrecibo.get("Concepto");
            vSerie = itemrecibo.get("Serie");
            lista = new ArrayList<>();
            lista = RecibosH.ObtenerRecibo(IdRecibo, vSerie);
            if (lista.size() > 0) {
                for (int i = 0; i < lista.size(); i++) {
                    vRpc = lista.get(i).get("ReciboI");
                    vId = lista.get(i).get("IdCliente");
                    vCliente = lista.get(i).get("Cliente");
                    vFecha = lista.get(i).get("Fecha");
                    vDoc = lista.get(i).get("NoCheque");
                    vConcepto = lista.get(i).get("Concepto");
                }
                lista = new ArrayList<>();
                lista = RecibosH.ObtenerReciboMontosRecibo(IdRecibo, vSerie);

                for (int i = 0; i < lista.size(); i++) {
                    vMonto = lista.get(i).get("Monto");
                }
                String tipoimpresion = RecibosH.ObtenerTipoImpresion(IdRecibo, vSerie);

                if (tipoimpresion.equalsIgnoreCase("0")) {
                    tImpresion = "Original";
                } else {
                    tImpresion = "Copia";
                }
                vLetra = Funciones.Convertir(vMonto, true);

                vCantLetra = vLetra;
                vDocumento = vDoc;
            } else {
                tImpresion = "Copia";
                vMonto = itemrecibo.get("Monto");
                vLetra = Funciones.Convertir(vMonto, true);

                vCantLetra = vLetra;
                vDocumento = vDoc;
            }

            double saldocliente = 0;
            saldocliente = FacturasPendientesH.BuscarSaldoCliente(vId);

            try {
                findBT();
                openBT();
                printRecibo(tImpresion, vRpc, vId, vCliente, vMonto, vFecha, vDocumento, vCantLetra, vConcepto, saldocliente);
                closeBT();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
        }
    }

    void findBT() {

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
/*

            if(mBluetoothAdapter == null) {
//                myLabel.setText("No bluetooth adapter available");
            }
*/

            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetooth, 0);
            }

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {

                    // RPP300 is the name of the bluetooth printer device
                    // we got this name from the list of paired devices
                    if (device.getName().equals(variables_publicas.vImpresoraBT)) {
                        mmDevice = device;
                        break;
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // tries to open a connection to the bluetooth printer device
    void openBT() throws IOException {
        try {

            // Standard SerialPortService ID
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mmSocket.connect();
            mmOutputStream = mmSocket.getOutputStream();
            mmInputStream = mmSocket.getInputStream();

            beginListenForData();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void beginListenForData() {
        try {
            final Handler handler = new Handler();

            // this is the ASCII code for a newline character
            final byte delimiter = 10;

            stopWorker = false;
            readBufferPosition = 0;
            readBuffer = new byte[1024];

            workerThread = new Thread(new Runnable() {
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker) {

                        try {

                            int bytesAvailable = mmInputStream.available();

                            if (bytesAvailable > 0) {

                                byte[] packetBytes = new byte[bytesAvailable];
                                mmInputStream.read(packetBytes);

                                for (int i = 0; i < bytesAvailable; i++) {

                                    byte b = packetBytes[i];
                                    if (b == delimiter) {

                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer, 0,
                                                encodedBytes, 0,
                                                encodedBytes.length
                                        );

                                        // specify US-ASCII encoding
                                        //final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;

                                        // tell the user data were sent to bluetooth printer device
                                        handler.post(new Runnable() {
                                            public void run() {
                                                //myLabel.setText(data);
                                            }
                                        });

                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }

                        } catch (IOException ex) {
                            stopWorker = true;
                        }

                    }
                }
            });

            workerThread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeBT() throws IOException {
        try {
            stopWorker = true;
            mmOutputStream.close();
            mmInputStream.close();
            mmSocket.close();
            //myLabel.setText("Bluetooth Closed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void printRecibo(String tImpresion,String vRpc,String vId,String vCliente,String vMonto,String vFecha,String vDoc,String vLetra,String vConcepto,double saldo) {
        OutputStream opstream = null;
        try {
            opstream = mmSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mmOutputStream = opstream;

        //print command
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //Formato inicial.
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
            Date d = formato.parse(vFecha);

            //Aplica formato requerido.
            formato.applyPattern("dd/MM/yyyy");
            String vFechaPago = formato.format(d);

            mmOutputStream = mmSocket.getOutputStream();

            //byte[] printformat = { 0x1B, 0*21, FONT_TYPE };
            printNewLine();
            printNewLine();
            printNewLine();
            //print title
            printUnicode();
            //print normal text
            printPhoto(R.mipmap.logoelnortenobmp);
            printCustom("CAFE EL NORTENO, S.A." , 6, 1);
            printNewLine();
            printCustom("Carretera a Veracruz. Plaza", 4, 1);
            printCustom("Montana. Managua, Nicaragua,", 4, 1);
            printCustom("Tel 5794-1666", 4, 1);
            printCustom("RUC: 4491912900001Q", 4, 1);
            printUnicode();
            printNewLine();
            printCustom("RECIBO NO. " + vRpc, 4, 1);
            printNewLine();
            printCustom("CLIENTE: " + vId + "-"+Funciones.formateartexto(vCliente), 4, 0);
            printNewLine();
            printCustom("ABONO C$: " + df.format(Double.parseDouble(vMonto)), 4, 0);
            printNewLine();
            printCustom("CONCEPTO: ", 4, 0);
            printCustom(vConcepto, 4, 0);
            printNewLine();
            printCustom("SALDO C$: " + df.format(saldo), 4, 0);
            printNewLine();
            printCustom("Vendedor: " + Funciones.formateartexto(variables_publicas.usuario.getNombre()), 4, 0);
            printNewLine();
            String fecha[] = getDateTime();
            printCustom("Impreso: " + fecha[0] + " " + fecha[1], 4, 0);
            printUnicode();
            printNewLine();
            printCustom(">>>>  "+ tImpresion + "  <<<<",4,1);
            printNewLine();
            printNewLine();
            printNewLine();
            printNewLine();
            mmOutputStream.flush();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        //}
    }
    //print custom
    private void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    mmOutputStream.write(cc);
                    break;
                case 1:
                    mmOutputStream.write(bb);
                    break;
                case 2:
                    mmOutputStream.write(bb2);
                    break;
                case 3:
                    mmOutputStream.write(bb3);
                    break;
                case 4:
                    mmOutputStream.write(cc1);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    mmOutputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            mmOutputStream.write(msg.getBytes());
            mmOutputStream.write(PrinterCommands.LF);
            //outputStream.write(cc);
            //printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //print photo
    public void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = Utils.decodeBitmap(bmp);
                mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    //print unicode
    public void printUnicode(){
        try {
            mmOutputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(Utils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //print new line
    private void printNewLine() {
        try {
            mmOutputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
/*    //print text
    private void printText(String msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    //print byte[]
    private void printText(byte[] msg) {
        try {
            // Print normal text
            mmOutputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


/*
    private String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <31){
            int n = (31 - str1.length() + str2.length());
            ans = str1 + new String(new char[n]).replace("\0", " ") + str2;
        }
        return ans;
    }
*/


    private String[] getDateTime() {
        Calendar c = new GregorianCalendar();
        String dateTime [] = new String[2];
        dateTime[0] =  c.get(Calendar.DATE) +"/"+String.valueOf(c.get(Calendar.MONTH)+1) +"/"+ c.get(Calendar.YEAR);
        dateTime[1] = c.get(Calendar.HOUR) +":"+ c.get(Calendar.MINUTE);
        return dateTime;

    }
    public void mensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void updateLabel() {
        String myFormat = ("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaRecibo.setText(sdf.format(myCalendar.getTime()));
        btnBuscar.performClick();
    }
    private void updateLabel2() {
        String myFormat = ("yyyy-MM-dd");
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaRecibo2.setText(sdf.format(myCalendar.getTime()));
        btnBuscar.performClick();
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
