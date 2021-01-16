package com.safi_d.sistemas.safiapp.Informes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.safi_d.sistemas.safiapp.Auxiliar.Funciones;
import com.safi_d.sistemas.safiapp.Auxiliar.variables_publicas;
import com.safi_d.sistemas.safiapp.HttpHandler;
import com.safi_d.sistemas.safiapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sistemas on 9/2/2018.
 */

public class ListaFacturasMoraClientes extends Activity {
    static final String KEY_IdCliente = "IdCliente";
    static final String KEY_NombreCliente = "Nombre";


    private String Nombre="";
    private String ClienteId="";
    private String vClienteVario= "";
    private String TAG = ListaFacturasMoraClientes.class.getSimpleName();
    private ListView lv;
    private TextView lblFooterCantidad;
    private TextView lblFooterSubtotal;
    private TextView lblIdyNombreCliente;
    private TextView lblTitulo;
    public static ArrayList<HashMap<String, String>> listaFacturas;
    private SimpleAdapter adapter;
    private DecimalFormat df;
    final String urlGetFacturasClientes = variables_publicas.direccionIp + "/ServicioRecibos.svc/ObtieneFacturasMora/";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_facturas_mora_clientes_layout);

        df = new DecimalFormat("#0.00");
        DecimalFormatSymbols fmts = new DecimalFormatSymbols();
        fmts.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setGroupingUsed(true);
        df.setDecimalFormatSymbols(fmts);
        lv = (ListView) findViewById(R.id.listFacSaldoMora);
        registerForContextMenu(lv);

        Intent in = getIntent();

        //vClienteVario=in.getStringExtra(variables_publicas.CLIENTES_COLUMN_CodCv).toString().replace("Cod_Cv: ", "");
        vClienteVario="";
        if (vClienteVario.equals("")){
            ClienteId= in.getStringExtra(KEY_IdCliente);
        }else {
            ClienteId = vClienteVario;
        }
        Nombre = in.getStringExtra(KEY_NombreCliente);

        lblFooterCantidad = (TextView) findViewById(R.id.lblFooterCantidad);
        lblFooterSubtotal = (TextView) findViewById(R.id.lblFooterSubtotal);
        lblIdyNombreCliente = (TextView) findViewById(R.id.lblIdyNombreCliente);
        lblTitulo = (TextView) findViewById(R.id.lblTitulo);

        lblTitulo.setText("Listado de Facturas con Saldo y Mora ");
        lblIdyNombreCliente.setText(Nombre);

        listaFacturas = new ArrayList<>();
        CargarFacturas();
    }
    private void CargarFacturas() {
        lv.setAdapter(adapter);
        listaFacturas.clear();
        GetFacturasService();
          ActualizarLista();
    }
    private void ActualizarFooter() {
        lblFooterCantidad.setText("Cantidad: " + String.valueOf(listaFacturas.size()));
        double subtotal = 0.00;
        for (HashMap<String, String> devdet : listaFacturas) {
            subtotal += Double.parseDouble(devdet.get("Saldo").replace("C$", "").replace(",", ""));
        }
        lblFooterSubtotal.setText("Total: C$" + df.format(subtotal));
        if (adapter!=null) adapter.notifyDataSetChanged();
    }

    private void ActualizarLista() {
        adapter = new SimpleAdapter(
                getApplicationContext(), listaFacturas,
                R.layout.resumen_factura_sal_mora_list_item, new
                String[]{"Factura","Fecha", "Total", "Abono", "Saldo", "Dias"}, new
                int[]{R.id.lblFactura,R.id.lblFecha, R.id.lblTotal, R.id.lblAbono, R.id.lblSaldo, R.id.lblDiasMora}) {

        };

        lv.setAdapter(adapter);
        if (adapter!=null ){
            adapter.notifyDataSetChanged();
        }
        ActualizarFooter();
    }
    private void GetFacturasService()  {
        String encodeUrl = "";
        HttpHandler sh = new HttpHandler();
        String urlString = urlGetFacturasClientes  + ClienteId;
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
                new Funciones().SendMail("Ha ocurrido un error al obtener lista de facturas con saldo y mora del cliente. Respuesta nula GET", variables_publicas.info+urlString, "dlunasistemas@gmail.com", variables_publicas.correosErrores);
            } else {
                Log.e(TAG, "Response from url: " + jsonStr);

                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray facturasSalMora = jsonObj.getJSONArray("ObtieneFacturasMoraResult");
                listaFacturas.clear();
                for (int i = 0; i < facturasSalMora.length(); i++) {
                    JSONObject c = facturasSalMora.getJSONObject(i);

                    String vFactura = c.getString("No_Factura");
                    String vFecha= c.getString("Fecha");
                    String vTotal = c.getString("Total");
                    String vAbono= c.getString("Abono");
                    String vSaldo = c.getString("Saldo");
                    String vDias = c.getString("Dias_Mora");

                    HashMap<String, String> hasFacSalMora = new HashMap<>();

                    hasFacSalMora.put("Factura", vFactura);
                    hasFacSalMora.put("Fecha", vFecha);
                    hasFacSalMora.put("Total", df.format(Double.parseDouble(vTotal)));
                    hasFacSalMora.put("Abono",  df.format(Double.parseDouble(vAbono)));
                    hasFacSalMora.put("Saldo", df.format(Double.parseDouble(vSaldo)));
                    hasFacSalMora.put("Dias", vDias);
                    listaFacturas.add(hasFacSalMora);
                }
            }
        } catch (Exception ex) {
            new Funciones().SendMail("Ha ocurrido un error al obtener lista de facturas con saldo y mora del cliente. Excepcion controlada", variables_publicas.info+ex.getMessage(), "dlunasistemas@gmail.com", variables_publicas.correosErrores);

        }
    }
}
