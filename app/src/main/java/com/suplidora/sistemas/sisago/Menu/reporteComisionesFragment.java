package com.suplidora.sistemas.sisago.Menu;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
import com.suplidora.sistemas.sisago.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class reporteComisionesFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;

    private String TAG = reporteComisionesFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private Spinner cboMeses;
    private String mes="1";
    private String tipo="1";
    private String año="2018";
    private ListView lv;
    private TextView lblFooterComision;
    private TextView lblFooterRecuperado;
    private TextView txtAnio;
    private TextView txtVendedor;
    public static ArrayList<HashMap<String, String>> listaComisiones;
    private SimpleAdapter adapter;
    final String urlcomisionesVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/ConsultarComisiones";
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.reportecomisiones, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Reporte de Ingresos");
        lv = (ListView) myView.findViewById(R.id.listComisiones);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            }
        });

        txtAnio = (TextView) myView.findViewById(R.id.txtAnio);
        cboMeses = (Spinner) myView.findViewById(R.id.cboMeses);
        lblFooterComision = (TextView) myView.findViewById(R.id.lblFooterComision);
        lblFooterRecuperado = (TextView) myView.findViewById(R.id.lblFooterRecuperado);
        txtVendedor=(TextView) myView.findViewById(R.id.lblvendedor);

        String[] valores = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        cboMeses.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, valores));

        Calendar fecha = Calendar.getInstance();
        año =String.valueOf(fecha.get(Calendar.YEAR));
        mes = String.valueOf(fecha.get(Calendar.MONTH)+1);
        txtAnio.setText(año);
        txtVendedor.setText(variables_publicas.usuario.getNombre());
        int mesInt = fecha.get(Calendar.MONTH)+1;
        cboMeses.setSelection(getIndex(cboMeses, daMes(mesInt)));

        cboMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                String text = cboMeses.getSelectedItem().toString();
                if (text.equals("Enero")){
                    mes="1";
                }else if (text.equals("Febrero")){
                    mes="2";
                }else if (text.equals("Marzo")) {
                    mes="3";
                }else if (text.equals("Abril")) {
                    mes="4";
                }else if (text.equals("Mayo")) {
                    mes="5";
                }else if (text.equals("Junio")) {
                    mes="6";
                }else if (text.equals("Julio")) {
                    mes="7";
                }else if (text.equals("Agosto")) {
                    mes="8";
                }else if (text.equals("Septiembre")) {
                    mes="9";
                }else if (text.equals("Octubre")) {
                    mes="10";
                }else if (text.equals("Noviembre")) {
                    mes="11";
                }else if (text.equals("Diciembre")) {
                    mes="12";
                }
                CargarComisiones();
               }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.reporte_comisiones_list_item, null);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());

        if(variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor"))
        {
            tipo = "2";
        }
        else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")){
            tipo="1";
        }

        listaComisiones = new ArrayList<>();

        return myView;
    }

    private void CargarComisiones() {
        listaComisiones.clear();
        new GetComisiones().execute();
        ActualizarFooter();
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    public String daMes(int vmes){
        String[] mesesString = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
        String mesString = "";
        mesString = mesesString[vmes-1];
        return mesString;
    }


    private void ActualizarFooter() {
        double vcomision = 0.00;
        double vrecuperado = 0.00;
        for (HashMap<String, String> comisiones : listaComisiones) {
            vcomision += Double.parseDouble(comisiones.get("Comision").replace("C$", "").replace(",", ""));
            vrecuperado += Double.parseDouble(comisiones.get("Recuperado").replace("C$", "").replace(",", ""));
        }
        lblFooterRecuperado.setText("Recuperado: C$" + df.format(vrecuperado));
        lblFooterComision.setText("Ingreso: C$" + df.format(vcomision));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private class GetComisiones extends AsyncTask<Void, Void, Void> {
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
                if(getActivity()==null) return null;
                boolean connectionOK = Funciones.TestInternetConectivity();
                if (connectionOK) {
                    GetComisionesService();
                } else {
                    if(getActivity()==null) return null;
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
                if(getActivity()==null) return null;
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
            if (pDialog.isShowing())
                pDialog.dismiss();

            DecimalFormat df = new DecimalFormat("C$ #0.00");
            DecimalFormatSymbols fmts = new DecimalFormatSymbols();
            fmts.setGroupingSeparator(',');
            df.setGroupingSize(3);
            df.setGroupingUsed(true);
            df.setDecimalFormatSymbols(fmts);
            for (HashMap<String, String> item : listaComisiones) {
                double valfacturado = Double.parseDouble(item.get("Facturado").replace("C$", "").replace(",", ""));
                double valRecuperado = Double.parseDouble(item.get("Recuperado").replace("C$", "").replace(",", ""));
                double valComision = Double.parseDouble(item.get("Comision").replace("C$", "").replace(",", ""));
                item.put("Facturado", df.format(valfacturado));
                item.put("Recuperado", df.format(valRecuperado));
                item.put("Comision", df.format(valComision));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listaComisiones,
                    R.layout.reporte_comisiones_list_item, new String[]{"Escala", "Facturado","Recuperado","Comision"},
                    new int[]{R.id.lblDetEscala, R.id.lblDetFacturado, R.id.lblDetRecuperado,R.id.lblDetComision}) {
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
                            "ConsultarComisiones OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetComisionesService() throws Exception {
        String codigoVendedor = variables_publicas.usuario.getCodigo();
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlcomisionesVendedor + "/" + mes + "/" + año + "/" + codigoVendedor + "/" + tipo;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener el detalle de las comisiones, Respuesta nula GET", variables_publicas.info+urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);
                String vEscala="0-45 días";
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Comisiones = jsonObj.getJSONArray("ConsultarComisionesResult");
                listaComisiones.clear();
                for (int i = 0; i < Comisiones.length(); i++) {
                    JSONObject c = Comisiones.getJSONObject(i);

                    String Escala = c.getString("ESCALA");
                    if (Escala.equals("1")) {
                        vEscala="0-30 días";
                    }else if (Escala.equals("2")) {
                        vEscala="31-60 días";
                    }else{
                        vEscala=" > 60 días";
                    }
                    String Facturado = c.getString("FACTURADO");
                    String Recuperado = c.getString("RECUPERADO");
                    String Comision = c.getString("COMISION");

                    HashMap<String, String> comisiones = new HashMap<>();

                    comisiones.put("Escala", vEscala);
                    comisiones.put("Facturado", Facturado);
                    comisiones.put("Recuperado", Recuperado);
                    comisiones.put("Comision", Comision);
                    listaComisiones.add(comisiones);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener el detalle de las comisiones. Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }


    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            String nn=spinner.getItemAtPosition(i).toString();

            if (nn.equals(myString)){
                index = i;
                break;
            }
        }
        return index;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
                CargarComisiones();
        } catch (Exception ex) {
        }
    }
}