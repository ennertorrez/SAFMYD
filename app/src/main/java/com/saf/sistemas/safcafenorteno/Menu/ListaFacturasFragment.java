package com.saf.sistemas.safcafenorteno.Menu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.saf.sistemas.safcafenorteno.AccesoDatos.ClientesHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasHelper;
import com.saf.sistemas.safcafenorteno.AccesoDatos.FacturasLineasHelper;
import com.saf.sistemas.safcafenorteno.Auxiliar.Funciones;
import com.saf.sistemas.safcafenorteno.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safcafenorteno.Auxiliar.variables_publicas;
import com.saf.sistemas.safcafenorteno.Entidades.Cliente;
import com.saf.sistemas.safcafenorteno.HttpHandler;
import com.saf.sistemas.safcafenorteno.Pedidos.FacturasActivity;
import com.saf.sistemas.safcafenorteno.R;

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

public class ListaFacturasFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private FacturasHelper FacturasH;
    private FacturasLineasHelper FacturasLineasH;
    private ClientesHelper ClientesH;


    private String TAG = ListaFacturasFragment.class.getSimpleName();
    private String busqueda = "%";
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
    private String jsonAnulaFactura;
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
        lv = (ListView) myView.findViewById(R.id.listfacturas);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        btnSincronizar = (Button) myView.findViewById(R.id.btnSincronizar);
        txtFechaInicio = (EditText) myView.findViewById(R.id.txtFechaDesdeF);
        txtFechaFin = (EditText) myView.findViewById(R.id.txtFechaHastaF);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_facturas_guardadas, null);
        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        FacturasH = new FacturasHelper(DbOpenHelper.database);
        FacturasLineasH = new FacturasLineasHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        variables_publicas.Facturas = FacturasH.BuscarFacturasSinconizar();
        txtFechaInicio.setText(getDatePhone());
        txtFechaFin.setText(getDatePhone());
        fecha1 = txtFechaInicio.getText().toString();
        fecha2 = txtFechaFin.getText().toString();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }
        };

        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date2, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        listafacturas = new ArrayList<>();

        try{

            new GetListaFacturas().execute();


        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SincronizarFactura();
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarFacturas();

            }
        });

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
                subtotal += Double.parseDouble(facturas.get(variables_publicas.FACTURAS_COLUMN_total).replace("C$", "").replace(",", ""));
                if (facturas.get("Estado").equalsIgnoreCase("FACTURADA") || facturas.get("ANULADA").equalsIgnoreCase("Facturado") ) {
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
        Gson gson = new Gson();

        jsonAnulaFactura = gson.toJson(factura);
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
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran las facturas locales que no se han sincronizados! ",
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
                                "No es posible conectarse al servidor. \n Solo se mostraran las facturas locales que no se han sincronizados! ",
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
            for (HashMap<String, String> item : listafacturas) {
                double subtotal = Double.parseDouble(item.get(variables_publicas.FACTURAS_COLUMN_total).replace("C$", "").replace(",", ""));
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
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    tvEstado = (TextView) currView.findViewById(R.id.Estado);
                    if (currItem.get("Estado").equalsIgnoreCase("NO ENVIADA")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                        //tvEstado.setBackgroundColor(Color.parseColor("#FFB9B9B9"));
                        tvEstado.setTextColor(Color.parseColor("#FF6C6C6C"));
                    } else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    if (currItem.get("Estado").equals("ANULADA")) {
                        tvEstado.setTextColor(Color.parseColor("#FFFF0000"));
                    }
                    if (currItem.get("Estado").equals("FACTURADA")) {
                        tvEstado.setTextColor(Color.parseColor("#FF2D8600"));
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
        busqueda = busqueda.isEmpty() ? "%" : busqueda;
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

                    HashMap<String, String> facturas = new HashMap<>();

                    facturas.put("Factura", FACTURA);
                    facturas.put("Cliente", cliente);
                    facturas.put("Ruta", ruta);
                    facturas.put("Condicion", condicion);
                    facturas.put("Fecha", fecha);
                    facturas.put("Total", total);
                    facturas.put("Estado", status);
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
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().isFinishing()) return;
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
                if ( pDialog.isShowing())
                    pDialog.dismiss();

                btnBuscar.performClick();


            } catch (final Exception ex) {
                if(getActivity()==null) return ;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(getActivity().isFinishing()) return;
                        Toast.makeText(getActivity().getApplicationContext(),
                                "SincronizarFacturas onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    //region ServiceAnularFactura
    private class AnulaFactura extends AsyncTask<Void, Void, Void> {
        private String NoFactura;

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
                    String resultState = ((String) result.get("AnularFacturaResult")).split(",")[0];
                    final String mensaje = ((String) result.get("AnularFacturaResult")).split(",")[1];
                    if (resultState.equals("false")) {
                        if(getActivity()==null) return null;
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
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al Anular la Factura,Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    if(getActivity()==null) return null;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener la lista de facturas,respuesta nulla GET", variables_publicas.info + urlStr, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                if(getActivity()==null) return null;
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
                if(getActivity()==null) return ;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Anular Factura onPostExecute: " + ex.getMessage(),
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

            String HeaderMenu = obj.get("Factura") + "\n" + obj.get("Cliente");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.facturas_list_menu_context, menu);
            MenuItem tv = menu.getItem(1); //Boton Eliminar

            if (obj.get("Estado").equalsIgnoreCase("ANULADA")) {
                tv.setEnabled(false);

            } else {
                tv.setEnabled(true);
            }
            if (obj.get("Estado").equalsIgnoreCase("ANULADA") || obj.get("Estado").equalsIgnoreCase("FACTURADA"))
            {
                ((MenuItem) menu.getItem(0)).setEnabled(false);
            }else{
                ((MenuItem) menu.getItem(0)).setEnabled(true);
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
/*
                    fecha1 = txtFechaInicio.getText().toString();
                    fecha2 = txtFechaFin.getText().toString();
                    listafacturas.clear();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();
                    try {
                        new GetListaFacturas().get();
                    } catch (Exception e) {

                    }
                    ActualizarFooter();

*/

                    HashMap<String, String> itemFactura = listafacturas.get(info.position);
//                    factura = FacturasH.ObtenerFactura(itemFactura.get(variables_publicas.FACTURAS_COLUMN_noFactura));

                    FacturaId = itemFactura.get(variables_publicas.FACTURAS_COLUMN_noFactura);
                    if (itemFactura.get("Estado").equalsIgnoreCase("NO ENVIADA")) {
                        //final HashMap<String, String> finalPedido = pedido;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("¿Está seguro que desea anular la factura?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        /*PedidosH.EliminaPedido(IdPedido);
                                        PedidosDetalleH.EliminarDetallePedido(IdPedido);*/
                                        btnBuscar.performClick();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                    }
                                })
                                .show();

                    } else if (Funciones.checkInternetConnection(getActivity())) {

                        final HashMap<String, String> finalFactura = itemFactura;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("¿Está seguro que desea anular la factura?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnularFactura(finalFactura);
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

                case R.id.itemEditarFactura: {
/*                    fecha1 = txtFechaInicio.getText().toString();
                    fecha2 = txtFechaFin.getText().toString();
                    listafacturas.clear();
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();
                    try {
                        new GetListaFacturas().get();
                    } catch (Exception e) {

                    }
                    ActualizarFooter();*/

                    if (listafacturas.size() == 0) {
                        return true;
                    }

                    //Editar
                    HashMap<String, String> obj = listafacturas.get(info.position);

                    if (obj.get("Estado").equalsIgnoreCase("NO ENVIADA")) {
                            factura = FacturasH.ObtenerFactura(obj.get("Factura"));
                            if (factura == null) {
                                Funciones.MensajeAviso(getActivity(), "Esta factura no se puede editar, ya que no fue creada en este dispositivo");
                                return true;
                            }

                            String IdCliente = factura.get("Cliente");
                            Cliente cliente = ClientesH.BuscarCliente(IdCliente);
                            String Nombre = cliente.getNombre();
                            // Starting new intent
                            Intent in = new Intent(getActivity().getApplicationContext(), FacturasActivity.class);

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

/*                    fecha1 = txtFechaInicio.getText().toString();
                    fecha2 = txtFechaFin.getText().toString();
                    listafacturas.clear();
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();
*//*                    try {
                        new GetListaFacturas().execute().get();
                    } catch (Exception e) {

                    }*//*
                    ActualizarFooter();*/

                    if (listafacturas.size() == 0) {
                        return true;
                    }

                    //Visualizar
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
                    String IdCliente = factura.get("IdCliente");
                    Cliente cliente = ClientesH.BuscarCliente(IdCliente);
                    String Nombre = cliente.getNombre();
                    // Starting new intent
                    Intent in = new Intent(getActivity().getApplicationContext(), FacturasActivity.class);

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
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    private void updateLabel() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaInicio.setText(sdf.format(myCalendar.getTime()));
        btnBuscar.performClick();
    }

    private void updateLabel2() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
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
