package com.saf.sistemas.safmyd.Menu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
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

import androidx.annotation.Nullable;

import com.saf.sistemas.safmyd.AccesoDatos.DataBaseOpenHelper;
import com.saf.sistemas.safmyd.AccesoDatos.VendedoresHelper;
import com.saf.sistemas.safmyd.Auxiliar.Funciones;
import com.saf.sistemas.safmyd.Auxiliar.SincronizarDatos;
import com.saf.sistemas.safmyd.Auxiliar.variables_publicas;
import com.saf.sistemas.safmyd.Entidades.Cliente;
import com.saf.sistemas.safmyd.Entidades.Vendedor;
import com.saf.sistemas.safmyd.HttpHandler;
import com.saf.sistemas.safmyd.Pedidos.FacturasActivity;
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

public class ListaEfectividadSupervisor extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private String TAG = ListaEfectividadSupervisor.class.getSimpleName();
    private String fechadesde = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private Button btnBuscar;
    private TextView txtFecha;
    public static ArrayList<HashMap<String, String>> listaEficiencia;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;
    final String urlVisitasRutasSupervisor= variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerVisitasSupervidor";
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.lista_cumplimiento_supervisor, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Resumen Cumplimiento");
        lv = (ListView) myView.findViewById(R.id.listResumenCumpliento);
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            }
        });
        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        txtFecha = (EditText) myView.findViewById(R.id.txtFecha);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidadF);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotalF);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.resumen_cumplimiento_list_item, null);
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        txtFecha.setText(getDatePhone());
        fechadesde = txtFecha.getText().toString();

        /***DatePickerDesde***/
        final DatePickerDialog.OnDateSetListener dateConsulta = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelFecha();
            }
        };
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), dateConsulta, myCalendar
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
        listaEficiencia= new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarVisitas();
            }
        });

        return myView;
    }

    private void CargarVisitas() {
        fechadesde = txtFecha.getText().toString();
        listaEficiencia.clear();
        new ListaEfectividadSupervisor.GetListaVisitas().execute();
        ActualizarFooter();
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaEficiencia.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> facturacion : listaEficiencia) {
            subtotal += Double.parseDouble(facturacion.get("Total").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }


    private class GetListaVisitas extends AsyncTask<Void, Void, Void> {
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
                    GetVisitasService();
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = obj.get("Ruta") ;

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();

            inflater.inflate(R.menu.cumplimiento_list_menu_context, menu);

        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        HashMap<String, String> visitas = null;
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemVerDetalle: {

                    if (listaEficiencia.size() == 0) {
                        return true;
                    }

                    //Visualizar
                    HashMap<String, String> obj = listaEficiencia.get(info.position);
                    String fecha = obj.get("Fecha");
                    String ruta = obj.get("IdRuta");
                    String vendedor = obj.get("IdVendedor");
                    String dia = obj.get("Dia");
                    String nombreruta = obj.get("Ruta");
                    String empresa = variables_publicas.usuario.getEmpresa_ID();

                    // Starting new intent
                    Intent in = new Intent(getActivity().getApplicationContext(), ListaEfectividadSupervisorDetalle.class);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
                    Date data = sdf.parse(fecha);
                    String fechaFormateada = output.format(data);

                    in.putExtra("Fecha", fechaFormateada);
                    in.putExtra("Fecha2", fecha);
                    in.putExtra("Dia", dia);
                    in.putExtra("IdRuta", ruta);
                    in.putExtra("IdVendedor",vendedor);
                    in.putExtra("Ruta",nombreruta);
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
            for (HashMap<String, String> item : listaEficiencia) {
                double subtotal = Double.parseDouble(item.get("Total").replace("C$", "").replace(",", ""));
                item.put("Total", df.format(subtotal));

            }
            adapter = new SimpleAdapter(
                    getActivity(), listaEficiencia,
                    R.layout.resumen_cumplimiento_list_item, new String[]{"Dia","Fecha","Ruta","Vendedor","TotalCliente","TotalClienteVenta","TotalClienteNoVenta","TotalClienteNoVisita", "Eficiencia","Total","IdRuta","IdVendedor"},
                    new int[]{R.id.lblDetDia, R.id.lblDetFecha, R.id.lblDetRuta, R.id.lblDetVendedor, R.id.lblDetClientes, R.id.lblDetClientesVentas, R.id.lblDetClientesNoVenta, R.id.lblDetClientesNoVisita, R.id.lblDetEficiencia, R.id.lblDetTotal, R.id.lblDetIdRuta, R.id.lblDetIdVendedor}) {
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
                            "GetListaVisitas OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetVisitasService() throws Exception {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String Fdesde = fechadesde;

        String urlString = urlVisitasRutasSupervisor + "/" +  Fdesde + "/" +  variables_publicas.usuario.getCodigo() + "/" + variables_publicas.usuario.getEmpresa_ID();
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
                new Funciones().SendMail("Ha ocurrido un error al obtener listado de Eficiencia. Respuesta nula GET", variables_publicas.info+urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Visitas = jsonObj.getJSONArray("ObtenerVisitasSupervidorResult");
                listaEficiencia.clear();
                for (int i = 0; i < Visitas.length(); i++) {
                    JSONObject c = Visitas.getJSONObject(i);

                    String Dia = c.getString("DIA");
                    String Fecha = c.getString("FECHA");
                    String Ruta = c.getString("RUTA");
                    String Vendedor = c.getString("VENDEDOR");
                    String TotalCliente = c.getString("TOTAL_CLIENTES");
                    String TotalClienteVenta = c.getString("VISITA_CON_VENTA");
                    String TotalClienteNoVenta = c.getString("VISITA_SIN_VENTA");
                    String TotalClienteNoVisita = c.getString("NO_VISITADOS");
                    String Eficincia = c.getString("EFICIENCIA");
                    String Total = c.getString("VENTA_NETA");
                    String IdRuta = c.getString("IdRuta");
                    String IdVendedor = c.getString("IdVendedor");

                    HashMap<String, String> visitas = new HashMap<>();

                    visitas.put("Dia", Dia);
                    visitas.put("Fecha", Fecha);
                    visitas.put("Ruta", Ruta);
                    visitas.put("Vendedor", Vendedor);
                    visitas.put("TotalCliente", TotalCliente);
                    visitas.put("TotalClienteVenta", TotalClienteVenta);
                    visitas.put("TotalClienteNoVenta", TotalClienteNoVenta);
                    visitas.put("TotalClienteNoVisita", TotalClienteNoVisita);
                    visitas.put("Eficiencia", Eficincia);
                    visitas.put("Total", Total);
                    visitas.put("IdRuta", IdRuta);
                    visitas.put("IdVendedor", IdVendedor);
                    listaEficiencia.add(visitas);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de Eficiencia. Excepcion controlada", variables_publicas.info+ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

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

    private void updateLabelFecha() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFecha.setText(sdf.format(myCalendar.getTime()));
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
        try {

        } catch (Exception ex) {

        }
    }
}
