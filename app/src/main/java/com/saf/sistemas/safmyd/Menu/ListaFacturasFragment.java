package com.saf.sistemas.safmyd.Menu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saf.sistemas.safmyd.AccesoDatos.ArticulosHelper;
import com.saf.sistemas.safmyd.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasLineasHelper;
import com.saf.sistemas.safmyd.AccesoDatos.FacturasPendientesHelper;
import com.saf.sistemas.safmyd.AccesoDatos.RutasHelper;
import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Cliente;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.Pedidos.FacturasActivityNew;
import com.saf.sistemas.safmyd.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ListaFacturasFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private FacturasHelper FacturasH;
    private FacturasLineasHelper FacturasLineasH;
    private ClientesHelper ClientesH;
    private String CodigoCliente="";
    private FacturasPendientesHelper FacturasPendientesH;
    private final String TAG = ListaFacturasFragment.class.getSimpleName();
    private String busqueda = "0";
    private String fecha1 = "";
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
    private TextView txtFechaInicio;
    private TextView txtFechaFin;
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listafacturas;
    public Calendar myCalendar = Calendar.getInstance();
    final String urlFacturasVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerFacturasVendedor";
    private String FacturaId;
    private boolean guardadoOK = true;
    private DecimalFormat df;
    private boolean isOnline = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listafacturas_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Facturas");
        lv = myView.findViewById(R.id.listfacturas);
        registerForContextMenu(lv);
        lv.setOnItemClickListener((arg0, arg1, position, arg3) -> {
        });
        btnBuscar = myView.findViewById(R.id.btnBuscar);
        btnSincronizar = myView.findViewById(R.id.btnSincronizar);
        txtFechaInicio = myView.findViewById(R.id.txtFechaDesdeF);
        txtFechaFin = myView.findViewById(R.id.txtFechaHastaF);
        lblFooterCantidad = myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_facturas_guardadas, null);
        tvSincroniza = dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        FacturasH = new FacturasHelper(DbOpenHelper.database);
        FacturasLineasH = new FacturasLineasHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
        variables_publicas.Facturas = FacturasH.BuscarFacturasSinconizar();
        txtFechaInicio.setText(getDatePhone());
        txtFechaFin.setText(getDatePhone());
        fecha1 = txtFechaInicio.getText().toString();
        fecha2 = txtFechaFin.getText().toString();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        final DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel2();
        };

        txtFechaInicio.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        txtFechaFin.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            new DatePickerDialog(getActivity(), date2, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        txtBusqueda = myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                btnBuscar.performClick();
            }
            return false;
        });
        listafacturas = new ArrayList<>();

        try{

            new GetListaFacturas().execute();


        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        btnSincronizar.setOnClickListener(v -> SincronizarFactura());

        btnBuscar.setOnClickListener(v -> CargarFacturas());

        return myView;
    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
    }

    private void CargarFacturas() {
        fecha1 = txtFechaInicio.getText().toString();
        fecha2 = txtFechaFin.getText().toString();
        listafacturas.clear();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
        busqueda = txtBusqueda.getText().toString();
        new GetListaFacturas().execute();
    }
    private void ActualizarFooter() {

        try{
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> facturas : listafacturas) {

                if (Objects.requireNonNull(facturas.get("Estado")).equalsIgnoreCase("FACTURADA") ||  Objects.requireNonNull(facturas.get("Estado")).equalsIgnoreCase("Facturado") ) {
                    subtotal += Double.parseDouble(Objects.requireNonNull(facturas.get(variables_publicas.FACTURAS_COLUMN_total)).replace("C$", "").replace(",", ""));
                    cantidad += 1;
                }
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));

        }catch (Exception ex){
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de facturas, Excepcion controlada",variables_publicas.info+" --- "+ex.getMessage(),variables_publicas.correoError,variables_publicas.correosErrores );
            Log.e("Error:",ex.getMessage());
            ex.printStackTrace();
        }


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean SincronizarFactura() {
        try {
            new SincronizardorFacturas().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private boolean AnularFactura(HashMap<String, String> factura) {
          try {
            new AnulaFactura().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    //region ObtieneListaFacturasLocal
    private class GetListaFacturas extends AsyncTask<Void, Void, Void> {
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
                if(getActivity()==null) return null;
                DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                FacturasH = new FacturasHelper(DbOpenHelper.database);
                listafacturas.clear();
                List<HashMap<String, String>> ListaLocal = null;

                ListaLocal = FacturasH.ObtenerFacturasLocales(fecha1,fecha2, busqueda);

                for (HashMap<String, String> item : ListaLocal) {
                    HashMap<String, String> itemfactura = new HashMap<>();
                    itemfactura.put("Factura", item.get("Factura"));
                    itemfactura.put("Estado", item.get("Estado"));
                    itemfactura.put("Cliente", item.get("NombreCliente"));
                    itemfactura.put("Condicion", item.get("FormaPago"));
                    itemfactura.put("Fecha", item.get("Fecha"));
                    itemfactura.put("Ruta", item.get("NombreRuta"));
                    itemfactura.put("Total", item.get(variables_publicas.FACTURAS_COLUMN_total));
                    itemfactura.put("Empresa", item.get(variables_publicas.FACTURAS_COLUMN_empresa));
                    listafacturas.add(itemfactura);
                }
                CheckConnectivity();
                if (isOnline) {
                    GetFacturasService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(() -> {

                        //   for (int i = 0; i < 2; i++) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor. \n Solo se mostraran las facturas locales que no se han sincronizados! ",
                                Toast.LENGTH_LONG).show();
                        //  }
                    });
                }
            } catch (final Exception e) {
                if(getActivity()==null) return null;
                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(() -> {

                    //   for (int i = 0; i < 2; i++) {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No es posible conectarse al servidor. \n Solo se mostraran las facturas locales que no se han sincronizados! ",
                            Toast.LENGTH_LONG).show();
                    //  }
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
            for (HashMap<String, String> item : listafacturas) {
                double subtotal = Double.parseDouble(Objects.requireNonNull(item.get(variables_publicas.FACTURAS_COLUMN_total)).replace("C$", "").replace(",", ""));
                item.put(variables_publicas.FACTURAS_COLUMN_total, df.format(subtotal));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listafacturas,
                    R.layout.list_facturas_guardadas, new String[]{"Factura", "Estado",
                    "Cliente", "Condicion", "Fecha", "Ruta", "Total"},
                    new int[]{R.id.NoFactura, R.id.Estado, R.id.Cliente, R.id.CondicionPago, R.id.Fecha,
                            R.id.Ruta, R.id.TotalFactura}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = currView.findViewById(R.id.tvSincronizar);
                    tvEstado = currView.findViewById(R.id.Estado);
                    if (Objects.requireNonNull(currItem.get("Estado")).equalsIgnoreCase("NO ENVIADA")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                        //tvEstado.setBackgroundColor(Color.parseColor("#FFB9B9B9"));
                        tvEstado.setTextColor(Color.parseColor("#FF6C6C6C"));
                    } else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    if (Objects.equals(currItem.get("Estado"), "ANULADA")) {
                        tvEstado.setTextColor(Color.parseColor("#FFFF0000"));
                    }
                    if (Objects.equals(currItem.get("Estado"), "FACTURADA")) {
                        tvEstado.setTextColor(Color.parseColor("#FF2D8600"));
                    }
                    if (Objects.equals(currItem.get("Estado"), "IMPRESA")) {
                        tvEstado.setTextColor(Color.parseColor("#FFA500"));
                    }
                    return currView;
                }
            };
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ActualizarFooter();

        } catch (final Exception ex) {
            if(getActivity()==null) return ;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "GetListaFacturas OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetFacturasService() throws Exception {
        String CodigoVendedor = variables_publicas.usuario.getCodigo();
        String Empresa=variables_publicas.usuario.getEmpresa_ID();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        busqueda = busqueda.isEmpty() ? "0" : busqueda;
        String Fdesde = fecha1.replace("-","") ,Fhasta=fecha2.replace("-","");
        String urlString = urlFacturasVendedor + "/" + CodigoVendedor + "/" + Fdesde + "/" + Fhasta + "/" + busqueda + "/" + Empresa  + "/" + variables_publicas.rutacargada;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de facturas,Respuesta nula GET", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Facturas = jsonObj.getJSONArray("ObtenerFacturasVendedorResult");

                for (int i = 0; i < Facturas.length(); i++) {
                    JSONObject c = Facturas.getJSONObject(i);
                    String FACTURA = c.getString("Factura");
                    String cliente = c.getString("Cliente");
                    String condicion = c.getString("Condicion");
                    String fecha = c.getString("Fecha");
                    String ruta = c.getString("Ruta");
                    String total = c.getString("Total");
                    String status = c.getString("Estado");
                    String IdCliente = c.getString("IdCliente");

                    HashMap<String, String> facturas = new HashMap<>();

                    facturas.put("Factura", FACTURA);
                    facturas.put("Cliente", cliente);
                    facturas.put("Ruta", ruta);
                    facturas.put("Condicion", condicion);
                    facturas.put("Fecha", fecha);
                    facturas.put("Total", total);
                    facturas.put("Estado", status);
                    facturas.put("IdCliente", IdCliente);
                    listafacturas.add(facturas);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de facturas,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

        }
    }
    //endregion

    private class SincronizardorFacturas extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sincronizando facturas...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            if(getActivity()==null) return null;

            CheckConnectivity();
            if(isOnline){
                List<HashMap<String, String>> FacturasLocal = FacturasH.ObtenerFacturasLocales(fecha1,fecha2, "");
                for (HashMap<String, String> item : FacturasLocal) {
                    Gson gson = new Gson();
                    Cliente cliente = ClientesH.BuscarCliente(item.get(variables_publicas.FACTURAS_COLUMN_cliente));
                    String jsonFactura = gson.toJson(FacturasH.ObtenerFactura(item.get(variables_publicas.FACTURAS_COLUMN_noFactura)));
                    guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarFactura( FacturasH, FacturasLineasH, cliente, item.get(variables_publicas.FACTURAS_COLUMN_noFactura),jsonFactura).split(",")[0]);
                }
            }else{
                getActivity().runOnUiThread(() -> {
                    if(getActivity().isFinishing()) return;
                    Toast.makeText(getActivity().getApplicationContext(),
                            "No es posible conectarse con el servidor, por favor verifique su conexion a internet",
                            Toast.LENGTH_LONG).show();
                });
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {

                // Dismiss the progress dialog
                if ( pDialog.isShowing())
                    pDialog.dismiss();

                btnBuscar.performClick();


            } catch (final Exception ex) {
                if(getActivity()==null) return ;
                getActivity().runOnUiThread(() -> {
                    if(getActivity().isFinishing()) return;
                    Toast.makeText(getActivity().getApplicationContext(),
                            "SincronizarFacturas onPostExecute: " + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        }
    }

    //region ServiceAnularFactura
    private class AnulaFactura  extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Anulando Factura...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(getActivity()==null) return null;
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/AnularFactura/" + FacturaId + "/" + variables_publicas.usuario.getUsuario()+ "/" + variables_publicas.usuario.getEmpresa_ID();

            String encodeUrl = "";
              try {
                URL Url = new URL(url);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCall(encodeUrl);

            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    String resultState = ((String) result.get("AnularFacturaResult")).split(",")[0];
                    final String mensaje = ((String) result.get("AnularFacturaResult")).split(",")[1];
                    if (resultState.equals("false")) {
                        if(getActivity()==null) return null;
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                                mensaje,
                                Toast.LENGTH_LONG).show());

                    } else {
                        HttpHandler shRutas= new HttpHandler();
                        String urlStringRutas= variables_publicas.direccionIp + "/ServicioClientes.svc/GetRutas/" + variables_publicas.usuario.getCodigo();
                        String jsonStrRutas = shRutas.makeServiceCall(urlStringRutas);

                        if (jsonStrRutas == null) {
                            new Funciones().SendMail("Ha ocurrido un error al sincronicar las Rutas, Respuesta nula GET", variables_publicas.info + urlStringRutas, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        }else {

                            RutasHelper Rutas2H;
                            Rutas2H = new RutasHelper(DbOpenHelper.database);
                            Rutas2H.EliminaRutas();
                            JSONObject jsonObjRutas = new JSONObject(jsonStrRutas);
                            // Getting JSON Array node
                            JSONArray rutas = jsonObjRutas.getJSONArray("GetRutasResult");

                            for (int i = 0; i < rutas.length(); i++) {
                                JSONObject c = rutas.getJSONObject(i);
                                String idruta = c.getString("IDRUTA");
                                String ruta = c.getString("RUTA");
                                String vendedor = c.getString("VENDEDOR");
                                String serie = c.getString("SERIE");
                                String orden = c.getString("ORDEN");
                                String recibo = c.getString("RECIBO");
                                String ultrecibo = c.getString("ULTRECIBO");
                                Rutas2H.GuardarRutas(idruta, ruta, vendedor, serie, orden,recibo,ultrecibo);
                            }
                        }

                        HttpHandler shC = new HttpHandler();
                        String urlStringC = variables_publicas.direccionIp + "/ServicioTotalArticulos.svc/ObtenerExistencias"+ "/" + variables_publicas.rutacargada  + "/" + 1;
                        String jsonStrC = shC.makeServiceCall(urlStringC);

                        if (jsonStrC == null) {
                            new Funciones().SendMail("Ha ocurrido un error al sincronizar las Existencias, Respuesta nula GET", variables_publicas.info + urlStringC, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                        }
                        ArticulosHelper Articulos2H;
                        Articulos2H = new ArticulosHelper(DbOpenHelper.database);
                        Articulos2H.EliminaExistencias();
                        assert jsonStrC != null;
                        JSONObject jsonObjC = new JSONObject(jsonStrC);

                        JSONArray existencia = jsonObjC.getJSONArray("ObtenerExistenciasResult");

                        for (int i = 0; i < existencia.length(); i++) {
                            JSONObject c = existencia.getJSONObject(i);

                            String CodArticulo = c.getString("codigo_articulo");
                            String Bodega = c.getString("codigo_bodega");
                            String Existencia = c.getString("existencia");
                            Articulos2H.GuardarTotalExistencias(CodArticulo,Bodega,Existencia);
                        }


                        HttpHandler shC2 = new HttpHandler();
                        String urlGetFacturasPendientes = variables_publicas.direccionIp + "/ServicioRecibos.svc/SpObtieneFacturasSaldoPendiente/";
                        //String urlStringC = urlGetFacturasPendientes + vRuta + "/" + vCliente + "/" + variables_publicas.usuario.getEmpresa_ID();
                        String urlStringC2 = urlGetFacturasPendientes + variables_publicas.usuario.getCodigo() + "/0/" + variables_publicas.usuario.getEmpresa_ID();
                        //String encodeUrl = "";
                        try {
                            URL Url = new URL(urlStringC2);
                            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                            encodeUrl = uri.toURL().toString();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String jsonStrC2 = shC2.makeServiceCall(encodeUrl);

                        if (jsonStrC2 != null) {

                            try {
                                //DbOpenHelper.database.beginTransactionNonExclusive();
                                JSONObject jsonObj = new JSONObject(jsonStrC2);
                                // Getting JSON Array node
                                JSONArray articulos = jsonObj.getJSONArray("SpObtieneFacturasSaldoPendienteResult");
                                if (articulos.length() == 0) {
                                    return null;
                                }
                                FacturasPendientesHelper FacturasPendientesH;
                                FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
                                FacturasPendientesH.EliminaFacturasPendientes();
                                // looping through All Contacts
                                for (int i = 0; i < articulos.length(); i++) {
                                    JSONObject c = articulos.getJSONObject(i);
                                    String codvendedor = c.getString("codvendedor");
                                    String No_Factura = c.getString("No_Factura");
                                    String CodigoCliente = c.getString("CodigoCliente");
                                    String Fecha = c.getString("Fecha");
                                    String Total = c.getString("Total");
                                    String Abono = c.getString("Abono");
                                    String Saldo = c.getString("Saldo");
                                    String Ruta = c.getString("Ruta");
                                    String Guardada = c.getString("Guardada");
                                    FacturasPendientesH.GuardarFacturasPendientes(codvendedor, Fecha, No_Factura, CodigoCliente,  Total, Abono, Saldo,Ruta, Guardada);
                                }
                                FacturasPendientesH.ActualizarSaldoCliente("0");
                                FacturasHelper FacturasH;
                                FacturasH = new FacturasHelper(DbOpenHelper.database);
                                FacturasH.ActualizarFactura(FacturaId,"Anulada");
                            } catch (Exception ex) {
                                Log.e("Error", ex.getMessage());
                                new Funciones().SendMail("Ha ocurrido un error al obtener el listado de facturas pendientes. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
                            }

                        } else {
                            new Funciones().SendMail("Ha ocurrido un error al actualizar listado de facturas pendientes. Ha ocurrido un error al sincronizar las Facturas Pendientes,Respuesta nula", variables_publicas.info + urlStringC, "sisrutas@suplidora.com.ni", variables_publicas.correosErrores);
                        }
                        guardadoOK = true;
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al Anular la Factura,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(() -> {

                        for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    ex.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al obtener la lista de facturas,respuesta nulla GET", variables_publicas.info + url, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < 2; i++) {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "No se ha podido obtener los datos del servidor ",
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
                if (pDialog != null && pDialog.isShowing())
                    pDialog.dismiss();

                btnBuscar.performClick();

            } catch (final Exception ex) {
                if(getActivity()==null) return ;
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity().getApplicationContext(),
                        "Anular Factura onPostExecute: " + ex.getMessage(),
                        Toast.LENGTH_LONG).show());
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

            String HeaderMenu = obj.get("Factura") + "\n" + obj.get("Cliente");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.facturas_list_menu_context, menu);
            menu.getItem(3).setVisible(false);

            if (Objects.requireNonNull(obj.get("Estado")).equalsIgnoreCase("ANULADA") || Objects.requireNonNull(obj.get("Estado")).equalsIgnoreCase("IMPRESA")){
                menu.getItem(1).setEnabled(false); //Anular
                menu.getItem(0).setEnabled(false);//Editar
            } else if (Objects.requireNonNull(obj.get("Estado")).equalsIgnoreCase("FACTURADA")) {
                menu.getItem(1).setEnabled(true);//Anular
                menu.getItem(0).setEnabled(true);//Editar
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> factura = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemAnularFactura:
                    HashMap<String, String> itemFactura = listafacturas.get(info.position);


                    FacturaId = itemFactura.get(variables_publicas.FACTURAS_COLUMN_noFactura);

                    factura = FacturasH.ObtenerFactura(itemFactura.get("Factura"));
                    CodigoCliente = factura.get("Cliente");


                    if (Objects.requireNonNull(itemFactura.get("Estado")).equalsIgnoreCase("NO ENVIADA")) {
                        mensajeAviso("La Factura no está guardada en el servidor; no se puede anular. Por favor verifique su conexión a internet");
                    } else if (Funciones.checkInternetConnection(getActivity())) {

                        final HashMap<String, String> finalFactura = itemFactura;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("¿Está seguro que desea anular la factura?")
                                .setCancelable(false)
                                .setPositiveButton("Si", (dialog, id) -> AnularFactura(finalFactura))
                                .setNegativeButton("No", (dialog, id) -> {
                                    if (pDialog.isShowing())
                                        pDialog.dismiss();
                                })
                                .show();
                    } else {
                        mensajeAviso("No es posible connectarse con el servidor, por favor verifique su conexión a internet");
                    }
                    return true;

                case R.id.itemEditarFactura: {

                    if (listafacturas.size() == 0) {
                        return true;
                    }

                    //Editar
                    HashMap<String, String> obj = listafacturas.get(info.position);

                    if (Objects.requireNonNull(obj.get("Estado")).equalsIgnoreCase("NO ENVIADA")) {
                            factura = FacturasH.ObtenerFactura(obj.get("Factura"));
                            if (factura == null) {
                                Funciones.MensajeAviso(getActivity(), "Esta factura no se puede editar, ya que no fue creada en este dispositivo");
                                return true;
                            }

                            String IdCliente = factura.get("Cliente");
                            Cliente cliente = ClientesH.BuscarCliente(IdCliente);
                            String Nombre = cliente.getNombre();
                            // Starting new intent
                            Intent in = new Intent(getActivity().getApplicationContext(), FacturasActivityNew.class);

                            in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                            in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                            in.putExtra(variables_publicas.FACTURAS_COLUMN_noFactura, obj.get("Factura"));
                            in.putExtra(variables_publicas.vVisualizar,"False");
                            startActivity(in);
                    } else {
                        Funciones.MensajeAviso(getActivity(), "Esta factura no se puede editar.");
                    }


                    return true;
                }
                case R.id.itemVerFactura: {

                    if (listafacturas.size() == 0) {
                        return true;
                    }

                    HashMap<String, String> obj = listafacturas.get(info.position);
                    String fact = obj.get("Factura");
                    factura = FacturasH.ObtenerFactura(fact);
                    if (factura == null) {
                        if (SincronizarDatos.ObtenerFacturaGuardada(fact,FacturasH)){
                            SincronizarDatos.ObtenerFacturaGuardadoDetalle(fact,FacturasLineasH);
                            factura = FacturasH.ObtenerFactura(fact);
                        }else{
                            Funciones.MensajeAviso(getActivity(), "Esta factura no se puede Visualizar. Ocurrió un error al obtener los datos.");
                            return true;
                        }
                    }
                    String IdCliente = factura.get("Cliente");
                    Cliente cliente = ClientesH.BuscarCliente(IdCliente);
                    String Nombre = cliente.getNombre();
                    // Starting new intent
                    Intent in = new Intent(getActivity().getApplicationContext(), FacturasActivityNew.class);

                    in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                    in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                    in.putExtra(variables_publicas.FACTURAS_COLUMN_noFactura, fact);
                    in.putExtra(variables_publicas.vVisualizar,"True");
                    startActivity(in);

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

    public void mensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, (dialog, whichButton) -> {
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void updateLabel() {
        String myFormat = ("yyyy-MM-dd");
        //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaInicio.setText(sdf.format(myCalendar.getTime()));
        btnBuscar.performClick();
    }

    private void updateLabel2() {
        String myFormat = ("yyyy-MM-dd");
        //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaFin.setText(sdf.format(myCalendar.getTime()));
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
    public void onResume() {
        super.onResume();
    }
}
