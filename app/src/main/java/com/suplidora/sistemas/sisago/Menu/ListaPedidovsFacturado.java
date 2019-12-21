package com.suplidora.sistemas.sisago.Menu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.suplidora.sistemas.sisago.AccesoDatos.DataBaseOpenHelper;
import com.suplidora.sistemas.sisago.AccesoDatos.VendedoresHelper;
import com.suplidora.sistemas.sisago.Auxiliar.Funciones;
import com.suplidora.sistemas.sisago.Auxiliar.variables_publicas;
import com.suplidora.sistemas.sisago.Entidades.Vendedor;
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

public class ListaPedidovsFacturado extends Fragment {
    View myView;
    private DataBaseOpenHelper DbOpenHelper;
    private VendedoresHelper VendedoresH;
    private Vendedor vendedor = null;

    private String vVendedor = "0";
    private String CodigoSupervisor;

    private String TAG = ListaPedidosSupFragment.class.getSimpleName();
    private String fechadesde = "";
    private String fechahasta = "";
    private ProgressDialog pDialog;
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private Button btnBuscar;
    private TextView txtFechaDesde;
    private TextView txtFechaHasta;
    private String NoFactura = "";
    private String NoPedido = "";
    private Spinner cboVendedor;
    public static ArrayList<HashMap<String, String>> listapedidosFacturas;
    public Calendar myCalendar = Calendar.getInstance();
    private SimpleAdapter adapter;
    final String urlPedidosFactura = variables_publicas.direccionIp + "/ServicioPedidos.svc/ObtenerPedidoVsFacturado";
    private DecimalFormat df;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.listapedidofacturado_layout, container, false);
        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        getActivity().setTitle("Pedido vs Facturado");
        lv = (ListView) myView.findViewById(R.id.listaPedidoFactura);
        registerForContextMenu(lv);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
            }
        });

        btnBuscar = (Button) myView.findViewById(R.id.btnBuscar);
        txtFechaDesde = (EditText) myView.findViewById(R.id.txtFechaDesde);
        txtFechaHasta = (EditText) myView.findViewById(R.id.txtFechaHasta);
        lblFooterCantidad = (TextView) myView.findViewById(R.id.lblFooterPedido);
        lblFooterSubtotal = (TextView) myView.findViewById(R.id.lblFooterFacturado);
        cboVendedor = (Spinner) myView.findViewById(R.id.cboVendedor);

        if (variables_publicas.usuario.getTipo().equals("Vendedor")){
            cboVendedor.setEnabled(false);
        }else {
            cboVendedor.setEnabled(true);
        }

        DbOpenHelper = new DataBaseOpenHelper(getActivity().getApplicationContext());
        VendedoresH = new VendedoresHelper(DbOpenHelper.database);
        txtFechaDesde.setText(getDatePhone());
        fechadesde = txtFechaDesde.getText().toString();

        txtFechaHasta.setText(getDatePhone());
        fechahasta = txtFechaHasta.getText().toString();

        if(variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor"))
        {
            CodigoSupervisor = variables_publicas.usuario.getCodigo();
        }
        else if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Vendedor")){
            Vendedor vendedor = VendedoresH.ObtenerVendedor(variables_publicas.usuario.getCodigo());
            CodigoSupervisor = vendedor.getCodsuper();
        }
        else
        {
            CodigoSupervisor = "0";
        }

        CargaDatosCombo();

        cboVendedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                vendedor = (Vendedor) adapter.getItemAtPosition(position);
                vVendedor = vendedor.getCODIGO().toString();
                btnBuscar.performClick();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

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

        listapedidosFacturas = new ArrayList<>();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
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
        listapedidosFacturas.clear();
        new GetListaPedidos().execute();
        ActualizarFooter();
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void CargaDatosCombo() {
        //Combo Vendedores
        List<Vendedor> vendedores;

        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor")){
            vendedores = VendedoresH.ObtenerListaVendedorSup(variables_publicas.usuario.getCodigo());
        }else {
            vendedores = VendedoresH.ObtenerListaVendedores();
        }
        ArrayAdapter<Vendedor> adapterVendedor = new ArrayAdapter<Vendedor>(getActivity(), android.R.layout.simple_spinner_item, vendedores);
        adapterVendedor.setDropDownViewResource(android.R.layout.simple_list_item_checked);
        cboVendedor.setAdapter(adapterVendedor);

        if (variables_publicas.usuario.getTipo().equals("Vendedor")){
            vVendedor = variables_publicas.usuario.getCodigo();
            cboVendedor.setSelection(getIndex(cboVendedor,variables_publicas.usuario.getNombre()));
        }else{
            cboVendedor.setSelection(0);
            vendedor = (Vendedor) adapterVendedor.getItem(cboVendedor.getSelectedItemPosition());
            vVendedor=vendedor.getCODIGO();
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

    private void ActualizarFooter() {
        //lblFooterCantidad.setText("Cantidad: " + String.valueOf(listapedidosFacturas.size()));
        double totalpedido = 0.00;
        double totalfacturado = 0.00;
        for (HashMap<String, String> pedido : listapedidosFacturas) {
            totalpedido += Double.parseDouble(pedido.get("TotPedido").replace("C$", "").replace(",", ""));
            totalfacturado += Double.parseDouble(pedido.get("TotFactura").replace("C$", "").replace(",", ""));
        }
        lblFooterCantidad.setText("Pedido: C$" + df.format(totalpedido));
        lblFooterSubtotal.setText("Fact.: C$" + df.format(totalfacturado));
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
                    GetPedidosService();
                } else {
                    if(getActivity()==null) return null;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "No es posible conectarse al servidor. \n Solo se mostraran los pedidos locales que no se han sincronizados! ",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } catch (final Exception e) {
                if(getActivity()==null) return null;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "No es posible conectarse al servidor. \n Solo se mostraran los pedidos locales que no se han sincronizados! ",
                                Toast.LENGTH_LONG).show();
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
            for (HashMap<String, String> item : listapedidosFacturas) {
                double TotPedido = Double.parseDouble(item.get("TotPedido").replace("C$", "").replace(",", ""));
                item.put("TotPedido", df.format(TotPedido));
                double TotFacturado= Double.parseDouble(item.get("TotFactura").replace("C$", "").replace(",", ""));
                item.put("TotFactura", df.format(TotFacturado));
            }
            adapter = new SimpleAdapter(
                    getActivity(), listapedidosFacturas,

                    R.layout.resumen_pedido_factura_item, new String[]{"Ruta", "Pedido","Factura","Cliente","TotPedido","TotFactura"},
                    new int[]{R.id.lblDetRuta, R.id.lblDetPedido, R.id.lblDetFactura, R.id.lblDetCliente, R.id.lblDetTotPedido, R.id.lblDetTotFacturado}) {
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
                            "GetListaPedidos OnPostExecute:" + ex.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void GetPedidosService() throws Exception {
        String urlString="";

        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String Fdesde = fechadesde.replace("-","") ,Fhasta=fechahasta.replace("-","");

        if (variables_publicas.usuario.getTipo().equalsIgnoreCase("Supervisor")){
            urlString = urlPedidosFactura + "/0/" + Fdesde + "/" + Fhasta + "/"+ vVendedor +"/" + CodigoSupervisor;
        }else {
            urlString = urlPedidosFactura + "/0/" + Fdesde + "/" + Fhasta + "/" + vVendedor + "/0";
        }

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
                JSONArray PedidoFactura = jsonObj.getJSONArray("ObtenerPedidoVsFacturadoResult");
                listapedidosFacturas.clear();
                for (int i = 0; i < PedidoFactura.length(); i++) {
                    JSONObject c = PedidoFactura.getJSONObject(i);

                    String Ruta = c.getString("RUTA");
                    String Pedido = c.getString("PEDIDO");
                    String Factura = c.getString("FACTURA");
                    String Cliente = c.getString("NOMBRE");
                    String TotPedido = c.getString("TOTPEDIDO");
                    String TotFactura = c.getString("TOTFACTURA");

                    HashMap<String, String> pedidofactura = new HashMap<>();

                    pedidofactura.put("Ruta", Ruta);
                    pedidofactura.put("Pedido", Pedido);
                    pedidofactura.put("Factura", Factura);
                    pedidofactura.put("Cliente", Cliente);
                    pedidofactura.put("TotPedido", TotPedido);
                    pedidofactura.put("TotFactura", TotFactura);
                    listapedidosFacturas.add(pedidofactura);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de pedidos,Excepcion controlada", variables_publicas.info+ex.getMessage(), "sisago@suplidora.com.ni", variables_publicas.correosErrores);

        }
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        try {
            super.onCreateContextMenu(menu, v, menuInfo);
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;


            HashMap<String, String> obj = (HashMap<String, String>) lv.getItemAtPosition(info.position);

            String HeaderMenu = "Pedido: " +obj.get("Pedido") ;

            NoFactura=obj.get("Factura");
            NoPedido=obj.get("Pedido");

            menu.setHeaderTitle(HeaderMenu);
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.pedidos_factura_menu_context, menu);

/*            if (obj.get("Factura").equalsIgnoreCase(""))
            {
                ((MenuItem) menu.getItem(0)).setEnabled(false);

            }else {
                ((MenuItem) menu.getItem(0)).setEnabled(true);
            }*/
        } catch (Exception e) {
            mensajeAviso(e.getMessage());
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.itemVerPedidofact:
                    // Starting new intent
                    Intent in = new Intent(getActivity().getApplicationContext(), ListaDetallePedidoFacturado.class);

                    in.putExtra(variables_publicas.PEDIDOS_COLUMN_CodigoPedido, NoPedido);
                    in.putExtra(variables_publicas.FACTURAS_PENDIENTES_COLUMN_No_Factura, NoFactura);
                    startActivity(in);

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

    private void updateLabelDesde() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaDesde.setText(sdf.format(myCalendar.getTime()));
      //  btnBuscar.performClick();
    }
    private void updateLabelHasta() {
        String myFormat = ("yyyy-MM-dd");
        ; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        txtFechaHasta.setText(sdf.format(myCalendar.getTime()));
       // btnBuscar.performClick();
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
  /*      try {
            CargarPedidos();
        } catch (Exception ex) {
        }*/
    }
}
