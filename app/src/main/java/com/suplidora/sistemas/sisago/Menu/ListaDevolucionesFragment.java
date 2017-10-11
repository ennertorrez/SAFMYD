package com.suplidora.sistemas.sisago.Menu;

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
import android.support.annotation.Nullable;
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
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DevolucionesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;

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


/**
 * Created by usuario on 20/3/2017.
 */

public class ListaDevolucionesFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private DevolucionesHelper DevolucionesH;
    private DevolucionesDetalleHelper DevolucionesDetalleH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;

    private String TAG = ListaDevolucionesFragment.class.getSimpleName();
    private String busqueda = "%";
    private String fecha = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView tvSincroniza;
    private TextView tvEstado;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private Button btnSincronizar;
    private TextView txtFechaDevolucion;
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listadevoluciones;
    public Calendar myCalendar = Calendar.getInstance();
    //  private SimpleAdapter adapter;
    final String urlDevolucionesVehiculo = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidosVendedor";
    final String urlAnularPedido = variables_publicas.direccionIp + "/ServicioPedidos.svc/AnularPedido";
    //AnularPedido/{Pedido}/{Usuario}
    private String jsonPedido;
    private String jsonAnulaDevolucion;
    private String IdPedido;
    private Cliente Clientes;
    private String IdVendedor;
    private boolean guardadoOK = true;
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listadevoluciones_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Pedidos");
        lv = (ListView) myView.findViewById(R.id.listdevolucionesdia);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        btnSincronizar = (Button) myView.findViewById(R.id.btnSincronizar);
        txtFechaDevolucion = (EditText) myView.findViewById(R.id.txtFechaDevolucion);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_devoluciones_guardados, null);
        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        DevolucionesH = new DevolucionesHelper(DbOpenHelper.database);
        DevolucionesDetalleH = new DevolucionesDetalleHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        variables_publicas.Devoluciones = DevolucionesH.BuscarDevolucionesSinconizar();
        txtFechaDevolucion.setText(getDatePhone());
        fecha = txtFechaDevolucion.getText().toString();


        /***DatePicker***/
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
        txtFechaDevolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                /*InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.RESULT_HIDDEN);*/
            }
        });

        /******/

        txtBusqueda = (EditText) myView.findViewById(R.id.txtBusqueda);
        txtBusqueda.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    btnBuscar.performClick();
                }
                return false;
            }
        });
        listadevoluciones = new ArrayList<>();

        try{
            new GetListaDevoluciones().execute();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new Funciones().checkInternetConnection(getActivity())) {
                    SincronizarDevolucion();

                } else {
                    mensajeAviso("Verifique su conexion a internet");
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarPedidos();

            }
        });

        return myView;
    }

    private void CargarPedidos() {
        fecha = txtFechaDevolucion.getText().toString();
        listadevoluciones.clear();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
        busqueda = txtBusqueda.getText().toString();
        new GetListaDevoluciones().execute();
    }

    private void ActualizarFooter() {

        try{
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> devolucion : listadevoluciones) {
                subtotal += Double.parseDouble(devolucion.get(variables_publicas.DEVOLUCIONES_COLUMN_subtotal).replace("C$", "").replace(",", ""));
                if (devolucion.get("Estado").equalsIgnoreCase("Aprobado") || devolucion.get("Estado").equalsIgnoreCase("Facturado") || devolucion.get("Estado").equalsIgnoreCase("Pendiente")) {
                    cantidad += 1;
                }
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        }catch (Exception ex){
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de devoluciones, Excepcion controlada",variables_publicas.info+" --- "+ex.getMessage(),variables_publicas.correoError,variables_publicas.correosErrores );
            Log.e("Error:",ex.getMessage());
            ex.printStackTrace();
        }


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean SincronizarDevolucion() {
        try {
            new SincronizardorDevoluciones().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private boolean AnularDevolucion(HashMap<String, String> devolucion) {
        Gson gson = new Gson();

        jsonAnulaDevolucion = gson.toJson(devolucion);
        try {
            new AnulaDevolucion().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    //region ObtieneListaDevolucionesLocal
    private class GetListaDevoluciones extends AsyncTask<Void, Void, Void> {
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
                DevolucionesH = new DevolucionesHelper(DbOpenHelper.database);
                listadevoluciones.clear();
                List<HashMap<String, String>> ListaLocal = null;

                ListaLocal = DevolucionesH.ObtenerDevolucionesLocales(fecha, busqueda);

                for (HashMap<String, String> item : ListaLocal) {
                    HashMap<String, String> itempedido = new HashMap<>();
                    /*itempedido.put("Factura", item.get("Factura"));
                    itempedido.put("Estado", item.get("Estado"));
                    itempedido.put("Detallista", item.get("Detallista"));
                    itempedido.put("NombreCliente", item.get("NombreCliente"));
                    itempedido.put("FormaPago", item.get("FormaPago"));
                    itempedido.put("Fecha", item.get("Fecha"));
                    itempedido.put("CodigoPedido", item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
                    itempedido.put("Total", item.get(variables_publicas.PEDIDOS_COLUMN_Subtotal));*/
                    listadevoluciones.add(item);
                }
                boolean connectionOK = Funciones.TestInternetConectivity();
                if (connectionOK) {
                    GetDevolucionService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran las devoluciones locales que no se han sincronizados! ",
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
                                "No es posible conectarse al servidor. \n Solo se mostraran las devoluciones locales que no se han sincronizados! ",
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
            /**
             * Updating parsed JSON data into ListView
             * */

            DecimalFormat df = new DecimalFormat("C$ #0.00");
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            df.setDecimalFormatSymbols(fmts);
            for (HashMap<String, String> item : listadevoluciones) {
                double subtotal = Double.parseDouble(item.get(variables_publicas.DEVOLUCIONES_COLUMN_subtotal).replace("C$", "").replace(",", ""));
                item.put(variables_publicas.DEVOLUCIONES_COLUMN_subtotal, df.format(subtotal));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listadevoluciones,
                    R.layout.list_devoluciones_guardados, new String[]{"Factura", "Estado",
                    "NombreCliente", "FormaPago", "Fecha", variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion, variables_publicas.DEVOLUCIONES_COLUMN_subtotal},
                    new int[]{R.id.Factura, R.id.Estado, R.id.Cliente, R.id.CondicionPago, R.id.Fecha,
                            R.id.CodigoPedido, R.id.TotalPedido}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    tvEstado = (TextView) currView.findViewById(R.id.Estado);
                    if (currItem.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido).startsWith("-")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                        //tvEstado.setBackgroundColor(Color.parseColor("#FFB9B9B9"));
                        tvEstado.setTextColor(Color.parseColor("#FF6C6C6C"));
                    } else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    if (currItem.get("Estado").equals("PENDIENTE")) {
                        tvEstado.setTextColor(Color.parseColor("#FFBF5300"));
                    }
                    if (currItem.get("Estado").equals("APROBADO")) {
                        tvEstado.setTextColor(Color.parseColor("#303F9F"));
                    }
                    if (currItem.get("Estado").equals("ANULADO")) {
                        tvEstado.setTextColor(Color.parseColor("#FFFF0000"));
                    }
                    if (currItem.get("Estado").equals("FACTURADO")) {
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
                            "GetListaDevoluciones OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetDevolucionService() throws Exception {
        String CodigoVehiculo = variables_publicas.usuario.getCodigo();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        busqueda = busqueda.isEmpty() ? "%" : busqueda;
        String urlString = urlDevolucionesVehiculo + "/" + CodigoVehiculo + "/" + fecha + "/" + busqueda;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de devoluciones,Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Devoluciones = jsonObj.getJSONArray("ObtenerDevolucionesVehiculoResult");

                for (int i = 0; i < Devoluciones.length(); i++) {
                    JSONObject c = Devoluciones.getJSONObject(i);
                    /*String FACTURA = c.getString("FACTURA");
                    String StatusPedido = c.getString("StatusPedido");
                    String cliente = c.getString("cliente");
                    String condicion = c.getString("condicion");
                    String fecha = c.getString("fecha");
                    String pedido = c.getString("pedido");
                    String subtotal = c.getString("subtotal");
                    String total = c.getString("total");
                    String detallista = c.getString("detallista");*/


                    HashMap<String, String> devoluciones = new HashMap<>();

                    /*devoluciones.put("Factura", FACTURA);
                    devoluciones.put("Estado", StatusPedido);
                    devoluciones.put("NombreCliente", cliente);
                    devoluciones.put("FormaPago", condicion);
                    devoluciones.put("Fecha", fecha);
                    devoluciones.put("CodigoPedido", pedido);
                    devoluciones.put("subtotal", subtotal);
                    devoluciones.put("Total", total);
                    devoluciones.put("Detallista", detallista);*/
                    listadevoluciones.add(devoluciones);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de devoluciones,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    //endregion

    private class SincronizardorDevoluciones extends AsyncTask<Void, Void, Void> {
        private String NoPedido;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Sincronizando datos...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            if(getActivity()==null) return null;
            List<HashMap<String, String>> DevolucionesLocal = DevolucionesH.ObtenerDevolucionesLocales(fecha, "");
            for (HashMap<String, String> item : DevolucionesLocal) {
                if (guardadoOK == false) {
                    break;
                }
                Gson gson = new Gson();

                //Cambiar
                /*Vendedor vendedor = VendedoresH.ObtenerVendedor(item.get(variables_publicas.PEDIDOS_COLUMN_IdVendedor));
                Cliente cliente = ClientesH.BuscarCliente(item.get(variables_publicas.PEDIDOS_COLUMN_IdCliente), item.get(variables_publicas.PEDIDOS_COLUMN_Cod_cv));
                String jsonDevolucion = gson.toJson(DevolucionesH.ObtenerDevolucion(item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido)));
                guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarDevolucion( DevolucionesH, DevolucionesDetalleH, vendedor, cliente, item.get(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion), jsonPedido, false).split(",")[0]);*/
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
                                "SincronizarPedidos onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    //region ServiceAnularPedido
    private class AnulaDevolucion extends AsyncTask<Void, Void, Void> {
        private String NoPedido;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Anulando Pedido...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(getActivity()==null) return null;
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/AnularPedido/" + IdPedido + "/" + variables_publicas.usuario.getUsuario();

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
                    String resultState = ((String) result.get("AnularPedidoResult")).split(",")[0];
                    final String mensaje = ((String) result.get("AnularPedidoResult")).split(",")[1];
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
                    new Funciones().SendMail("Ha ocurrido un error al Anular pedido,Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de pedidos,respuesta nulla GET", variables_publicas.info + urlStr, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
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
                                "Anular Pedido onPostExecute: " + ex.getMessage(),
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

            String HeaderMenu = obj.get("CodigoPedido") + "\n" + obj.get("NombreCliente");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.pedidos_list_menu_context, menu);
            MenuItem tv = menu.getItem(1); //Boton Eliminar
            if (!obj.get("CodigoPedido").startsWith("-"))
                tv.setTitle("Anular Pedido");

            if (!obj.get("Factura").equalsIgnoreCase("") || obj.get("Estado").equalsIgnoreCase("Anulado")) {
                tv.setEnabled(false);

            } else {
                tv.setEnabled(true);
            }
            if (obj.get("Estado").equalsIgnoreCase("ANULADO") || !obj.get("Factura").equalsIgnoreCase("")
                    || (obj.get("Estado").equalsIgnoreCase("APROBADO") && obj.get("Detallista").equalsIgnoreCase("false")) )
            {
                //Ponemos el boton Editar en falso
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
        HashMap<String, String> pedido = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemEliminarPedido:
                    fecha = txtFechaDevolucion.getText().toString();
                    listadevoluciones.clear();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();
                    new GetListaDevoluciones().execute().get();
                    ActualizarFooter();

                    HashMap<String, String> itemPedido = listadevoluciones.get(info.position);
                    pedido = DevolucionesH.ObtenerDevolucion(itemPedido.get(variables_publicas.DEVOLUCIONES_COLUMN_ndevolucion));

                    IdPedido = itemPedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido);
                    if (itemPedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido).startsWith("-")) {
                        final HashMap<String, String> finalPedido = pedido;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea eliminar el pedido?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        DevolucionesH.EliminaDevolucion(IdPedido);
                                        DevolucionesDetalleH.EliminarDetalleDevolucion(IdPedido);
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

                    } else if (new Funciones().checkInternetConnection(getActivity())) {

                        final HashMap<String, String> finalPedido = itemPedido;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea anular el pedido?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnularDevolucion(finalPedido);
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

                case R.id.itemEditarPedido: {
                    fecha = txtFechaDevolucion.getText().toString();
                    listadevoluciones.clear();
                    inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();
                    try {
                        new GetListaDevoluciones().execute().get();
                    } catch (Exception e) {

                    }
                    ActualizarFooter();

                    if (listadevoluciones.size() == 0) {
                        return true;
                    }

                    //Editar
                    HashMap<String, String> obj = listadevoluciones.get(info.position);
                    String ndevolucion = obj.get("ndevolucion");
                    if (obj.get("Factura").equalsIgnoreCase("")) {
                        if (((obj.get("FormaPago").equalsIgnoreCase("Contado") || obj.get("FormaPago").equalsIgnoreCase("CONTADO (*)")) && (obj.get("Estado").equalsIgnoreCase("Aprobado")) || obj.get("Estado").equalsIgnoreCase("NO ENVIADO") || obj.get("Estado").equalsIgnoreCase("Pendiente"))) {

                            pedido = DevolucionesH.ObtenerDevolucion(ndevolucion);
                            if (pedido == null) {
                                Funciones.MensajeAviso(getActivity(), "Este pedido no se puede editar, ya que no fue creado en este dispositivo");
                                return true;
                            }

                            String IdCliente = pedido.get("IdCliente");
                            String CodCv = pedido.get("Cod_cv");
                            Cliente cliente = ClientesH.BuscarCliente(IdCliente, CodCv);
                            String Nombre = cliente.getNombreCliente();
                            // Starting new intent
                            Intent in = new Intent(getActivity().getApplicationContext(), PedidosActivity.class);

                            in.putExtra(variables_publicas.CLIENTES_COLUMN_IdCliente, IdCliente);
                            in.putExtra(variables_publicas.CLIENTES_COLUMN_Nombre, Nombre);
                            in.putExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, ndevolucion);
                            in.putExtra(variables_publicas.CLIENTES_COLUMN_CodCv, CodCv);
                            startActivity(in);
                        }
                    } else {
                        Funciones.MensajeAviso(getActivity(), "Este pedido no se puede anular, ya que fue facturado");
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
        txtFechaDevolucion.setText(sdf.format(myCalendar.getTime()));
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
       /* try {
            CargarPedidos();

        } catch (Exception ex) {
                Log.e("Error",ex.getMessage());
        }*/
    }
}