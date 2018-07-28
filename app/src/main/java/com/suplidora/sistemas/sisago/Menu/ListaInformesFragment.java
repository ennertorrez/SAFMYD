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
import com.suplidora.sistemas.sisago.AccesoDatos.FacturasPendientesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.InformesHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosDetalleHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.PedidosHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Cliente;
import com.suplidora.sistemas.sisago.Entidades.FacturasPendientes;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.Informes.ListaDetalleInformesClientes;
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

public class ListaInformesFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;

    private String TAG = ListaInformesFragment.class.getSimpleName();
    private String busqueda = "0";
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
    private TextView txtFechaInforme;
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listainforme;
    public Calendar myCalendar = Calendar.getInstance();
    //  private SimpleAdapter adapter;
    final String urlInformeVendedor = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerInformeVendedor";
    final String urlInformeSupervisor = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtenerInformeSupervisor";
    final String urlAnularInforme = variables_publicas.direccionIp + "/ServicioRecibos.svc/AnularInforme";
    private String jsonInforme;
    private String jsonAnulaInforme;
    private String IdInforme;
    private String IdVendedor;
    private boolean guardadoOK = true;
    private DecimalFormat df;
    private boolean isOnline = false;

    private InformesHelper InformesH;
    private InformesDetalleHelper InformesDetalleH;
    private FacturasPendientesHelper FacturasPendientesH;
    private String vIdSerie;
    private String vIdVendedor;
    public static ArrayList<HashMap<String, String>> lista;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listainformes_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Informes");
        lv = (ListView) myView.findViewById(R.id.listinformesdia);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        btnSincronizar = (Button) myView.findViewById(R.id.btnSincronizar);
        txtFechaInforme = (EditText) myView.findViewById(R.id.txtFechaInforme);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.list_informes_guardados, null);
        tvSincroniza = (TextView) dialogView.findViewById(R.id.tvSincronizar);
        tvEstado = (TextView) dialogView.findViewById(R.id.Estado);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        InformesH = new InformesHelper(DbOpenHelper.database);
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        FacturasPendientesH = new FacturasPendientesHelper(DbOpenHelper.database);
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        //variables_publicas.Informes = InformesH.BuscarInformesSinconizar();
        txtFechaInforme.setText(getDatePhone());
        fecha = txtFechaInforme.getText().toString();


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
        txtFechaInforme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        listainforme = new ArrayList<>();

        try{

                new GetListaInformes().execute();


        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        btnSincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    SincronizarInforme();


            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String IdInforme = ((TextView) view.findViewById(R.id.CodigoInforme)).getText().toString();
                String Estado = ((TextView) view.findViewById(R.id.Estado)).getText().toString();
                // Starting new intent
                Intent in = new Intent(getActivity().getApplicationContext(), ListaDetalleInformesClientes.class);

                in.putExtra(variables_publicas.CodInforme, IdInforme);
                in.putExtra(variables_publicas.estadoInforme, Estado);

                startActivity(in);
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarInformes();

            }
        });

        return myView;
    }

    private void CheckConnectivity() {
        isOnline =Funciones.TestServerConectivity();
    }

    private void CargarInformes() {
        fecha = txtFechaInforme.getText().toString();
        listainforme.clear();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
        busqueda = txtBusqueda.getText().toString();
        new GetListaInformes().execute();
    }

    private void ActualizarFooter() {

        try{
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> informe : listainforme) {
                subtotal += Double.parseDouble(informe.get("Monto").replace("C$", "").replace(",", ""));
                if (informe.get("Estado").equalsIgnoreCase("PENDIENTE") || informe.get("Estado").equalsIgnoreCase("APROBADO") || informe.get("Estado").equalsIgnoreCase("ANULADO")) {
                    cantidad += 1;
                }
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        }catch (Exception ex){
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de informes. Excepcion controlada",variables_publicas.info+" --- "+ex.getMessage(),variables_publicas.correoError,variables_publicas.correosErrores );
            Log.e("Error:",ex.getMessage());
            ex.printStackTrace();
        }


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private boolean SincronizarInforme() {
        try {
            new SincronizadorInformes().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private boolean AnularInforme(HashMap<String, String> informe) {
        Gson gson = new Gson();

        jsonAnulaInforme = gson.toJson(informe);
        try {
            new AnulaInforme().execute();
        } catch (Exception ex) {
            Funciones.MensajeAviso(getActivity().getApplicationContext(), ex.getMessage());
        }
        return false;
    }

    private class GetListaInformes extends AsyncTask<Void, Void, Void> {
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
                InformesH = new InformesHelper(DbOpenHelper.database);
                listainforme.clear();
                List<HashMap<String, String>> ListaLocal = null;

                ListaLocal = InformesH.ObtenerInformesLocales(fecha, busqueda);

                for (HashMap<String, String> item : ListaLocal) {
                    HashMap<String, String> iteminforme = new HashMap<>();
                    iteminforme.put("Informe", item.get("Informe"));
                    iteminforme.put("Vendedor", item.get("Vendedor"));
                    iteminforme.put("Fecha", item.get("Fecha"));
                    iteminforme.put("Monto", item.get("Monto"));
                    iteminforme.put("Recibos", item.get("Recibos"));
                    iteminforme.put("Estado", item.get("Estado"));
                    listainforme.add(iteminforme);
                }
                CheckConnectivity();
                if (isOnline) {
                    GetInformesService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran los informes locales que no se han sincronizados! ",
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
                                "No es posible conectarse al servidor. \n Solo se mostraran los informes locales que no se han sincronizados! ",
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
            for (HashMap<String, String> item : listainforme) {
                double subtotal = Double.parseDouble(item.get("Monto").replace("C$", "").replace(",", ""));
                item.put("Monto", df.format(subtotal));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listainforme,
                    R.layout.list_informes_guardados, new String[]{"Informe", "Vendedor",
                    "Fecha", "Monto", "Recibos", "Estado"},
                    new int[]{R.id.CodigoInforme, R.id.Vendedor, R.id.Fecha, R.id.TotalInforme, R.id.txtRecibos,
                            R.id.Estado}) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View currView = super.getView(position, convertView, parent);
                    HashMap<String, String> currItem = (HashMap<String, String>) getItem(position);
                    tvSincroniza = (TextView) currView.findViewById(R.id.tvSincronizar);
                    tvEstado = (TextView) currView.findViewById(R.id.Estado);
                    if (currItem.get("Informe").length()>=13) {
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
                            "GetListaInformes OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetInformesService() throws Exception {
        String CodigoVendedor= variables_publicas.usuario.getCodigo();
        String urlString= "";
        String vResultado="";
        busqueda = busqueda.isEmpty() ? "0" : busqueda;
        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor")) {
            urlString = urlInformeSupervisor + "/" + CodigoVendedor + "/" + fecha + "/" + busqueda;
            vResultado="ObtenerInformeSupervisorResult";
        }else if ( variables_publicas.usuario.getTipo().equalsIgnoreCase("User")){
            urlString = urlInformeSupervisor + "/0/" + fecha + "/" + busqueda;
            vResultado="ObtenerInformeSupervisorResult";
        }else {
            urlString = urlInformeVendedor + "/" + CodigoVendedor + "/" + fecha + "/" + busqueda;
            vResultado="ObtenerInformeVendedorResult";
        }

        String encodeUrl = "";

        HttpHandler sh = new HttpHandler();
        //String urlString = urlInformeVendedor + "/" + CodigoVendedor + "/" + fecha + "/" + busqueda;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de informes. Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
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
                    String Informe = c.getString("Informe");
                    String Monto = c.getString("Monto");
                    String Recibos = c.getString("Recibos");
                    String Vendedor = c.getString("Vendedor");

                    HashMap<String, String> informes = new HashMap<>();

                    informes.put("Estado", Estado);
                    informes.put("Fecha", Fecha);
                    informes.put("Informe", Informe);
                    informes.put("Monto",df.format(Double.parseDouble(Monto)));
                    informes.put("Recibos", Recibos);
                    informes.put("Vendedor", Vendedor);
                    listainforme.add(informes);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de informes. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    //endregion

    private class SincronizadorInformes extends AsyncTask<Void, Void, Void> {

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

            CheckConnectivity();
            if(isOnline){
                List<HashMap<String, String>> InformesLocal = InformesH.ObtenerInformesLocales(fecha, "0");
                for (HashMap<String, String> item : InformesLocal) {
                    if (guardadoOK == false) {
                        break;
                    }
                    Gson gson = new Gson();
                    String vendedor = variables_publicas.usuario.getCodigo();
                    String jsonInforme = gson.toJson(InformesH.ObtenerInforme(item.get("Informe")));
                    guardadoOK = Boolean.parseBoolean(SincronizarDatos.SincronizarInforme(InformesH, InformesDetalleH, vendedor, item.get("Informe"), jsonInforme, false).split(",")[0]);
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
                                "SincronizarInformes onPostExecute: " + ex.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    //region ServiceAnularInforme
    private class AnulaInforme extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Anulando Informe...Por favor espere...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(getActivity()==null) return null;
            HttpHandler sh = new HttpHandler();
            final String url = variables_publicas.direccionIp + "/ServicioRecibos.svc/AnularInforme/" + IdInforme;

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
                    String resultState = ((String) result.get("AnularInformeResult")).split(",")[0];
                    final String mensaje = ((String) result.get("AnularInformeResult")).split(",")[1];
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
                    new Funciones().SendMail("Ha ocurrido un error al Anular el Informe. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);
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
                new Funciones().SendMail("Ha ocurrido un error al Anular el Informe. Respuesta nulla GET", variables_publicas.info + urlStr, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
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
                                "Anular Informe onPostExecute: " + ex.getMessage(),
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

            String HeaderMenu = "Informe: " +obj.get("Informe") + " Recibos: " + obj.get("Recibos");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.informes_list_menu_context, menu);

            if (obj.get("Estado").equalsIgnoreCase("NO ENVIADO"))
            {
                ((MenuItem) menu.getItem(0)).setEnabled(false);
                ((MenuItem) menu.getItem(1)).setEnabled(true);

            }else if (obj.get("Estado").equalsIgnoreCase("PENDIENTE")){
                ((MenuItem) menu.getItem(0)).setEnabled(true);
                ((MenuItem) menu.getItem(1)).setEnabled(false);
            }else if (obj.get("Estado").equalsIgnoreCase("ANULADO") || obj.get("Estado").equalsIgnoreCase("APROBADO")){
                ((MenuItem) menu.getItem(0)).setEnabled(false);
                ((MenuItem) menu.getItem(1)).setEnabled(false);
            }
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> informe = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemAnularInforme:
                    fecha = txtFechaInforme.getText().toString();
                    listainforme.clear();
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();

                        new GetListaInformes().execute().get();


                    ActualizarFooter();

                    HashMap<String, String> itemInforme = listainforme.get(info.position);
                    informe = InformesH.ObtenerInforme(itemInforme.get("Informe"));

                    IdInforme = itemInforme.get("Informe");
                    if (Funciones.checkInternetConnection(getActivity())) {

                        final HashMap<String, String> finalInforme = itemInforme;
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea Anular el informe?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AnularInforme(finalInforme);
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

                case R.id.itemEliminarInforme: {
                    fecha = txtFechaInforme.getText().toString();
                    listainforme.clear();
                    InputMethodManager inputMethodManager2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputMethodManager2.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
                    busqueda = txtBusqueda.getText().toString();

                    new GetListaInformes().execute().get();


                    ActualizarFooter();

                    HashMap<String, String> itemInforme2 = listainforme.get(info.position);
                    informe = InformesH.ObtenerInforme(itemInforme2.get("Informe"));

                    IdInforme = itemInforme2.get("Informe");
                    vIdVendedor = informe.get("IdVendedor");
                    lista = new ArrayList<HashMap<String, String>>();
                    lista = InformesDetalleH.ObtenerUltimoCodigoRecibo(vIdVendedor);

                    for (int i = 0; i < lista.size(); i++) {
                        vIdSerie = lista.get(i).get("IdSerie");
                    }
                    if (itemInforme2.get("Informe").length()>=13) {

                        new AlertDialog.Builder(getActivity())
                                .setTitle("Confirmación Requerida")
                                .setMessage("Esta seguro que desea Eliminar el informe?")
                                .setCancelable(false)
                                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int valrecibo = InformesH.BuscarMinimoRecibo(IdInforme);
                                        if (valrecibo>0){
                                            InformesDetalleH.ActualizarCodigoRecibo(vIdSerie,String.valueOf(valrecibo-1),vIdVendedor);
                                        }
                                        InformesH.EliminaInforme(IdInforme);
                                        FacturasPendientesH.ActualizarTodasFacturasPendientes(IdInforme);
                                        InformesDetalleH.EliminarDetalleInforme(IdInforme);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (pDialog.isShowing())
                                            pDialog.dismiss();
                                    }
                                })
                                .show();

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
        txtFechaInforme.setText(sdf.format(myCalendar.getTime()));
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