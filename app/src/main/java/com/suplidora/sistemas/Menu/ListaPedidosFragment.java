package com.suplidora.sistemas.Menu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.Auxiliar.variables_publicas;
import com.suplidora.sistemas.HttpHandler;
import com.suplidora.sistemas.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;


/**
 * Created by usuario on 20/3/2017.
 */

public class ListaPedidosFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private PedidosHelper PedidosH;
    private String TAG = ListaPedidosFragment.class.getSimpleName();
    private String busqueda = "";
    private String fecha = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooter;
    private TextView tvSincroniza;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private TextView txtFechaPedido;
    public static ArrayList<HashMap<String, String>> listapedidos;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;

    final String urlPedidosVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/GetConfiguraciones";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView= inflater.inflate(R.layout.listapedidos_layout,container,false);
        getActivity().setTitle("Lista de Pedidos");
        lv = (ListView) myView.findViewById(R.id.listpedidosdia);
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        txtFechaPedido = (EditText) myView.findViewById(R.id.txtFechaPedido);
        lblFooter = (TextView) myView.findViewById(R.id.lblFooter);

       // txtFechaPedido.setText(getDatePhone());

        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_pedidos_guardados, null);

        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        PedidosH = new PedidosHelper(DbOpenHelper.database);
        variables_publicas.Pedidos = PedidosH.BuscarPedidosSinconizar();
        fecha = getDatePhone();


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
            }
        });

        /******/

        txtBusqueda = (EditText)myView.findViewById(R.id.txtBusqueda);
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
        new GetPedidoVendedor().execute();
        lblFooter.setText("Cliente encontrados: " + String.valueOf(listapedidos.size()));

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fecha = txtFechaPedido.getText().toString();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                busqueda = txtBusqueda.getText().toString();
                try{
                    new GetListaPedidos().execute().get();}
                catch (Exception e){
                    mensajeAviso(e.getMessage());
                }
                lblFooter.setText("Pedidos encontrados: " + String.valueOf(listapedidos.size()));
            }
        });

        return myView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //region ObtienePedidoVendedor
    private class GetPedidoVendedor extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            String urlString = urlPedidosVendedor+"/"+variables_publicas.CodigoVendedor+"/"+getDatePhone();
            String jsonStr = sh.makeServiceCall(urlString);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {

                try {
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

                        pedidos.put("FACTURA",FACTURA);
                        pedidos.put("StatusPedido",StatusPedido);
                        pedidos.put("cliente",cliente);
                        pedidos.put("condicion",condicion);
                        pedidos.put("fecha",fecha);
                        pedidos.put("pedido",pedido);
                        pedidos.put("total",total);
                        listapedidos.add(pedidos);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    mensajeAviso(e.getMessage());
                }
            } else {

                Log.e(TAG, "Couldn't get json from server.");
               mensajeAviso("Couldn't get json from server. Check LogCat for possible errors!");
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            adapter = new SimpleAdapter(
                    getActivity(), listapedidos,
                    R.layout.list_pedidos_guardados, new String[]{"FACTURA", "StatusPedido",
                    "cliente","condicion","fecha","pedido","total"},
                    new int[]{R.id.Factura,R.id.Estado,R.id.Cliente,R.id.CondicionPago,R.id.Fecha,
                            R.id.CodigoPedido, R.id.TotalPedido}){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    if (currItem.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido).startsWith("-")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    }
                    else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    return currView;
                }
            };

            lv.setAdapter(adapter);
            lblFooter.setText("Pedidos Encontrados: " + String.valueOf(listapedidos.size()));
        }
    }
    //endregion

    //region ObtieneListaPedidos
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
                    listapedidos = PedidosH.ObtenerPedidosXfechaNomb(fecha,busqueda);
                } catch (final Exception e) {
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
            /**
             * Updating parsed JSON data into ListView
             * */
            adapter = new SimpleAdapter(
                    getActivity(), listapedidos,
                    R.layout.list_pedidos_guardados, new String[]{"00000000","No enviado",
                    variables_publicas.CLIENTES_COLUMN_Nombre,variables_publicas.FORMA_PAGO_COLUMN_NOMBRE,
                    variables_publicas.PEDIDOS_COLUMN_Fecha,variables_publicas.PEDIDOS_COLUMN_CodigoPedido,variables_publicas.PEDIDOS_COLUMN_Total},
                    new int[]{R.id.Factura,R.id.Estado,R.id.Cliente,R.id.CondicionPago,R.id.Fecha,
                            R.id.CodigoPedido,R.id.TotalPedido}){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    if (currItem.get(variables_publicas.PEDIDOS_DETALLE_COLUMN_CodigoPedido).startsWith("-")) {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_red));
                    }
                    else {
                        tvSincroniza.setBackground(getResources().getDrawable(R.drawable.rounded_corner_green));
                    }
                    return currView;
                }
            };

            lv.setAdapter(adapter);
            lblFooter.setText("Pedidos Encontrados: " + String.valueOf(listapedidos.size()));
        }
    }
    //endregion

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
        String myFormat = ("yyyy-MM-dd");; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaPedido.setText(sdf.format(myCalendar.getTime()));
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }
}