package com.safi_d.sistemas.safiapp.Menu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import com.safi_d.sistemas.safiapp.AccesoDatos.DataBaseOpenHelper;
import com.safi_d.sistemas.safiapp.AccesoDatos.InformesDetalleHelper;
import com.safi_d.sistemas.safiapp.Auxiliar.Funciones;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.Entidades.Bancos;
import com.safi_d.sistemas.safiapp.HttpHandler;
import com.safi_d.sistemas.safiapp.R;

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
public class ListaRecibosPendFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;

    private String TAG = ListaRecibosPendFragment.class.getSimpleName();
    private String busqueda = "0";
    private String fecha = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private EditText txtBusqueda;
    private Button btnBuscar;
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listaRecibos;
    public Calendar myCalendar = Calendar.getInstance();
    //  private SimpleAdapter adapter;
    final String urlRecibosPedientes= variables_publicas.direccionIp + "/ServicioRecibos.svc/RecibosPendientesDeposito";
    private boolean guardadoOK = true;
    private DecimalFormat df;
    private boolean isOnline = false;

    private InformesDetalleHelper InformesDetalleH;
    public static ArrayList<HashMap<String, String>> lista;

    private  String InformeId;
    private  String ReciboId;
    private  String MinutaId;
    private boolean finalizar = false;
    private String fechaDoc = "";
    private String vBancoF = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listarecibospendientes_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Recibos");
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        InformesDetalleH = new InformesDetalleHelper(DbOpenHelper.database);
        lv = (ListView) myView.findViewById(R.id.listRecibosPend);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        txtBusqueda = (EditText)  myView.findViewById(R.id.txtBusqueda);

        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.resumen_recibos_pend_list_item, null);

        listaRecibos = new ArrayList<>();
        try{
                new GetListaRecibos().execute();
        }catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        lv.setTextFilterEnabled(true);
        lv.setFastScrollEnabled(true);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String IdRecibo = ((TextView) view.findViewById(R.id.lblRecibo)).getText().toString();
                String IdInforme= ((TextView) view.findViewById(R.id.lblInforme)).getText().toString();
                String vMonto= ((TextView) view.findViewById(R.id.lblMonto)).getText().toString();

                MostrarMensajeGuardar(vMonto,IdInforme,IdRecibo);
            }
        });

        txtBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        isOnline =Funciones.TestServerConectivity();
    }

    private void CargarRecibos() {
        listaRecibos.clear();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(txtBusqueda.getWindowToken(), 0);
       // busqueda = txtBusqueda.getText().toString();
        new GetListaRecibos().execute();
    }

    private void ActualizarFooter() {

        try{
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> informe : listaRecibos) {
                subtotal += Double.parseDouble(informe.get("Monto").replace("C$", "").replace(",", ""));
                cantidad += 1;
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        }catch (Exception ex){
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de recibos. Excepcion controlada",variables_publicas.info+" --- "+ex.getMessage(),variables_publicas.correoError,variables_publicas.correosErrores );
            Log.e("Error:",ex.getMessage());
            ex.printStackTrace();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                listaRecibos.clear();
                if(getActivity()==null) return null;

                CheckConnectivity();
                if (isOnline) {
                    GetRecibosService();
                } else {
                    if(getActivity()==null) return null;
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
                if(getActivity()==null) return null;
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

            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();

            DecimalFormat df = new DecimalFormat("C$ #0.00");
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            df.setDecimalFormatSymbols(fmts);
            for (HashMap<String, String> item : listaRecibos) {
                double total = Double.parseDouble(item.get("Monto").replace("C$", "").replace(",", ""));
                item.put("Monto", df.format(total));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listaRecibos,
                    R.layout.resumen_recibos_pend_list_item, new String[]{"Informe", "Recibo",
                    "Cliente", "Monto", "Facturas"},
                    new int[]{R.id.lblInforme, R.id.lblRecibo, R.id.lblCliente, R.id.lblMonto, R.id.lblFacturas}) {
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
                            "RecibosPendientesDeposito OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetRecibosService() throws Exception {
        String CodigoVendedor= variables_publicas.usuario.getCodigo();
        String urlString= "";
        String vResultado="";

            urlString = urlRecibosPedientes + "/" + CodigoVendedor;
            vResultado="RecibosPendientesDepositoResult";

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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Respuesta nula GET", variables_publicas.info + urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);
                //listainforme.clear();
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Informes = jsonObj.getJSONArray(vResultado);

                for (int i = 0; i < Informes.length(); i++) {
                    JSONObject c = Informes.getJSONObject(i);
                    String Informe = c.getString("ninforme");
                    String Recibo = c.getString("recibo");
                    String Cliente = c.getString("cliente");
                    String Monto = c.getString("Monto");
                    String Facturas = c.getString("facturas");

                    HashMap<String, String> recibos = new HashMap<>();

                    recibos.put("Informe", Informe);
                    recibos.put("Recibo", Recibo);
                    recibos.put("Cliente", Cliente);
                    recibos.put("Monto",df.format(Double.parseDouble(Monto)));
                    recibos.put("Facturas", Facturas);
                    listaRecibos.add(recibos);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de recibos. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

        }
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
    public void MostrarMensajeGuardar( String valormonto,final String valorInforme,final String valorrecibo) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = null;
                dialogBuilder.setCancelable(false);
                    dialogView = inflater.inflate(R.layout.recibopendientguardar, null);
                    Button btnOK = (Button) dialogView.findViewById(R.id.btnGuardar);
                    Button btnNOK = (Button) dialogView.findViewById(R.id.btnCancelar);
                    TextView txtIdRecibo = (TextView) dialogView.findViewById(R.id.txtRecibo);
                    TextView txtValMonto = (TextView) dialogView.findViewById(R.id.txtMonto);
                    final TextView txtFechaDocPago = (TextView) dialogView.findViewById(R.id.txtFecha);
                    final Spinner cboBancoDestino = (Spinner) dialogView.findViewById(R.id.cboBancoDepositado);
                    final EditText txtValMinuta = (EditText) dialogView.findViewById(R.id.txtMinuta);
                    txtIdRecibo.setText(valorrecibo);
                    txtValMonto.setText(valormonto);

                    txtFechaDocPago.setText(getDatePhone());
                    fechaDoc = txtFechaDocPago.getText().toString();
                    final DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            String myFormat = ("yyyy-MM-dd");
                            ; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                            txtFechaDocPago.setText(sdf.format(myCalendar.getTime()));
                        }
                    };

                    txtFechaDocPago.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(getActivity(), date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                            InputMethodManager inputManager = (InputMethodManager)
                                    getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    });


                    final List<Bancos> CBancoD;
                    CBancoD = InformesDetalleH.ObtenerListaBancos();
                    ArrayAdapter<Bancos> adapterBancoD = new ArrayAdapter<Bancos>(getActivity(), android.R.layout.simple_spinner_item, CBancoD);
                    adapterBancoD.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                    cboBancoDestino.setAdapter(adapterBancoD);
                    cboBancoDestino.setSelection(1);

                    cboBancoDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                            vBancoF=cboBancoDestino.getSelectedItem().toString();
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> arg0) {
                        }
                    });
                    dialogBuilder.setView(dialogView);

                    final  AlertDialog alertDialog = dialogBuilder.create();

                    btnOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (txtValMinuta.getText().toString().equals("") || txtValMinuta.getText().toString().isEmpty() || txtValMinuta.equals("0")) {
                                MensajeAviso("Debe ingresar el número de Minuta de depósito.");
                                return;
                            }
                            ReciboId=valorrecibo;
                            InformeId=valorInforme;
                            MinutaId=txtValMinuta.getText().toString();
                            fechaDoc=txtFechaDocPago.getText().toString();
                            try{

                                new ActualizaRecibo().execute();


                            }catch (Exception e){
                                Log.e("Error",e.getMessage());
                            }
                            if (guardadoOK) {
                                try {

                                    new GetListaRecibos().execute();
                                    alertDialog.dismiss();
                                } catch (Exception e) {
                                    Log.e("Error", e.getMessage());
                                }
                            }else{
                                MensajeAviso("Hubo un error al actualizar el recibo. Intente más tarde.");
                                alertDialog.dismiss();
                            }
                        }
                    });

                    btnNOK.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });


                alertDialog.show();
    }
    public void MensajeAviso(String texto) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());
        dlgAlert.setMessage(texto);
        dlgAlert.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }
    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

    private class ActualizaRecibo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            if(ListaRecibosPendFragment.this==null) return null;
            HttpHandler sh = new HttpHandler();

            final String url = variables_publicas.direccionIp + "/ServicioRecibos.svc/ActualizarReciboPendiente/" + InformeId + "/" + ReciboId + "/" + MinutaId + "/" + vBancoF + "/" + fechaDoc;

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
                    String resultState = ((String) result.get("ActualizarReciboPendienteResult")).split(",")[0];
                    final String mensaje = ((String) result.get("ActualizarReciboPendienteResult")).split(",")[1];
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
                        guardadoOK = false;
                    } else {
                        guardadoOK = true;
                    }


                } catch (final Exception ex) {
                    guardadoOK = false;
                    new Funciones().SendMail("Ha ocurrido un error al actualizar el Recibo. Excepcion controlada", variables_publicas.info + ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor",
                                    Toast.LENGTH_LONG).show();
                            //  }
                        }
                    });
                }
            } else {
                new Funciones().SendMail("Ha ocurrido un error al actualizar el Recibo. Respuesta nula GET", variables_publicas.info + urlStr, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }

}