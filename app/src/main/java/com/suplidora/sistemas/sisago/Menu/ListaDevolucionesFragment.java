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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
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
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.SincronizarDatos;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.HttpHandler;
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
 * Created by Sistemas on 29/1/2018.
 */

public class ListaDevolucionesFragment extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;

    private String TAG = ListaDevolucionesFragment.class.getSimpleName();
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private Button btnBuscarDev;
    private TextView txtFechaIni;
    private String fechadesde = "";
    private String fechahasta = "";
    private SimpleAdapter adapter;
    public static ArrayList<HashMap<String, String>> listadevoluciones;
    public Calendar myCalendar = Calendar.getInstance();
    //  private SimpleAdapter adapter;
    final String urlDevoluciones = variables_publicas.direccionIp + "/ServicioDevoluciones.svc/ObtenerListaDevVendedor";

    private DecimalFormat df;
    private boolean isOnline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.lista_devoluciones_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Lista de Devoluciones");
        lv = (ListView) myView.findViewById(R.id.listResumenDevoluciones);
        registerForContextMenu(lv);
      /*  lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            }
        });*/
        btnBuscarDev = (Button) myView.findViewById(R.id.btnBuscaDev);
        txtFechaIni = (EditText) myView.findViewById(R.id.txtFechaDevolucion);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterSubtotal);
        LayoutInflater inflate = getActivity().getLayoutInflater();
        View dialogView = inflate.inflate(R.layout.lista_devoluciones_guardados, null);
        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        txtFechaIni.setText(getDatePhone());
        fechadesde = txtFechaIni.getText().toString();

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
        txtFechaIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        listadevoluciones = new ArrayList<>();

        try {
            new GetListaDevoluciones().execute();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        btnBuscarDev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarDevoluciones();

            }
        });
        return myView;
    }



    private void CargarDevoluciones() {
        listadevoluciones.clear();

        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            new GetListaDevoluciones().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            new GetListaDevoluciones().execute();
        }
    }

    private void ActualizarFooter() {

        try {
            double subtotal = 0.00;
            int cantidad = 0;
            for (HashMap<String, String> devolucion : listadevoluciones) {
                subtotal += Double.parseDouble(devolucion.get("MONTO").replace("C$", "").replace(",", ""));
                cantidad += 1;
            }
            lblFooterCantidad.setText("Cantidad: " + String.valueOf(cantidad));
            lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al actualizar footer en la lista de devoluciones, Excepcion controlada", variables_publicas.info + " --- " + ex.getMessage(), variables_publicas.correoError, variables_publicas.correosErrores);
            Log.e("Error:", ex.getMessage());
            ex.printStackTrace();
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                if (getActivity() == null) return null;
                listadevoluciones.clear();
                CheckConnectivity();
                if (isOnline) {
                    GetDevolucionService();
                } else {
                    if (getActivity() == null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (final Exception e) {
                if (getActivity() == null) return null;
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
            if (pDialog != null && pDialog.isShowing())
                pDialog.dismiss();
            ActualizarLista();

        }
    }

    private void CheckConnectivity() {
        isOnline = Funciones.TestServerConectivity();
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
            for (HashMap<String, String> item : listadevoluciones) {
                double subtotal = Double.parseDouble(item.get("MONTO").replace("C$", "").replace(",", ""));
                item.put("MONTO", df.format(subtotal));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listadevoluciones,
                    R.layout.lista_devoluciones_guardados, new String[]{"FACTURA", "CLIENTE",
                    "RANGO",  "MONTO", "TIPO", "ESTADO"},
                    new int[]{R.id.nFactura, R.id.nCliente, R.id.nRango,
                            R.id.nMonto, R.id.nTipo, R.id.nEstado}) {
            };
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ActualizarFooter();

        } catch (final Exception ex) {
            if (getActivity() == null) return;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "ObtenerListaDevVendedor OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetDevolucionService() throws Exception {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        fechadesde= txtFechaIni.getText().toString();
        fechahasta= txtFechaIni.getText().toString();
        String FormatFechaIni = fechadesde.replace("-", "");
        String FormatFechaFin = fechahasta.replace("-", "");
        String codVendedor = variables_publicas.usuario.getCodigo();
        String urlString = urlDevoluciones + "/" + FormatFechaIni + "/" + FormatFechaFin + "/" + codVendedor;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de devoluciones. Respuesta nula GET", variables_publicas.info + urlString, "sisago@suplidora.com.ni", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray Devoluciones = jsonObj.getJSONArray("ObtenerListaDevVendedorResult");

                /*Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");*/


                for (int i = 0; i < Devoluciones.length(); i++) {
                    JSONObject c = Devoluciones.getJSONObject(i);

                    String factura = c.getString("FACTURA");
                    String cliente = c.getString("CLIENTE");
                    String rango = c.getString("RANGO");
                    String monto = c.getString("MONTO");
                    String tipo = c.getString("TIPO");
                    String estado = c.getString("ESTADO");


                    HashMap<String, String> devoluciones = new HashMap<>();

                    devoluciones.put("FACTURA", factura);
                    devoluciones.put("CLIENTE", cliente);
                    devoluciones.put("RANGO", rango);
                    devoluciones.put("MONTO", monto);
                    devoluciones.put("TIPO", tipo);
                    devoluciones.put("ESTADO", estado);

                    listadevoluciones.add(devoluciones);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de devoluciones. Excepcion controlada", variables_publicas.info + ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

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

    private void updateLabel() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaIni.setText(sdf.format(myCalendar.getTime()));
        btnBuscarDev.performClick();
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