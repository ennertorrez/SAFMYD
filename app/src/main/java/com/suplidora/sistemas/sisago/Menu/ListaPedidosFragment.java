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
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONException;
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

import static com.suplidora.sistemas.sisago.Auxiliar.Funciones.Codificar;


/**
 * Created by usuario on 20/3/2017.
 */

public class ListaPedidosFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private PedidosHelper PedidosH;
    private PedidosDetalleHelper PedidosDetalleH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;

    private String TAG = ListaPedidosFragment.class.getSimpleName();
    private String busqueda = "%";
    private String fecha = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private TextView tvSincroniza;
    private TextView tvEstado;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private Button btnSincronizar;
    private TextView txtFechaPedido;
    public static ArrayList<HashMap<String, String>> listapedidos;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;

    final String urlPedidosVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidosVendedor";
    final String urlAnularPedido = variables_publicas.direccionIp + "/ServicioPedidos.svc/AnularPedido";
    //AnularPedido/{Pedido}/{Usuario}
    private String jsonPedido;
    private String jsonAnulaPedido;
    private String IdPedido;
    private Cliente Clientes;
    private String IdVendedor;
    private boolean guardadoOK = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listapedidos_layout, container, false);
        getActivity().setTitle("Lista de Pedidos");
        lv = (ListView) myView.findViewById(R.id.listpedidosdia);
        registerForContextMenu(lv);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        btnSincronizar = (Button) myView.findViewById(R.id.btnSincronizar);
        txtFechaPedido = (EditText) myView.findViewById(R.id.txtFechaPedido);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);

        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_pedidos_guardados, null);
        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        PedidosH = new PedidosHelper(DbOpenHelper.database);
        PedidosDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        ClientesH = new ClientesHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        variables_publicas.Pedidos = PedidosH.BuscarPedidosSinconizar();
        txtFechaPedido.setText(getDatePhone());
        fecha = txtFechaPedido.getText().toString();


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
        txtFechaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
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
        listapedidos = new ArrayList<>();

        new GetListaPedidos().execute();

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Funciones.checkInternetConnection(getActivity())) {
                    List<HashMap<String, String>> PedidosLocal = PedidosH.ObtenerPedidosLocales(fecha, "");
                    for (HashMap<String, String> item : PedidosLocal) {
                        Clientes = ClientesH.BuscarCliente(item.get(variables_publicas.PEDIDOS_COLUMN_IdCliente));
                        if (Clientes == null) {
                            mensajeAviso("No se ha podido obtener datos del clientes");
                            break;
                        }
                        IdVendedor = Clientes.getIdVendedor();
                        HashMap<String, String> pedido = PedidosH.ObtenerPedido(item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
                        IdPedido = pedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido);
                        SincronizarPedido(pedido);
                        if (guardadoOK == false) {
                            mensajeAviso("No se ha podido sincronizar el pedido: " + item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
                            break;
                        }
                    }
                    btnBuscar.performClick();
                } else {
                    mensajeAviso("Verifique su conexion a internet");
                }
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = txtFechaPedido.getText().toString();
                listapedidos.clear();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                new GetListaPedidos().execute();
                lblFooter.setText("Pedidos encontrados: " + String.valueOf(listapedidos.size()));
            }
        });

        return myView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean SincronizarPedido(HashMap<String, String> pedido) {
        Gson gson = new Gson();

        jsonPedido = gson.toJson(pedido);
        try {
            new SincronizardorPedidos().execute().get();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }

        return false;
    }

    private boolean AnularPedido(HashMap<String, String> pedido) {
        Gson gson = new Gson();

        jsonAnulaPedido = gson.toJson(pedido);
        try {
            new AnulaPedido().execute().get();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }

        return false;
    }

    //region ObtieneListaPedidosLocal
    private class GetListaPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
                PedidosH = new PedidosHelper(DbOpenHelper.database);

                List<HashMap<String, String>> ListaLocal = null;

                ListaLocal = PedidosH.ObtenerPedidosLocales(fecha, busqueda);

                for (HashMap<String, String> item : ListaLocal) {
                    HashMap<String, String> itempedido = new HashMap<>();
                    itempedido.put("Factura", item.get("Factura"));
                    itempedido.put("Estado", item.get("Estado"));
                    itempedido.put("NombreCliente", item.get("NombreCliente"));
                    itempedido.put("FormaPago", item.get("FormaPago"));
                    itempedido.put("Fecha", item.get("Fecha"));
                    itempedido.put("CodigoPedido", item.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
                    itempedido.put("Total", item.get(variables_publicas.PEDIDOS_COLUMN_Total));
                    listapedidos.add(item);
                }
                GetPedidosService();
            } catch (final Exception e) {
                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran los pedidos locales que no se han sincronizados! ",
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
            lblFooter.setText("Cliente encontrados: " + String.valueOf(listapedidos.size()));
            // Dismiss the progress dialog
            if (pDialog.isShowing())
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
            for (HashMap<String, String> item : listapedidos) {
                double total = Double.parseDouble(item.get(variables_publicas.PEDIDOS_COLUMN_Total));
                item.put(variables_publicas.PEDIDOS_COLUMN_Total, df.format(total));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listapedidos,
                    R.layout.list_pedidos_guardados, new String[]{"Factura", "Estado",
                    "NombreCliente", "FormaPago", "Fecha", variables_publicas.PEDIDOS_COLUMN_CodigoPedido, variables_publicas.PEDIDOS_COLUMN_Total},
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
            lblFooter.setText("Pedidos Encontrados: " + String.valueOf(listapedidos.size()));
        }
    }

    private void GetPedidosService() throws Exception {
        String CodigoVendedor = variables_publicas.usuario.getCodigo();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        busqueda = busqueda.isEmpty() ? "%" : busqueda;
        String urlString = urlPedidosVendedor + "/" + CodigoVendedor + "/" + fecha + "/" + busqueda;
        try {
            URL Url = new URL(urlString);
            URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
            encodeUrl = uri.toURL().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jsonStr = sh.makeServiceCall(encodeUrl);
        Log.e(TAG, "Response from url: " + jsonStr);

        JSONObject jsonObj = new JSONObject(jsonStr);
        // Getting JSON Array node
        JSONArray Pedidos = jsonObj.getJSONArray("ObtenerPedidosVendedorResult");

        for (int i = 0; i < Pedidos.length(); i++) {
            JSONObject c = Pedidos.getJSONObject(i);
            String FACTURA = c.getString("FACTURA");
            String StatusPedido = c.getString("StatusPedido");
            String cliente = c.getString("cliente");
            String condicion = c.getString("condicion");
            String fecha = c.getString("fecha");
            String pedido = c.getString("pedido");
            String total = c.getString("total");

            HashMap<String, String> pedidos = new HashMap<>();

            pedidos.put("Factura", FACTURA);
            pedidos.put("Estado", StatusPedido);
            pedidos.put("NombreCliente", cliente);
            pedidos.put("FormaPago", condicion);
            pedidos.put("Fecha", fecha);
            pedidos.put("CodigoPedido", pedido);
            pedidos.put("Total", total);
            listapedidos.add(pedidos);
        }
    }
    //endregion

    private class SincronizardorPedidos extends AsyncTask<Void, Void, Void> {
        private String NoPedido;

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedido/";
            String urlString = url + jsonPedido;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCallPost(encodeUrl);


            /**********************************Actualizamos los datos del pedido**************************************/
            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    NoPedido = (String) result.get("SincronizarPedidoResult");
                    if (NoPedido.equals("false")) {
                        guardadoOK = false;
                        return null;
                    }
                    PedidosH.ActualizarPedido(IdPedido, NoPedido);
                    PedidosDetalleH.ActualizarCodigoPedido(IdPedido, NoPedido);

                    Gson gson = new Gson();
                    List<HashMap<String, String>> pedidoDetalle = PedidosDetalleH.ObtenerPedidoDetalle(NoPedido);
                    for (HashMap<String, String> item : pedidoDetalle) {
                        item.put("SubTotal", item.get("SubTotal").replace(",", ""));
                        item.put("Costo", item.get("Costo").replace(",", ""));
                        item.put("Total", item.get("Total").replace(",", ""));
                        item.put("Iva", item.get("Iva").replace(",", ""));
                        item.put("Precio", item.get("Precio").replace(",", ""));
                        item.put("Descuento", item.get("Descuento").replace(",", ""));
                        item.put("Descripcion", Codificar(item.get("Descripcion")));
                    }
                    String jsonPedidoDetalle = gson.toJson(pedidoDetalle);
                    //    jsonPedidoDetalle = URLEncoder.encode(jsonPedidoDetalle,"UTF-8");
                    final String urlDetalle = variables_publicas.direccionIp + "/ServicioPedidos.svc/SincronizarPedidoDetalle/";
                    String urlStringDetalle = urlDetalle + Clientes.getCodigoLetra() + "/" + IdVendedor + "/" + jsonPedidoDetalle;

                    try {
                        URL Url = new URL(urlStringDetalle);
                        URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                        encodeUrl = uri.toURL().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String jsonStrDetalle = sh.makeServiceCallPost(encodeUrl);
                    if (jsonStrDetalle == null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 2; i++) {
                                    Toast.makeText(getActivity().getApplicationContext(),
                                            "Ha ocurrido un error al sincronizar el detalle del pedido",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        result = new JSONObject(jsonStrDetalle);
                        // Getting JSON Array node
                        guardadoOK = ((String) result.get("SincronizarPedidoDetalleResult")).equalsIgnoreCase("true");
                    }
                } catch (final Exception ex) {
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
                guardadoOK = false;
            }
            return null;
        }
    }

    //region ServiceAnularPedido
    private class AnulaPedido extends AsyncTask<Void, Void, Void> {

        private String NoPedido;

        @Override
        protected Void doInBackground(Void... params) {
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioPedidos.svc/AnularPedido/" + IdPedido + "/" + variables_publicas.usuario.getUsuario();

            String urlString = url + jsonAnulaPedido;
            String urlStr = urlString;
            String encodeUrl = "";
            try {
                URL Url = new URL(urlStr);
                URI uri = new URI(Url.getProtocol(), Url.getUserInfo(), Url.getHost(), Url.getPort(), Url.getPath(), Url.getQuery(), Url.getRef());
                encodeUrl = uri.toURL().toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonStr = sh.makeServiceCallPost(encodeUrl);

            if (jsonStr != null) {
                try {
                    JSONObject result = new JSONObject(jsonStr);
                    // Getting JSON Array node


                } catch (final Exception ex) {
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
                guardadoOK = false;
            }
            return null;
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
            inflater.inflate(R.menu.eliminar_pedido, menu);
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();


            switch (item.getItemId()) {
                case R.id.Elimina_pedido:

                    HashMap<String, String> itemPedido = listapedidos.get(info.position);
                    //listapedidos.remove(info.position);
                    if (itemPedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido).startsWith("-"))
                {
                    HashMap<String, String> pedido = PedidosH.ObtenerPedido(itemPedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido));
                    IdPedido = pedido.get(variables_publicas.PEDIDOS_COLUMN_CodigoPedido);
                    PedidosH.EliminaPedidos(IdPedido);
                    PedidosDetalleH.EliminarDetallePedido(IdPedido);
                } else if (Funciones.checkInternetConnection(getActivity())) {

                }

                btnBuscar.performClick();
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
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
        txtFechaPedido.setText(sdf.format(myCalendar.getTime()));
        btnBuscar.performClick();
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}