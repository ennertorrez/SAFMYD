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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.ClientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.ClienteSucursal;
import com.suplidora.sistemas.sisago.Entidades.FormaPago;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.Entidades.Ruta;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Pedidos.PedidosActivity;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.select.Evaluator;

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

public class ListaPedidosSupFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private PedidosHelper PedidosH;
    private PedidosDetalleHelper PedidosDetalleH;
    private ClientesHelper ClientesH;
    private VendedoresHelper VendedoresH;

    private Ruta ruta = null;
    private String CodigoSupervisor;

    private String TAG = ListaPedidosSupFragment.class.getSimpleName();
    private String busqueda = "%";
    private String fechadesde = "";
    private String fechahasta = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView tvEstado;
    private Button btnBuscaVentaVendedor;
    private Button btnSincronizar;
    private TextView txtFechaDesde;
    private TextView txtFechaHasta;
    private Spinner cboRuta;
    public static ArrayList<HashMap<String, String>> listapedidos;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;
    final String urlPedidosVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidosSupervisor";
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listaresumenpreventa_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Resumen Pre-Venta");
        lv = (ListView) myView.findViewById(R.id.listResumenPreventa);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscaVentaVendedor = (Button) myView.findViewById(R.id.btnBuscaVentaVendedor);
        txtFechaDesde = (EditText) myView.findViewById(R.id.txtFechaDesde);
        txtFechaHasta = (EditText) myView.findViewById(R.id.txtFechaHasta);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.resumen_preventa_list_item, null);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);
        cboRuta = (Spinner) myView.findViewById(R.id.cboRuta);

        final Spinner cboRuta = (Spinner) myView.findViewById(R.id.cboRuta);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        PedidosH = new PedidosHelper(DbOpenHelper.database);
        PedidosDetalleH = new PedidosDetalleHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        variables_publicas.Pedidos = PedidosH.BuscarPedidosSinconizar();
        txtFechaDesde.setText(getDatePhone());
        fechadesde = txtFechaDesde.getText().toString();

        txtFechaHasta.setText(getDatePhone());
        fechahasta = txtFechaHasta.getText().toString();

        if(variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor"))
        {
            CodigoSupervisor = variables_publicas.usuario.getCodigo();
        }
        else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")){
            Vendedor IdSupervisor = VendedoresH.ObtenerVendedor(variables_publicas.usuario.getCodigo());
            CodigoSupervisor = IdSupervisor.getCodsuper();
        }
        else
        {
            CodigoSupervisor = "0";
        }

        CargaDatosCombo();


        /***DatePickerDesde***/
        final DatePickerDialog.OnDateSetListener dateDesde = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelDesde();
            }
        };
        /***DatePickerHasta***/
        final DatePickerDialog.OnDateSetListener dateHasta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelHasta();
            }
        };
        txtFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateDesde, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

            }
        });
        txtFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateHasta, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        /******/
            listapedidos = new ArrayList<>();

        btnBuscaVentaVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarPedidos();

            }
        });

        return myView;
    }

    private void CargarPedidos() {
        fechadesde = txtFechaDesde.getText().toString();
        fechahasta = txtFechaHasta.getText().toString();
        listapedidos.clear();
        new GetListaPedidos().execute();
        ActualizarFooter();
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    /**CARGAR COMBOS**/
    private void CargaDatosCombo() {

        final List<Ruta> rutas;
        if (CodigoSupervisor.equals("0")){
            rutas= VendedoresH.ObtenerTodasRutas();
        }else{
            rutas= VendedoresH.ObtenerListaRutas(CodigoSupervisor);
        }

        //ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<Ruta>(getActivity(),android.R.layout.simple_spinner_item,rutas);
        ArrayAdapter<Ruta> adapterRuta = new ArrayAdapter<Ruta>(getActivity(),android.R.layout.simple_spinner_item,rutas);
        adapterRuta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboRuta.setAdapter(adapterRuta);

        cboRuta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                ruta = (Ruta) adapter.getItemAtPosition(position);
                btnBuscaVentaVendedor.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listapedidos.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> pedido : listapedidos) {
            subtotal += Double.parseDouble(pedido.get("SubTotal").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    //region ObtieneListaPedidosLocal
    private class GetListaPedidos extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(pDialog!=null && pDialog.isShowing())
            {
                pDialog.dismiss();
            }
            // Showing progress dialog
            if (pDialog!=null && pDialog.isShowing())
                pDialog.dismiss();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {

                boolean connectionOK = Funciones.TestInternetConectivity();
                if (connectionOK) {
                    GetPedidosService();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran los pedidos locales que no se han sincronizados! ",
                                    Toast.LENGTH_LONG).show();
                            //  }
                        }
                    });
                }
            } catch (final Exception e) {
                //Log.e(TAG, "Json parsing error: " + e.getMessage());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   for (int i = 0; i < 2; i++) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor. \n Solo se mostraran los pedidos locales que no se han sincronizados! ",
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
            if( pDialog.isShowing())
            {
                pDialog.dismiss();
            }
            ActualizarLista();

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void ActualizarLista() {
        try {
            ActualizarFooter();
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
                double subtotal = Double.parseDouble(item.get("SubTotal").replace("C$", "").replace(",", ""));
                item.put("SubTotal", df.format(subtotal));

            }
            adapter = new SimpleAdapter(
                    getActivity(), listapedidos,
                    R.layout.resumen_preventa_list_item, new String[]{"Ruta", "Cantidad",
                    "SubTotal"},
                    new int[]{R.id.lblDetRuta, R.id.lblDetCantidad, R.id.lblDetSubtotal}) {
            };

            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ActualizarFooter();

        } catch (final Exception ex) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "GetListaPedidos OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetPedidosService() throws Exception {
        String CodRuta = variables_publicas.usuario.getCodigo();


        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        busqueda = busqueda.isEmpty() ? "%" : busqueda;
        String Fdesde = fechadesde.replace("-","") ,Fhasta=fechahasta.replace("-","");
        String urlString = urlPedidosVendedor + "/" + ruta + "/" + Fdesde + "/" + Fhasta + "/" + CodigoSupervisor;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de pedidos,Respuesta nula GET", variables_publicas.info+urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Pedidos = jsonObj.getJSONArray("ObtenerPedidosSupervisorResult");
                    listapedidos.clear();
                for (int i = 0; i < Pedidos.length(); i++) {
                    JSONObject c = Pedidos.getJSONObject(i);

                    String Cantidad = c.getString("Cantidad");
                    String Ruta = c.getString("Ruta");
                    String SubTotal = c.getString("SubTotal");

                    HashMap<String, String> pedidos = new HashMap<>();

                    pedidos.put("Cantidad", Cantidad);
                    pedidos.put("Ruta", Ruta);
                    pedidos.put("SubTotal", SubTotal);
                    listapedidos.add(pedidos);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de pedidos,Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

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

    private void updateLabelDesde() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaDesde.setText(sdf.format(myCalendar.getTime()));
        btnBuscaVentaVendedor.performClick();
    }
    private void updateLabelHasta() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaHasta.setText(sdf.format(myCalendar.getTime()));
        btnBuscaVentaVendedor.performClick();
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
        try {
//            if (adapter != null) {
                CargarPedidos();
//            }
        } catch (Exception ex) {

        }
    }
}