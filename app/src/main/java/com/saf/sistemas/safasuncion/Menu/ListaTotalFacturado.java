package com.saf.sistemas.safasuncion.Menu;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.saf.sistemas.safasuncion.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safasuncion.AccesoDatos.VendedoresHelper;
import com.saf.sistemas.safasuncion.Auxiliar.Funciones;
import com.saf.sistemas.safasuncion.Auxiliar.variables_publicas;
import com.saf.sistemas.safasuncion.Entidades.Vendedor;
import com.saf.sistemas.safasuncion.HttpHandler;
import com.saf.sistemas.safasuncion.R;

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
public class ListaTotalFacturado extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;

    private Vendedor vendedor = null;

    private String TAG = ListaTotalFacturado.class.getSimpleName();
    private String fechadesde = "";
    private String fechahasta = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private Button btnBuscaVentaVendedor;
    private TextView txtFechaDesde;
    private TextView txtFechaHasta;
    private Spinner cboVendedor;
    public static ArrayList<HashMap<String, String>> listafacturas;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;
    final String urlFacturasVendedor = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerFacturacionVendedor";
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listaresumen_facturado, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Resumen Facturacion");
        lv = (ListView) myView.findViewById(R.id.listResumenPreventaF);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {


            }
        });
        btnBuscaVentaVendedor = (Button) myView.findViewById(R.id.btnBuscaVentaVendedorF);
        txtFechaDesde = (EditText) myView.findViewById(R.id.txtFechaDesdeF);
        txtFechaHasta = (EditText) myView.findViewById(R.id.txtFechaHastaF);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidadF);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotalF);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.resumenfacturado_list_item, null);
        cboVendedor= (Spinner) myView.findViewById(R.id.cboVendedorF);

        final Spinner cboVendedor = (Spinner) myView.findViewById(R.id.cboVendedorF);

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        txtFechaDesde.setText(getDatePhone());
        fechadesde = txtFechaDesde.getText().toString();

        List<Vendedor> vend;
        vend= VendedoresH.ObtenerListaVendedores();
        vendedor =  vend.get(0);

        txtFechaHasta.setText(getDatePhone());
        fechahasta = txtFechaHasta.getText().toString();

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

                View focusedView = getActivity().getCurrentFocus();
                if (focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

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

                View focusedView = getActivity().getCurrentFocus();
                if (focusedView != null) {
                    inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        /******/
        listafacturas= new ArrayList<>();

        btnBuscaVentaVendedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarFacturas();
            }
        });

        return myView;
    }

    private void CargarFacturas() {
        fechadesde = txtFechaDesde.getText().toString();
        fechahasta = txtFechaHasta.getText().toString();
        listafacturas.clear();
        new ListaTotalFacturado.GetListaFacturas().execute();
        ActualizarFooter();
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    /**CARGAR COMBOS**/
    private void CargaDatosCombo() {

        final List<Vendedor> vendedores;

        vendedores = VendedoresH.ObtenerVendedorxSup("0");
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(getActivity(), android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboVendedor.setAdapter(adapterVendedor);

        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                // On selecting a spinner item
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                btnBuscaVentaVendedor.performClick();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }


    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listafacturas.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> facturacion : listafacturas) {
            subtotal += Double.parseDouble(facturacion.get("Total").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }


    private class GetListaFacturas extends AsyncTask<Void, Void, Void> {
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
                    GetFacturacionService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   for (int i = 0; i < 2; i++) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. ",
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
                                "No es posible conectarse al servidor.",
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
            for (HashMap<String, String> item : listafacturas) {
                double subtotal = Double.parseDouble(item.get("Total").replace("C$", "").replace(",", ""));
                item.put("Total", df.format(subtotal));

            }
            adapter = new SimpleAdapter(
                    getActivity(), listafacturas,
                    R.layout.resumenfacturado_list_item, new String[]{"Vendedor", "Cantidad","Total"},
                    new int[]{R.id.lblDetVendedorF, R.id.lblDetCantidadF, R.id.lblDetTotalF}) {
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

    private void GetFacturacionService() throws Exception {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String Fdesde = fechadesde.replace("-","") ,Fhasta=fechahasta.replace("-","");

        String urlString = urlFacturasVendedor + "/" +  Fdesde + "/" + Fhasta + "/" + vendedor.getCODIGO() + "/" + variables_publicas.usuario.getEmpresa_ID();
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
                new Funciones().SendMail("Ha ocurrido un error al obtener la Facturacion de los vendedores. Respuesta nula GET", variables_publicas.info+urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Facturacion = jsonObj.getJSONArray("ObtenerFacturacionVendedorResult");
                listafacturas.clear();
                for (int i = 0; i < Facturacion.length(); i++) {
                    JSONObject c = Facturacion.getJSONObject(i);

                    String Cantidad = c.getString("Cantidad");
                    String Ruta = c.getString("Vendedor");
                    String Total = c.getString("Total");

                    HashMap<String, String> facturacion = new HashMap<>();

                    facturacion.put("Cantidad", Cantidad);
                    facturacion.put("Vendedor", Ruta);
                    facturacion.put("Total", Total);
                    listafacturas.add(facturacion);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener la facturacion de los vendedores. Excepcion controlada", variables_publicas.info+ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

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

        } catch (Exception ex) {

        }
    }
}
